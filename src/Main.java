import view.MainFrame;
import javax.swing.SwingUtilities;

public class Main {
    public static void main(String[] args) {

        System.setProperty("awt.useSystemAAFontSettings", "on");
        System.setProperty("swing.aatext", "true");

        SwingUtilities.invokeLater(() -> {
            new MainFrame();
        });
    }
}