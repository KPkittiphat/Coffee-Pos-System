package util;

import model.CartItem;
import javax.swing.*;
import java.awt.*;
import java.awt.print.*;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class ReceiptPrinter implements Printable {
    private HashMap<Integer, CartItem> cartItems;
    private double totalAmount;
    private double receivedAmount;
    private double changeAmount;
    private String receiptContent;
    private static final String STORE_NAME = "Coffee POS Store";
    private static final String STORE_ADDRESS = "Nonthaburi, Thailand";
    private static final String STORE_PHONE = "Tel: +66 640-297-030";
    private static final String STORE_EMAIL = "Kittiphatphengnamkham@gmail.com";

    public ReceiptPrinter(HashMap<Integer, CartItem> cartItems, double totalAmount,
            double receivedAmount, double changeAmount) {
        this.cartItems = cartItems;
        this.totalAmount = totalAmount;
        this.receivedAmount = receivedAmount;
        this.changeAmount = changeAmount;
        this.receiptContent = generateReceiptContent();
    }

    private String generateReceiptContent() {
        StringBuilder receipt = new StringBuilder();
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
        String currentDateTime = dateFormat.format(new Date());

        // Header
        receipt.append(repeatString("=", 50)).append("\n");
        receipt.append(centerText(STORE_NAME, 50)).append("\n");
        receipt.append(centerText(STORE_ADDRESS, 50)).append("\n");
        receipt.append(centerText(STORE_PHONE, 50)).append("\n");
        receipt.append(centerText(STORE_EMAIL, 50)).append("\n");
        receipt.append(repeatString("=", 50)).append("\n");
        receipt.append(centerText("SALES RECEIPT", 50)).append("\n");
        receipt.append(repeatString("=", 50)).append("\n");

        // Date and Receipt Number
        receipt.append(String.format("Date: %s\n", currentDateTime));
        receipt.append(String.format("Receipt#: POS%d\n", System.currentTimeMillis() % 100000));
        receipt.append(repeatString("-", 50)).append("\n");

        // Items Header
        receipt.append(String.format("%-20s %5s %8s %12s\n", "Item", "Qty", "Price", "Total"));
        receipt.append(repeatString("-", 50)).append("\n");

        // Items
        for (Map.Entry<Integer, CartItem> entry : cartItems.entrySet()) {
            CartItem item = entry.getValue();
            String itemName = item.getProduct().getName();
            if (itemName.length() > 20) {
                itemName = itemName.substring(0, 17) + "...";
            }

            receipt.append(String.format("%-20s %5d %8.2f %12.2f\n",
                    itemName,
                    item.getQuantity(),
                    item.getProduct().getPrice(),
                    item.getTotalPrice()));
        }

        receipt.append(repeatString("-", 50)).append("\n");

        // Totals
        int totalItems = cartItems.values().stream().mapToInt(CartItem::getQuantity).sum();
        receipt.append(String.format("Total Items: %d\n", totalItems));
        receipt.append(String.format("Subtotal: ฿%.2f\n", totalAmount));
        receipt.append(String.format("Tax (7%%): ฿%.2f\n", totalAmount * 0.07));
        receipt.append(String.format("TOTAL: ฿%.2f\n", totalAmount * 1.07));
        receipt.append(repeatString("-", 50)).append("\n");

        // Payment
        receipt.append(String.format("Cash Received: ฿%.2f\n", receivedAmount));
        receipt.append(String.format("Change: ฿%.2f\n", changeAmount));
        receipt.append(repeatString("=", 50)).append("\n");

        // Footer
        receipt.append(centerText("Thank You for Your Purchase!", 50)).append("\n");
        receipt.append(centerText("Have a Great Day!", 50)).append("\n");
        receipt.append(centerText("Visit us again soon!", 50)).append("\n");
        receipt.append(repeatString("=", 50)).append("\n");
        receipt.append(centerText("** This is a computer generated receipt **", 50)).append("\n");
        receipt.append(centerText("No signature required", 50)).append("\n");

        return receipt.toString();
    }

    private String centerText(String text, int width) {
        if (text.length() >= width) {
            return text;
        }
        int padding = (width - text.length()) / 2;
        return repeatString(" ", padding) + text;
    }

    // Helper method to replace String.repeat() for Java compatibility
    private String repeatString(String str, int count) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < count; i++) {
            sb.append(str);
        }
        return sb.toString();
    }

    public void showReceiptPreview(Component parent) {
        JDialog previewDialog = new JDialog((Frame) SwingUtilities.getWindowAncestor(parent),
                "Receipt Preview", true);
        previewDialog.setSize(500, 700);
        previewDialog.setLocationRelativeTo(parent);

        // Create text area with receipt content
        JTextArea textArea = new JTextArea(receiptContent);
        textArea.setFont(new Font("Courier New", Font.PLAIN, 11));
        textArea.setEditable(false);
        textArea.setBackground(Color.WHITE);
        textArea.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JScrollPane scrollPane = new JScrollPane(textArea);

        // Buttons panel
        JPanel buttonPanel = new JPanel(new FlowLayout());
        buttonPanel.setBackground(IconManager.BACKGROUND_COLOR);

        String printEmoji = IconManager.getPrintEmoji();
        JButton printButton = new JButton(printEmoji + " Print");
        printButton.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 12));
        printButton.setBackground(IconManager.PRIMARY_COLOR);
        printButton.setForeground(Color.WHITE);
        printButton.setBorder(BorderFactory.createEmptyBorder(8, 15, 8, 15));
        printButton.setFocusPainted(false);
        printButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        printButton.addActionListener(e -> {
            printReceipt(parent);
            previewDialog.dispose();
        });

        JButton saveButton = new JButton("💾 Save");
        saveButton.setFont(new Font(Font.SANS_SERIF, Font.PLAIN, 12));
        saveButton.setBackground(IconManager.SUCCESS_COLOR);
        saveButton.setForeground(Color.WHITE);
        saveButton.setBorder(BorderFactory.createEmptyBorder(8, 15, 8, 15));
        saveButton.setFocusPainted(false);
        saveButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        saveButton.addActionListener(e -> {
            saveReceiptToFile(parent);
        });

        JButton closeButton = new JButton("❌ Close");
        closeButton.setFont(new Font(Font.SANS_SERIF, Font.PLAIN, 12));
        closeButton.setBackground(IconManager.SECONDARY_COLOR);
        closeButton.setForeground(Color.WHITE);
        closeButton.setBorder(BorderFactory.createEmptyBorder(8, 15, 8, 15));
        closeButton.setFocusPainted(false);
        closeButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        closeButton.addActionListener(e -> previewDialog.dispose());

        buttonPanel.add(printButton);
        buttonPanel.add(saveButton);
        buttonPanel.add(closeButton);

        previewDialog.add(scrollPane, BorderLayout.CENTER);
        previewDialog.add(buttonPanel, BorderLayout.SOUTH);

        previewDialog.setVisible(true);
    }

    public void printReceipt(Component parent) {
        try {
            PrinterJob job = PrinterJob.getPrinterJob();
            job.setPrintable(this);

            // Show print dialog
            if (job.printDialog()) {
                job.print();
                JOptionPane.showMessageDialog(parent,
                        "Receipt sent to printer successfully!",
                        "Print Success",
                        JOptionPane.INFORMATION_MESSAGE);
            }
        } catch (PrinterException e) {
            JOptionPane.showMessageDialog(parent,
                    "Failed to print receipt: " + e.getMessage(),
                    "Print Error",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    public void saveReceiptToFile(Component parent) {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle("Save Receipt");
        fileChooser.setSelectedFile(new java.io.File("receipt_" +
                new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date()) + ".txt"));

        int userSelection = fileChooser.showSaveDialog(parent);

        if (userSelection == JFileChooser.APPROVE_OPTION) {
            try (FileWriter writer = new FileWriter(fileChooser.getSelectedFile())) {
                writer.write(receiptContent);
                JOptionPane.showMessageDialog(parent,
                        "Receipt saved successfully!",
                        "Save Success",
                        JOptionPane.INFORMATION_MESSAGE);
            } catch (IOException e) {
                JOptionPane.showMessageDialog(parent,
                        "Failed to save receipt: " + e.getMessage(),
                        "Save Error",
                        JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    @Override
    public int print(Graphics graphics, PageFormat pageFormat, int pageIndex) throws PrinterException {
        if (pageIndex > 0) {
            return NO_SUCH_PAGE;
        }

        Graphics2D g2d = (Graphics2D) graphics;
        g2d.translate(pageFormat.getImageableX(), pageFormat.getImageableY());

        // Set font for printing
        Font font = new Font("Courier New", Font.PLAIN, 8);
        g2d.setFont(font);
        FontMetrics fm = g2d.getFontMetrics();

        // Print each line
        String[] lines = receiptContent.split("\n");
        int lineHeight = fm.getHeight();
        int y = lineHeight;

        for (String line : lines) {
            g2d.drawString(line, 0, y);
            y += lineHeight;

            // Check if we need a new page (simple implementation)
            if (y > pageFormat.getImageableHeight() - lineHeight) {
                break;
            }
        }

        return PAGE_EXISTS;
    }

    public String getReceiptContent() {
        return receiptContent;
    }
}