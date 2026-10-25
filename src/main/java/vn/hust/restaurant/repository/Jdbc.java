package vn.hust.restaurant.repository;

import java.math.BigDecimal;
import java.sql.*;
import java.time.LocalDate;
import java.time.LocalDateTime;

final class Jdbc {
    private Jdbc() {}
    static String str(ResultSet rs, String c) throws SQLException { return rs.getString(c); }
    static Integer integer(ResultSet rs, String c) throws SQLException { int v=rs.getInt(c); return rs.wasNull()?null:v; }
    static BigDecimal dec(ResultSet rs, String c) throws SQLException { BigDecimal v=rs.getBigDecimal(c); return v==null?BigDecimal.ZERO:v; }
    static LocalDateTime dateTime(ResultSet rs, String c) throws SQLException { Timestamp t=rs.getTimestamp(c); return t==null?null:t.toLocalDateTime(); }
    static LocalDate date(ResultSet rs, String c) throws SQLException { Date d=rs.getDate(c); return d==null?null:d.toLocalDate(); }
    static void nullableInt(PreparedStatement ps, int index, Integer value) throws SQLException { if(value==null) ps.setNull(index,Types.INTEGER); else ps.setInt(index,value); }
}
