package view;

import controller.POSController;
import model.Product;
import util.IconManager;
import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.List;

public class ProductPanel extends JPanel {
    private POSController controller;
    private static final Color HOVER_COLOR = new Color(230, 230, 230);
    private static final Color CLICK_COLOR = new Color(200, 200, 200);

    public ProductPanel(POSController controller) {
        this.controller = controller;
        setupPanel();
        loadProducts();
    }

    private void setupPanel() {

        setLayout(new GridLayout(0, 4, 10, 10));
        setBackground(IconManager.BACKGROUND_COLOR);

        // Simple title matching the example style
        String title = "🛒 Products Menu ";
        setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createTitledBorder(
                        BorderFactory.createLineBorder(IconManager.PRIMARY_COLOR, 2),
                        title,
                        0, 0,
                        new Font(Font.SANS_SERIF, Font.BOLD, 16),
                        IconManager.PRIMARY_COLOR),
                BorderFactory.createEmptyBorder(20, 20, 20, 20)));
    }

    public void loadProducts() {
        removeAll();
        List<Product> products = controller.getProducts();

        for (Product product : products) {
            JButton productButton = createProductButton(product);
            add(productButton);
        }

        revalidate();
        repaint();
    }

    private JButton createProductButton(Product product) {
        // Get product emoji
        String emoji = IconManager.getProductEmoji(product.getName());

        // Enhanced button text with optimized spacing to prevent price cutoff
        String buttonText = String.format(
                "<html><center><div style='padding: 5px; line-height: 1.2;'>" +
                        "<div style='font-size: 36px; margin-bottom: 5px;'>%s</div>" +
                        "<div style='font-size: 13px; font-weight: bold; margin-bottom: 3px; color: #333333;'>%s</div>"
                        +
                        "<div style='font-size: 15px; color: #27ae60; font-weight: bold;'>฿%.2f</div>" +
                        "</div></center></html>",
                emoji, product.getName(), product.getPrice());

        JButton button = new JButton(buttonText);

        // Set font that supports emojis
        button.setFont(new Font(Font.SANS_SERIF, Font.PLAIN, 14));

        // Remove icon since we're using emoji in text
        button.setIcon(null);
        button.setVerticalTextPosition(SwingConstants.CENTER);
        button.setHorizontalTextPosition(SwingConstants.CENTER);

        button.setPreferredSize(new Dimension(250, 170)); // Increased height to show full price
        button.setMinimumSize(new Dimension(230, 150)); // Minimum size ensuring content fits
        button.setMaximumSize(new Dimension(270, 190)); // Maximum size for layout consistency
        button.setBackground(IconManager.CARD_COLOR);
        button.setForeground(Color.BLACK);
        button.setFont(new Font(Font.SANS_SERIF, Font.PLAIN, 12));

        // Enhanced border with reduced internal padding to maximize content space
        button.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(200, 200, 200), 1), // Simple gray border
                BorderFactory.createEmptyBorder(8, 8, 8, 8) // Reduced internal padding to fit content
        ));
        button.setFocusPainted(false);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));

        // Simple hover effects matching the clean example design
        button.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                button.setBackground(new Color(245, 245, 245)); // Light gray hover
                button.setBorder(BorderFactory.createCompoundBorder(
                        BorderFactory.createLineBorder(IconManager.PRIMARY_COLOR, 2), // Blue border on hover
                        BorderFactory.createEmptyBorder(8, 8, 8, 8)));
            }

            @Override
            public void mouseExited(MouseEvent e) {
                button.setBackground(IconManager.CARD_COLOR);
                button.setBorder(BorderFactory.createCompoundBorder(
                        BorderFactory.createLineBorder(new Color(200, 200, 200), 1), // Back to gray border
                        BorderFactory.createEmptyBorder(8, 8, 8, 8)));
            }

            @Override
            public void mousePressed(MouseEvent e) {
                button.setBackground(new Color(230, 230, 230)); // Darker gray when pressed
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                button.setBackground(new Color(245, 245, 245)); // Back to hover state
            }
        });

        // Add action listener
        button.addActionListener(e -> {
            controller.addProductToCart(product);
            // Add visual feedback
            showAddToCartFeedback(button);
        });

        return button;
    }

    private void showAddToCartFeedback(JButton button) {
        Color originalColor = button.getBackground();

        // Flash green to indicate success
        Timer timer = new Timer(100, null);
        timer.addActionListener(e -> {
            button.setBackground(IconManager.SUCCESS_COLOR);
            Timer resetTimer = new Timer(200, event -> {
                button.setBackground(originalColor);
            });
            resetTimer.setRepeats(false);
            resetTimer.start();
        });
        timer.setRepeats(false);
        timer.start();
    }
}