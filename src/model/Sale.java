package model;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;

public class Sale {
    private LocalDateTime saleDateTime;
    private HashMap<String, Integer> items; // Product name -> quantity
    private double totalAmount;
    private double receivedAmount;
    private double changeAmount;

    public Sale(HashMap<Integer, CartItem> cartItems, double totalAmount, double receivedAmount, double changeAmount) {
        this.saleDateTime = LocalDateTime.now();
        this.items = new HashMap<>();
        this.totalAmount = totalAmount;
        this.receivedAmount = receivedAmount;
        this.changeAmount = changeAmount;

        // Convert cart items to product name and quantity mapping
        for (CartItem cartItem : cartItems.values()) {
            this.items.put(cartItem.getProduct().getName(), cartItem.getQuantity());
        }
    }

    public LocalDateTime getSaleDateTime() {
        return saleDateTime;
    }

    public HashMap<String, Integer> getItems() {
        return items;
    }

    public double getTotalAmount() {
        return totalAmount;
    }

    public double getReceivedAmount() {
        return receivedAmount;
    }

    public double getChangeAmount() {
        return changeAmount;
    }

    public String getFormattedDateTime() {
        return saleDateTime.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
    }

    public String getFormattedDate() {
        return saleDateTime.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
    }

    public String getFormattedTime() {
        return saleDateTime.format(DateTimeFormatter.ofPattern("HH:mm:ss"));
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Sale on ").append(getFormattedDateTime()).append("\n");
        sb.append("Items: ");
        for (Map.Entry<String, Integer> entry : items.entrySet()) {
            sb.append(entry.getKey()).append(" x").append(entry.getValue()).append(", ");
        }
        if (!items.isEmpty()) {
            sb.setLength(sb.length() - 2);
        }
        sb.append("\nTotal: ฿").append(String.format("%.2f", totalAmount));
        return sb.toString();
    }
}