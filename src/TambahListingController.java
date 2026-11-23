import java.io.File;
import java.io.IOException;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

public class TambahListingController {

    @FXML private ImageView imgPreviewVilla;
    @FXML private Label lblPlaceholderFoto;
    @FXML private TextField txtNamaPenginapan;
    @FXML private VBox boxFasilitasContainer;
    @FXML private TextField txtFasilitas1;
    @FXML private TextField txtFasilitas2;
    @FXML private TextArea txtAlamat;
    @FXML private Label lblKapasitas;
    @FXML private TextArea txtDeskripsi;
    @FXML private TextField txtHarga;

    private int jumlahTamu = 1;

    @FXML
    void handleTambahFoto(MouseEvent event) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Pilih Foto Kamar / Villa");
        fileChooser.getExtensionFilters().addAll(
            new FileChooser.ExtensionFilter("File Gambar", "*.png", "*.jpg", "*.jpeg")
        );

        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        File selectedFile = fileChooser.showOpenDialog(stage);

        if (selectedFile != null) {
            Image image = new Image(selectedFile.toURI().toString());
            imgPreviewVilla.setImage(image);
            lblPlaceholderFoto.setVisible(false);
        }
    }

    @FXML
    void handleTambahBarisFasilitas(ActionEvent event) {
        HBox barisBaru = new HBox(8.0);
        barisBaru.setAlignment(Pos.CENTER_LEFT);

        TextField inputBaru = new TextField();
        inputBaru.setPromptText("Fasilitas tambahan...");
        inputBaru.setStyle("-fx-background-color: #FFFFFF; -fx-border-color: #2E5230; -fx-border-radius: 6; -fx-background-radius: 6; -fx-padding: 8 12 8 12; -fx-font-size: 13px;");
        HBox.setHgrow(inputBaru, Priority.ALWAYS);

        Region spacer = new Region();
        spacer.setPrefWidth(26.0);

        barisBaru.getChildren().addAll(inputBaru, spacer);
        boxFasilitasContainer.getChildren().add(barisBaru);
    }

    @FXML
    void handleKurangTamu(ActionEvent event) {
        if (jumlahTamu > 1) {
            jumlahTamu--;
            lblKapasitas.setText(String.valueOf(jumlahTamu));
        }
    }

    @FXML
    void handleTambahTamu(ActionEvent event) {
        jumlahTamu++;
        lblKapasitas.setText(String.valueOf(jumlahTamu));
    }

    @FXML
    void handleSelesai(ActionEvent event) {
        String nama = txtNamaPenginapan.getText().trim();
        String harga = txtHarga.getText().trim();

        if (nama.isEmpty() || harga.isEmpty()) {
            Alert warning = new Alert(AlertType.WARNING);
            warning.setTitle("Data Belum Lengkap");
            warning.setHeaderText(null);
            warning.setContentText("Mohon isi minimal Nama Penginapan dan Harga per Malam terlebih dahulu.");
            warning.showAndWait();
            return;
        }

        Alert info = new Alert(AlertType.INFORMATION);
        info.setTitle("Berhasil");
        info.setHeaderText(null);
        info.setContentText("Listing \"" + nama + "\" berhasil disimpan!");
        info.showAndWait();

        pindahHalaman(event, "ListingHost.fxml");
    }

    @FXML
    void handleKembaliKeListing(ActionEvent event) {
        pindahHalaman(event, "ListingHost.fxml");
    }

    @FXML
    void handleBeranda(ActionEvent event) {
        pindahHalaman(event, "LandingPage.fxml");
    }

    @FXML
    void handleProfile(MouseEvent event) {
        System.out.println("Ikon Profil diklik dari halaman Tambah Listing");
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