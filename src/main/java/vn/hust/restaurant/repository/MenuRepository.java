package vn.hust.restaurant.repository;

import java.sql.*;import java.util.*;import vn.hust.restaurant.config.Database;import vn.hust.restaurant.model.MenuItem;

public class MenuRepository {
    public List<MenuItem> findAll(String keyword,boolean onlyAvailable)throws SQLException{
        List<MenuItem> out=new ArrayList<>();String k=keyword==null?"":keyword.trim();
        String sql="""
          SELECT m.MaMon,m.MaMonCode,m.MaDanhMucMon,d.TenDanhMuc,m.TenMon,m.DonViTinh,m.DonGia,m.MoTa,m.TrangThaiPhucVu
          FROM MonAn m JOIN DanhMucMon d ON d.MaDanhMucMon=m.MaDanhMucMon
          WHERE (?='' OR m.TenMon LIKE '%' + ? + '%' OR m.MaMonCode LIKE '%' + ? + '%') AND (?=0 OR m.TrangThaiPhucVu='CON_MON') ORDER BY d.TenDanhMuc,m.TenMon
          """;
        try(Connection c=Database.getConnection();PreparedStatement ps=c.prepareStatement(sql)){ps.setString(1,k);ps.setString(2,k);ps.setString(3,k);ps.setInt(4,onlyAvailable?1:0);try(ResultSet rs=ps.executeQuery()){while(rs.next())out.add(new MenuItem(rs.getInt(1),rs.getString(2),rs.getInt(3),rs.getString(4),rs.getString(5),rs.getString(6),rs.getBigDecimal(7),rs.getString(8),"CON_MON".equals(rs.getString(9))));}}return out;
    }
    public Map<Integer,String> categories()throws SQLException{Map<Integer,String> m=new LinkedHashMap<>();try(Connection c=Database.getConnection();Statement st=c.createStatement();ResultSet rs=st.executeQuery("SELECT MaDanhMucMon,TenDanhMuc FROM DanhMucMon ORDER BY TenDanhMuc")){while(rs.next())m.put(rs.getInt(1),rs.getString(2));}return m;}
    public void save(MenuItem x)throws SQLException{String sql=x.id()==0?"INSERT MonAn(MaMonCode,MaDanhMucMon,TenMon,DonViTinh,DonGia,MoTa,TrangThaiPhucVu) VALUES(?,?,?,?,?,?,?)":"UPDATE MonAn SET MaMonCode=?,MaDanhMucMon=?,TenMon=?,DonViTinh=?,DonGia=?,MoTa=?,TrangThaiPhucVu=? WHERE MaMon=?";try(Connection c=Database.getConnection();PreparedStatement ps=c.prepareStatement(sql)){ps.setString(1,x.code());ps.setInt(2,x.categoryId());ps.setString(3,x.name());ps.setString(4,x.unit());ps.setBigDecimal(5,x.price());ps.setString(6,x.description());ps.setString(7,x.available()?"CON_MON":"HET_MON");if(x.id()!=0)ps.setInt(8,x.id());ps.executeUpdate();}}
    public void toggle(int id,boolean available)throws SQLException{try(Connection c=Database.getConnection();PreparedStatement ps=c.prepareStatement("UPDATE MonAn SET TrangThaiPhucVu=? WHERE MaMon=?")){ps.setString(1,available?"CON_MON":"HET_MON");ps.setInt(2,id);ps.executeUpdate();}}
}
