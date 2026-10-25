package vn.hust.restaurant.repository;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.Optional;
import vn.hust.restaurant.config.Database;
import vn.hust.restaurant.model.UserSession;

public class AccountRepository {
    public record AccountData(int accountId, int employeeId, String username, String passwordHash,
                              String accountStatus, int failedAttempts, LocalDateTime lockedUntil,
                              String fullName, String role, String email, String phone) {}

    public Optional<AccountData> findByUsername(String username) throws SQLException {
        String sql="""
            SELECT tk.MaTaiKhoan,tk.MaNhanVien,tk.TenDangNhap,tk.MatKhauHash,tk.TrangThai,
                   tk.SoLanDangNhapSai,tk.KhoaDen,nv.HoTen,nv.VaiTro,nv.Email,nv.SDT
            FROM TaiKhoan tk JOIN NhanVien nv ON nv.MaNhanVien=tk.MaNhanVien
            WHERE tk.TenDangNhap=?
            """;
        try(Connection c=Database.getConnection(); PreparedStatement ps=c.prepareStatement(sql)){
            ps.setString(1,username);
            try(ResultSet rs=ps.executeQuery()){
                if(!rs.next()) return Optional.empty();
                return Optional.of(new AccountData(rs.getInt(1),rs.getInt(2),rs.getString(3),rs.getString(4),
                    rs.getString(5),rs.getInt(6),Jdbc.dateTime(rs,"KhoaDen"),rs.getString(8),rs.getString(9),rs.getString(10),rs.getString(11)));
            }
        }
    }
    public Optional<AccountData> findByIdentity(String identity) throws SQLException {
        String sql="""
            SELECT TOP 1 tk.MaTaiKhoan,tk.MaNhanVien,tk.TenDangNhap,tk.MatKhauHash,tk.TrangThai,
                   tk.SoLanDangNhapSai,tk.KhoaDen,nv.HoTen,nv.VaiTro,nv.Email,nv.SDT
            FROM TaiKhoan tk JOIN NhanVien nv ON nv.MaNhanVien=tk.MaNhanVien
            WHERE tk.TenDangNhap=? OR nv.Email=? OR nv.SDT=?
            """;
        try(Connection c=Database.getConnection(); PreparedStatement ps=c.prepareStatement(sql)){
            ps.setString(1,identity); ps.setString(2,identity); ps.setString(3,identity);
            try(ResultSet rs=ps.executeQuery()){
                if(!rs.next()) return Optional.empty();
                return Optional.of(new AccountData(rs.getInt(1),rs.getInt(2),rs.getString(3),rs.getString(4),
                    rs.getString(5),rs.getInt(6),Jdbc.dateTime(rs,"KhoaDen"),rs.getString(8),rs.getString(9),rs.getString(10),rs.getString(11)));
            }
        }
    }
    public void recordSuccess(int accountId) throws SQLException {
        update("UPDATE TaiKhoan SET SoLanDangNhapSai=0,KhoaDen=NULL,LanDangNhapCuoi=SYSDATETIME() WHERE MaTaiKhoan=?",accountId);
    }
    public void recordFailure(int accountId, int failures) throws SQLException {
        String sql=failures>=5?
            "UPDATE TaiKhoan SET SoLanDangNhapSai=?,KhoaDen=DATEADD(MINUTE,15,SYSDATETIME()) WHERE MaTaiKhoan=?":
            "UPDATE TaiKhoan SET SoLanDangNhapSai=? WHERE MaTaiKhoan=?";
        try(Connection c=Database.getConnection(); PreparedStatement ps=c.prepareStatement(sql)){
            ps.setInt(1,failures); ps.setInt(2,accountId); ps.executeUpdate();
        }
    }
    public void saveOtp(int accountId,String otp,LocalDateTime expiry) throws SQLException {
        String sql="UPDATE TaiKhoan SET MaOTP=?,HanOTP=?,SoLanNhapSaiOTP=0 WHERE MaTaiKhoan=?";
        try(Connection c=Database.getConnection(); PreparedStatement ps=c.prepareStatement(sql)){
            ps.setString(1,otp); ps.setTimestamp(2,Timestamp.valueOf(expiry)); ps.setInt(3,accountId); ps.executeUpdate();
        }
    }
    public boolean verifyOtp(int accountId,String otp) throws SQLException {
        return Database.inTransaction(c->{
            String q="SELECT MaOTP,HanOTP,SoLanNhapSaiOTP FROM TaiKhoan WITH (UPDLOCK,HOLDLOCK) WHERE MaTaiKhoan=?";
            try(PreparedStatement ps=c.prepareStatement(q)){ ps.setInt(1,accountId); try(ResultSet rs=ps.executeQuery()){
                if(!rs.next()) return false;
                int tries=rs.getInt(3); Timestamp expiry=rs.getTimestamp(2); String stored=rs.getString(1);
                boolean ok=stored!=null&&stored.equals(otp)&&expiry!=null&&expiry.toLocalDateTime().isAfter(LocalDateTime.now())&&tries<5;
                try(PreparedStatement up=c.prepareStatement(ok?
                    "UPDATE TaiKhoan SET MaOTP=NULL,HanOTP=NULL,SoLanNhapSaiOTP=0 WHERE MaTaiKhoan=?":
                    "UPDATE TaiKhoan SET SoLanNhapSaiOTP=SoLanNhapSaiOTP+1 WHERE MaTaiKhoan=?")){
                    up.setInt(1,accountId); up.executeUpdate();
                }
                return ok;
            }}
        });
    }
    public void updatePassword(int accountId,String hash) throws SQLException {
        String sql="UPDATE TaiKhoan SET MatKhauHash=?,MaOTP=NULL,HanOTP=NULL,SoLanDangNhapSai=0,KhoaDen=NULL WHERE MaTaiKhoan=?";
        try(Connection c=Database.getConnection();PreparedStatement ps=c.prepareStatement(sql)){ps.setString(1,hash);ps.setInt(2,accountId);ps.executeUpdate();}
    }
    public UserSession toSession(AccountData a){ return new UserSession(a.accountId(),a.employeeId(),a.username(),a.fullName(),a.role()); }
    private void update(String sql,int id)throws SQLException{try(Connection c=Database.getConnection();PreparedStatement ps=c.prepareStatement(sql)){ps.setInt(1,id);ps.executeUpdate();}}
}
