package vn.hust.restaurant.config;

import javax.swing.UIManager;

public final class Theme {
    private Theme() {}
    public static void install() {
        try {
            Class<?> c = Class.forName("com.formdev.flatlaf.FlatLightLaf");
            c.getMethod("setup").invoke(null);
        } catch (Exception ex) {
            try { UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName()); }
            catch (Exception ignored) {}
        }
        UIManager.put("Button.arc", 10);
        UIManager.put("Component.arc", 8);
        UIManager.put("TextComponent.arc", 8);
        UIManager.put("Table.showHorizontalLines", true);
    }
}
