package vn.hust.restaurant.model;

import java.util.List;
public record ReportTable(String title, List<String> columns, List<List<Object>> rows) {}
