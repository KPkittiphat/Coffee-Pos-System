package view;

import util.IconManager;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.FileInputStream;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class SalesSummaryDialog extends JDialog {
    private JTextArea summaryTextArea;
    private JTextArea detailsTextArea;
    private JLabel statusLabel;
    private String salesDataPath;
    private String summaryDataPath;

    public SalesSummaryDialog(JFrame parent) {
        super(parent, "📊 สรุปยอดขายประจำวัน", true);
        this.salesDataPath = "sales_data" + File.separator + "daily_sales_" + getCurrentDate() + ".txt";
        this.summaryDataPath = "sales_data" + File.separator + "sales_summary_" + getCurrentDate() + ".txt";

        initializeDialog();
        createComponents();
        loadSalesData();
    }

    private void initializeDialog() {
        setSize(1000, 700);
        setLocationRelativeTo(getParent());
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);

        // Set modern look and feel
        getContentPane().setBackground(IconManager.BACKGROUND_COLOR);
    }

    private void createComponents() {
        setLayout(new BorderLayout(10, 10));

        // Header Panel
        JPanel headerPanel = createHeaderPanel();
        add(headerPanel, BorderLayout.NORTH);

        // Main Content Panel
        JPanel mainPanel = createMainPanel();
        add(mainPanel, BorderLayout.CENTER);

        // Footer Panel
        JPanel footerPanel = createFooterPanel();
        add(footerPanel, BorderLayout.SOUTH);
    }

    private JPanel createHeaderPanel() {
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBackground(IconManager.PRIMARY_COLOR);
        headerPanel.setBorder(new EmptyBorder(20, 25, 20, 25));

        // Title
        JLabel titleLabel = new JLabel("📊 สรุปยอดขายประจำวัน", JLabel.CENTER);
        titleLabel.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 24));
        titleLabel.setForeground(Color.WHITE);

        // Date
        JLabel dateLabel = new JLabel("วันที่: " + getCurrentDateFormatted(), JLabel.CENTER);
        dateLabel.setFont(new Font(Font.SANS_SERIF, Font.PLAIN, 16));
        dateLabel.setForeground(Color.WHITE);

        // Status
        statusLabel = new JLabel("", JLabel.CENTER);
        statusLabel.setFont(new Font(Font.SANS_SERIF, Font.ITALIC, 14));
        statusLabel.setForeground(Color.WHITE);

        JPanel titlePanel = new JPanel(new GridLayout(3, 1, 5, 5));
        titlePanel.setBackground(IconManager.PRIMARY_COLOR);
        titlePanel.add(titleLabel);
        titlePanel.add(dateLabel);
        titlePanel.add(statusLabel);

        headerPanel.add(titlePanel, BorderLayout.CENTER);

        return headerPanel;
    }

    private JPanel createMainPanel() {
        JPanel mainPanel = new JPanel(new GridLayout(1, 2, 15, 15));
        mainPanel.setBorder(new EmptyBorder(20, 20, 20, 20));
        mainPanel.setBackground(IconManager.BACKGROUND_COLOR);

        // Summary Panel
        JPanel summaryPanel = createSummaryPanel();
        mainPanel.add(summaryPanel);

        // Details Panel
        JPanel detailsPanel = createDetailsPanel();
        mainPanel.add(detailsPanel);

        return mainPanel;
    }

    private JPanel createSummaryPanel() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBackground(Color.WHITE);
        panel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(IconManager.PRIMARY_COLOR, 2),
                new EmptyBorder(15, 15, 15, 15)));

        // Title
        JLabel titleLabel = new JLabel("📋 สรุปยอดขาย", JLabel.CENTER);
        titleLabel.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 18));
        titleLabel.setForeground(IconManager.PRIMARY_COLOR);
        panel.add(titleLabel, BorderLayout.NORTH);

        // Summary text area
        summaryTextArea = new JTextArea();
        summaryTextArea.setFont(new Font(Font.SANS_SERIF, Font.PLAIN, 14));
        summaryTextArea.setEditable(false);
        summaryTextArea.setBackground(new Color(248, 251, 255));
        summaryTextArea.setBorder(new EmptyBorder(10, 10, 10, 10));
        summaryTextArea.setLineWrap(true);
        summaryTextArea.setWrapStyleWord(true);

        JScrollPane summaryScrollPane = new JScrollPane(summaryTextArea);
        summaryScrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        summaryScrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        panel.add(summaryScrollPane, BorderLayout.CENTER);

        return panel;
    }

    private JPanel createDetailsPanel() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBackground(Color.WHITE);
        panel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(IconManager.SECONDARY_COLOR, 2),
                new EmptyBorder(15, 15, 15, 15)));

        // Title
        JLabel titleLabel = new JLabel("📄 รายละเอียดการขาย", JLabel.CENTER);
        titleLabel.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 18));
        titleLabel.setForeground(IconManager.SECONDARY_COLOR);
        panel.add(titleLabel, BorderLayout.NORTH);

        // Details text area
        detailsTextArea = new JTextArea();
        detailsTextArea.setFont(new Font(Font.SANS_SERIF, Font.PLAIN, 12));
        detailsTextArea.setEditable(false);
        detailsTextArea.setBackground(new Color(248, 251, 255));
        detailsTextArea.setBorder(new EmptyBorder(10, 10, 10, 10));

        JScrollPane detailsScrollPane = new JScrollPane(detailsTextArea);
        detailsScrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        detailsScrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        panel.add(detailsScrollPane, BorderLayout.CENTER);

        return panel;
    }

    private JPanel createFooterPanel() {
        JPanel footerPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 15, 15));
        footerPanel.setBackground(IconManager.BACKGROUND_COLOR);

        // Quick sales summary button
        JButton quickSummaryButton = new JButton("💰 ยอดขายวันนี้");
        quickSummaryButton.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 14));
        quickSummaryButton.setBackground(new Color(230, 126, 34)); // Orange color for revenue theme
        quickSummaryButton.setForeground(Color.WHITE);
        quickSummaryButton.setFocusPainted(false);
        quickSummaryButton.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createRaisedBevelBorder(),
                new EmptyBorder(10, 20, 10, 20)));
        quickSummaryButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        quickSummaryButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                showQuickSalesSummary();
            }
        });

        // Refresh button
        JButton refreshButton = new JButton("🔄 รีเฟรช");
        refreshButton.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 14));
        refreshButton.setBackground(IconManager.PRIMARY_COLOR);
        refreshButton.setForeground(Color.WHITE);
        refreshButton.setFocusPainted(false);
        refreshButton.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createRaisedBevelBorder(),
                new EmptyBorder(10, 20, 10, 20)));
        refreshButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                loadSalesData();
            }
        });

        // Close button
        JButton closeButton = new JButton("❌ ปิด");
        closeButton.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 14));
        closeButton.setBackground(IconManager.SECONDARY_COLOR);
        closeButton.setForeground(Color.WHITE);
        closeButton.setFocusPainted(false);
        closeButton.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createRaisedBevelBorder(),
                new EmptyBorder(10, 20, 10, 20)));
        closeButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose();
            }
        });

        footerPanel.add(quickSummaryButton);
        footerPanel.add(refreshButton);
        footerPanel.add(closeButton);

        return footerPanel;
    }

    private void loadSalesData() {
        try {
            // Load summary data
            String summaryContent = loadFileContent(summaryDataPath);
            if (summaryContent != null && !summaryContent.trim().isEmpty()) {
                summaryTextArea.setText(summaryContent);
                statusLabel.setText("✅ ข้อมูลโหลดเรียบร้อย");
            } else {
                summaryTextArea.setText(
                        "📊 ยังไม่มีข้อมูลยอดขายสำหรับวันนี้\n\nเมื่อมีการขายสินค้า ระบบจะสร้างรายงานโดยอัตโนมัติ");
                statusLabel.setText("⚠️ ยังไม่มีข้อมูลยอดขาย");
            }

            // Load details data
            String detailsContent = loadFileContent(salesDataPath);
            if (detailsContent != null && !detailsContent.trim().isEmpty()) {
                detailsTextArea.setText(detailsContent);
            } else {
                detailsTextArea.setText(
                        "📄 ยังไม่มีรายละเอียดการขายสำหรับวันนี้\n\nรายละเอียดการขายจะปรากฏที่นี่เมื่อมีการทำธุรกรรม");
            }

        } catch (Exception e) {
            summaryTextArea.setText("❌ เกิดข้อผิดพลาดในการโหลดข้อมูล:\n" + e.getMessage());
            detailsTextArea.setText("❌ เกิดข้อผิดพลาดในการโหลดข้อมูล:\n" + e.getMessage());
            statusLabel.setText("❌ เกิดข้อผิดพลาดในการโหลดข้อมูล");
        }
    }

    private String loadFileContent(String filePath) {
        File file = new File(filePath);
        if (!file.exists()) {
            return null;
        }

        StringBuilder content = new StringBuilder();
        try (BufferedReader reader = new BufferedReader(
                new InputStreamReader(new FileInputStream(file), StandardCharsets.UTF_8))) {
            String line;
            while ((line = reader.readLine()) != null) {
                content.append(line).append("\n");
            }
        } catch (IOException e) {
            return "เกิดข้อผิดพลาดในการอ่านไฟล์: " + e.getMessage();
        }

        return content.toString();
    }

    private String getCurrentDate() {
        return LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
    }

    private String getCurrentDateFormatted() {
        return LocalDate.now().format(DateTimeFormatter.ofPattern("dd/MM/yyyy"));
    }

    private void showQuickSalesSummary() {
        try {
            // Read today's sales data from file
            String summaryContent = loadFileContent(summaryDataPath);

            if (summaryContent == null || summaryContent.trim().isEmpty()) {
                JOptionPane.showMessageDialog(this,
                        "<html><div style='text-align: center; font-family: sans-serif; padding: 20px;'>" +
                                "<h2 style='color: #3498db;'>📊 สรุปยอดขายวันนี้</h2>" +
                                "<p style='font-size: 16px; color: #e74c3c;'>🚫 ยังไม่มียอดขายในวันนี้</p>" +
                                "<p style='font-size: 14px; color: #7f8c8d;'>เมื่อมีการขาย ข้อมูลจะแสดงที่นี่</p>" +
                                "</div></html>",
                        "สรุปยอดขายด่วน",
                        JOptionPane.INFORMATION_MESSAGE);
                return;
            }

            // Parse the summary content to extract key information
            String totalRevenue = "฿0.00";
            String totalTransactions = "0";
            StringBuilder topItems = new StringBuilder();

            String[] lines = summaryContent.split("\n");
            boolean inTopSection = false;
            int topItemCount = 0;

            for (String line : lines) {
                // Extract total revenue
                if (line.contains("รายได้รวม:")) {
                    String[] parts = line.split(":");
                    if (parts.length > 1) {
                        totalRevenue = parts[1].trim();
                    }
                }

                // Extract transaction count
                if (line.contains("จำนวนธุรกรรมทั้งหมด:")) {
                    String[] parts = line.split(":");
                    if (parts.length > 1) {
                        totalTransactions = parts[1].trim().replace("ครั้ง", "").trim();
                    }
                }

                // Extract top 3 items
                if (line.contains("สินค้าขายดี TOP 3:")) {
                    inTopSection = true;
                    continue;
                }

                if (inTopSection && topItemCount < 3 && line.trim().length() > 0 && !line.contains("----")
                        && !line.contains("====")) {
                    if (line.trim().matches(".*\\(\\d+.*")) {
                        String medal = topItemCount == 0 ? "🥇" : topItemCount == 1 ? "🥈" : "🥉";
                        topItems.append("<p style='font-size: 16px; margin: 8px 0; text-align: left;'>");
                        topItems.append(String.format("%s <b>%s</b>", medal, line.trim()));
                        topItems.append("</p>");
                        topItemCount++;
                    }
                }
            }

            // Build quick summary message
            StringBuilder summary = new StringBuilder();
            summary.append("<html><div style='text-align: center; font-family: sans-serif; padding: 20px;'>");
            summary.append("<h2 style='color: #2c3e50; margin-bottom: 20px;'>📊 สรุปยอดขายวันนี้</h2>");

            // Total revenue
            summary.append(
                    "<div style='background: #ecf0f1; padding: 15px; border-radius: 8px; margin-bottom: 20px;'>");
            summary.append("<h3 style='color: #27ae60; margin: 0;'>💰 ยอดขายรวม</h3>");
            summary.append("<p style='font-size: 24px; font-weight: bold; color: #27ae60; margin: 10px 0;'>");
            summary.append(totalRevenue);
            summary.append("</p>");
            summary.append("<p style='font-size: 14px; color: #7f8c8d; margin: 0;'>");
            summary.append(String.format("จากธุรกรรม %s ครั้ง", totalTransactions));
            summary.append("</p></div>");

            // Top 3 best-selling items
            if (topItems.length() > 0) {
                summary.append("<div style='background: #ecf0f1; padding: 15px; border-radius: 8px;'>");
                summary.append("<h3 style='color: #3498db; margin: 0 0 15px 0;'>🏆 สินค้าขายดี TOP 3</h3>");
                summary.append(topItems.toString());
                summary.append("</div>");
            }

            summary.append("</div></html>");

            // Show quick summary popup
            JOptionPane.showMessageDialog(this,
                    summary.toString(),
                    "สรุปยอดขายด่วน - " + getCurrentDateFormatted(),
                    JOptionPane.INFORMATION_MESSAGE);

        } catch (Exception e) {
            JOptionPane.showMessageDialog(this,
                    "เกิดข้อผิดพลาดในการคำนวณยอดขาย: " + e.getMessage(),
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
        }
    }
}