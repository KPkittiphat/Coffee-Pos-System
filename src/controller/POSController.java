package controller;

import model.CartItem;
import model.CSVManager;
import model.Product;
import util.ReceiptPrinter;
import util.SalesTracker;
import view.MainFrame;
import view.PaymentDialog;
import view.SalesSummaryDialog;
import javax.swing.JOptionPane;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class POSController {
    private MainFrame mainFrame;
    private CSVManager csvManager;
    private HashMap<Integer, CartItem> currentCart;
    private ReceiptPrinter lastReceiptPrinter;
    private double lastReceivedAmount;
    private SalesTracker salesTracker;

    public POSController(MainFrame mainFrame) {
        this.mainFrame = mainFrame;
        this.csvManager = new CSVManager();
        this.currentCart = new HashMap<>();
        this.salesTracker = new SalesTracker();
    }

    public List<Product> getProducts() {
        return csvManager.getAllProducts();
    }

    public void addProductToCart(Product product) {
        if (currentCart.containsKey(product.getId())) {
            currentCart.get(product.getId()).incrementQuantity();
        } else {
            currentCart.put(product.getId(), new CartItem(product));
        }
        mainFrame.updateCartView(currentCart);
    }

    public void removeFromCart(String productName) {
        System.out.println("[DEBUG] Attempting to remove product: " + productName);
        System.out.println("[DEBUG] Current cart size: " + currentCart.size());

        // Find the product by name and remove it from cart
        Integer productIdToRemove = null;
        for (Map.Entry<Integer, CartItem> entry : currentCart.entrySet()) {
            if (entry.getValue().getProduct().getName().equals(productName)) {
                productIdToRemove = entry.getKey();
                System.out.println("[DEBUG] Found product with ID: " + productIdToRemove);
                break;
            }
        }

        if (productIdToRemove != null) {
            CartItem item = currentCart.get(productIdToRemove);
            System.out.println("[DEBUG] Current quantity: " + item.getQuantity());

            if (item.getQuantity() > 1) {
                // If quantity > 1, just decrease quantity
                item.decrementQuantity();
                System.out.println("[DEBUG] Decremented quantity to: " + item.getQuantity());
            } else {
                // If quantity = 1, remove the item completely
                currentCart.remove(productIdToRemove);
                System.out.println("[DEBUG] Removed item completely from cart");
            }

            // Update the cart view
            System.out.println("[DEBUG] Updating cart view...");
            mainFrame.updateCartView(currentCart);
            System.out.println("[DEBUG] Cart updated successfully");
        } else {
            System.err.println("[ERROR] Product not found in cart: " + productName);
            // Show error dialog
            JOptionPane.showMessageDialog(mainFrame,
                    "Product '" + productName + "' not found in cart!",
                    "Remove Error",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    public void showPaymentDialog() {
        double total = mainFrame.getCartPanel().getTotalAmount();
        if (total > 0) {
            PaymentDialog dialog = new PaymentDialog(mainFrame, this, total);
            dialog.setVisible(true);
        } else {
            JOptionPane.showMessageDialog(mainFrame,
                    "Cart is empty! Please add some items first.",
                    "Empty Cart",
                    JOptionPane.WARNING_MESSAGE);
        }
    }

    public void completeSale() {
        completeSale(0.0); // Default with no received amount for backwards compatibility
    }

    public void completeSale(double receivedAmount) {
        if (currentCart.isEmpty()) {
            JOptionPane.showMessageDialog(mainFrame,
                    "Cannot complete sale: Cart is empty!",
                    "Sale Error",
                    JOptionPane.ERROR_MESSAGE);
            return;
        }

        // Store received amount for receipt
        this.lastReceivedAmount = receivedAmount;

        try {
            // Create receipt printer for later use
            double totalAmount = mainFrame.getCartPanel().getTotalAmount();
            double changeAmount = receivedAmount > 0 ? receivedAmount - totalAmount : 0;
            lastReceiptPrinter = new ReceiptPrinter(
                    new HashMap<>(currentCart), // Create copy for receipt
                    totalAmount,
                    receivedAmount,
                    changeAmount);

            // Record the sale in sales tracker
            salesTracker.recordSale(new HashMap<>(currentCart), totalAmount, receivedAmount, changeAmount);

            // Generate daily summary after each sale
            salesTracker.generateDailySummary();

            // Clear the cart
            currentCart.clear();
            mainFrame.getCartPanel().clearCart();

            // Show success message
            JOptionPane.showMessageDialog(mainFrame,
                    "Payment Successful!\nThank you for your purchase!",
                    "Sale Complete",
                    JOptionPane.INFORMATION_MESSAGE);

        } catch (Exception e) {
            JOptionPane.showMessageDialog(mainFrame,
                    "Error completing sale: " + e.getMessage(),
                    "Sale Error",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    public void printReceipt() {
        if (lastReceiptPrinter != null) {
            lastReceiptPrinter.showReceiptPreview(mainFrame);
        } else {
            JOptionPane.showMessageDialog(mainFrame,
                    "No recent transaction found to print receipt.",
                    "No Receipt Available",
                    JOptionPane.WARNING_MESSAGE);
        }
    }

    public void setReceivedAmount(double receivedAmount) {
        this.lastReceivedAmount = receivedAmount;
    }

    public HashMap<Integer, CartItem> getCurrentCart() {
        return new HashMap<>(currentCart); // Return copy to prevent external modification
    }

    public boolean hasItemsInCart() {
        return !currentCart.isEmpty();
    }

    public SalesTracker getSalesTracker() {
        return salesTracker;
    }

    public void showDailySalesSummary() {
        try {
            // Generate daily summary to ensure latest data
            salesTracker.generateDailySummary();

            // Show the comprehensive sales summary dialog
            SalesSummaryDialog dialog = new SalesSummaryDialog(mainFrame);
            dialog.setVisible(true);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(mainFrame,
                    "เกิดข้อผิดพลาดในการสร้างรายงาน: " + e.getMessage(),
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    public void showQuickSalesSummary() {
        try {
            // Generate daily summary to ensure latest data
            salesTracker.generateDailySummary();

            // Get today's sales data
            java.util.List<model.Sale> todaysSales = salesTracker.getTodaysSales();

            if (todaysSales.isEmpty()) {
                JOptionPane.showMessageDialog(mainFrame,
                        "<html><div style='text-align: center; font-family: sans-serif; padding: 20px;'>" +
                                "<h2 style='color: #3498db;'>📊 สรุปยอดขายวันนี้</h2>" +
                                "<p style='font-size: 16px; color: #e74c3c;'>🚫 ยังไม่มียอดขายในวันนี้</p>" +
                                "<p style='font-size: 14px; color: #7f8c8d;'>เมื่อมีการขาย ข้อมูลจะแสดงที่นี่</p>" +
                                "</div></html>",
                        "สรุปยอดขายด่วน",
                        JOptionPane.INFORMATION_MESSAGE);
                return;
            }

            // Calculate total revenue
            double totalRevenue = 0.0;
            java.util.Map<String, Integer> itemSummary = new java.util.HashMap<>();

            for (model.Sale sale : todaysSales) {
                totalRevenue += sale.getTotalAmount();
                for (java.util.Map.Entry<String, Integer> entry : sale.getItems().entrySet()) {
                    itemSummary.merge(entry.getKey(), entry.getValue(), Integer::sum);
                }
            }

            // Get top 3 best-selling items
            java.util.List<java.util.Map.Entry<String, Integer>> topItems = itemSummary.entrySet().stream()
                    .sorted(java.util.Map.Entry.<String, Integer>comparingByValue().reversed())
                    .limit(3)
                    .collect(java.util.stream.Collectors.toList());

            // Build quick summary message
            StringBuilder summary = new StringBuilder();
            summary.append("<html><div style='text-align: center; font-family: sans-serif; padding: 20px;'>");
            summary.append("<h2 style='color: #2c3e50; margin-bottom: 20px;'>📊 สรุปยอดขายวันนี้</h2>");

            // Total revenue
            summary.append(
                    "<div style='background: #ecf0f1; padding: 15px; border-radius: 8px; margin-bottom: 20px;'>");
            summary.append("<h3 style='color: #27ae60; margin: 0;'>💰 ยอดขายรวม</h3>");
            summary.append("<p style='font-size: 24px; font-weight: bold; color: #27ae60; margin: 10px 0;'>");
            summary.append(String.format("฿%.2f", totalRevenue));
            summary.append("</p>");
            summary.append("<p style='font-size: 14px; color: #7f8c8d; margin: 0;'>");
            summary.append(String.format("จากธุรกรรม %d ครั้ง", todaysSales.size()));
            summary.append("</p></div>");

            // Top 3 best-selling items
            if (!topItems.isEmpty()) {
                summary.append("<div style='background: #ecf0f1; padding: 15px; border-radius: 8px;'>");
                summary.append("<h3 style='color: #3498db; margin: 0 0 15px 0;'>🏆 สินค้าขายดี TOP 3</h3>");

                for (int i = 0; i < topItems.size(); i++) {
                    java.util.Map.Entry<String, Integer> item = topItems.get(i);
                    String medal = i == 0 ? "🥇" : i == 1 ? "🥈" : "🥉";
                    summary.append("<p style='font-size: 16px; margin: 8px 0; text-align: left;'>");
                    summary.append(String.format("%s <b>%s</b> (%d แก้ว)", medal, item.getKey(), item.getValue()));
                    summary.append("</p>");
                }
                summary.append("</div>");
            }

            summary.append("</div></html>");

            // Show quick summary popup
            JOptionPane.showMessageDialog(mainFrame,
                    summary.toString(),
                    "สรุปยอดขายด่วน - " + java.time.LocalDate.now().format(
                            java.time.format.DateTimeFormatter.ofPattern("dd/MM/yyyy")),
                    JOptionPane.INFORMATION_MESSAGE);

        } catch (Exception e) {
            JOptionPane.showMessageDialog(mainFrame,
                    "เกิดข้อผิดพลาดในการคำนวณยอดขาย: " + e.getMessage(),
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    public void showSalesDataLocation() {
        String salesPath = salesTracker.getDailySalesFilePath();
        String summaryPath = salesTracker.getDailySummaryFilePath();

        JOptionPane.showMessageDialog(mainFrame,
                "<html><body style='width: 400px'>" +
                        "📊 <b>ตำแหน่งไฟล์ข้อมูลการขาย</b><br><br>" +
                        "🗂️ <b>รายละเอียดการขายวันนี้:</b><br>" +
                        "<code>" + salesPath + "</code><br><br>" +
                        "📋 <b>สรุปยอดขายวันนี้:</b><br>" +
                        "<code>" + summaryPath + "</code><br><br>" +
                        "💡 <i>ไฟล์เหล่านี้จะถูกสร้างโดยอัตโนมัติเมื่อมีการขาย</i>" +
                        "</body></html>",
                "Sales Data Location",
                JOptionPane.INFORMATION_MESSAGE);
    }
}