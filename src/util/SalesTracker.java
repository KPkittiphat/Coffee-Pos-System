package util;

import model.Sale;
import model.CartItem;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;

public class SalesTracker {
    private static final String SALES_DIR = "sales_data";
    private static final String DAILY_SALES_FILE = "daily_sales_";
    private static final String SALES_SUMMARY_FILE = "sales_summary_";
    private List<Sale> todaysSales;

    public SalesTracker() {
        this.todaysSales = new ArrayList<>();
        createSalesDirectory();
    }

    private String repeat(String str, int count) {
        StringBuilder result = new StringBuilder();
        for (int i = 0; i < count; i++) {
            result.append(str);
        }
        return result.toString();
    }

    private void createSalesDirectory() {
        File salesDir = new File(SALES_DIR);
        if (!salesDir.exists()) {
            salesDir.mkdirs();
        }
    }

    public void recordSale(HashMap<Integer, CartItem> cartItems, double totalAmount, double receivedAmount,
            double changeAmount) {
        Sale sale = new Sale(cartItems, totalAmount, receivedAmount, changeAmount);
        todaysSales.add(sale);
        saveSaleToFile(sale);
    }

    private void saveSaleToFile(Sale sale) {
        String today = LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
        String filename = SALES_DIR + File.separator + DAILY_SALES_FILE + today + ".txt";

        try (PrintWriter writer = new PrintWriter(new OutputStreamWriter(
                new FileOutputStream(filename, true), StandardCharsets.UTF_8))) {
            writer.println("=== การขาย ณ เวลา " + sale.getFormattedTime() + " ===");
            writer.println("รายการสินค้า:");

            for (Map.Entry<String, Integer> entry : sale.getItems().entrySet()) {
                writer.println("  - " + entry.getKey() + " จำนวน " + entry.getValue() + " แก้ว");
            }

            writer.println("ยอดรวม: ฿" + String.format("%.2f", sale.getTotalAmount()));
            writer.println("เงินที่รับ: ฿" + String.format("%.2f", sale.getReceivedAmount()));
            writer.println("เงินทอน: ฿" + String.format("%.2f", sale.getChangeAmount()));
            writer.println("------------------------------------------------");
            writer.println();

        } catch (IOException e) {
            System.err.println("Error saving sale to file: " + e.getMessage());
        }
    }

    public void generateDailySummary() {
        generateDailySummary(LocalDate.now());
    }

    public void generateDailySummary(LocalDate date) {
        String dateStr = date.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
        String salesFile = SALES_DIR + File.separator + DAILY_SALES_FILE + dateStr + ".txt";
        String summaryFile = SALES_DIR + File.separator + SALES_SUMMARY_FILE + dateStr + ".txt";

        // Check if sales file exists
        File salesFileObj = new File(salesFile);
        if (!salesFileObj.exists()) {
            // Create empty summary for days with no sales
            try (PrintWriter writer = new PrintWriter(new OutputStreamWriter(
                    new FileOutputStream(summaryFile), StandardCharsets.UTF_8))) {
                writer.println("📊 สรุปยอดขายประจำวัน");
                writer.println("วันที่: " + dateStr);
                writer.println(repeat("=", 50));
                writer.println("ไม่มียอดขายในวันนี้");
                writer.println(repeat("=", 50));
            } catch (IOException e) {
                System.err.println("Error creating empty summary: " + e.getMessage());
            }
            return;
        }

        // Load today's sales and generate summary
        List<Sale> salesList = loadSalesFromDate(date);
        Map<String, Integer> itemSummary = new HashMap<>();
        double totalRevenue = 0.0;
        int totalTransactions = salesList.size();

        // Calculate summary data
        for (Sale sale : salesList) {
            totalRevenue += sale.getTotalAmount();
            for (Map.Entry<String, Integer> entry : sale.getItems().entrySet()) {
                itemSummary.merge(entry.getKey(), entry.getValue(), Integer::sum);
            }
        }

        // Write summary to file
        try (PrintWriter writer = new PrintWriter(new OutputStreamWriter(
                new FileOutputStream(summaryFile), StandardCharsets.UTF_8))) {
            writer.println("📊 สรุปยอดขายประจำวัน");
            writer.println("วันที่: " + dateStr);
            writer.println(repeat("=", 50));
            writer.println();

            writer.println("📈 ภาพรวมการขาย:");
            writer.println("  จำนวนธุรกรรมทั้งหมด: " + totalTransactions + " ครั้ง");
            writer.println("  รายได้รวม: ฿" + String.format("%.2f", totalRevenue));

            if (totalTransactions > 0) {
                writer.println("  ยอดขายเฉลี่ยต่อธุรกรรม: ฿" + String.format("%.2f", totalRevenue / totalTransactions));
            }

            writer.println();
            writer.println("☕ รายการสินค้าที่ขายได้:");
            writer.println(repeat("-", 40));

            if (itemSummary.isEmpty()) {
                writer.println("  ไม่มีสินค้าที่ขายได้ในวันนี้");
            } else {
                // Sort items by quantity sold (descending)
                itemSummary.entrySet().stream()
                        .sorted(Map.Entry.<String, Integer>comparingByValue().reversed())
                        .forEach(entry -> {
                            writer.println(String.format("  %-25s %3d แก้ว", entry.getKey(), entry.getValue()));
                        });

                writer.println(repeat("-", 40));
                int totalItems = itemSummary.values().stream().mapToInt(Integer::intValue).sum();
                writer.println(String.format("  %-25s %3d แก้ว", "รวมทั้งหมด:", totalItems));
            }

            writer.println();
            writer.println("🏆 สินค้าขายดี TOP 3:");
            writer.println(repeat("-", 30));

            itemSummary.entrySet().stream()
                    .sorted(Map.Entry.<String, Integer>comparingByValue().reversed())
                    .limit(3)
                    .forEach(entry -> {
                        writer.println("  " + entry.getKey() + " (" + entry.getValue() + " แก้ว)");
                    });

            writer.println();
            writer.println(repeat("=", 50));
            writer.println("📄 รายงานสร้างเมื่อ: " + java.time.LocalDateTime.now()
                    .format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));

        } catch (IOException e) {
            System.err.println("Error generating daily summary: " + e.getMessage());
        }
    }

    private List<Sale> loadSalesFromDate(LocalDate date) {
        List<Sale> sales = new ArrayList<>();
        String dateStr = date.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));

        if (date.equals(LocalDate.now())) {
            // Return today's in-memory sales
            return new ArrayList<>(todaysSales);
        }

        // For other dates, we would need to parse from file
        // For now, return empty list for past dates
        return sales;
    }

    public List<Sale> getTodaysSales() {
        return new ArrayList<>(todaysSales);
    }

    public String getDailySummaryFilePath() {
        String today = LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
        return SALES_DIR + File.separator + SALES_SUMMARY_FILE + today + ".txt";
    }

    public String getDailySalesFilePath() {
        String today = LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
        return SALES_DIR + File.separator + DAILY_SALES_FILE + today + ".txt";
    }

    public void clearTodaysSales() {
        todaysSales.clear();
    }
}