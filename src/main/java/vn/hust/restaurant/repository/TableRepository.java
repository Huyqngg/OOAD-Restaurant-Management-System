package vn.hust.restaurant.repository;

import java.sql.*;
import java.util.*;
import vn.hust.restaurant.config.Database;
import vn.hust.restaurant.model.DiningTable;

public class TableRepository {
    public List<DiningTable> findAll() throws SQLException {
        List<DiningTable> out=new ArrayList<>();
        String sql="SELECT MaBan,MaBanCode,TenBan,KhuVuc,SucChua,TrangThai FROM BanAn ORDER BY KhuVuc,MaBanCode";
        try(Connection c=Database.getConnection();Statement st=c.createStatement();ResultSet rs=st.executeQuery(sql)){
            while(rs.next())out.add(new DiningTable(rs.getInt(1),rs.getString(2),rs.getString(3),rs.getString(4),rs.getInt(5),rs.getString(6)));
        } return out;
    }
    public List<DiningTable> findAvailable(String area,int capacity) throws SQLException {
        List<DiningTable> out=new ArrayList<>();
        String sql="SELECT MaBan,MaBanCode,TenBan,KhuVuc,SucChua,TrangThai FROM BanAn WHERE TrangThai='TRONG' AND (?='' OR KhuVuc=?) AND SucChua>=? ORDER BY SucChua";
        try(Connection c=Database.getConnection();PreparedStatement ps=c.prepareStatement(sql)){
            ps.setString(1,area==null?"":area);ps.setString(2,area==null?"":area);ps.setInt(3,capacity);
            try(ResultSet rs=ps.executeQuery()){while(rs.next())out.add(new DiningTable(rs.getInt(1),rs.getString(2),rs.getString(3),rs.getString(4),rs.getInt(5),rs.getString(6)));}
        } return out;
    }
    public void save(DiningTable t) throws SQLException {
        if(t.id()==0){
            String sql="INSERT BanAn(MaBanCode,TenBan,KhuVuc,SucChua,TrangThai) VALUES(?,?,?,?,?)";
            try(Connection c=Database.getConnection();PreparedStatement ps=c.prepareStatement(sql)){bind(ps,t,false);ps.executeUpdate();}
        } else {
            String sql="UPDATE BanAn SET MaBanCode=?,TenBan=?,KhuVuc=?,SucChua=?,TrangThai=? WHERE MaBan=?";
            try(Connection c=Database.getConnection();PreparedStatement ps=c.prepareStatement(sql)){bind(ps,t,false);ps.setInt(6,t.id());ps.executeUpdate();}
        }
    }
    private void bind(PreparedStatement ps,DiningTable t,boolean dummy)throws SQLException{ps.setString(1,t.code());ps.setString(2,t.name());ps.setString(3,t.area());ps.setInt(4,t.capacity());ps.setString(5,t.status());}
    public void changeStatus(int id,String status)throws SQLException{try(Connection c=Database.getConnection();PreparedStatement ps=c.prepareStatement("UPDATE BanAn SET TrangThai=? WHERE MaBan=?")){ps.setString(1,status);ps.setInt(2,id);ps.executeUpdate();}}
    public void deactivate(int id)throws SQLException{changeStatus(id,"NGUNG_SU_DUNG");}
}
