package util;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.Ellipse2D;
import java.awt.geom.RoundRectangle2D;
import java.awt.image.BufferedImage;
import java.util.HashMap;
import java.util.Map;

public class IconManager {
    private static final Map<String, ImageIcon> iconCache = new HashMap<>();
    private static final int ICON_SIZE = 32;
    private static final int BUTTON_ICON_SIZE = 24;

    // Color scheme for modern UI
    public static final Color PRIMARY_COLOR = new Color(52, 152, 219);
    public static final Color SUCCESS_COLOR = new Color(46, 204, 113);
    public static final Color WARNING_COLOR = new Color(241, 196, 15);
    public static final Color DANGER_COLOR = new Color(231, 76, 60);
    public static final Color SECONDARY_COLOR = new Color(149, 165, 166);
    public static final Color BACKGROUND_COLOR = new Color(236, 240, 241);
    public static final Color CARD_COLOR = Color.WHITE;

    public static ImageIcon getProductIcon(String productName) {
        String key = "product_" + productName.toLowerCase();
        if (iconCache.containsKey(key)) {
            return iconCache.get(key);
        }

        ImageIcon icon = createProductIcon(productName);
        iconCache.put(key, icon);
        return icon;
    }

    public static ImageIcon getCartIcon() {
        if (iconCache.containsKey("cart")) {
            return iconCache.get("cart");
        }

        ImageIcon icon = createCartIcon();
        iconCache.put("cart", icon);
        return icon;
    }

    public static ImageIcon getPaymentIcon() {
        if (iconCache.containsKey("payment")) {
            return iconCache.get("payment");
        }

        ImageIcon icon = createPaymentIcon();
        iconCache.put("payment", icon);
        return icon;
    }

    public static ImageIcon getPrintIcon() {
        if (iconCache.containsKey("print")) {
            return iconCache.get("print");
        }

        ImageIcon icon = createPrintIcon();
        iconCache.put("print", icon);
        return icon;
    }

    public static ImageIcon getCheckoutIcon() {
        if (iconCache.containsKey("checkout")) {
            return iconCache.get("checkout");
        }

        ImageIcon icon = createCheckoutIcon();
        iconCache.put("checkout", icon);
        return icon;
    }

    private static ImageIcon createProductIcon(String productName) {
        BufferedImage image = new BufferedImage(ICON_SIZE, ICON_SIZE, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2d = image.createGraphics();
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        // Choose color based on product name
        Color color = getColorForProduct(productName);

        // Create gradient
        GradientPaint gradient = new GradientPaint(0, 0, color, ICON_SIZE, ICON_SIZE, color.darker());
        g2d.setPaint(gradient);

        // Draw rounded rectangle
        g2d.fill(new RoundRectangle2D.Float(2, 2, ICON_SIZE - 4, ICON_SIZE - 4, 8, 8));

        // Add icon symbol based on product type
        g2d.setColor(Color.WHITE);
        g2d.setFont(new Font("Arial", Font.BOLD, 16));
        FontMetrics fm = g2d.getFontMetrics();
        String symbol = getSymbolForProduct(productName);
        int x = (ICON_SIZE - fm.stringWidth(symbol)) / 2;
        int y = (ICON_SIZE + fm.getAscent()) / 2;
        g2d.drawString(symbol, x, y);

        g2d.dispose();
        return new ImageIcon(image);
    }

    private static ImageIcon createCartIcon() {
        BufferedImage image = new BufferedImage(BUTTON_ICON_SIZE, BUTTON_ICON_SIZE, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2d = image.createGraphics();
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        g2d.setColor(PRIMARY_COLOR);
        g2d.setStroke(new BasicStroke(2.0f));

        // Draw cart body
        g2d.drawRect(4, 8, 12, 8);
        g2d.drawLine(4, 8, 6, 4);
        g2d.drawLine(6, 4, 18, 4);

        // Draw wheels
        g2d.fillOval(6, 18, 3, 3);
        g2d.fillOval(13, 18, 3, 3);

        g2d.dispose();
        return new ImageIcon(image);
    }

    private static ImageIcon createPaymentIcon() {
        BufferedImage image = new BufferedImage(BUTTON_ICON_SIZE, BUTTON_ICON_SIZE, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2d = image.createGraphics();
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        g2d.setColor(SUCCESS_COLOR);

        // Draw credit card
        g2d.fillRoundRect(2, 8, 20, 12, 4, 4);
        g2d.setColor(Color.WHITE);
        g2d.fillRect(2, 12, 20, 2);
        g2d.fillRect(4, 16, 6, 1);
        g2d.fillRect(4, 18, 8, 1);

        g2d.dispose();
        return new ImageIcon(image);
    }

    private static ImageIcon createPrintIcon() {
        BufferedImage image = new BufferedImage(BUTTON_ICON_SIZE, BUTTON_ICON_SIZE, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2d = image.createGraphics();
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        g2d.setColor(SECONDARY_COLOR);

        // Draw printer
        g2d.fillRoundRect(4, 8, 16, 10, 2, 2);
        g2d.setColor(Color.WHITE);
        g2d.fillRect(6, 10, 12, 6);

        // Draw paper
        g2d.setColor(SECONDARY_COLOR);
        g2d.fillRect(8, 4, 8, 6);
        g2d.fillRect(8, 18, 8, 4);

        g2d.dispose();
        return new ImageIcon(image);
    }

    private static ImageIcon createCheckoutIcon() {
        BufferedImage image = new BufferedImage(BUTTON_ICON_SIZE, BUTTON_ICON_SIZE, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2d = image.createGraphics();
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        g2d.setColor(SUCCESS_COLOR);
        g2d.setStroke(new BasicStroke(3.0f, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));

        // Draw checkmark
        g2d.drawLine(6, 12, 10, 16);
        g2d.drawLine(10, 16, 18, 8);

        g2d.dispose();
        return new ImageIcon(image);
    }

    private static Color getColorForProduct(String productName) {
        String lower = productName.toLowerCase();
        if (lower.contains("coffee") || lower.contains("espresso") || lower.contains("latte")
                || lower.contains("cappuccino") || lower.contains("americano") || lower.contains("macchiato")
                || lower.contains("mocha") || lower.contains("flat white") || lower.contains("iced coffee")
                || lower.contains("frappuccino") || lower.contains("turkish coffee") || lower.contains("cold brew")
                || lower.contains("cortado") || lower.contains("affogato") || lower.contains("gibraltar")
                || lower.contains("black eye") || lower.contains("red eye") || lower.contains("ristretto")
                || lower.contains("romano") || lower.contains("con panna") || lower.contains("breve")
                || lower.contains("vienna")) {
            return new Color(139, 69, 19); // Brown for coffee
        } else if (lower.contains("croissant") || lower.contains("bread") || lower.contains("pastry")) {
            return new Color(255, 215, 0); // Gold for pastries
        } else if (lower.contains("tea")) {
            return new Color(34, 139, 34); // Green for tea
        } else if (lower.contains("juice") || lower.contains("smoothie")) {
            return new Color(255, 140, 0); // Orange for drinks
        } else {
            return PRIMARY_COLOR; // Default blue
        }
    }

    private static String getSymbolForProduct(String productName) {
        String lower = productName.toLowerCase();
        if (lower.contains("coffee") || lower.contains("espresso") || lower.contains("latte")
                || lower.contains("cappuccino") || lower.contains("americano") || lower.contains("macchiato")
                || lower.contains("mocha") || lower.contains("flat white") || lower.contains("iced coffee")
                || lower.contains("frappuccino") || lower.contains("turkish coffee") || lower.contains("cold brew")
                || lower.contains("cortado") || lower.contains("affogato") || lower.contains("gibraltar")
                || lower.contains("black eye") || lower.contains("red eye") || lower.contains("ristretto")
                || lower.contains("romano") || lower.contains("con panna") || lower.contains("breve")
                || lower.contains("vienna")) {
            return "☕";
        } else if (lower.contains("croissant") || lower.contains("bread") || lower.contains("pastry")) {
            return "🥐";
        } else if (lower.contains("tea")) {
            return "🍵";
        } else if (lower.contains("juice") || lower.contains("smoothie")) {
            return "🥤";
        } else {
            return "🍽";
        }
    }

    // Emoji methods for getting emoji strings instead of icons
    public static String getProductEmoji(String productName) {
        return getSymbolForProduct(productName);
    }

    public static String getCartEmoji() {
        return "🛒";
    }

    public static String getPaymentEmoji() {
        return "💳";
    }

    public static String getPrintEmoji() {
        return "🖨️";
    }

    public static String getCheckoutEmoji() {
        return "✅";
    }

    public static String getMoneyEmoji() {
        return "💰";
    }

    public static String getReceiptEmoji() {
        return "🧾";
    }
}