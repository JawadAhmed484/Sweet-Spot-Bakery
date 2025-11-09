package thesweetspot;

import java.io.File;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.chart.AreaChart;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Dialog;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.TextInputDialog;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.stage.FileChooser;
import javafx.stage.FileChooser.ExtensionFilter;
import javafx.stage.Stage;
import thesweetspot.Data.customersData;
import thesweetspot.Data.data;
import thesweetspot.Data.employeeData;
import thesweetspot.Data.productData;
import thesweetspot.patterns.bridge.CustomerReceiptExporter;
import thesweetspot.patterns.bridge.DialogReceiptFormat;
import thesweetspot.patterns.bridge.PDFReceiptFormat;
import thesweetspot.patterns.bridge.ReceiptExporter;
import thesweetspot.patterns.bridge.ReceiptFormat;
import thesweetspot.patterns.builder.Receipt_Builder;
import thesweetspot.patterns.builder.receiptBuilder;
import thesweetspot.patterns.command.AddProductCommand;
import thesweetspot.patterns.command.ClearCartCommand;
import thesweetspot.patterns.command.ClearFormCommand;
import thesweetspot.patterns.command.DeleteProductCommand;
import thesweetspot.patterns.command.ImportImageCommand;
import thesweetspot.patterns.command.PayCommand;
import thesweetspot.patterns.command.RemoveFromCartCommand;
import thesweetspot.patterns.command.UpdateProductCommand;
import thesweetspot.patterns.command.command;
import thesweetspot.patterns.nullobject.nullProductData;
import thesweetspot.patterns.observer.dashboardObserver;
import thesweetspot.patterns.observer.dashboardSubject;
import thesweetspot.patterns.singleton.DBConnection;

public class Main_FormController implements Initializable, dashboardObserver {

    @FXML
    private Button customers_btn;

    @FXML
    private Label username;

    @FXML
    private TableColumn<customersData, String> customers_col_cashier;

    @FXML
    private TableColumn<customersData, String> customers_col_customerID;

    @FXML
    private TableColumn<customersData, String> customers_col_date;

    @FXML
    private TableColumn<customersData, String> customers_col_total;

    @FXML
    private AnchorPane customers_form;

    @FXML
    private TableView<customersData> customers_tableView;

    @FXML
    private BarChart<?, ?> dashboard_CustomerChart;

    @FXML
    private Label dashboard_NC;

    @FXML
    private Label dashboard_NSP;

    @FXML
    private Label dashboard_TI;

    @FXML
    private Label dashboard_TotalI;

    @FXML
    private Button dashboard_btn;

    @FXML
    private AnchorPane dashboard_form;

    @FXML
    private AreaChart<?, ?> dashboard_incomeChart;

    @FXML
    private Button employee_btn;

    @FXML
    private TableColumn<employeeData, Integer> employee_col_ID;

    @FXML
    private TableColumn<employeeData, String> employee_col_Name;

    @FXML
    private TableColumn<employeeData, Integer> employee_col_customersHandled;

    @FXML
    private TableColumn<employeeData, Date> employee_col_date;

    @FXML
    private TableColumn<employeeData, Integer> employee_col_itemsSold;

    @FXML
    private TableColumn<employeeData, Double> employee_col_sale;

    @FXML
    private AnchorPane employee_form;

    @FXML
    private TableView<employeeData> employee_tableView;

    @FXML
    private Button inventory_addBtn;

    @FXML
    private Button inventory_btn;

    @FXML
    private Button inventory_clearBtn;

    @FXML
    private TableColumn<productData, String> inventory_col_date;

    @FXML
    private TableColumn<productData, String> inventory_col_price;

    @FXML
    private TableColumn<productData, String> inventory_col_productID;

    @FXML
    private TableColumn<productData, String> inventory_col_productName;

    @FXML
    private TableColumn<productData, String> inventory_col_status;

    @FXML
    private TableColumn<productData, String> inventory_col_stock;

    @FXML
    private TableColumn<productData, String> inventory_col_type;

    @FXML
    private Button inventory_deleteBtn;

    @FXML
    private AnchorPane inventory_form;

    @FXML
    private Button inventory_importBtn;

    @FXML
    private TextField inventory_price;

    @FXML
    private TextField inventory_productID;

    @FXML
    private TextField inventory_productName;

    @FXML
    private ComboBox<?> inventory_status;

    @FXML
    private TextField inventory_stock;

    @FXML
    private TableView<productData> inventory_tableView;

    @FXML
    private ComboBox<?> inventory_type;

    @FXML
    private Button inventory_updateBtn;

    @FXML
    private Button logout_btn;

    @FXML
    private TextField menu_amount;

    @FXML
    private Button menu_btn;

    @FXML
    private Label menu_change;

    @FXML
    private TableColumn<productData, String> menu_col_price;

    @FXML
    private TableColumn<productData, String> menu_col_productName;

    @FXML
    private TableColumn<productData, String> menu_col_quantity;

    @FXML
    private AnchorPane menu_form;

    @FXML
    private GridPane menu_gridPane;

    @FXML
    private Button menu_payBtn;

    @FXML
    private Button menu_clearBtn;

    @FXML
    private Button menu_receiptBtn;

    @FXML
    private Button menu_removeBtn;

    @FXML
    private ScrollPane menu_scrollPane;

    @FXML
    private TableView<productData> menu_tableView;

    @FXML
    private Label menu_total;

    @FXML
    private AnchorPane main_form;

    @FXML
    private ImageView inventory_imageView;

    public static Main_FormController instance;
    private Alert alert;
    private Image image;
    private Connection connect;
    private PreparedStatement prepare;
    private Statement statement;
    private ResultSet result;
    private double amount;
    private double change;
    private ObservableList<productData> cardListData = FXCollections.observableArrayList();

    // POS System Variables
    private double totalP = 0.0; // 8.25% tax
    private String lastReceiptData = "";

    public void dashboardDisplayNC() {
        String sql = "SELECT COUNT(ID) FROM receipt";
        connect = DBConnection.getInstance();

        try {
            int nc = 0;
            prepare = connect.prepareStatement(sql);
            result = prepare.executeQuery();

            if (result.next()) {
                nc = result.getInt(1); // Use column index instead of column name
            }
            dashboard_NC.setText(String.valueOf(nc));
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (result != null) {
                    result.close();
                }
                if (prepare != null) {
                    prepare.close();
                }
                if (connect != null) {
                    connect.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public void dashboardDisplayTI() {
        // Get today's income
        String sql = "SELECT ISNULL(SUM(Total), 0) FROM receipt WHERE CAST(Date AS DATE) = CAST(GETDATE() AS DATE)";
        connect = DBConnection.getInstance();

        try {
            double ti = 0;
            prepare = connect.prepareStatement(sql);
            result = prepare.executeQuery();

            if (result.next()) {
                ti = result.getDouble(1);
            }

            dashboard_TI.setText("$" + String.format("%.2f", ti));

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (result != null) {
                    result.close();
                }
                if (prepare != null) {
                    prepare.close();
                }
                if (connect != null) {
                    connect.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public void dashboardTotalI() {
        String sql = "SELECT ISNULL(SUM(Total), 0) FROM receipt";
        connect = DBConnection.getInstance();

        try {
            double ti = 0;
            prepare = connect.prepareStatement(sql);
            result = prepare.executeQuery();

            if (result.next()) {
                ti = result.getDouble(1);
            }
            dashboard_TotalI.setText("$" + String.format("%.2f", ti));

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (result != null) {
                    result.close();
                }
                if (prepare != null) {
                    prepare.close();
                }
                if (connect != null) {
                    connect.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public void dashboardNSP() {
        // Count products sold today
        String sql = "SELECT ISNULL(SUM(Quantity), 0) FROM customer";
        connect = DBConnection.getInstance();

        try {
            int q = 0;
            prepare = connect.prepareStatement(sql);
            result = prepare.executeQuery();

            if (result.next()) {
                q = result.getInt(1);
            }
            dashboard_NSP.setText(String.valueOf(q));

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (result != null) {
                    result.close();
                }
                if (prepare != null) {
                    prepare.close();
                }
                if (connect != null) {
                    connect.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public void dashboardIncomeChart() {
        dashboard_incomeChart.getData().clear();

        String sql = "SELECT Date, SUM(Total) FROM receipt GROUP BY Date ORDER BY Date";
        connect = DBConnection.getInstance();

        XYChart.Series chart = new XYChart.Series();
        chart.setName("Daily Income");

        try {
            prepare = connect.prepareStatement(sql);
            result = prepare.executeQuery();

            while (result.next()) {
                String date = result.getDate(1).toString();
                double total = result.getDouble(2);
                chart.getData().add(new XYChart.Data(date, total));
            }

            dashboard_incomeChart.getData().add(chart);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void dashboardCustomerChart() {
        dashboard_CustomerChart.getData().clear();

        String sql = "SELECT Date, COUNT(ID) FROM receipt GROUP BY Date ORDER BY Date";
        connect = DBConnection.getInstance();

        XYChart.Series chart = new XYChart.Series();
        chart.setName("Daily Customers");

        try {
            prepare = connect.prepareStatement(sql);
            result = prepare.executeQuery();

            while (result.next()) {
                String date = result.getDate(1).toString();
                int count = result.getInt(2);
                chart.getData().add(new XYChart.Data(date, count));
            }

            dashboard_CustomerChart.getData().add(chart);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void refreshDashboard() {
        dashboardDisplayNC();
        dashboardDisplayTI();
        dashboardTotalI();
        dashboardNSP();
        dashboardIncomeChart();
        dashboardCustomerChart();
    }

    @Override
    public void updateDashboard() {
        refreshDashboard();  // <-- Auto-refresh dashboard data
    }

    // ========== EXISTING INVENTORY METHODS (PRESERVED) ==========
    public void inventoryAddBtn() {
        command addProduct = new AddProductCommand(
                inventory_productID,
                inventory_productName,
                inventory_type,
                inventory_stock,
                inventory_price,
                inventory_status,
                this::inventoryShowData,
                this::inventoryClearBtn
        );
        addProduct.execute();

    }

    public void inventoryUpdateBtn() {
        command updateProduct = new UpdateProductCommand(
                inventory_productID,
                inventory_productName,
                inventory_type,
                inventory_stock,
                inventory_price,
                inventory_status,
                this::inventoryShowData,
                this::inventoryClearBtn
        );
        updateProduct.execute();
    }

    public void inventoryDeleteBtn() {
        command deleteProduct = new DeleteProductCommand(
                inventory_productID,
                this::inventoryShowData,
                this::inventoryClearBtn
        );
        deleteProduct.execute();
    }

    public void inventoryClearBtn() {
        command clearForm = new ClearFormCommand(
                inventory_productID,
                inventory_productName,
                inventory_type,
                inventory_stock,
                inventory_price,
                inventory_status,
                inventory_imageView
        );
        clearForm.execute();
    }

    public void inventoryImportBtn() {
        command importImage = new ImportImageCommand(inventory_imageView, main_form.getScene().getWindow());
        importImage.execute();
    }

    public ObservableList<productData> inventoryDataList() {
        ObservableList<productData> listData = FXCollections.observableArrayList();

        String sql = "SELECT * FROM products";
        connect = DBConnection.getInstance();

        try {
            prepare = connect.prepareStatement(sql);
            result = prepare.executeQuery();

            productData prodData;

            while (result.next()) {
                prodData = new productData(result.getInt("id"),
                        result.getString("product_id"),
                        result.getString("product_name"),
                        result.getString("type"),
                        result.getInt("stock"),
                        result.getDouble("price"),
                        result.getString("status"),
                        result.getString("image"),
                        result.getDate("date"));

                listData.add(prodData);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return listData;
    }

    private ObservableList<productData> inventoryListData;

    public void inventoryShowData() {
        inventoryListData = inventoryDataList();

        inventory_col_productID.setCellValueFactory(new PropertyValueFactory<>("productId"));
        inventory_col_productName.setCellValueFactory(new PropertyValueFactory<>("productName"));
        inventory_col_type.setCellValueFactory(new PropertyValueFactory<>("type"));
        inventory_col_stock.setCellValueFactory(new PropertyValueFactory<>("stock"));
        inventory_col_price.setCellValueFactory(new PropertyValueFactory<>("price"));
        inventory_col_status.setCellValueFactory(new PropertyValueFactory<>("status"));
        inventory_col_date.setCellValueFactory(new PropertyValueFactory<>("date"));

        inventory_tableView.setItems(inventoryListData);
    }

    public void inventorySelectData() {
        productData prodData = Optional.ofNullable(inventory_tableView.getSelectionModel().getSelectedItem())
                .orElse(new nullProductData());
        if (prodData.isNull()) {
            inventory_productID.setText("No product selected");
            inventory_productName.setText("");
            inventory_stock.setText("");
            inventory_price.setText("");
            inventory_status.getSelectionModel().clearSelection();
            inventory_type.getSelectionModel().clearSelection();
            inventory_imageView.setImage(null);

            // Optionally disable buttons
            inventory_updateBtn.setDisable(true);
            inventory_clearBtn.setDisable(true);
            inventory_deleteBtn.setDisable(true);
            return;
        } else {
            inventory_updateBtn.setDisable(false);
            inventory_clearBtn.setDisable(false);
            inventory_deleteBtn.setDisable(false);
        }

        int num = inventory_tableView.getSelectionModel().getSelectedIndex();

        if ((num - 1) < -1) {
            return;
        }

        inventory_productID.setText(prodData.getProductId());
        inventory_productName.setText(prodData.getProductName());
        inventory_stock.setText(String.valueOf(prodData.getStock()));
        inventory_price.setText(String.valueOf(prodData.getPrice()));

        data.path = prodData.getImage();
        data.date = String.valueOf(prodData.getDate());
        data.id = prodData.getId();

        String path = data.path;

        Image image;

        try {
            if (path.startsWith("http://") || path.startsWith("https://")) {
                // It's a URL
                image = new Image(path, 121, 126, false, true);
            } else {
                // Assume local file path
                image = new Image(new File(path).toURI().toString(), 121, 126, false, true);
            }
            inventory_imageView.setImage(image);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private String[] typeList = {"Biscuits", "Beverages", "Sweets", "Baking Goods", "Cake", "Cookies", "Bread", "Pastry", "Muffins", "Others"};

    public void inventoryTypeList() {
        List<String> typeL = new ArrayList<>();

        for (String data : typeList) {
            typeL.add(data);
        }

        ObservableList listData = FXCollections.observableArrayList(typeL);
        inventory_type.setItems(listData);
    }

    private String[] statusList = {"Available", "Unavailable"};

    public void inventoryStatusList() {
        List<String> statusL = new ArrayList<>();

        for (String data : statusList) {
            statusL.add(data);
        }

        ObservableList listData = FXCollections.observableArrayList(statusL);
        inventory_status.setItems(listData);
    }

    // ========== ENHANCED POS SYSTEM METHODS ==========
    public ObservableList<productData> menuGetData() {
        String sql = "SELECT * FROM products WHERE status = 'Available'";

        ObservableList<productData> listData = FXCollections.observableArrayList();
        connect = DBConnection.getInstance();
        try {
            prepare = connect.prepareStatement(sql);
            result = prepare.executeQuery();

            productData prod;

            while (result.next()) {
                prod = new productData(result.getInt("ID"),
                        result.getString("Product_ID"),
                        result.getString("Product_Name"),
                        result.getString("Type"),
                        result.getInt("Stock"),
                        result.getDouble("Price"),
                        result.getString("Status"),
                        result.getString("Image"),
                        result.getDate("Date"));

                listData.add(prod);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return listData;
    }

    public void menuDisplayCard() {
        productCardFactory factory = new productCardFactory();
        cardListData.clear();
        cardListData.addAll(menuGetData());

        int row = 0;
        int column = 0;

        menu_gridPane.getChildren().clear();
        menu_gridPane.getRowConstraints().clear();
        menu_gridPane.getColumnConstraints().clear();

        for (int q = 0; q < cardListData.size(); q++) {
            AnchorPane pane = factory.createProductCard(cardListData.get(q));
            if (pane != null) {
                if (column == 3) {
                    column = 0;
                    row++;
                }

                menu_gridPane.add(pane, column++, row);
                GridPane.setMargin(pane, new Insets(10));
            }
        }
    }

    public ObservableList<productData> menuDisplayOrder() {
        ObservableList<productData> listData = FXCollections.observableArrayList();
        // Debug: Print customer ID
        System.out.println("Current Customer ID: " + data.cID);

        if (data.cID == null) {
            System.out.println("No customer ID - returning empty list");
            return listData;
        }
        String sql = "SELECT * FROM customer WHERE customer_id = ?";
        connect = DBConnection.getInstance();
        try {
            prepare = connect.prepareStatement(sql);
            prepare.setInt(1, data.cID);
            result = prepare.executeQuery();

            boolean hasResults = false;

            productData prod;
            while (result.next()) {
                hasResults = true;
                prod = new productData(result.getInt("ID"),
                        result.getString("Product_ID"),
                        result.getString("Product_Name"),
                        result.getString("Type"),
                        result.getInt("Quantity"),
                        result.getDouble("Price"),
                        result.getString("Image"),
                        result.getDate("Date"));
                listData.add(prod);
            }

            System.out.println("Query returned " + listData.size() + " items");
        } catch (Exception e) {
            System.out.println("Error in menuDisplayOrder: " + e.getMessage());
            e.printStackTrace();
        }
        return listData;
    }

    private ObservableList<productData> menuListData;

    public void menuShowOrderData() {
        menuListData = menuDisplayOrder();

        menu_col_productName.setCellValueFactory(new PropertyValueFactory<>("productName"));
        menu_col_quantity.setCellValueFactory(new PropertyValueFactory<>("quantity"));
        menu_col_price.setCellValueFactory(new PropertyValueFactory<>("price"));

        menu_tableView.setItems(menuListData);

    }

    public void menuGetTotal() {
        String sql = "SELECT SUM(price) FROM customer WHERE customer_id = ?";
        connect = DBConnection.getInstance();

        try {
            prepare = connect.prepareStatement(sql);
            prepare.setInt(1, data.cID != null ? data.cID : 0);
            result = prepare.executeQuery();

            if (result.next()) {
                totalP = result.getDouble(1);
            }

            // Display total without tax
            menu_total.setText("$" + String.format("%.2f", totalP));

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void menuAmount() {
        menuGetTotal();
        if (menu_amount.getText().isEmpty() || totalP == 0) {
            alert = new Alert(AlertType.ERROR);
            alert.setTitle("Error Message");
            alert.setHeaderText(null);
            alert.setContentText("Invalid :3");
            DialogUtils.applyBakeryIcon(alert);
            alert.showAndWait();
        } else {
            amount = Double.parseDouble(menu_amount.getText());
            if (amount < totalP) {
                menu_amount.setText("");
            } else {
                change = (amount - totalP);
                menu_change.setText("$" + change);
            }
        }
    }

    public void menuPayBtn() {
        command pay = new PayCommand(
                menu_amount, // Payment input field
                menu_change, // Label to show calculated change
                menu_total, // Label showing total price
                menu_receiptBtn, // Button to enable after payment
                this::menuGetTotal, // Method reference to refresh total
                () -> generateReceipt( // Lambda to generate receipt with live values
                        Double.parseDouble(menu_total.getText().replace("$", "")),
                        Double.parseDouble(menu_amount.getText()),
                        Double.parseDouble(menu_change.getText().replace("$", ""))
                )
        );
        pay.execute();
        updateEmployeeStats();
        employeeShowData();

    }

    private void generateReceipt(double total, double amountReceived, double change) {
        Receipt_Builder rb = new receiptBuilder();

        rb.header("THE SWEET SPOT BAKERY", data.username);

        try {
            String sql = "SELECT * FROM customer WHERE customer_id = ?";
            connect = DBConnection.getInstance();
            prepare = connect.prepareStatement(sql);
            prepare.setInt(1, data.cID);
            result = prepare.executeQuery();

            while (result.next()) {
                String productName = result.getString("Product_Name");
                int quantity = result.getInt("Quantity");
                double price = result.getDouble("Price");

                String type = result.getString("Type");
                rb.item(productName, type, quantity, price);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        rb.total(total, amountReceived, change)
                .footer();

        lastReceiptData = rb.build();
    }

    public void menuReceiptBtn() {
        if (lastReceiptData == null || lastReceiptData.isEmpty()) {
            alert = new Alert(AlertType.ERROR);
            alert.setTitle("Error Message");
            alert.setHeaderText(null);
            alert.setContentText("No receipt available. Please complete a payment first.");
            DialogUtils.applyBakeryIcon(alert);
            alert.showAndWait();
            return;
        }

        // Show export format choice dialog
        Alert choiceAlert = new Alert(AlertType.CONFIRMATION);
        choiceAlert.setTitle("Export Receipt");
        choiceAlert.setHeaderText(null);
        choiceAlert.setContentText("Choose how you want to view the receipt.\nSelect format:");
        DialogUtils.applyBakeryIcon(choiceAlert);

        ButtonType dialogOption = new ButtonType("Show on Screen");
        ButtonType pdfOption = new ButtonType("Save as PDF");
        ButtonType cancelOption = new ButtonType("Cancel", ButtonBar.ButtonData.CANCEL_CLOSE);

        choiceAlert.getButtonTypes().setAll(dialogOption, pdfOption, cancelOption);
        Optional<ButtonType> result = choiceAlert.showAndWait();

        if (result.isPresent()) {
            if (result.get() == dialogOption) {
                ReceiptFormat guiFormat = new DialogReceiptFormat();
                ReceiptExporter exporter = new CustomerReceiptExporter(guiFormat);
                exporter.export(lastReceiptData);

            } else if (result.get() == pdfOption) {
                ReceiptFormat pdfFormat = new PDFReceiptFormat(menu_receiptBtn.getScene().getWindow());
                ReceiptExporter exporter = new CustomerReceiptExporter(pdfFormat);
                exporter.export(lastReceiptData);

            } else {
                // Cancelled
                System.out.println("User cancelled receipt export.");
            }
        }
    }

    public void menuRemoveBtn() {
        command removeCommand = new RemoveFromCartCommand(menu_tableView,
                this::menuShowOrderData, this::menuGetTotal, this::menuDisplayCard);
        removeCommand.execute();

    }

    public void menuClearCartBtn() {
        command clearCart = new ClearCartCommand(this::menuShowOrderData, menu_total, menu_change);
        clearCart.execute();
    }

    public void menuRestart() {
        // Reset customer ID and totals
        data.cID = null;
        totalP = 0.0;

        // Clear menu displays
        menu_total.setText("$0.00");
        menu_amount.setText("");
        menu_change.setText("$0.00");

        // Refresh the menu table to show empty state
        menuShowOrderData();
    }

    public ObservableList<customersData> customersDataList() {
        ObservableList<customersData> listData = FXCollections.observableArrayList();

        try {
            String sql = "SELECT * FROM receipt ORDER BY Date DESC";
            connect = DBConnection.getInstance();

            if (connect != null) {
                prepare = connect.prepareStatement(sql);
                result = prepare.executeQuery();

                while (result.next()) {
                    customersData cData = new customersData(
                            result.getInt("ID"), // Exact column name
                            result.getInt("Customer_ID"), // Exact column name
                            result.getDouble("Total"), // Exact column name
                            result.getDate("Date"), // Exact column name
                            result.getString("Em_Username")); // Exact column name

                    listData.add(cData);
                }
            }

        } catch (Exception e) {
            System.out.println("Error loading customer data: " + e.getMessage());
            e.printStackTrace();
        } finally {
            try {
                if (result != null) {
                    result.close();
                }
                if (prepare != null) {
                    prepare.close();
                }
                if (connect != null) {
                    connect.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        return listData;
    }

    public void customersShowData() {
        try {
            ObservableList<customersData> customersListData = customersDataList();

            System.out.println("Loading customer data. Found " + customersListData.size() + " records");

            customers_col_customerID.setCellValueFactory(new PropertyValueFactory<>("customerID"));
            customers_col_total.setCellValueFactory(new PropertyValueFactory<>("total"));
            customers_col_date.setCellValueFactory(new PropertyValueFactory<>("date"));
            customers_col_cashier.setCellValueFactory(new PropertyValueFactory<>("emUsername"));

            customers_tableView.setItems(customersListData);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void updateEmployeeStats() {
        String getUserId = "SELECT ID FROM users WHERE UserName = ?";
        String checkEmployee = "SELECT * FROM employees WHERE Employee_ID = ? AND Date = CAST(GETDATE() AS DATE)";
        String updateEmployee = "UPDATE employees SET Items_Sold = Items_Sold + ?, Customers_Handled = Customers_Handled + 1, Sale = Sale + ? WHERE Employee_ID = ? AND Date = CAST(GETDATE() AS DATE)";
        String insertEmployee = "INSERT INTO employees (Employee_ID, Em_Username, Items_Sold, Customers_Handled, Sale, Date) VALUES (?, ?, ?, 1, ?, GETDATE())";

        try {
            connect = DBConnection.getInstance();

            int userId = -1;
            int totalItems = 0;
            double totalSale = 0.0;

            // 1. Get user ID
            prepare = connect.prepareStatement(getUserId);
            prepare.setString(1, data.username);
            result = prepare.executeQuery();
            if (result.next()) {
                userId = result.getInt("ID");
            }
            result.close();
            prepare.close();

            if (userId == -1) {
                return;
            }

            // 2. Get sale info
            prepare = connect.prepareStatement("SELECT SUM(Quantity) AS totalQty, SUM(Price) AS totalPrice FROM customer WHERE customer_id = ?");
            prepare.setInt(1, data.cID);
            result = prepare.executeQuery();
            if (result.next()) {
                totalItems = result.getInt("totalQty");
                totalSale = result.getDouble("totalPrice");
            }
            result.close();
            prepare.close();

            // 3. Check today's entry
            prepare = connect.prepareStatement(checkEmployee);
            prepare.setInt(1, userId);
            result = prepare.executeQuery();

            if (result.next()) {
                prepare = connect.prepareStatement(updateEmployee);
                prepare.setInt(1, totalItems);
                prepare.setDouble(2, totalSale);
                prepare.setInt(3, userId);
            } else {
                prepare = connect.prepareStatement(insertEmployee);
                prepare.setInt(1, userId);
                prepare.setString(2, data.username);
                prepare.setInt(3, totalItems);
                prepare.setDouble(4, totalSale);
            }

            prepare.executeUpdate();

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (result != null) {
                    result.close();
                }
            } catch (Exception e) {
            }
            try {
                if (prepare != null) {
                    prepare.close();
                }
            } catch (Exception e) {
            }
            try {
                if (connect != null) {
                    connect.close();
                }
            } catch (Exception e) {
            }
        }
    }

    public ObservableList<employeeData> employeeDataList() {
        ObservableList<employeeData> listData = FXCollections.observableArrayList();

        String sql = "SELECT * FROM employees  WHERE Date = CAST(GETDATE() AS DATE) ORDER BY Sale DESC";
        connect = DBConnection.getInstance();

        try {
            prepare = connect.prepareStatement(sql);
            result = prepare.executeQuery();

            while (result.next()) {
                employeeData empData = new employeeData(
                        result.getInt("Employee_ID"),
                        result.getString("Em_Username"),
                        result.getInt("Items_Sold"),
                        result.getInt("Customers_Handled"),
                        result.getDouble("Sale"),
                        result.getDate("Date")
                );

                listData.add(empData);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return listData;
    }

    private ObservableList<employeeData> employeeListData;

    public void employeeShowData() {
        employeeListData = employeeDataList();

        employee_col_ID.setCellValueFactory(new PropertyValueFactory<>("employeeId"));
        employee_col_Name.setCellValueFactory(new PropertyValueFactory<>("emUsername")); // Changed this line
        employee_col_itemsSold.setCellValueFactory(new PropertyValueFactory<>("itemsSold"));
        employee_col_customersHandled.setCellValueFactory(new PropertyValueFactory<>("customersHandled"));
        employee_col_sale.setCellValueFactory(new PropertyValueFactory<>("todaySales"));
        employee_col_date.setCellValueFactory(new PropertyValueFactory<>("todayDate"));

        employee_tableView.setItems(employeeListData);
    }

    // ========== EXISTING METHODS (PRESERVED) ==========
    public void logout() {
        try {
            alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Confirmation Message");
            alert.setHeaderText(null);
            alert.setContentText("Are you sure you want to logout?");
            DialogUtils.applyBakeryIcon(alert);
            Optional<ButtonType> option = alert.showAndWait();

            if (option.get().equals(ButtonType.OK)) {
                logout_btn.getScene().getWindow().hide();

                Parent root = FXMLLoader.load(getClass().getResource("FXMLDocument.fxml"));

                Stage stage = new Stage();
                Scene scene = new Scene(root);

                stage.setTitle("The Sweet Spot Bakery");
                stage.setScene(scene);

                Image image = new Image("resources/Bakery(Main Form).jpg");
                stage.getIcons().add(image);

                stage.show();
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void switchForm(ActionEvent event) {
        if (event.getSource() == dashboard_btn) {
            dashboard_form.setVisible(true);
            inventory_form.setVisible(false);
            menu_form.setVisible(false);
            customers_form.setVisible(false);
            employee_form.setVisible(false);

            refreshDashboard();

        } else if (event.getSource() == inventory_btn) {
            dashboard_form.setVisible(false);
            inventory_form.setVisible(true);
            menu_form.setVisible(false);
            customers_form.setVisible(false);
            employee_form.setVisible(false);

            inventoryTypeList();
            inventoryStatusList();
            inventoryShowData();

        } else if (event.getSource() == menu_btn) {
            dashboard_form.setVisible(false);
            inventory_form.setVisible(false);
            menu_form.setVisible(true);
            customers_form.setVisible(false);
            employee_form.setVisible(false);

            menuDisplayCard();
            menuShowOrderData();
            menuGetTotal();

        } else if (event.getSource() == customers_btn) {
            dashboard_form.setVisible(false);
            inventory_form.setVisible(false);
            menu_form.setVisible(false);
            customers_form.setVisible(true);
            employee_form.setVisible(false);

            customersShowData();
        } else if (event.getSource() == employee_btn) {
            dashboard_form.setVisible(false);
            inventory_form.setVisible(false);
            menu_form.setVisible(false);
            customers_form.setVisible(false);
            employee_form.setVisible(true);

            employeeShowData();
        }
    }

    public void displayUsername() {
        String user = data.username;
        user = user.substring(0, 1).toUpperCase() + user.substring(1);
        username.setText(user);
    }

    public void refreshMenuDisplays() {
        menuShowOrderData();
        menuGetTotal();
        // Also refresh product cards to update stock displays
        menuDisplayCard();
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        instance = this;
        displayUsername();
        dashboardSubject.addObserver(this);

        refreshDashboard();

        inventoryTypeList();
        inventoryStatusList();
        inventoryShowData();

        data.cID = 1;

        menuDisplayCard();
        menuShowOrderData();
        menuGetTotal();
        menuRestart();
        menu_receiptBtn.setDisable(true);

        customersShowData();
        employeeShowData();
    }

}
