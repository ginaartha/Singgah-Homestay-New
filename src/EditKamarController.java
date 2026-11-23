import javafx.scene.layout.Region;

import java.io.File;
import java.io.IOException;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

public class EditKamarController {

    // ===== Navbar =====
    @FXML private Button btnBeranda;
    @FXML private Button btnListing;

    // ===== Foto =====
    @FXML private Label lblPlaceholderFoto;
    @FXML private ImageView imgPreviewVilla;

    // ===== Form =====
    @FXML private TextField txtNamaPenginapan;
    @FXML private VBox boxFasilitasContainer;
    @FXML private TextField txtFasilitas1;
    @FXML private TextField txtFasilitas2;
    @FXML private TextArea txtAlamat;
    @FXML private Label lblKapasitas;
    @FXML private TextArea txtDeskripsi;
    @FXML private TextField txtHarga;
    @FXML private Button btnSimpan;

    private int kapasitas = 1;

    @FXML
    public void initialize() {
        lblKapasitas.setText(String.valueOf(kapasitas));
    }

    // ===== Navigasi =====
    @FXML
    void handleBeranda(ActionEvent event) {
        pindahHalaman(event, "LandingPage.fxml");
    }

    @FXML
    void handleKembaliKeListing(ActionEvent event) {
        pindahHalaman(event, "ListingHost.fxml");
    }

    @FXML
    void handleProfile(MouseEvent event) {
        System.out.println("Ikon Profile diklik!");
    }

    // ===== Foto =====
    @FXML
    void handleTambahFoto(MouseEvent event) {
        FileChooser chooser = new FileChooser();
        chooser.setTitle("Pilih Foto Kamar");
        chooser.getExtensionFilters().add(
            new FileChooser.ExtensionFilter("Gambar", "*.png", "*.jpg", "*.jpeg", "*.gif"));

        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        File file = chooser.showOpenDialog(stage);
        if (file != null) {
            imgPreviewVilla.setImage(new Image(file.toURI().toString()));
            lblPlaceholderFoto.setVisible(false);
        }
    }

    // ===== Fasilitas =====
    @FXML
    void handleTambahBarisFasilitas(ActionEvent event) {
        TextField baru = new TextField();
        baru.setPromptText("Tambahkan fasilitas lain...");
        baru.setStyle("-fx-background-color: #FFFFFF; -fx-border-color: #2E5230; "
                + "-fx-border-radius: 6; -fx-background-radius: 6; "
                + "-fx-padding: 8 12 8 12; -fx-font-size: 13px;");
        HBox.setHgrow(baru, Priority.ALWAYS);

        Region spacer = new Region();
        spacer.setPrefWidth(26.0);

        HBox baris = new HBox(8.0, baru, spacer);
        baris.setAlignment(javafx.geometry.Pos.CENTER_LEFT);
        boxFasilitasContainer.getChildren().add(baris);
    }

    // ===== Kapasitas =====
    @FXML
    void handleTambahTamu(ActionEvent event) {
        kapasitas++;
        lblKapasitas.setText(String.valueOf(kapasitas));
    }

    @FXML
    void handleKurangTamu(ActionEvent event) {
        if (kapasitas > 1) {
            kapasitas--;
            lblKapasitas.setText(String.valueOf(kapasitas));
        }
    }

    // ===== Simpan =====
    @FXML
    void handleSimpan(ActionEvent event) {
        String nama = txtNamaPenginapan.getText().trim();
        String alamat = txtAlamat.getText().trim();
        String deskripsi = txtDeskripsi.getText().trim();
        String harga = txtHarga.getText().trim();

        if (nama.isEmpty() || alamat.isEmpty() || harga.isEmpty()) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Data belum lengkap");
            alert.setHeaderText(null);
            alert.setContentText("Nama, alamat, dan harga wajib diisi.");
            alert.showAndWait();
            return;
        }

        // TODO: simpan ke database / model di sini
        System.out.println("Disimpan: " + nama + " | " + alamat + " | " + kapasitas
                + " tamu | " + deskripsi + " | " + harga);

        pindahHalaman(event, "ListingHost.fxml");
    }

    // ===== Helper =====
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