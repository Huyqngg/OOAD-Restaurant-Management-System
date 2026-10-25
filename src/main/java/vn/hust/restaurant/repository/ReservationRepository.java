package vn.hust.restaurant.repository;

import java.sql.*;import java.time.LocalDateTime;import java.util.*;
import vn.hust.restaurant.config.Database;import vn.hust.restaurant.model.Reservation;

public class ReservationRepository {
    public List<Reservation> upcoming() throws SQLException {
        List<Reservation> out=new ArrayList<>();
        String sql="""
          SELECT d.MaDatBan,d.MaBan,b.MaBanCode,d.MaKhachHang,d.MaNhanVien,d.TenNguoiDat,d.SDTDat,d.ThoiGianDat,d.SoNguoi,d.TrangThai,d.GhiChu
          FROM DatBan d JOIN BanAn b ON b.MaBan=d.MaBan
          WHERE d.TrangThai IN('DA_DAT','DA_XAC_NHAN') AND d.ThoiGianDat>=DATEADD(DAY,-1,SYSDATETIME()) ORDER BY d.ThoiGianDat
          """;
        try(Connection c=Database.getConnection();Statement st=c.createStatement();ResultSet rs=st.executeQuery(sql)){while(rs.next())out.add(map(rs));}return out;
    }
    public long create(int tableId,Integer customerId,int employeeId,String name,String phone,LocalDateTime at,int party,String note)throws SQLException{
        return Database.inTransaction(c->{
            try(PreparedStatement ck=c.prepareStatement("SELECT SucChua,TrangThai FROM BanAn WITH(UPDLOCK,HOLDLOCK) WHERE MaBan=?")){ck.setInt(1,tableId);try(ResultSet rs=ck.executeQuery()){if(!rs.next())throw new SQLException("Bàn không tồn tại");if(rs.getInt(1)<party)throw new SQLException("Số khách vượt sức chứa");if(!"TRONG".equals(rs.getString(2)))throw new SQLException("Bàn không ở trạng thái trống");}}
            try(PreparedStatement dup=c.prepareStatement("SELECT 1 FROM DatBan WHERE MaBan=? AND TrangThai IN('DA_DAT','DA_XAC_NHAN') AND ABS(DATEDIFF(MINUTE,ThoiGianDat,?))<120")){dup.setInt(1,tableId);dup.setTimestamp(2,Timestamp.valueOf(at));try(ResultSet rs=dup.executeQuery()){if(rs.next())throw new SQLException("Bàn đã có lịch đặt gần khung giờ này");}}
            String sql="INSERT DatBan(MaBan,MaKhachHang,MaNhanVien,TenNguoiDat,SDTDat,ThoiGianDat,SoNguoi,TrangThai,GhiChu) OUTPUT INSERTED.MaDatBan VALUES(?,?,?,?,?,?,?,'DA_DAT',?)";
            long id;try(PreparedStatement ps=c.prepareStatement(sql)){ps.setInt(1,tableId);Jdbc.nullableInt(ps,2,customerId);ps.setInt(3,employeeId);ps.setString(4,name);ps.setString(5,phone);ps.setTimestamp(6,Timestamp.valueOf(at));ps.setInt(7,party);ps.setString(8,note);try(ResultSet rs=ps.executeQuery()){rs.next();id=rs.getLong(1);}}
            try(PreparedStatement ps=c.prepareStatement("UPDATE BanAn SET TrangThai='DA_DAT' WHERE MaBan=?")){ps.setInt(1,tableId);ps.executeUpdate();}
            return id;
        });
    }
    public void cancel(long id)throws SQLException{Database.inTransaction(c->{int tableId;try(PreparedStatement ps=c.prepareStatement("SELECT MaBan FROM DatBan WITH(UPDLOCK) WHERE MaDatBan=?")){ps.setLong(1,id);try(ResultSet rs=ps.executeQuery()){if(!rs.next())throw new SQLException("Không tìm thấy đặt bàn");tableId=rs.getInt(1);}}try(PreparedStatement ps=c.prepareStatement("UPDATE DatBan SET TrangThai='DA_HUY' WHERE MaDatBan=?")){ps.setLong(1,id);ps.executeUpdate();}try(PreparedStatement ps=c.prepareStatement("UPDATE BanAn SET TrangThai='TRONG' WHERE MaBan=? AND TrangThai='DA_DAT'")){ps.setInt(1,tableId);ps.executeUpdate();}return null;});}
    private Reservation map(ResultSet rs)throws SQLException{return new Reservation(rs.getLong(1),rs.getInt(2),rs.getString(3),Jdbc.integer(rs,"MaKhachHang"),rs.getInt(5),rs.getString(6),rs.getString(7),rs.getTimestamp(8).toLocalDateTime(),rs.getInt(9),rs.getString(10),rs.getString(11));}
}
