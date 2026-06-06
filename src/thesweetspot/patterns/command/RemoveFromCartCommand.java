
package thesweetspot.patterns.command;

import java.sql.Connection;
import java.sql.PreparedStatement;
import javafx.scene.control.Alert;
import javafx.scene.control.TableView;
import thesweetspot.DialogUtils;
import thesweetspot.Data.data;
import thesweetspot.patterns.singleton.DBConnection;
import thesweetspot.Data.productData;

public class RemoveFromCartCommand implements command {

    private final TableView<productData> tableView;
    private final Runnable refreshOrderData;
    private final Runnable refreshTotal;
    private final Runnable refreshCards;

    public RemoveFromCartCommand(
            TableView<productData> tableView,
            Runnable refreshOrderData,
            Runnable refreshTotal,
            Runnable refreshCards
    ) {
        this.tableView = tableView;
        this.refreshOrderData = refreshOrderData;
        this.refreshTotal = refreshTotal;
        this.refreshCards = refreshCards;
    }

    @Override
    public void execute() {
        productData selectedItem = tableView.getSelectionModel().getSelectedItem();

        if (selectedItem == null) {
            showAlert("Please select an item to remove", Alert.AlertType.ERROR);
            return;
        }

        if (data.cID == null) {
            showAlert("No items in cart", Alert.AlertType.ERROR);
            return;
        }

        try {
            Connection connect = DBConnection.getInstance();
            String sql = "EXEC sp_RemoveFromCart ?, ?";
            PreparedStatement prepare = connect.prepareStatement(sql);
            prepare.setInt(1, data.cID);
            prepare.setString(2, selectedItem.getProductId());
            prepare.executeUpdate();

            showAlert("Item removed from cart", Alert.AlertType.INFORMATION);

            // GUI Refresh
            if (refreshOrderData != null) {
                refreshOrderData.run();
            }
            if (refreshTotal != null) {
                refreshTotal.run();
            }
            if (refreshCards != null) {
                refreshCards.run();
            }

        } catch (Exception e) {
            e.printStackTrace();
            showAlert("Failed to remove item: " + e.getMessage(), Alert.AlertType.ERROR);
        }
    }

    private void showAlert(String message, Alert.AlertType type) {
        Alert alert = new Alert(type);
        alert.setTitle("Cart");
        alert.setHeaderText(null);
        alert.setContentText(message);
        DialogUtils.applyBakeryIcon(alert); 
        alert.showAndWait();
    }
}