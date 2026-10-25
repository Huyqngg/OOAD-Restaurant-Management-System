package vn.hust.restaurant.view;
 
import java.awt.*;
import java.util.List;
import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.table.DefaultTableModel;
import vn.hust.restaurant.config.AppContext;
import vn.hust.restaurant.model.*;
 
public class TableOrderPanel extends JPanel implements Refreshable {
    private final AppContext ctx;
    private final UserSession session;
    private final DefaultTableModel tableModel = Ui.model("ID", "Mã bàn", "Tên bàn", "Khu vực", "Sức chứa", "Trạng thái");
    private final DefaultTableModel menuModel = Ui.model("ID", "Mã món", "Tên món", "Danh mục", "Đơn giá");
    private final DefaultTableModel itemModel = Ui.model("ID", "Tên món", "SL", "Đơn giá", "Thành tiền", "Ghi chú", "Bếp");
    private final JTable tableGrid = new JTable(tableModel), menuGrid = new JTable(menuModel), itemGrid = new JTable(itemModel);
    private final JTextField menuSearch = new JTextField(16), qty = new JTextField("1", 4), note = new JTextField(14);
    private final JLabel orderLabel = new JLabel("Chưa chọn bàn/order");
    private List<DiningTable> tables = List.of();
    private List<vn.hust.restaurant.model.MenuItem> menu = List.of();
    private List<OrderItemView> items = List.of();
    private OrderSummary activeOrder;
 
    public TableOrderPanel(AppContext c, UserSession s) {
        ctx = c; session = s;
        setLayout(new BorderLayout(8, 8));
        JSplitPane leftMiddle = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, tablePane(), menuPane());
        leftMiddle.setResizeWeight(.35);
        JSplitPane root = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, leftMiddle, orderPane());
        root.setResizeWeight(.66);
        add(root, BorderLayout.CENTER);
        tableGrid.getSelectionModel().addListSelectionListener(this::tableSelected);
        refresh();
    }
 
    private JPanel tablePane() {
        JPanel p = new JPanel(new BorderLayout());
        JButton reload = new JButton("Làm mới"), create = new JButton("Tạo order"), addTable = new JButton("Thêm bàn"), editTable = new JButton("Sửa bàn"), disable = new JButton("Ngừng dùng");
        JPanel bar = Ui.toolbar(reload, create);
        if (session.hasRole("CHU_NHA_HANG")) { bar.add(addTable); bar.add(editTable); bar.add(disable); }
        p.add(bar, BorderLayout.NORTH); p.add(new JScrollPane(tableGrid), BorderLayout.CENTER);
        reload.addActionListener(e -> refresh()); create.addActionListener(e -> createOrder());
        addTable.addActionListener(e -> editTable(null));
        editTable.addActionListener(e -> { DiningTable t = selectedTable(); if (t != null) editTable(t); });
        disable.addActionListener(e -> { DiningTable t = selectedTable(); if (t != null && Ui.confirm(this,"Ngừng sử dụng bàn " + t.code() + "?")) Ui.run(this, () -> { ctx.tables.deactivate(t.id()); refresh(); }); });
        return p;
    }
 
    private JPanel menuPane() {
        JPanel p = new JPanel(new BorderLayout());
        JButton find = new JButton("Tìm"), add = new JButton("Thêm vào order");
        JPanel searchBar = Ui.toolbar(new JLabel("Tìm món:"), menuSearch, find);
        JPanel addBar = Ui.toolbar(new JLabel("SL:"), qty, new JLabel("Ghi chú:"), note, add);
        JPanel north = new JPanel(new BorderLayout());
        north.add(searchBar, BorderLayout.NORTH);
        north.add(addBar, BorderLayout.SOUTH);
        p.add(north, BorderLayout.NORTH);
        p.add(new JScrollPane(menuGrid), BorderLayout.CENTER);
        find.addActionListener(e -> loadMenu()); menuSearch.addActionListener(e -> loadMenu()); add.addActionListener(e -> addItem());
        return p;
    }
 
    private JPanel orderPane() {
        JPanel p = new JPanel(new BorderLayout());
        orderLabel.setBorder(BorderFactory.createEmptyBorder(8,8,8,8));
        p.add(orderLabel, BorderLayout.NORTH); p.add(new JScrollPane(itemGrid), BorderLayout.CENTER);
        JButton remove = new JButton("Hủy món chờ"), send = new JButton("Gửi bếp"), ready = new JButton("Chờ thanh toán"), cancel = new JButton("Hủy order");
        p.add(Ui.toolbar(remove, send, ready, cancel), BorderLayout.SOUTH);
        remove.addActionListener(e -> removeItem()); send.addActionListener(e -> orderAction("send")); ready.addActionListener(e -> orderAction("ready")); cancel.addActionListener(e -> orderAction("cancel"));
        return p;
    }
 
    public void refresh() {
        Ui.run(this, () -> {
            int selectedId = selectedTable() == null ? -1 : selectedTable().id();
            tables = ctx.tables.all(); tableModel.setRowCount(0);
            int rowToSelect = -1;
            for (int i=0;i<tables.size();i++) { DiningTable t=tables.get(i); tableModel.addRow(new Object[]{t.id(),t.code(),t.name(),t.area(),t.capacity(),t.status()}); if(t.id()==selectedId)rowToSelect=i; }
            if(rowToSelect>=0)tableGrid.setRowSelectionInterval(rowToSelect,rowToSelect);
            loadMenu(); if(selectedTable()!=null)loadOrder(selectedTable().id());
        });
    }
    private void loadMenu() { Ui.run(this, () -> { menu=ctx.menu.list(menuSearch.getText(),true); menuModel.setRowCount(0); for(vn.hust.restaurant.model.MenuItem m:menu)menuModel.addRow(new Object[]{m.id(),m.code(),m.name(),m.categoryName(),m.price()}); }); }
    private void tableSelected(ListSelectionEvent e) { if(!e.getValueIsAdjusting()){ DiningTable t=selectedTable(); if(t!=null)loadOrder(t.id()); } }
    private DiningTable selectedTable(){int r=tableGrid.getSelectedRow();return r<0||r>=tables.size()?null:tables.get(r);}
    private void loadOrder(int tableId){Ui.run(this,()->{activeOrder=ctx.orders.active(tableId).orElse(null);items=activeOrder==null?List.of():ctx.orders.items(activeOrder.id());itemModel.setRowCount(0);for(OrderItemView x:items)itemModel.addRow(new Object[]{x.id(),x.menuItemName(),x.quantity(),x.unitPrice(),x.lineTotal(),x.note(),x.kitchenStatus()});orderLabel.setText(activeOrder==null?"Bàn chưa có order":"Order "+activeOrder.code()+" | "+activeOrder.status()+" | Tạm tính: "+activeOrder.total()+" đ");});}
    private void createOrder(){DiningTable t=selectedTable();if(t==null){Ui.info(this,"Chọn bàn trước");return;}Ui.run(this,()->{long id=ctx.orders.create(t.id(),session.employeeId());Ui.info(this,"Đã tạo order #"+id);refresh();loadOrder(t.id());});}
    private void addItem(){int r=menuGrid.getSelectedRow();if(r<0||activeOrder==null){Ui.info(this,"Chọn món và tạo order trước");return;}vn.hust.restaurant.model.MenuItem m=menu.get(r);Ui.run(this,()->{ctx.orders.add(activeOrder.id(),m.id(),Ui.integer(qty.getText()),note.getText());note.setText("");loadOrder(activeOrder.tableId());});}
    private void removeItem(){int r=itemGrid.getSelectedRow();if(r<0)return;Ui.run(this,()->{ctx.orders.remove(items.get(r).id());loadOrder(activeOrder.tableId());});}
    private void orderAction(String a){if(activeOrder==null)return;Ui.run(this,()->{switch(a){case"send"->ctx.orders.send(activeOrder.id());case"ready"->ctx.orders.ready(activeOrder.id());case"cancel"->{if(!Ui.confirm(this,"Hủy order hiện tại?"))return;ctx.orders.cancel(activeOrder.id());}}refresh();});}
    private void editTable(DiningTable t){JTextField code=Ui.field(t==null?"B"+(tables.size()+1):t.code(),8),name=Ui.field(t==null?"Bàn "+(tables.size()+1):t.name(),12),area=Ui.field(t==null?"Tầng 1":t.area(),12),cap=Ui.field(t==null?"4":String.valueOf(t.capacity()),5);JComboBox<String>status=new JComboBox<>(new String[]{"TRONG","DA_DAT","DANG_PHUC_VU","CHO_THANH_TOAN","NGUNG_SU_DUNG"});if(t!=null)status.setSelectedItem(t.status());Object[]f={"Mã bàn",code,"Tên bàn",name,"Khu vực",area,"Sức chứa",cap,"Trạng thái",status};if(JOptionPane.showConfirmDialog(this,f,t==null?"Thêm bàn":"Sửa bàn",JOptionPane.OK_CANCEL_OPTION)==JOptionPane.OK_OPTION)Ui.run(this,()->{ctx.tables.save(new DiningTable(t==null?0:t.id(),code.getText(),name.getText(),area.getText(),Ui.integer(cap.getText()),String.valueOf(status.getSelectedItem())));refresh();});}
}
 
 