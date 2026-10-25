package vn.hust.restaurant.config;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public final class Database {
    private Database() {}
    static {
        try { Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver"); }
        catch (ClassNotFoundException ignored) { /* JDBC 4 auto-loading when Maven dependencies are present */ }
    }
    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(AppConfig.jdbcUrl(), AppConfig.db("db.user", "restaurant_app"), AppConfig.db("db.password", ""));
    }
    public static boolean testConnection() {
        try (Connection c = getConnection()) { return c.isValid(3); }
        catch (SQLException e) { return false; }
    }
    public static <T> T inTransaction(SqlWork<T> work) throws SQLException {
        try (Connection c = getConnection()) {
            boolean old = c.getAutoCommit();
            c.setAutoCommit(false);
            try {
                T result = work.run(c);
                c.commit();
                return result;
            } catch (Exception e) {
                c.rollback();
                if (e instanceof SQLException se) throw se;
                throw new SQLException(e.getMessage(), e);
            } finally { c.setAutoCommit(old); }
        }
    }
    @FunctionalInterface public interface SqlWork<T> { T run(Connection connection) throws Exception; }
}
