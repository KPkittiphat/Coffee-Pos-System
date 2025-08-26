package view;

import controller.POSController;
import model.CartItem;
import util.IconManager;
import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import java.awt.*;
import java.util.HashMap;
import java.util.Map;

public class CartPanel extends JPanel {
    private POSController controller;
    private JTable cartTable;
    private DefaultTableModel tableModel;
    private JLabel totalLabel;
    private JLabel itemCountLabel;

    public CartPanel(POSController controller) {
        this.controller = controller;
        setupPanel();
        createCartTable();
        createBottomPanel();
    }

    private void setupPanel() {
        setLayout(new BorderLayout(15, 15)); // Increased spacing
        setPreferredSize(new Dimension(470, 0)); // Slightly wider
        setBackground(IconManager.BACKGROUND_COLOR);

        // Create beautiful title with enhanced styling
        String title = "🛒 Shopping Cart";
        setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createCompoundBorder(
                        BorderFactory.createLineBorder(IconManager.PRIMARY_COLOR, 3), // Thicker border
                        BorderFactory.createLineBorder(Color.WHITE, 2) // Inner white border for depth
                ),
                BorderFactory.createCompoundBorder(
                        BorderFactory.createTitledBorder(
                                BorderFactory.createEmptyBorder(),
                                title,
                                0, 0,
                                new Font(Font.SANS_SERIF, Font.BOLD, 18), // Larger title font
                                IconManager.PRIMARY_COLOR),
                        BorderFactory.createEmptyBorder(20, 20, 20, 20) // More padding
                )));
    }

    private void createCartTable() {
        String[] columnNames = { "🍽️ Product", "💰 Price", "🔢 Qty", "💸 Total", "🗑️ Delete" };
        tableModel = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return column == 4; // Only delete column is editable
            }
        };

        cartTable = new JTable(tableModel);
        setupTableStyling();

        JScrollPane scrollPane = new JScrollPane(cartTable);
        // Enhanced scroll pane with beautiful borders and shadow effect
        scrollPane.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createCompoundBorder(
                        BorderFactory.createLineBorder(new Color(200, 200, 200), 1),
                        BorderFactory.createLineBorder(Color.WHITE, 2) // Inner white border
                ),
                BorderFactory.createLineBorder(new Color(240, 240, 240), 1) // Subtle inner shadow
        ));
        scrollPane.getViewport().setBackground(Color.WHITE);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);

        // Enhance scrollbar appearance
        scrollPane.getVerticalScrollBar().setUnitIncrement(20);
        scrollPane.getVerticalScrollBar().setBackground(new Color(245, 245, 245));

        add(scrollPane, BorderLayout.CENTER);
    }

    private void setupTableStyling() {
        // Enhanced table styling with proper font for emoji support
        cartTable.setFont(new Font(Font.SANS_SERIF, Font.PLAIN, 13)); // Slightly larger font
        cartTable.setRowHeight(40); // Taller rows for better readability
        cartTable.setGridColor(new Color(230, 230, 230)); // Lighter grid lines
        cartTable.setSelectionBackground(new Color(52, 152, 219, 50)); // Semi-transparent blue
        cartTable.setSelectionForeground(IconManager.PRIMARY_COLOR);
        cartTable.setShowGrid(true);
        cartTable.setIntercellSpacing(new Dimension(1, 1));
        cartTable.setFillsViewportHeight(true); // Fill available space

        // Enhanced header styling with gradient effect
        JTableHeader header = cartTable.getTableHeader();
        header.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 14)); // Larger header font
        header.setBackground(IconManager.PRIMARY_COLOR);
        header.setForeground(Color.WHITE);
        header.setPreferredSize(new Dimension(0, 45)); // Taller header
        header.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(IconManager.PRIMARY_COLOR.darker(), 1),
                BorderFactory.createEmptyBorder(5, 10, 5, 10)));

        // Optimized column widths for better layout
        cartTable.getColumnModel().getColumn(0).setPreferredWidth(130); // Product
        cartTable.getColumnModel().getColumn(1).setPreferredWidth(75); // Price
        cartTable.getColumnModel().getColumn(2).setPreferredWidth(55); // Qty
        cartTable.getColumnModel().getColumn(3).setPreferredWidth(85); // Total
        cartTable.getColumnModel().getColumn(4).setPreferredWidth(85); // Delete

        // Center align numeric columns
        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment(JLabel.CENTER);
        cartTable.getColumnModel().getColumn(1).setCellRenderer(centerRenderer);
        cartTable.getColumnModel().getColumn(2).setCellRenderer(centerRenderer);
        cartTable.getColumnModel().getColumn(3).setCellRenderer(centerRenderer);

        // Create delete button renderer
        cartTable.getColumnModel().getColumn(4).setCellRenderer(new DeleteButtonRenderer());
        cartTable.getColumnModel().getColumn(4).setCellEditor(new DeleteButtonEditor());

        // Enhanced custom renderer for beautiful alternating row colors and better
        // formatting
        cartTable.setDefaultRenderer(Object.class, new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value,
                    boolean isSelected, boolean hasFocus, int row, int column) {
                Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);

                // Enhanced row coloring with subtle gradients
                if (!isSelected && column != 4) { // Don't apply to delete button column
                    if (row % 2 == 0) {
                        c.setBackground(Color.WHITE);
                    } else {
                        c.setBackground(new Color(248, 251, 255)); // Very light blue
                    }
                } else if (isSelected && column != 4) {
                    c.setBackground(new Color(52, 152, 219, 80)); // Semi-transparent selection
                    c.setForeground(new Color(25, 25, 112)); // Dark blue text when selected
                }

                // Enhanced text formatting and alignment
                if (column == 1 || column == 2 || column == 3) {
                    ((JLabel) c).setHorizontalAlignment(JLabel.CENTER);
                    if (column == 1 || column == 3) { // Price and Total columns
                        c.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 13)); // Bold for money amounts
                        if (column == 3) {
                            c.setForeground(IconManager.SUCCESS_COLOR); // Green for totals
                        }
                    }
                } else if (column == 0) { // Product name column
                    c.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 13));
                    c.setForeground(new Color(44, 62, 80)); // Dark blue-gray for product names
                }

                // Add subtle padding
                if (c instanceof JLabel) {
                    ((JLabel) c).setBorder(BorderFactory.createEmptyBorder(8, 12, 8, 12));
                }

                return c;
            }
        });
    }

    private void createBottomPanel() {
        JPanel bottomPanel = new JPanel(new BorderLayout(10, 10));
        bottomPanel.setBackground(IconManager.BACKGROUND_COLOR);
        bottomPanel.setBorder(BorderFactory.createEmptyBorder(15, 0, 0, 0));

        // Item count and total panel
        JPanel summaryPanel = createSummaryPanel();
        bottomPanel.add(summaryPanel, BorderLayout.NORTH);

        // Checkout button only
        JButton checkoutButton = createCheckoutButton();
        bottomPanel.add(checkoutButton, BorderLayout.SOUTH);

        add(bottomPanel, BorderLayout.SOUTH);
    }

    private JPanel createSummaryPanel() {
        JPanel summaryPanel = new JPanel(new GridLayout(2, 1, 8, 8)); // Increased spacing
        summaryPanel.setBackground(IconManager.CARD_COLOR);

        // Enhanced summary panel with beautiful gradient-like effect
        summaryPanel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createCompoundBorder(
                        BorderFactory.createLineBorder(IconManager.PRIMARY_COLOR.brighter(), 2),
                        BorderFactory.createLineBorder(Color.WHITE, 2) // Inner white border
                ),
                BorderFactory.createEmptyBorder(20, 20, 20, 20) // More padding
        ));

        // Enhanced item count label with better styling
        itemCountLabel = new JLabel("📦 Items: 0", JLabel.LEFT);
        itemCountLabel.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 15)); // Slightly larger and bold
        itemCountLabel.setForeground(new Color(52, 73, 94)); // Dark blue-gray
        itemCountLabel.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));

        // Enhanced total label with premium styling
        totalLabel = new JLabel("💰 Total: ฿0.00", JLabel.RIGHT);
        totalLabel.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 22)); // Larger font
        totalLabel.setForeground(IconManager.PRIMARY_COLOR);
        totalLabel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(230, 240, 250), 1),
                BorderFactory.createEmptyBorder(8, 15, 8, 15)));
        totalLabel.setOpaque(true);
        totalLabel.setBackground(new Color(248, 251, 255)); // Very light blue background

        summaryPanel.add(itemCountLabel);
        summaryPanel.add(totalLabel);

        return summaryPanel;
    }

    private JButton createCheckoutButton() {
        String checkoutEmoji = IconManager.getCheckoutEmoji();
        JButton checkoutButton = new JButton(checkoutEmoji + " Checkout Now");

        // Enhanced button styling with premium appearance
        checkoutButton.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 17)); // Larger font
        checkoutButton.setPreferredSize(new Dimension(0, 55)); // Taller button
        checkoutButton.setBackground(IconManager.SUCCESS_COLOR);
        checkoutButton.setForeground(Color.WHITE);

        // Beautiful 3D border effect
        checkoutButton.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createRaisedBevelBorder(),
                BorderFactory.createCompoundBorder(
                        BorderFactory.createLineBorder(IconManager.SUCCESS_COLOR.darker(), 1),
                        BorderFactory.createEmptyBorder(12, 15, 12, 15))));

        checkoutButton.setFocusPainted(false);
        checkoutButton.setCursor(new Cursor(Cursor.HAND_CURSOR));

        // Enhanced hover effects with smooth transitions
        checkoutButton.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseEntered(java.awt.event.MouseEvent e) {
                checkoutButton.setBackground(IconManager.SUCCESS_COLOR.darker());
                checkoutButton.setBorder(BorderFactory.createCompoundBorder(
                        BorderFactory.createLoweredBevelBorder(), // Pressed effect on hover
                        BorderFactory.createCompoundBorder(
                                BorderFactory.createLineBorder(IconManager.SUCCESS_COLOR.darker().darker(), 1),
                                BorderFactory.createEmptyBorder(12, 15, 12, 15))));
            }

            @Override
            public void mouseExited(java.awt.event.MouseEvent e) {
                checkoutButton.setBackground(IconManager.SUCCESS_COLOR);
                checkoutButton.setBorder(BorderFactory.createCompoundBorder(
                        BorderFactory.createRaisedBevelBorder(),
                        BorderFactory.createCompoundBorder(
                                BorderFactory.createLineBorder(IconManager.SUCCESS_COLOR.darker(), 1),
                                BorderFactory.createEmptyBorder(12, 15, 12, 15))));
            }
        });

        checkoutButton.addActionListener(e -> controller.showPaymentDialog());

        return checkoutButton;
    }

    public void updateCartTable(HashMap<Integer, CartItem> cartItems) {
        // Stop any ongoing editing to prevent conflicts
        if (cartTable.isEditing()) {
            try {
                cartTable.getCellEditor().stopCellEditing();
            } catch (Exception e) {
                // If stopping fails, cancel editing
                cartTable.getCellEditor().cancelCellEditing();
            }
        }

        tableModel.setRowCount(0);
        double total = 0;
        int itemCount = 0;

        for (Map.Entry<Integer, CartItem> entry : cartItems.entrySet()) {
            CartItem item = entry.getValue();
            Object[] rowData = {
                    item.getProduct().getName(),
                    String.format("฿%.2f", item.getProduct().getPrice()),
                    item.getQuantity(),
                    item.getTotalPrice(), // Store actual numeric value, not formatted string
                    "Delete" // Delete button text
            };
            tableModel.addRow(rowData);
            total += item.getTotalPrice();
            itemCount += item.getQuantity();
        }

        totalLabel.setText(String.format("💰 Total: ฿%.2f", total));
        itemCountLabel.setText(String.format("📦 Items: %d", itemCount));

        // Add visual feedback when cart is updated
        if (itemCount > 0) {
            totalLabel.setForeground(IconManager.SUCCESS_COLOR);
        } else {
            totalLabel.setForeground(IconManager.SECONDARY_COLOR);
        }
    }

    public double getTotalAmount() {
        double total = 0;
        for (int i = 0; i < tableModel.getRowCount(); i++) {
            Object value = tableModel.getValueAt(i, 3);
            if (value instanceof Double) {
                total += (Double) value;
            } else if (value instanceof Number) {
                total += ((Number) value).doubleValue();
            } else {
                // Fallback: parse string if needed
                String totalStr = value.toString();
                String cleanTotal = totalStr.replace("฿", "").trim();
                try {
                    total += Double.parseDouble(cleanTotal);
                } catch (NumberFormatException e) {
                    System.err.println("Error parsing total: " + totalStr);
                }
            }
        }
        return total;
    }

    public void clearCart() {
        tableModel.setRowCount(0);
        totalLabel.setText("💰 Total: ฿0.00");
        totalLabel.setForeground(IconManager.SECONDARY_COLOR);
        itemCountLabel.setText("📦 Items: 0");
    }

    // Enhanced delete button renderer class with premium styling
    private class DeleteButtonRenderer extends JPanel implements javax.swing.table.TableCellRenderer {
        private JButton deleteButton;

        public DeleteButtonRenderer() {
            setLayout(new FlowLayout(FlowLayout.CENTER, 2, 2));
            deleteButton = new JButton("🗑️");
            deleteButton.setFont(new Font(Font.SANS_SERIF, Font.PLAIN, 14)); // Larger emoji
            deleteButton.setPreferredSize(new Dimension(65, 30)); // Slightly larger
            deleteButton.setBackground(IconManager.DANGER_COLOR);
            deleteButton.setForeground(Color.WHITE);

            // Enhanced 3D button effect
            deleteButton.setBorder(BorderFactory.createCompoundBorder(
                    BorderFactory.createRaisedBevelBorder(),
                    BorderFactory.createEmptyBorder(3, 8, 3, 8)));

            deleteButton.setFocusPainted(false);
            deleteButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
            add(deleteButton);
        }

        @Override
        public Component getTableCellRendererComponent(JTable table, Object value,
                boolean isSelected, boolean hasFocus, int row, int column) {
            if (isSelected) {
                setBackground(table.getSelectionBackground());
                deleteButton.setBackground(IconManager.DANGER_COLOR.darker());
                deleteButton.setBorder(BorderFactory.createCompoundBorder(
                        BorderFactory.createLoweredBevelBorder(), // Pressed effect when selected
                        BorderFactory.createEmptyBorder(3, 8, 3, 8)));
            } else {
                setBackground(table.getBackground());
                deleteButton.setBackground(IconManager.DANGER_COLOR);
                deleteButton.setBorder(BorderFactory.createCompoundBorder(
                        BorderFactory.createRaisedBevelBorder(),
                        BorderFactory.createEmptyBorder(3, 8, 3, 8)));
            }
            return this;
        }
    }

    // Enhanced delete button editor class with premium styling and better UX
    private class DeleteButtonEditor extends DefaultCellEditor {
        private JPanel panel;
        private JButton deleteButton;
        private int currentRow;
        private String currentProductName;

        public DeleteButtonEditor() {
            super(new JCheckBox());
            panel = new JPanel(new FlowLayout(FlowLayout.CENTER, 2, 2));
            deleteButton = new JButton("🗑️");
            deleteButton.setFont(new Font(Font.SANS_SERIF, Font.PLAIN, 14));
            deleteButton.setPreferredSize(new Dimension(65, 30));
            deleteButton.setBackground(IconManager.DANGER_COLOR);
            deleteButton.setForeground(Color.WHITE);
            deleteButton.setFocusPainted(false);
            deleteButton.setCursor(new Cursor(Cursor.HAND_CURSOR));

            // Enhanced 3D pressed effect
            deleteButton.setBorder(BorderFactory.createCompoundBorder(
                    BorderFactory.createLoweredBevelBorder(),
                    BorderFactory.createEmptyBorder(3, 8, 3, 8)));

            deleteButton.addActionListener(e -> {
                // Use stored product name to avoid accessing potentially invalid row
                if (currentProductName == null || currentProductName.isEmpty()) {
                    System.err.println("[ERROR] No product name available for deletion");
                    cancelCellEditing();
                    return;
                }

                // Enhanced confirmation dialog with custom styling
                int result = JOptionPane.showConfirmDialog(
                        CartPanel.this,
                        "<html><div style='text-align: center; font-family: sans-serif; padding: 10px;'>" +
                                "<h3 style='color: #e74c3c; margin: 10px;'>🗑️ Remove Item</h3>" +
                                "<p style='font-size: 14px; color: #2c3e50;'>" +
                                "Remove <b>" + currentProductName + "</b> from cart?</p>" +
                                "<p style='font-size: 12px; color: #7f8c8d;'>" +
                                "This action will reduce the quantity by 1</p>" +
                                "</div></html>",
                        "Confirm Removal",
                        JOptionPane.YES_NO_OPTION,
                        JOptionPane.QUESTION_MESSAGE);

                if (result == JOptionPane.YES_OPTION) {
                    // Stop editing before making changes to avoid table model conflicts
                    try {
                        // Cancel editing first to avoid conflicts
                        cancelCellEditing();

                        // Remove item from cart using stored product name
                        controller.removeFromCart(currentProductName);

                    } catch (Exception ex) {
                        System.err.println("[ERROR] Exception during item removal: " + ex.getMessage());
                        ex.printStackTrace();
                    }
                } else {
                    // User cancelled - stop editing normally
                    cancelCellEditing();
                }
            });

            panel.add(deleteButton);
        }

        @Override
        public Component getTableCellEditorComponent(JTable table, Object value,
                boolean isSelected, int row, int column) {
            currentRow = row;

            // Safely get and store the product name
            try {
                if (row >= 0 && row < tableModel.getRowCount()) {
                    Object productNameObj = tableModel.getValueAt(row, 0);
                    currentProductName = productNameObj != null ? productNameObj.toString() : null;
                } else {
                    currentProductName = null;
                    System.err.println("[WARNING] Invalid row index: " + row + ", table has " + tableModel.getRowCount()
                            + " rows");
                }
            } catch (Exception e) {
                currentProductName = null;
                System.err.println("[ERROR] Failed to get product name from row " + row + ": " + e.getMessage());
            }

            panel.setBackground(table.getSelectionBackground());
            deleteButton.setBackground(IconManager.DANGER_COLOR.darker());
            return panel;
        }

        @Override
        public Object getCellEditorValue() {
            return "Delete";
        }

        @Override
        public boolean stopCellEditing() {
            // Override to handle potential exceptions when table model changes
            try {
                return super.stopCellEditing();
            } catch (ArrayIndexOutOfBoundsException e) {
                System.err.println(
                        "[WARNING] ArrayIndexOutOfBoundsException caught in stopCellEditing, cancelling instead");
                cancelCellEditing();
                return false;
            } catch (Exception e) {
                System.err.println("[ERROR] Exception in stopCellEditing: " + e.getMessage());
                cancelCellEditing();
                return false;
            }
        }
    }
}