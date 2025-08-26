package view;

import controller.POSController;
import util.IconManager;
import javax.swing.*;
import java.awt.*;

public class MainFrame extends JFrame {
    private POSController controller;
    private ProductPanel productPanel;
    private CartPanel cartPanel;
    private JLabel dateTimeLabel;

    public MainFrame() {
        initializeFrame();
        initializeComponents();
        setupLayout();
        startRealTimeClock();
        setVisible(true);
    }

    private void initializeFrame() {
        setTitle("🛒 Coffee POS System");
        setSize(1200, 800);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setExtendedState(JFrame.MAXIMIZED_BOTH);

        // Set modern look and feel
        try {
            for (UIManager.LookAndFeelInfo info : UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (Exception e) {
            // Use default look and feel
        }

        // Set application icon (keep this for window icon)
        try {
            setIconImage(IconManager.getCartIcon().getImage());
        } catch (Exception e) {
            // Icon not available, continue without it
        }

        // Set background color
        getContentPane().setBackground(IconManager.BACKGROUND_COLOR);
    }

    private void initializeComponents() {
        controller = new POSController(this);
        productPanel = new ProductPanel(controller);
        cartPanel = new CartPanel(controller);
    }

    private void setupLayout() {
        setLayout(new BorderLayout());

        // Create and set menu bar
        setJMenuBar(createMenuBar());

        // Create header panel
        JPanel headerPanel = createHeaderPanel();
        add(headerPanel, BorderLayout.NORTH);

        // Create main content panel with modern styling
        JPanel mainPanel = new JPanel(new BorderLayout(15, 15));
        mainPanel.setBackground(IconManager.BACKGROUND_COLOR);
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        // Add components with modern styling
        mainPanel.add(productPanel, BorderLayout.CENTER);
        mainPanel.add(cartPanel, BorderLayout.EAST);

        add(mainPanel, BorderLayout.CENTER);

        // Create footer panel
        JPanel footerPanel = createFooterPanel();
        add(footerPanel, BorderLayout.SOUTH);
    }

    private JPanel createHeaderPanel() {
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBackground(IconManager.PRIMARY_COLOR);
        headerPanel.setPreferredSize(new Dimension(0, 80));
        headerPanel.setBorder(BorderFactory.createEmptyBorder(15, 25, 15, 25));

        // Title with emoji
        String cartEmoji = IconManager.getCartEmoji();
        JLabel titleLabel = new JLabel(cartEmoji + " Coffee POS System", JLabel.LEFT);
        titleLabel.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 28));
        titleLabel.setForeground(Color.WHITE);

        // Date/Time label
        dateTimeLabel = new JLabel(java.time.LocalDateTime.now().format(
                java.time.format.DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss")));
        dateTimeLabel.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        dateTimeLabel.setForeground(Color.WHITE);

        headerPanel.add(titleLabel, BorderLayout.WEST);
        headerPanel.add(dateTimeLabel, BorderLayout.EAST);

        return headerPanel;
    }

    private JPanel createFooterPanel() {
        JPanel footerPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        footerPanel.setBackground(IconManager.SECONDARY_COLOR);
        footerPanel.setPreferredSize(new Dimension(0, 40));

        JLabel footerLabel = new JLabel("© 2024 Coffe POS System - Ready to serve!");
        footerLabel.setFont(new Font("Segoe UI", Font.ITALIC, 12));
        footerLabel.setForeground(Color.WHITE);

        footerPanel.add(footerLabel);
        return footerPanel;
    }

    private JMenuBar createMenuBar() {
        JMenuBar menuBar = new JMenuBar();

        // Sales menu
        JMenu salesMenu = new JMenu("📊 ยอดขาย");
        salesMenu.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 14));

        JMenuItem dailySummaryItem = new JMenuItem("📄 สรุปยอดขายวันนี้");
        dailySummaryItem.setFont(new Font(Font.SANS_SERIF, Font.PLAIN, 12));
        dailySummaryItem.addActionListener(e -> controller.showDailySalesSummary());

        JMenuItem salesLocationItem = new JMenuItem("📁 ตำแหน่งไฟล์ข้อมูล");
        salesLocationItem.setFont(new Font(Font.SANS_SERIF, Font.PLAIN, 12));
        salesLocationItem.addActionListener(e -> controller.showSalesDataLocation());

        salesMenu.add(dailySummaryItem);
        salesMenu.addSeparator();
        salesMenu.add(salesLocationItem);

        // Help menu
        JMenu helpMenu = new JMenu("❓ ความช่วยเหลือ");
        helpMenu.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 14));

        JMenuItem aboutItem = new JMenuItem("ℹ️ เกี่ยวกับโปรแกรม");
        aboutItem.setFont(new Font(Font.SANS_SERIF, Font.PLAIN, 12));
        aboutItem.addActionListener(e -> showAboutDialog());

        helpMenu.add(aboutItem);

        menuBar.add(salesMenu);
        menuBar.add(helpMenu);

        return menuBar;
    }

    private void showAboutDialog() {
        JOptionPane.showMessageDialog(this,
                "<html><body style='width: 300px'>" +
                        "<h2>☕ Coffee POS System</h2>" +
                        "<p><b>เวอร์ชัน:</b> DEMO </p>" +
                        "<p><b>คุณสมบัติใหม่:</b></p>" +
                        "<ul>" +
                        "<li>📊 ระบบติดตามยอดขาย</li>" +
                        "<li>📄 รายงานสรุปรายวัน</li>" +
                        "<li>📁 บันทึกข้อมูลเป็นไฟล์ TXT</li>" +
                        "</ul>" +
                        "<p><i>ระบบจุดขายสำหรับร้านกาแฟ<br>พัฒนาด้วย Java Swing</i></p>" +
                        "</body></html>",
                "About Coffee POS System",
                JOptionPane.INFORMATION_MESSAGE);
    }

    public void updateCartView(java.util.HashMap<Integer, model.CartItem> cartItems) {
        cartPanel.updateCartTable(cartItems);
    }

    public CartPanel getCartPanel() {
        return cartPanel;
    }

    public void refreshProducts() {
        productPanel.loadProducts();
    }

    private void startRealTimeClock() {
        Timer timer = new Timer(1000, e -> {
            if (dateTimeLabel != null) {
                dateTimeLabel.setText(java.time.LocalDateTime.now().format(
                        java.time.format.DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss")));
            }
        });
        timer.start();
    }
}