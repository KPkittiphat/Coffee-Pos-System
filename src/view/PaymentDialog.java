package view;

import controller.POSController;
import util.IconManager;
import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public class PaymentDialog extends JDialog {
    private POSController controller;
    private JLabel totalLabel;
    private JTextField receivedField;
    private JLabel changeLabel;
    private JButton payButton;
    private JButton printReceiptButton;
    private double totalAmount;
    private boolean paymentCompleted = false;

    public PaymentDialog(JFrame parent, POSController controller, double totalAmount) {
        super(parent, "💳 Payment Processing", true);
        this.controller = controller;
        this.totalAmount = totalAmount;

        initializeDialog();
        createComponents();
        setupLayout();
        setupEventHandlers();
    }

    private void initializeDialog() {
        setSize(700, 600); // Reduced size as VAT information is removed
        setLocationRelativeTo(getParent());
        setResizable(true);
        setMinimumSize(new Dimension(900, 800));
        setDefaultCloseOperation(JDialog.DO_NOTHING_ON_CLOSE);

        // Set background color
        getContentPane().setBackground(IconManager.BACKGROUND_COLOR);

        // Set icon
        setIconImage(IconManager.getPaymentIcon().getImage());
    }

    private void createComponents() {
        // Title label
        JLabel titleLabel = new JLabel("💳 Payment Processing", JLabel.CENTER);
        titleLabel.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 18));
        titleLabel.setForeground(IconManager.PRIMARY_COLOR);
        titleLabel.setBorder(BorderFactory.createEmptyBorder(15, 0, 25, 0)); // Enhanced spacing for larger dialog

        // Amount display panel
        JPanel amountPanel = createAmountPanel();

        // Input panel
        JPanel inputPanel = createInputPanel();

        // Buttons panel
        JPanel buttonPanel = createButtonPanel();

        // Main content panel with optimal spacing
        JPanel contentPanel = new JPanel(new BorderLayout(20, 25));
        contentPanel.setBackground(IconManager.BACKGROUND_COLOR);
        contentPanel.setBorder(BorderFactory.createEmptyBorder(30, 40, 25, 40));

        contentPanel.add(titleLabel, BorderLayout.NORTH);
        contentPanel.add(amountPanel, BorderLayout.CENTER);
        contentPanel.add(inputPanel, BorderLayout.SOUTH);

        add(contentPanel, BorderLayout.CENTER);
        add(buttonPanel, BorderLayout.SOUTH);
    }

    private JPanel createAmountPanel() {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBackground(IconManager.CARD_COLOR);
        panel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(220, 220, 220), 1),
                BorderFactory.createEmptyBorder(30, 40, 30, 40)));
        panel.setPreferredSize(new Dimension(550, 250));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(15, 25, 15, 25);

        // Total amount
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.weightx = 0.5;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        JLabel totalLabelText = new JLabel("💸 Total Amount:");
        totalLabelText.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 18));
        totalLabelText.setPreferredSize(new Dimension(200, 40));
        totalLabelText.setMinimumSize(new Dimension(180, 35));
        panel.add(totalLabelText, gbc);

        gbc.gridx = 1;
        gbc.anchor = GridBagConstraints.EAST;
        gbc.weightx = 0.5;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        totalLabel = new JLabel(String.format("฿%.2f", totalAmount));
        totalLabel.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 24));
        totalLabel.setForeground(IconManager.PRIMARY_COLOR);
        totalLabel.setHorizontalAlignment(SwingConstants.RIGHT);
        totalLabel.setPreferredSize(new Dimension(200, 40));
        panel.add(totalLabel, gbc);

        // Received amount
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.weightx = 0.5;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        JLabel receivedLabelText = new JLabel("💵 Received Amount:");
        receivedLabelText.setFont(new Font(Font.SANS_SERIF, Font.PLAIN, 18));
        receivedLabelText.setPreferredSize(new Dimension(200, 40));
        receivedLabelText.setMinimumSize(new Dimension(180, 35));
        panel.add(receivedLabelText, gbc);

        gbc.gridx = 1;
        gbc.anchor = GridBagConstraints.EAST;
        gbc.weightx = 0.5;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.ipadx = 0;
        receivedField = new JTextField(15);

        // Enhanced styling for better visibility
        receivedField.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 20));
        receivedField.setBackground(Color.WHITE);
        receivedField.setForeground(new Color(0, 0, 0));
        receivedField.setCaretColor(IconManager.PRIMARY_COLOR);
        receivedField.setSelectionColor(IconManager.PRIMARY_COLOR);
        receivedField.setSelectedTextColor(Color.WHITE);

        // Set placeholder behavior
        receivedField.setText("Enter amount...");
        receivedField.setForeground(new Color(150, 150, 150));

        // Add focus listener for placeholder and visual feedback
        receivedField.addFocusListener(new java.awt.event.FocusAdapter() {
            @Override
            public void focusGained(java.awt.event.FocusEvent e) {
                if (receivedField.getText().equals("Enter amount...")) {
                    receivedField.setText("");
                    receivedField.setForeground(new Color(0, 0, 0));
                }
                receivedField.setBackground(new Color(255, 255, 240));
                receivedField.setBorder(BorderFactory.createCompoundBorder(
                        BorderFactory.createLineBorder(IconManager.PRIMARY_COLOR, 2),
                        BorderFactory.createEmptyBorder(8, 12, 8, 12)));
            }

            @Override
            public void focusLost(java.awt.event.FocusEvent e) {
                if (receivedField.getText().trim().isEmpty()) {
                    receivedField.setText("Enter amount...");
                    receivedField.setForeground(new Color(150, 150, 150));
                }
                receivedField.setBackground(Color.WHITE);
                receivedField.setBorder(BorderFactory.createCompoundBorder(
                        BorderFactory.createLineBorder(new Color(200, 200, 200), 2),
                        BorderFactory.createEmptyBorder(8, 12, 8, 12)));
            }
        });

        receivedField.setHorizontalAlignment(JTextField.RIGHT);
        receivedField.setPreferredSize(new Dimension(250, 45));
        receivedField.setMinimumSize(new Dimension(220, 40));
        receivedField.setMaximumSize(new Dimension(280, 50));

        panel.add(receivedField, gbc);

        // Change amount display
        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.weightx = 0.5;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.ipadx = 0;
        JLabel changeLabelText = new JLabel("💴 Change:");
        changeLabelText.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 18));
        changeLabelText.setPreferredSize(new Dimension(200, 40));
        changeLabelText.setMinimumSize(new Dimension(180, 35));
        panel.add(changeLabelText, gbc);

        gbc.gridx = 1;
        gbc.anchor = GridBagConstraints.EAST;
        gbc.weightx = 0.5;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        changeLabel = new JLabel("฿0.00");
        changeLabel.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 22));
        changeLabel.setForeground(IconManager.PRIMARY_COLOR);
        changeLabel.setOpaque(true);
        changeLabel.setBackground(new Color(248, 249, 250));
        changeLabel.setHorizontalAlignment(SwingConstants.RIGHT);
        changeLabel.setPreferredSize(new Dimension(200, 45));
        changeLabel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createRaisedBevelBorder(),
                BorderFactory.createEmptyBorder(8, 12, 8, 12)));
        panel.add(changeLabel, gbc);

        return panel;
    }

    private JPanel createInputPanel() {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 20));
        panel.setBackground(IconManager.BACKGROUND_COLOR);
        panel.setBorder(BorderFactory.createEmptyBorder(20, 0, 20, 0)); // Enhanced vertical spacing for larger dialog

        // Quick amount buttons with better styling
        double[] quickAmounts = { 100, 200, 500, 1000 };
        for (double amount : quickAmounts) {
            JButton quickButton = new JButton(String.format("฿%.0f", amount));
            quickButton.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 14)); // Better font
            quickButton.setBackground(IconManager.SECONDARY_COLOR);
            quickButton.setForeground(Color.WHITE);
            quickButton.setBorder(BorderFactory.createEmptyBorder(12, 18, 12, 18)); // Enhanced padding for better
                                                                                    // visual appeal
            quickButton.setFocusPainted(false);
            quickButton.setCursor(new Cursor(Cursor.HAND_CURSOR));

            // Add hover effect
            quickButton.addMouseListener(new java.awt.event.MouseAdapter() {
                @Override
                public void mouseEntered(java.awt.event.MouseEvent e) {
                    quickButton.setBackground(IconManager.SECONDARY_COLOR.darker());
                }

                @Override
                public void mouseExited(java.awt.event.MouseEvent e) {
                    quickButton.setBackground(IconManager.SECONDARY_COLOR);
                }
            });

            quickButton.addActionListener(e -> {
                receivedField.setText(String.format("%.2f", amount));
                calculateChange();
            });

            panel.add(quickButton);
        }

        return panel;
    }

    private JPanel createButtonPanel() {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.CENTER, 30, 25));
        panel.setBackground(IconManager.BACKGROUND_COLOR);
        panel.setBorder(BorderFactory.createEmptyBorder(25, 0, 30, 0)); // Enhanced spacing for larger dialog and better
                                                                        // balance

        // Cancel button
        JButton cancelButton = new JButton("❌ Cancel");
        cancelButton.setFont(new Font(Font.SANS_SERIF, Font.PLAIN, 14));
        cancelButton.setPreferredSize(new Dimension(120, 40));
        cancelButton.setBackground(IconManager.DANGER_COLOR);
        cancelButton.setForeground(Color.WHITE);
        cancelButton.setBorder(BorderFactory.createEmptyBorder());
        cancelButton.setFocusPainted(false);
        cancelButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        cancelButton.addActionListener(e -> dispose());

        // Pay button
        String paymentEmoji = IconManager.getPaymentEmoji();
        payButton = new JButton(paymentEmoji + " Process Payment");
        payButton.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 14));
        payButton.setPreferredSize(new Dimension(180, 40));
        payButton.setBackground(IconManager.SUCCESS_COLOR);
        payButton.setForeground(Color.WHITE);
        payButton.setBorder(BorderFactory.createEmptyBorder());
        payButton.setFocusPainted(false);
        payButton.setCursor(new Cursor(Cursor.HAND_CURSOR));

        // Print receipt button (initially hidden)
        String printEmoji = IconManager.getPrintEmoji();
        printReceiptButton = new JButton(printEmoji + " Print Receipt");
        printReceiptButton.setFont(new Font(Font.SANS_SERIF, Font.PLAIN, 14));
        printReceiptButton.setPreferredSize(new Dimension(150, 40));
        printReceiptButton.setBackground(IconManager.WARNING_COLOR);
        printReceiptButton.setForeground(Color.WHITE);
        printReceiptButton.setBorder(BorderFactory.createEmptyBorder());
        printReceiptButton.setFocusPainted(false);
        printReceiptButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        printReceiptButton.setVisible(false);

        panel.add(cancelButton);
        panel.add(payButton);
        panel.add(printReceiptButton);

        return panel;
    }

    private void setupLayout() {
        // Focus on received field (placeholder will be set by focus listener)
        receivedField.requestFocus();

        // Initialize the pay button state
        payButton.setEnabled(false);

        // Set initial change display to 0.00 with neutral styling
        changeLabel.setText("฿0.00");
        changeLabel.setForeground(new Color(108, 117, 125)); // Muted gray
        changeLabel.setBackground(new Color(248, 249, 250)); // Very light gray
        updateChangeLabelBorder(new Color(220, 220, 220)); // Light border
    }

    private void setupEventHandlers() {
        // Received field events
        receivedField.addKeyListener(new KeyAdapter() {
            @Override
            public void keyReleased(KeyEvent e) {
                calculateChange();
                if (e.getKeyCode() == KeyEvent.VK_ENTER && payButton.isEnabled()) {
                    processPayment();
                }
            }

            @Override
            public void keyTyped(KeyEvent e) {
                char c = e.getKeyChar();
                // Allow only digits, decimal point, and backspace
                if (!Character.isDigit(c) && c != '.' && c != KeyEvent.VK_BACK_SPACE) {
                    e.consume();
                }
                // Allow only one decimal point
                if (c == '.' && receivedField.getText().contains(".")) {
                    e.consume();
                }
            }
        });

        // Add mouse click listener to select all text when field has content
        receivedField.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseClicked(java.awt.event.MouseEvent e) {
                // Only select all if field has content
                if (!receivedField.getText().trim().isEmpty()) {
                    SwingUtilities.invokeLater(() -> receivedField.selectAll());
                }
            }
        });

        // Pay button event
        payButton.addActionListener(e -> processPayment());

        // Print receipt button event
        printReceiptButton.addActionListener(e -> {
            controller.printReceipt();
            dispose();
        });

        // Window close event
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                if (!paymentCompleted) {
                    int result = JOptionPane.showConfirmDialog(
                            PaymentDialog.this,
                            "Are you sure you want to cancel the payment?",
                            "Cancel Payment",
                            JOptionPane.YES_NO_OPTION,
                            JOptionPane.QUESTION_MESSAGE);
                    if (result == JOptionPane.YES_OPTION) {
                        dispose();
                    }
                } else {
                    dispose();
                }
            }
        });
    }

    private void processPayment() {
        if (!payButton.isEnabled()) {
            JOptionPane.showMessageDialog(this,
                    "Please enter a valid payment amount that covers the total.",
                    "Invalid Payment",
                    JOptionPane.WARNING_MESSAGE);
            return;
        }

        if (calculateChange()) {
            try {
                String amountText = receivedField.getText().trim();
                if (amountText.isEmpty() || amountText.equals("Enter amount...")) {
                    JOptionPane.showMessageDialog(this,
                            "Please enter the payment amount.",
                            "Missing Amount",
                            JOptionPane.WARNING_MESSAGE);
                    receivedField.requestFocus();
                    return;
                }

                double receivedAmount = Double.parseDouble(amountText);

                if (receivedAmount < totalAmount) {
                    JOptionPane.showMessageDialog(this,
                            String.format("Insufficient payment amount.\nRequired: ฿%.2f\nReceived: ฿%.2f",
                                    totalAmount, receivedAmount),
                            "Insufficient Payment",
                            JOptionPane.WARNING_MESSAGE);
                    receivedField.requestFocus();
                    return;
                }

                // Calculate change before completing payment
                double changeAmount = receivedAmount - totalAmount;

                paymentCompleted = true;

                // Set the received amount in controller and complete sale
                controller.setReceivedAmount(receivedAmount);
                controller.completeSale(receivedAmount);

                // Update UI for receipt printing
                showPaymentSuccess();
            } catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(this,
                        "Please enter a valid numeric amount.",
                        "Invalid Amount",
                        JOptionPane.ERROR_MESSAGE);
                receivedField.requestFocus();
            } catch (Exception e) {
                JOptionPane.showMessageDialog(this,
                        "Error processing payment: " + e.getMessage(),
                        "Payment Error",
                        JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private boolean calculateChange() {
        try {
            String text = receivedField.getText().trim();

            // Show 0.00 if field is empty, contains placeholder, or contains only "0" or
            // "0.00"
            if (text.isEmpty() || text.equals("Enter amount...") || text.equals("0") || text.equals("0.00")
                    || text.equals("0.")) {
                changeLabel.setText("฿0.00");
                changeLabel.setForeground(new Color(108, 117, 125)); // Muted gray
                changeLabel.setBackground(new Color(248, 249, 250)); // Very light gray
                updateChangeLabelBorder(new Color(220, 220, 220)); // Light border
                payButton.setEnabled(false);
                return false;
            }

            double receivedAmount = Double.parseDouble(text);

            // Only calculate if received amount is greater than 0
            if (receivedAmount <= 0) {
                changeLabel.setText("฿0.00");
                changeLabel.setForeground(new Color(108, 117, 125)); // Muted gray
                changeLabel.setBackground(new Color(248, 249, 250)); // Very light gray
                updateChangeLabelBorder(new Color(220, 220, 220)); // Light border
                payButton.setEnabled(false);
                return false;
            }

            if (receivedAmount >= totalAmount) {
                double change = receivedAmount - totalAmount;
                changeLabel.setText(String.format("฿%.2f", change));

                // Enhanced visual feedback with better colors
                if (change == 0) {
                    // Exact payment - blue theme
                    changeLabel.setForeground(new Color(52, 152, 219)); // Bright blue
                    changeLabel.setBackground(new Color(235, 245, 255)); // Light blue background
                    updateChangeLabelBorder(new Color(52, 152, 219)); // Blue border
                } else {
                    // Change required - green theme for positive change
                    changeLabel.setForeground(new Color(39, 174, 96)); // Beautiful green
                    changeLabel.setBackground(new Color(232, 248, 243)); // Light green background
                    updateChangeLabelBorder(new Color(39, 174, 96)); // Green border
                }

                payButton.setEnabled(true);
                return true;
            } else {
                double shortage = totalAmount - receivedAmount;
                changeLabel.setText(String.format("Need ฿%.2f more!", shortage));
                changeLabel.setForeground(new Color(231, 76, 60)); // Bright red
                changeLabel.setBackground(new Color(255, 235, 235)); // Light red background
                updateChangeLabelBorder(new Color(231, 76, 60)); // Red border
                payButton.setEnabled(false);
                return false;
            }
        } catch (NumberFormatException e) {
            changeLabel.setText("⚠️ Invalid input!");
            changeLabel.setForeground(new Color(230, 126, 34)); // Orange for error
            changeLabel.setBackground(new Color(255, 243, 224)); // Light orange background
            updateChangeLabelBorder(new Color(230, 126, 34)); // Orange border
            payButton.setEnabled(false);
            return false;
        }
    }

    private void updateChangeLabelBorder(Color borderColor) {
        // Create enhanced border with the specified color
        changeLabel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createCompoundBorder(
                        // Outer glow effect with transparency
                        BorderFactory.createLineBorder(
                                new Color(borderColor.getRed(), borderColor.getGreen(), borderColor.getBlue(), 80), 3),
                        // Main border
                        BorderFactory.createLineBorder(borderColor, 2)),
                BorderFactory.createEmptyBorder(12, 18, 12, 18) // Consistent inner padding
        ));
    }

    private void showPaymentSuccess() {
        // Update UI to show success
        payButton.setVisible(false);
        printReceiptButton.setVisible(true);

        // Show success message
        JOptionPane.showMessageDialog(this,
                "Payment Successful!\nThank you for your purchase.",
                "Payment Complete",
                JOptionPane.INFORMATION_MESSAGE);
    }
}