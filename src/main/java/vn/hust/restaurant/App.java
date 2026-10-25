package vn.hust.restaurant;

import javax.swing.SwingUtilities;
import vn.hust.restaurant.config.AppContext;
import vn.hust.restaurant.config.Theme;
import vn.hust.restaurant.view.LoginFrame;

public final class App {
    private App() {}
    public static void main(String[] args) {
        Thread.setDefaultUncaughtExceptionHandler((t, e) -> e.printStackTrace());
        Theme.install();
        SwingUtilities.invokeLater(() -> {
            try {
                AppContext context = new AppContext();
                new LoginFrame(context).setVisible(true);
            } catch (Exception ex) {
                ex.printStackTrace();
                javax.swing.JOptionPane.showMessageDialog(null,
                    "Không thể khởi động ứng dụng: " + ex.getMessage(),
                    "Lỗi khởi động", javax.swing.JOptionPane.ERROR_MESSAGE);
            }
        });
    }
}
