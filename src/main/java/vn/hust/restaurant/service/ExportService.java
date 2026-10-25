package vn.hust.restaurant.service;

import java.io.*;
import java.lang.reflect.Method;
import java.nio.charset.StandardCharsets;
import java.nio.file.*;
import java.util.*;
import vn.hust.restaurant.model.ReportTable;

/**
 * Xuất báo cáo. Dùng reflection để mã nguồn vẫn biên dịch bằng JDK thuần;
 * khi chạy bằng Maven, Apache POI và JasperReports trong pom.xml sẽ được sử dụng.
 */
public class ExportService {
    public Path exportExcel(ReportTable table, Path file) throws Exception {
        try {
            Class<?> wbClass = Class.forName("org.apache.poi.xssf.usermodel.XSSFWorkbook");
            Object workbook = wbClass.getConstructor().newInstance();
            Object sheet = wbClass.getMethod("createSheet", String.class).invoke(workbook, safeSheetName(table.title()));
            Method createRow = sheet.getClass().getMethod("createRow", int.class);
            Object header = createRow.invoke(sheet, 0);
            Method createCell = header.getClass().getMethod("createCell", int.class);
            for (int i = 0; i < table.columns().size(); i++) {
                Object cell = createCell.invoke(header, i);
                cell.getClass().getMethod("setCellValue", String.class).invoke(cell, table.columns().get(i));
            }
            for (int r = 0; r < table.rows().size(); r++) {
                Object row = createRow.invoke(sheet, r + 1);
                for (int c = 0; c < table.rows().get(r).size(); c++) {
                    Object cell = createCell.invoke(row, c);
                    Object value = table.rows().get(r).get(c);
                    cell.getClass().getMethod("setCellValue", String.class).invoke(cell, String.valueOf(value));
                }
            }
            for (int i = 0; i < table.columns().size(); i++) {
                try { sheet.getClass().getMethod("autoSizeColumn", int.class).invoke(sheet, i); }
                catch (ReflectiveOperationException ignored) {}
            }
            try (OutputStream out = Files.newOutputStream(file)) {
                wbClass.getMethod("write", OutputStream.class).invoke(workbook, out);
            }
            wbClass.getMethod("close").invoke(workbook);
            return file;
        } catch (ClassNotFoundException missingPoi) {
            Path csv = replaceExtension(file, "csv");
            try (BufferedWriter writer = Files.newBufferedWriter(csv, StandardCharsets.UTF_8)) {
                writer.write('\ufeff');
                writeCsvRow(writer, new ArrayList<>(table.columns()));
                for (List<Object> row : table.rows()) writeCsvRow(writer, row);
            }
            return csv;
        }
    }

    public Path exportPdf(ReportTable table, Path file) throws Exception {
        Path jrxml = Files.createTempFile("restaurant-report-", ".jrxml");
        Files.writeString(jrxml, buildJrxml(table), StandardCharsets.UTF_8);
        try (InputStream in = Files.newInputStream(jrxml)) {
            Class<?> compileManager = Class.forName("net.sf.jasperreports.engine.JasperCompileManager");
            Object jasperReport = compileManager.getMethod("compileReport", InputStream.class).invoke(null, in);

            List<Map<String, ?>> records = new ArrayList<>();
            for (List<Object> row : table.rows()) {
                Map<String, Object> record = new HashMap<>();
                for (int i = 0; i < table.columns().size(); i++) {
                    Object value = i < row.size() ? row.get(i) : "";
                    record.put("C" + i, value == null ? "" : String.valueOf(value));
                }
                records.add(record);
            }
            if (records.isEmpty()) {
                Map<String, Object> empty = new HashMap<>();
                for (int i = 0; i < table.columns().size(); i++) empty.put("C" + i, "");
                records.add(empty);
            }

            Class<?> dataSourceClass = Class.forName("net.sf.jasperreports.engine.data.JRMapCollectionDataSource");
            Object dataSource = dataSourceClass.getConstructor(Collection.class).newInstance(records);
            Class<?> jasperReportClass = Class.forName("net.sf.jasperreports.engine.JasperReport");
            Class<?> jrDataSourceClass = Class.forName("net.sf.jasperreports.engine.JRDataSource");
            Class<?> fillManager = Class.forName("net.sf.jasperreports.engine.JasperFillManager");
            Object jasperPrint = fillManager
                .getMethod("fillReport", jasperReportClass, Map.class, jrDataSourceClass)
                .invoke(null, jasperReport, Map.of("TITLE", table.title()), dataSource);
            Class<?> jasperPrintClass = Class.forName("net.sf.jasperreports.engine.JasperPrint");
            Class<?> exportManager = Class.forName("net.sf.jasperreports.engine.JasperExportManager");
            exportManager.getMethod("exportReportToPdfFile", jasperPrintClass, String.class)
                .invoke(null, jasperPrint, file.toString());
            return file;
        } finally {
            Files.deleteIfExists(jrxml);
        }
    }

    private String buildJrxml(ReportTable table) {
        int count = Math.max(1, table.columns().size());
        int pageWidth = 842, pageHeight = 595, margin = 25, usable = pageWidth - margin * 2;
        int colWidth = usable / count;
        StringBuilder fields = new StringBuilder();
        StringBuilder headers = new StringBuilder();
        StringBuilder details = new StringBuilder();
        for (int i = 0; i < count; i++) {
            int x = i * colWidth;
            int width = i == count - 1 ? usable - x : colWidth;
            String name = i < table.columns().size() ? table.columns().get(i) : "";
            fields.append("<field name=\"C").append(i).append("\" class=\"java.lang.String\"/>\n");
            headers.append("<staticText><reportElement x=\"").append(x).append("\" y=\"0\" width=\"").append(width).append("\" height=\"24\"/>"
                + "<box><pen lineWidth=\"0.5\"/></box><textElement><font size=\"9\" isBold=\"true\"/></textElement><text><![CDATA[")
                .append(name).append("]]></text></staticText>\n");
            details.append("<textField isStretchWithOverflow=\"true\"><reportElement x=\"").append(x).append("\" y=\"0\" width=\"").append(width).append("\" height=\"22\"/>"
                + "<box><pen lineWidth=\"0.25\"/></box><textElement><font size=\"8\"/></textElement><textFieldExpression><![CDATA[$F{C")
                .append(i).append("}]]></textFieldExpression></textField>\n");
        }
        return """
            <?xml version="1.0" encoding="UTF-8"?>
            <jasperReport xmlns="http://jasperreports.sourceforge.net/jasperreports"
              xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
              xsi:schemaLocation="http://jasperreports.sourceforge.net/jasperreports http://jasperreports.sourceforge.net/xsd/jasperreport.xsd"
              name="RestaurantReport" pageWidth="842" pageHeight="595" orientation="Landscape"
              columnWidth="792" leftMargin="25" rightMargin="25" topMargin="25" bottomMargin="25">
              <parameter name="TITLE" class="java.lang.String"/>
            """ + fields + """
              <title><band height="48"><textField><reportElement x="0" y="0" width="792" height="34"/>
                <textElement textAlignment="Center"><font size="18" isBold="true"/></textElement>
                <textFieldExpression><![CDATA[$P{TITLE}]]></textFieldExpression></textField></band></title>
              <columnHeader><band height="24">
            """ + headers + """
              </band></columnHeader>
              <detail><band height="22" splitType="Stretch">
            """ + details + """
              </band></detail>
              <pageFooter><band height="20"><textField><reportElement x="692" y="0" width="100" height="18"/>
                <textElement textAlignment="Right"/><textFieldExpression><![CDATA["Trang " + $V{PAGE_NUMBER}]]></textFieldExpression>
              </textField></band></pageFooter>
            </jasperReport>
            """;
    }

    private static void writeCsvRow(BufferedWriter writer, List<?> values) throws IOException {
        for (int i = 0; i < values.size(); i++) {
            if (i > 0) writer.write(',');
            writer.write('"');
            writer.write(String.valueOf(values.get(i)).replace("\"", "\"\""));
            writer.write('"');
        }
        writer.newLine();
    }
    private static String safeSheetName(String s) {
        String cleaned = s.replaceAll("[\\/:*?\"<>|]", "_");
        return cleaned.substring(0, Math.min(31, cleaned.length()));
    }
    private static Path replaceExtension(Path p, String ext) {
        String n = p.getFileName().toString();
        int i = n.lastIndexOf('.');
        n = (i < 0 ? n : n.substring(0, i)) + "." + ext;
        return p.resolveSibling(n);
    }
}
