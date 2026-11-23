import java.time.LocalDate;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;

public class LandingPageController {

    @FXML
    private Button homeButton;

    @FXML
    private Button searchRoomButton;

    @FXML
    private Button myReservationsButton;

    @FXML
    private Button registerButton;

    @FXML
    private Button loginButton;

    @FXML
    private ComboBox<String> locationComboBox;

    @FXML
    private DatePicker datePicker;

    @FXML
    private ComboBox<String> guestComboBox;

    @FXML
    private Button checkAvailabilityButton;

    @FXML
    public void initialize() {
        if (locationComboBox != null && locationComboBox.getItems().isEmpty()) {
            locationComboBox.setItems(FXCollections.observableArrayList(
                "Badung",
                "Bangli",
                "Buleleng",
                "Denpasar",
                "Gianyar",
                "Jembrana",
                "Karangasem",
                "Klungkung",
                "Tabanan"
            ));
        }

        if (guestComboBox != null && guestComboBox.getItems().isEmpty()) {
            guestComboBox.setItems(FXCollections.observableArrayList(
                "1 tamu",
                "2 tamu",
                "3 tamu",
                "4 tamu",
                "5+ tamu"
            ));
        }
    }

    @FXML
    private void handleCheckAvailability() {
        String location = locationComboBox.getValue();
        LocalDate date = datePicker.getValue();
        String guests = guestComboBox.getValue();

        StringBuilder summary = new StringBuilder("Filter pencarian:\n");
        summary.append("Lokasi: ").append(location != null ? location : "Belum dipilih").append("\n");
        summary.append("Tanggal: ").append(date != null ? date.toString() : "Belum dipilih").append("\n");
        summary.append("Jumlah tamu: ").append(guests != null ? guests : "Belum dipilih");

        showInfo("Cek Ketersediaan", summary.toString());
    }

    @FXML
    private void handleHome() {
        showInfo("Beranda", "Kamu sedang berada di halaman Beranda.");
    }

    @FXML
    private void handleSearchRoom() {
        showInfo("Cari Kamar", "Fitur halaman Cari Kamar belum dihubungkan.");
    }

    @FXML
    private void handleMyReservations() {
        showInfo("Reservasi Saya", "Fitur halaman Reservasi Saya belum dihubungkan.");
    }

    @FXML
    private void handleRegister() {
        showInfo("Daftar", "Fitur halaman Daftar akun belum dihubungkan.");
    }

    @FXML
    private void handleLogin() {
        showInfo("Masuk", "Fitur halaman Masuk akun belum dihubungkan.");
    }

    @FXML
    private void handleBookingVilla1() {
        showInfo("Booking", "Booking dipilih untuk Dhyana Villa (Card 1).");
    }

    @FXML
    private void handleBookingVilla2() {
        showInfo("Booking", "Booking dipilih untuk Dhyana Villa (Card 2).");
    }

    @FXML
    private void handleBookingVilla3() {
        showInfo("Booking", "Booking dipilih untuk Dhyana Villa (Card 3).");
    }

    private void showInfo(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}