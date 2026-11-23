import java.io.IOException;
import java.net.URL;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;

public class DashboardHostController {

    @FXML private Label lblReservasiMenunggu;
    @FXML private Label lblCheckinHariIni;
    @FXML private Label lblCheckoutHariIni;
    @FXML private VBox boxReservasiTerbaru;

    @FXML
    public void initialize() {
        // TODO: ganti dengan data dari database / model
        lblReservasiMenunggu.setText("5");
        lblCheckinHariIni.setText("2");
        lblCheckoutHariIni.setText("2");

        String[][] data = {
            {"Made Wirawan", "Menunggu"},
            {"Kadek Ayu", "Dikonfirmasi"},
            {"Putu Arya", "Menunggu"},
            {"Komang Sari", "Selesai"},
            {"Wayan Dewi", "Dibatalkan"}
        };
        for (String[] d : data) {
            boxReservasiTerbaru.getChildren().add(buatBaris(d[0], d[1]));
        }
    }

    private HBox buatBaris(String nama, String status) {
        Label lblNama = new Label(nama);
        lblNama.setFont(Font.font(14));

        Label lblStatus = new Label(status);
        lblStatus.setFont(Font.font("System", FontWeight.BOLD, 14));

        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);

        HBox baris = new HBox(lblNama, spacer, lblStatus);
        baris.setAlignment(Pos.CENTER_LEFT);
        return baris;
    }

    // ===== Sidebar =====
    @FXML
    void handleDashboard(ActionEvent event) {
        // sudah di halaman ini
    }

    @FXML
    void handleKelolaHomestay(ActionEvent event) {
        pindahHalaman(event, "ListingHost.fxml");
    }

    @FXML
    void handleKelolaReservasi(ActionEvent event) {
        pindahHalaman(event, "KelolaReservasi.fxml");
    }

    @FXML
    void handleProfile(MouseEvent event) {
        System.out.println("Ikon Profile diklik!");
    }

    // ===== Helper =====
    private void pindahHalaman(ActionEvent event, String namaFxml) {
        try {
            URL lokasi = getClass().getResource(namaFxml);
            if (lokasi == null) {
                tampilkanError("File " + namaFxml + " tidak ditemukan. Pastikan file sudah dibuat dan ada di folder src/bin.");
                return;
            }
            Parent root = FXMLLoader.load(lokasi);
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
            tampilkanError("Gagal memuat " + namaFxml + ": " + e.getMessage());
        }
    }

    private void tampilkanError(String pesan) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setHeaderText(null);
        alert.setContentText(pesan);
        alert.show();
    }
}