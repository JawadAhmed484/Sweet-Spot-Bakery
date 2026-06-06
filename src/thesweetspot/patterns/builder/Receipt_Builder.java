package thesweetspot.patterns.builder;

public interface Receipt_Builder {

    Receipt_Builder header(String bakeryName, String cashierName);
    Receipt_Builder item(String productName, String type, int quantity, double totalPrice);
    Receipt_Builder total(double total, double amountReceived, double change);
    Receipt_Builder footer();
    String build();

}


