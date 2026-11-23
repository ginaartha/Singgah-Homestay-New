import java.io.IOException;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

public class ListingHostController {

    @FXML
    private Button btnBeranda;

    @FXML
    private Button btnListing;

    @FXML
    private Button btnTambahListing;

    @FXML
    void handleBeranda(ActionEvent event) {
        pindahHalaman(event, "LandingPage.fxml");
    }

    @FXML
    void handleTambahListing(ActionEvent event) {
        pindahHalaman(event, "TambahListing.fxml");
    }

    @FXML
    void handleEditVilla(ActionEvent event) {
        System.out.println("1. Tombol Edit berhasil diklik!");
        System.out.println("2. Lokasi file EditKamar.fxml: " + getClass().getResource("EditKamar.fxml"));
        pindahHalaman(event, "EditKamar.fxml");
    }

    @FXML
    void handleProfile(MouseEvent event) {
        System.out.println("Ikon Profile diklik!");
    }

    private void pindahHalaman(ActionEvent event, String namaFxml) {
        try {
            Parent root = FXMLLoader.load(getClass().getResource(namaFxml));
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}