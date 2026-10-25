package vn.hust.restaurant.config;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.math.BigDecimal;
import java.nio.charset.StandardCharsets;
import java.util.Locale;
import java.util.Properties;

/** Đọc cấu hình UTF-8 và cho phép ghi đè bằng biến môi trường. */
public final class AppConfig {
    private static final Properties APP = load("application.properties");
    private static final Properties DB = load("database.properties");

    private AppConfig() {}

    private static Properties load(String name) {
        Properties properties = new Properties();
        try (InputStream in = AppConfig.class.getClassLoader().getResourceAsStream(name)) {
            if (in == null) throw new IllegalStateException("Không tìm thấy " + name);
            properties.load(new InputStreamReader(in, StandardCharsets.UTF_8));
            return properties;
        } catch (IOException e) {
            throw new IllegalStateException("Không đọc được " + name, e);
        }
    }

    public static String app(String key, String defaultValue) {
        return value(APP, key, defaultValue);
    }

    public static String db(String key, String defaultValue) {
        return value(DB, key, defaultValue);
    }

    private static String value(Properties properties, String key, String defaultValue) {
        String envKey = key.toUpperCase(Locale.ROOT).replace('.', '_');
        String environmentValue = System.getenv(envKey);
        if (environmentValue != null && !environmentValue.isBlank()) return environmentValue.trim();

        String systemValue = System.getProperty(key);
        if (systemValue != null && !systemValue.isBlank()) return systemValue.trim();

        return properties.getProperty(key, defaultValue).trim();
    }

    public static int appInt(String key, int defaultValue) {
        try { return Integer.parseInt(app(key, String.valueOf(defaultValue))); }
        catch (Exception ignored) { return defaultValue; }
    }

    public static int dbInt(String key, int defaultValue) {
        try { return Integer.parseInt(db(key, String.valueOf(defaultValue))); }
        catch (Exception ignored) { return defaultValue; }
    }

    public static boolean appBool(String key, boolean defaultValue) {
        return Boolean.parseBoolean(app(key, String.valueOf(defaultValue)));
    }

    public static BigDecimal appDecimal(String key, String defaultValue) {
        try { return new BigDecimal(app(key, defaultValue)); }
        catch (Exception ignored) { return new BigDecimal(defaultValue); }
    }

    public static String jdbcUrl() {
        return "jdbc:sqlserver://" + db("db.host", "localhost") + ':' + db("db.port", "1433")
            + ";databaseName=" + db("db.name", "HTQLNhaHang")
            + ";encrypt=" + db("db.encrypt", "true")
            + ";trustServerCertificate=" + db("db.trustServerCertificate", "true")
            + ";loginTimeout=" + db("db.loginTimeoutSeconds", "8") + ';';
    }
}
