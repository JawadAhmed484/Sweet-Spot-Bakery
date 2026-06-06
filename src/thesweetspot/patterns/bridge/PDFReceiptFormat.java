
package thesweetspot.patterns.bridge;

import com.itextpdf.text.*;
import com.itextpdf.text.pdf.PdfWriter;
import javafx.stage.FileChooser;
import javafx.stage.Window;

import java.io.FileOutputStream;
import java.util.Optional;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.ButtonType;

public class PDFReceiptFormat implements ReceiptFormat {

    private final Window parentWindow;

    public PDFReceiptFormat(Window parentWindow) {
        this.parentWindow = parentWindow;
    }

    @Override
    public void formatAndExport(String content) {
        try {
            String defaultFileName = "receipt.pdf";

            // FileChooser to select custom location
            FileChooser fileChooser = new FileChooser();
            fileChooser.setTitle("Save Receipt PDF");
            fileChooser.setInitialFileName(defaultFileName);
            fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("PDF Files", "*.pdf"));

            java.io.File file = fileChooser.showSaveDialog(parentWindow);
            if (file == null) {
                return; // User cancelled
            }

            // Create PDF
            Document document = new Document(PageSize.A5);
            PdfWriter.getInstance(document, new FileOutputStream(file));
            document.open();

            // Add Logo
            try {
                Image logo = Image.getInstance("src/resources/Bakery.jpg");
                logo.scaleToFit(80, 80);
                logo.setAlignment(Image.ALIGN_LEFT);
                document.add(logo);
            } catch (Exception ex) {
                System.out.println("Logo not found or could not be added.");
            }

            // Title
            Font titleFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 14);
            Paragraph title = new Paragraph("Transaction Receipt", titleFont);
            title.setAlignment(Element.ALIGN_LEFT);
            title.setSpacingAfter(10f);
            document.add(title);

            // Content
            Font monoFont = FontFactory.getFont(FontFactory.COURIER, 10);
            Paragraph receiptParagraph = new Paragraph(content, monoFont);
            receiptParagraph.setAlignment(Element.ALIGN_LEFT);
            receiptParagraph.setSpacingAfter(5f);
            document.add(receiptParagraph);

            document.close();

            // Success Dialog
            Alert success = new Alert(Alert.AlertType.CONFIRMATION);
            success.setTitle("Exported");
            success.setHeaderText(null);
            success.setContentText("PDF saved successfully!\nFile: " + file.getAbsolutePath() + "\nDo you want to open it now?");

            ButtonType yes = new ButtonType("Yes");
            ButtonType no = new ButtonType("No", ButtonBar.ButtonData.CANCEL_CLOSE);
            success.getButtonTypes().setAll(yes, no);

            Optional<ButtonType> result = success.showAndWait();
            if (result.isPresent() && result.get() == yes) {
                java.awt.Desktop.getDesktop().open(file);
            }

        } catch (Exception e) {
            e.printStackTrace();
            Alert error = new Alert(Alert.AlertType.ERROR);
            error.setTitle("Export Error");
            error.setHeaderText(null);
            error.setContentText("Could not export PDF:\n" + (e.getMessage() != null ? e.getMessage() : "Unknown error"));
            error.showAndWait();
        }
    }
}