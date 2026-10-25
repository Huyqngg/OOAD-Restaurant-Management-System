package vn.hust.restaurant.view;

import java.awt.*;
import java.math.BigDecimal;
import java.util.*;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import vn.hust.restaurant.config.AppContext;
import vn.hust.restaurant.model.*;

public class PaymentPanel extends JPanel implements Refreshable {
    private final AppContext ctx; private final UserSession session;
    private final DefaultListModel<OrderSummary> orderModel = new DefaultListModel<>();
    private final JList<OrderSummary> orderList = new JList<>(orderModel);
    private final DefaultTableModel itemModel = Ui.model("Món", "SL", "Đơn giá", "Thành tiền", "Trạng thái");
    private final JTable itemTable = new JTable(itemModel);
    private final JComboBox<Object> customer = new JComboBox<>(), promotion = new JComboBox<>();
    private final JTextField points = new JTextField("0",6), cash = new JTextField("0",10), transfer = new JTextField("0",10), card = new JTextField("0",10), transaction = new JTextField(12);
    private final JLabel subtotal = new JLabel("0"), discount = new JLabel("0"), vat = new JLabel("0"), pointDiscount = new JLabel("0"), total = new JLabel("0"), earned = new JLabel("0");
    private InvoicePreview current; private Long lastInvoice;
    private java.util.List<OrderSummary> orders = java.util.List.of();

    public PaymentPanel(AppContext c, UserSession s) {
        ctx=c; session=s; setLayout(new BorderLayout(10,10)); setBorder(BorderFactory.createEmptyBorder(10,10,10,10));
        orderList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION); orderList.addListSelectionListener(e->{if(!e.getValueIsAdjusting())preview();});
        JPanel left=new JPanel(new BorderLayout()); left.setPreferredSize(new Dimension(300,0)); left.add(new JLabel("Order đang mở/chờ thanh toán"),BorderLayout.NORTH); left.add(new JScrollPane(orderList),BorderLayout.CENTER); JButton reload=new JButton("Làm mới"); reload.addActionListener(e->refresh()); left.add(reload,BorderLayout.SOUTH);
        JPanel center=new JPanel(new BorderLayout()); center.add(new JScrollPane(itemTable),BorderLayout.CENTER); center.add(form(),BorderLayout.SOUTH);
        add(left,BorderLayout.WEST); add(center,BorderLayout.CENTER); refresh();
    }
    private JPanel form(){JPanel p=new JPanel(new BorderLayout());JPanel grid=new JPanel(new GridLayout(0,4,8,6));grid.setBorder(BorderFactory.createTitledBorder("Tính tiền"));
        grid.add(new JLabel("Khách hàng"));grid.add(customer);grid.add(new JLabel("Khuyến mãi"));grid.add(promotion);
        grid.add(new JLabel("Điểm sử dụng"));grid.add(points);JButton calc=new JButton("Tính lại");grid.add(calc);grid.add(new JLabel());
        grid.add(new JLabel("Tạm tính"));grid.add(subtotal);grid.add(new JLabel("Giảm khuyến mãi"));grid.add(discount);
        grid.add(new JLabel("VAT"));grid.add(vat);grid.add(new JLabel("Giảm bằng điểm"));grid.add(pointDiscount);
        grid.add(new JLabel("TỔNG THANH TOÁN"));grid.add(total);grid.add(new JLabel("Điểm tích mới"));grid.add(earned);
        grid.add(new JLabel("Tiền mặt"));grid.add(cash);grid.add(new JLabel("Chuyển khoản"));grid.add(transfer);
        grid.add(new JLabel("Thẻ"));grid.add(card);grid.add(new JLabel("Mã giao dịch"));grid.add(transaction);
        JPanel buttons=new JPanel(new FlowLayout(FlowLayout.RIGHT));JButton pay=new JButton("Thanh toán"),print=new JButton("In hóa đơn gần nhất");buttons.add(calc);buttons.add(pay);buttons.add(print);p.add(grid,BorderLayout.CENTER);p.add(buttons,BorderLayout.SOUTH);
        calc.addActionListener(e->preview());customer.addActionListener(e->preview());promotion.addActionListener(e->preview());pay.addActionListener(e->pay());print.addActionListener(e->print());return p;}
    public void refresh(){Ui.run(this,()->{orders=ctx.orders.open();orderModel.clear();for(OrderSummary o:orders)orderModel.addElement(o);customer.removeAllItems();customer.addItem("Khách vãng lai");for(Customer x:ctx.customers.list(""))if("HOAT_DONG".equals(x.status()))customer.addItem(x);promotion.removeAllItems();promotion.addItem("Không áp dụng");for(Promotion x:ctx.payments.promotions())promotion.addItem(x);current=null;itemModel.setRowCount(0);clearTotals();});}
    private void preview(){OrderSummary o=orderList.getSelectedValue();if(o==null)return;Ui.run(this,()->{Integer cid=customer.getSelectedItem() instanceof Customer c?c.id():null;Promotion pr=promotion.getSelectedItem() instanceof Promotion p?p:null;int pts=points.getText().isBlank()?0:Ui.integer(points.getText());current=ctx.payments.preview(o,cid,pr,pts);itemModel.setRowCount(0);for(OrderItemView x:current.items())itemModel.addRow(new Object[]{x.menuItemName(),x.quantity(),x.unitPrice(),x.lineTotal(),x.kitchenStatus()});subtotal.setText(money(current.subtotal()));discount.setText(money(current.promotionDiscount()));vat.setText(money(current.vat()));pointDiscount.setText(money(current.pointDiscount()));total.setText(money(current.total()));earned.setText(String.valueOf(current.pointsEarned()));if(Ui.dec(cash.getText()).signum()==0&&Ui.dec(transfer.getText()).signum()==0&&Ui.dec(card.getText()).signum()==0)cash.setText(current.total().toPlainString());});}
    private void pay(){if(current==null){preview();if(current==null)return;}if(!Ui.confirm(this,"Xác nhận thanh toán "+money(current.total())+"?"))return;Ui.run(this,()->{java.util.List<PaymentPart>parts=new ArrayList<>();addPart(parts,"TIEN_MAT",cash.getText(),null);addPart(parts,"CHUYEN_KHOAN",transfer.getText(),transaction.getText());addPart(parts,"THE",card.getText(),transaction.getText());lastInvoice=ctx.payments.pay(current,session.employeeId(),parts);Ui.info(this,"Thanh toán thành công. Mã nội bộ hóa đơn: "+lastInvoice);refresh();});}
    private void addPart(java.util.List<PaymentPart>p,String method,String amount,String code){BigDecimal a=Ui.dec(amount);if(a.signum()>0)p.add(new PaymentPart(method,a,code));}
    private void print(){if(lastInvoice==null){Ui.info(this,"Chưa có hóa đơn vừa thanh toán");return;}Ui.run(this,()->ctx.printer.print(ctx.payments.invoice(lastInvoice)));}
    private void clearTotals(){for(JLabel l:java.util.List.of(subtotal,discount,vat,pointDiscount,total,earned))l.setText("0");}
    private String money(BigDecimal x){return String.format("%,.0f đ",x);}
}
