package thesweetspot.patterns.composite;

public class ReceiptItem implements receiptComponent {

    private String productName;
    private int quantity;
    private double price;

    public ReceiptItem(String productName, int quantity, double price) {
        this.productName = productName;
        this.quantity = quantity;
        this.price = price;
    }

    @Override
    public String display() {
        double unitPrice = price / quantity;
        return String.format("%-16s %5d %8.2f %8.2f\n", productName, quantity, unitPrice, price);
    }
}
