
package thesweetspot.patterns.builder;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import thesweetspot.patterns.composite.ReceiptGroup;
import thesweetspot.patterns.composite.ReceiptItem;

public class receiptBuilder implements Receipt_Builder {

    private List<ReceiptGroup> groups = new ArrayList<>();
    private StringBuilder sb = new StringBuilder();
    private String totalSection = "";
    private String footerSection = "";
    
    public receiptBuilder header(String bakeryName, String cashierName) {
        sb.append("========================================\n");
        sb.append(String.format("         %-30s\n", bakeryName));
        sb.append("            BAKERY RECEIPT              \n");
        sb.append("========================================\n");
        sb.append(String.format("Receipt #: %d\n", System.currentTimeMillis()));
        sb.append("Date     : ").append(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date())).append("\n");
        sb.append("Cashier  : ").append(cashierName).append("\n");
        sb.append("----------------------------------------\n");
        sb.append(String.format("%-16s %5s %8s %8s\n", "Item", "Qty", "Price", "Total"));
        sb.append("----------------------------------------\n");
        return this;
    }

    public receiptBuilder item(String productName, String type, int quantity, double totalPrice) {
        ReceiptItem item = new ReceiptItem(productName, quantity, totalPrice);

        ReceiptGroup receiptGroup = groups.stream()
                .filter(g -> g.display().startsWith("> " + type))
                .findFirst()
                .orElse(null);

        if (receiptGroup == null) {
            receiptGroup = new ReceiptGroup(type);
            groups.add(receiptGroup);
        }

        receiptGroup.addItem(item);
        return this;
    }

    private String getProductType(String name) {
        String[] types = {"Biscuits", "Beverages", "Sweets", "Baking Goods", "Cake", "Cookies", "Bread", "Pastry", "Muffins"};
        String nameLower = name.toLowerCase();

        for (String type : types) {
            if (nameLower.contains(type.toLowerCase())) {
                return type;
            }
        }
        return "Others"; // fallback
    }

    public receiptBuilder total(double total, double amountReceived, double change) {
        totalSection = String.format(
                "----------------------------------------\n"
                + "%-30s $%7.2f\n"
                + "%-30s $%7.2f\n"
                + "%-30s $%7.2f\n",
                "TOTAL:", total,
                "AMOUNT RECEIVED:", amountReceived,
                "CHANGE:", change
        );
        return this;
    }

    public receiptBuilder footer() {
        footerSection = "----------------------------------------\n"
                + "         Thank you for shopping!      \n"
                + "========================================\n";
        return this;
    }

    public String build() {
        for (ReceiptGroup group : groups) {
            sb.append(group.display());
        }

        sb.append("\n").append(totalSection);
        sb.append(footerSection);
        return sb.toString();
    }
}
