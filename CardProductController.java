package thesweetspot;

import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Spinner;
import javafx.scene.control.SpinnerValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import thesweetspot.Data.data;
import thesweetspot.Data.productData;
import thesweetspot.patterns.adapter.FileImageLoader;
import thesweetspot.patterns.adapter.URLImage;
import thesweetspot.patterns.adapter.URLImageAdapter;
import thesweetspot.patterns.adapter.productImage;
import thesweetspot.patterns.singleton.DBConnection;

/**
 *
 * @author WINDOWS 10
 */
public class CardProductController implements Initializable {

    @FXML
    private AnchorPane card_form;

    @FXML
    private Label prod_name;

    @FXML
    private Label prod_price;

    @FXML
    private ImageView prod_imageView;

    @FXML
    private Spinner<Integer> prod_spinner;

    @FXML
    private Button prod_addBtn;

    private productData prodData;
    private Image image;

    private String prodID;
    private String type;
    private String prod_date;
    private String prod_image;

    private SpinnerValueFactory<Integer> spin;

    private Connection connect;
    private PreparedStatement prepare;
    private ResultSet result;

    private Alert alert;

    public void setData(productData prodData) {
        this.prodData = prodData;

        prod_image = prodData.getImage();
        prod_date = String.valueOf(prodData.getDate());
        type = prodData.getType();
        prodID = prodData.getProductId();
        prod_name.setText(prodData.getProductName());
        prod_price.setText("$" + String.valueOf(prodData.getPrice()));
        productImage imageLoader;
        if (prodData.getImage().startsWith("http")) {
            imageLoader = new URLImageAdapter(new URLImage());
        } else {
            imageLoader = new FileImageLoader();
        }

        image = imageLoader.loadImage(prodData.getImage());
        prod_imageView.setImage(image);
        pr = prodData.getPrice();

    }
    private int qty;

    public void setQuantity() {
        spin = new SpinnerValueFactory.IntegerSpinnerValueFactory(0, 100, 0);
        prod_spinner.setValueFactory(spin);
    }

    private double totalP;
    private double pr;

    public void addBtn() {
        qty = prod_spinner.getValue();

        if (qty == 0) {
            alert = new Alert(AlertType.ERROR);
            alert.setTitle("Error Message");
            alert.setHeaderText(null);
            alert.setContentText("Please select quantity greater than 0");
            DialogUtils.applyBakeryIcon(alert); 
            alert.showAndWait();
            return;
        }

        String check = "";
        String checkAvailable = "SELECT status FROM products WHERE product_id = ?";

        connect = DBConnection.getInstance();

        try {
            int checkStock = 0;
            String checkStockQuery = "SELECT stock FROM products WHERE product_id = ?";

            prepare = connect.prepareStatement(checkStockQuery);
            prepare.setString(1, prodID);
            result = prepare.executeQuery();

            if (result.next()) {
                checkStock = result.getInt("stock");
            }

            // If stock is 0, update status to unavailable
            if (checkStock == 0) {
                String updateStock = "UPDATE products SET status = 'Unavailable' WHERE product_id = ?";
                prepare = connect.prepareStatement(updateStock);
                prepare.setString(1, prodID);
                prepare.executeUpdate();
            }

            // Check if product is available
            prepare = connect.prepareStatement(checkAvailable);
            prepare.setString(1, prodID);
            result = prepare.executeQuery();

            if (result.next()) {
                check = result.getString("status");
            }

            if (!check.equals("Available") || qty == 0) {
                alert = new Alert(AlertType.ERROR);
                alert.setTitle("Error Message");
                alert.setHeaderText(null);
                alert.setContentText("Product is not available or out of stock");
                DialogUtils.applyBakeryIcon(alert); 
                alert.showAndWait();
            } else {
                if (checkStock < qty) {
                    alert = new Alert(AlertType.ERROR);
                    alert.setTitle("Error Message");
                    alert.setHeaderText(null);
                    alert.setContentText("Invalid. This product is Out of stock. Available: " + checkStock);
                    DialogUtils.applyBakeryIcon(alert); 
                    alert.showAndWait();
                } else {
                    // Generate customer ID if not exists
                    if (data.cID == null) {
                        int lastCustomerID = 0;

                        // Get highest customer_id from customer table
                        String getMaxCustomerId = "SELECT MAX(Customer_ID) AS maxID FROM customer";
                        PreparedStatement ps = connect.prepareStatement(getMaxCustomerId);
                        ResultSet rs = ps.executeQuery();
                        if (rs.next()) {
                            lastCustomerID = rs.getInt("maxID");
                        }

                        // Also check max from receipt (uses 'Custome_ID' due to typo)
                        String getMaxFromReceipt = "SELECT MAX(Customer_ID) AS maxID FROM receipt";
                        ps = connect.prepareStatement(getMaxFromReceipt);
                        rs = ps.executeQuery();
                        if (rs.next()) {
                            int receiptMax = rs.getInt("maxID");
                            if (receiptMax > lastCustomerID) {
                                lastCustomerID = receiptMax;
                            }
                        }

                        data.cID = lastCustomerID + 1;
                    }

                    prod_image = prod_image.replace("\\", "\\\\");
                    // Add to customer order
                    String insertData = "INSERT INTO customer "
                            + "(customer_id, product_id, product_name, type, quantity, price, date, image, em_username) "
                            + "VALUES(?,?,?,?,?,?,?,?,?)";
                    prepare = connect.prepareStatement(insertData);
                    prepare.setString(1, String.valueOf(data.cID));
                    prepare.setString(2, prodID);
                    prepare.setString(3, prod_name.getText());
                    prepare.setString(4, type);
                    prepare.setString(5, String.valueOf(qty));

                    totalP = (qty * prodData.getPrice());
                    prepare.setString(6, String.valueOf(totalP));

                    Date date = new Date();
                    java.sql.Date sqlDate = new java.sql.Date(date.getTime());
                    prepare.setString(7, String.valueOf(sqlDate));

                    prepare.setString(8, prod_image);
                    prepare.setString(9, data.username);

                    prepare.executeUpdate();

                    // Update product stock
                    int newStock = checkStock - qty;
                    String updateStock = "UPDATE products SET stock = ? WHERE product_id = ?";
                    prepare = connect.prepareStatement(updateStock);
                    prepare.setInt(1, newStock);
                    prepare.setString(2, prodID);
                    prepare.executeUpdate();

                    // If stock becomes 0, mark as unavailable
                    if (newStock == 0) {
                        String updateStatus = "UPDATE products SET status = 'Unavailable' WHERE product_id = ?";
                        prepare = connect.prepareStatement(updateStatus);
                        prepare.setString(1, prodID);
                        prepare.executeUpdate();
                    }

                    if (Main_FormController.instance != null) {
                        Main_FormController.instance.refreshMenuDisplays();
                    }

                    alert = new Alert(AlertType.INFORMATION);
                    alert.setTitle("Information Message");
                    alert.setHeaderText(null);
                    alert.setContentText("Successfully Added to Cart!");
                    DialogUtils.applyBakeryIcon(alert); 
                    alert.showAndWait();
                    // Reset spinner
                    prod_spinner.getValueFactory().setValue(0);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            alert = new Alert(AlertType.ERROR);
            alert.setTitle("Error Message");
            alert.setHeaderText(null);
            alert.setContentText("Error adding product: " + e.getMessage());
            DialogUtils.applyBakeryIcon(alert); 
            alert.showAndWait();
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
        setQuantity();
    }

}
