
package thesweetspot.patterns.bridge;

public abstract class ReceiptExporter {
    protected ReceiptFormat format;

    public ReceiptExporter(ReceiptFormat format) {
        this.format = format;
    }

    public abstract void export(String receiptContent);
}