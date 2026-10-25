package vn.hust.restaurant.view;

import java.awt.*;
import java.nio.file.Path;
import java.time.LocalDate;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import vn.hust.restaurant.config.AppContext;
import vn.hust.restaurant.model.ReportTable;

public class ReportPanel extends JPanel implements Refreshable {
    private final AppContext ctx;
    private final JTextField from = new JTextField(LocalDate.now().withDayOfMonth(1).format(Ui.D),10);
    private final JTextField to = new JTextField(LocalDate.now().format(Ui.D),10);
    private final JComboBox<String> type = new JComboBox<>(new String[]{"Doanh thu", "Món bán chạy", "Tồn kho thấp", "Nhập kho"});
    private final JTable table = new JTable();
    private ReportTable current;
    public ReportPanel(AppContext c){ctx=c;setLayout(new BorderLayout());JButton view=new JButton("Xem"),excel=new JButton("Xuất Excel"),pdf=new JButton("Xuất PDF");add(Ui.toolbar(new JLabel("Từ"),from,new JLabel("Đến"),to,type,view,excel,pdf),BorderLayout.NORTH);add(new JScrollPane(table),BorderLayout.CENTER);view.addActionListener(e->refresh());excel.addActionListener(e->export(false));pdf.addActionListener(e->export(true));refresh();}
    public void refresh(){Ui.run(this,()->{LocalDate f=Ui.date(from.getText()),t=Ui.date(to.getText());current=switch(type.getSelectedIndex()){case 0->ctx.reports.revenue(f,t);case 1->ctx.reports.top(f,t);case 2->ctx.reports.low();default->ctx.reports.receipts(f,t);};DefaultTableModel m=Ui.model(current.columns().toArray(String[]::new));for(var row:current.rows())m.addRow(row.toArray());table.setModel(m);});}
    private void export(boolean pdf){if(current==null)return;JFileChooser ch=new JFileChooser();ch.setSelectedFile(new java.io.File((pdf?"bao_cao.pdf":"bao_cao.xlsx")));if(ch.showSaveDialog(this)!=JFileChooser.APPROVE_OPTION)return;Ui.run(this,()->{Path out=pdf?ctx.exporter.exportPdf(current,ch.getSelectedFile().toPath()):ctx.exporter.exportExcel(current,ch.getSelectedFile().toPath());Ui.info(this,"Đã xuất: "+out);});}
}
