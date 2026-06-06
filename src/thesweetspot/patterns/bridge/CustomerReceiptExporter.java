
package thesweetspot.patterns.bridge;

public class CustomerReceiptExporter extends ReceiptExporter {
    public CustomerReceiptExporter(ReceiptFormat format) {
        super(format);
    }

    @Override
    public void export(String receiptContent) {
        format.formatAndExport(receiptContent);
    }
}
