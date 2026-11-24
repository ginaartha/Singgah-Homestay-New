import java.io.IOException;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.layout.HBox;

public class LandingPageController {

    // fx:id di LandingPage.fxml
    @FXML private HBox authButtonsBox;
    @FXML private Button profileButton;
    @FXML private ComboBox<String> locationComboBox;
    @FXML private DatePicker datePicker; // sebelumnya checkInDatePicker (tidak cocok dengan FXML)
    @FXML private ComboBox<String> guestComboBox;

    @FXML
    public void initialize() {
        boolean isLoggedIn = UserSession.isLoggedIn();
        if (authButtonsBox != null && profileButton != null) {
            authButtonsBox.setVisible(!isLoggedIn);
            authButtonsBox.setManaged(!isLoggedIn);

            profileButton.setVisible(isLoggedIn);
            profileButton.setManaged(isLoggedIn);
        }

        if (locationComboBox != null) {
            locationComboBox.setItems(FXCollections.observableArrayList(
                "Badung", "Bangli", "Buleleng", "Denpasar",
                "Gianyar", "Jembrana", "Karangasem", "Klungkung", "Tabanan"
            ));
        }

        if (guestComboBox != null) {
            guestComboBox.setItems(FXCollections.observableArrayList(
                "1 Tamu", "2 Tamu", "3 Tamu", "4 Tamu", "5+ Tamu"
            ));
        }
    }

    // ===== NAVBAR =====

    // onAction="#handleHome" (sebelumnya tidak ada di controller -> LoadException)
    @FXML
    private void handleHome() {
        navigate("LandingPage");
    }

    @FXML
    private void handleSearchRoom() {
        UserSession.setSearchFilter(null, null, null);
        navigate("HasilPencarian");
    }

    @FXML
    private void handleMyReservations() {
        if (!UserSession.isLoggedIn()) {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Masuk Diperlukan");
            alert.setHeaderText(null);
            alert.setContentText("Silakan masuk ke akun Anda terlebih dahulu untuk melihat Reservasi Saya.");
            alert.showAndWait();
            navigate("SignInPage");
            return;
        }
        navigate("ReservasiSaya");
    }

    @FXML
    private void handleRegister() {
        navigate("SignUpPage");
    }

    @FXML
    private void handleLogin() {
        navigate("SignInPage");
    }

    @FXML
    private void handleProfileClick() {
        if (!UserSession.isLoggedIn()) {
            return;
        }
        String role = UserSession.getRole();
        if ("HOST".equalsIgnoreCase(role)) {
            navigate("HostProfilPage");
        } else if ("ADMIN".equalsIgnoreCase(role)) {
            navigate("AdminDashboard");
        } else {
            navigate("TamuProfilPage");
        }
    }

    // ===== PENCARIAN =====

    @FXML
    private void handleCheckAvailability() {
        String loc = locationComboBox != null ? locationComboBox.getValue() : null;
        String date = (datePicker != null && datePicker.getValue() != null)
            ? datePicker.getValue().toString()
            : null;
        String guests = guestComboBox != null ? guestComboBox.getValue() : null;

        UserSession.setSearchFilter(loc, date, guests);
        navigate("HasilPencarian");
    }

    // ===== KARTU VILLA (nama handler disamakan dengan FXML: handleBookingVilla1/2/3) =====

    @FXML
    private void handleBookingVilla1() {
        UserSession.setSelectedVilla(
            "Dhyana Villa",
            "Jl. Menuju Hatimu, No. 01, Kec. Klungkung, Kab. Klungkung, Bali",
            "2 tamu • Kamar mandi dalam • Wi-Fi",
            250000,
            "/images/DhyanaVilla.png"
        );
        navigate("DetailVilla");
    }

    @FXML
    private void handleBookingVilla2() {
        UserSession.setSelectedVilla(
            "Inten Villa",
            "Jl. Tanah Lot No. 12, Kec. Kediri, Kab. Tabanan, Bali",
            "3 tamu • Kamar mandi dalam • Wi-Fi",
            250000,
            "/images/DhyanaVilla.png"
        );
        navigate("DetailVilla");
    }

    @FXML
    private void handleBookingVilla3() {
        UserSession.setSelectedVilla(
            "Gina Villa",
            "Jl. Raya Ubud No. 88, Kec. Ubud, Kab. Gianyar, Bali",
            "4 tamu • Kamar mandi dalam • Wi-Fi",
            250000,
            "/images/DhyanaVilla.png"
        );
        navigate("DetailVilla");
    }

    // ===== UTIL =====

    private void navigate(String fxml) {
        try {
            App.setRoot(fxml);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}