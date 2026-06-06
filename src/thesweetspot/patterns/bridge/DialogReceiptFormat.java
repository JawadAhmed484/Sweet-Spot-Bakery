package thesweetspot.patterns.bridge;

public class DialogReceiptFormat implements ReceiptFormat {

    @Override
    public void formatAndExport(String content) {
        receiptView viewer = new DialogReceiptView(); // already exists in your project
        viewer.showReceipt(content);
    }

}
