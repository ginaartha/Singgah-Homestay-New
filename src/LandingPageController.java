import java.io.IOException;
import java.time.LocalDate;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.layout.HBox;

public class LandingPageController {

    @FXML private Button homeButton;
    @FXML private Button searchRoomButton;
    @FXML private Button myReservationsButton;
    @FXML private HBox authButtonsBox;
    @FXML private Button registerButton;
    @FXML private Button loginButton;
    @FXML private Button profileButton;
    @FXML private ComboBox<String> locationComboBox;
    @FXML private DatePicker datePicker;
    @FXML private ComboBox<String> guestComboBox;
    @FXML private Button checkAvailabilityButton;

    @FXML
    public void initialize() {
        boolean isLoggedIn = UserSession.isLoggedIn();
        if (authButtonsBox != null && profileButton != null) {
            authButtonsBox.setVisible(!isLoggedIn);
            authButtonsBox.setManaged(!isLoggedIn);

            profileButton.setVisible(isLoggedIn);
            profileButton.setManaged(isLoggedIn);
        }

        if (locationComboBox != null && locationComboBox.getItems().isEmpty()) {
            locationComboBox.setItems(FXCollections.observableArrayList(
                "Badung", "Bangli", "Buleleng", "Denpasar",
                "Gianyar", "Jembrana", "Karangasem", "Klungkung", "Tabanan"
            ));
        }

        if (guestComboBox != null && guestComboBox.getItems().isEmpty()) {
            guestComboBox.setItems(FXCollections.observableArrayList(
                "1 tamu", "2 tamu", "3 tamu", "4 tamu", "5+ tamu"
            ));
        }
    }

    @FXML
    private void handleRegister() {
        try {
            App.setRoot("SignUpPage");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void handleLogin() {
        try {
            App.setRoot("SignInPage");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void handleProfileClick() {
        if (!UserSession.isLoggedIn()) {
            return;
        }
        try {
            if ("HOST".equalsIgnoreCase(UserSession.getRole())) {
                App.setRoot("HostProfilPage");
            } else {
                App.setRoot("TamuProfilPage");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void handleCheckAvailability() {
        String location = locationComboBox.getValue();
        LocalDate date = datePicker.getValue();
        String guests = guestComboBox.getValue();

        UserSession.setSearchFilter(
            location,
            date != null ? date.toString() : null,
            guests
        );

        try {
            App.setRoot("HasilPencarian");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void handleHome() {
        showInfo("Beranda", "Kamu sedang berada di halaman Beranda.");
    }

    @FXML
    private void handleSearchRoom() {
        UserSession.setSearchFilter(null, null, null);
        try {
            App.setRoot("HasilPencarian");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void handleMyReservations() {
        showInfo("Reservasi Saya", "Fitur halaman Reservasi Saya belum dihubungkan.");
    }

    @FXML
    private void handleBookingVilla1() {
        showInfo("Booking", "Booking dipilih untuk Dhyana Villa (Card 1).");
    }

    @FXML
    private void handleBookingVilla2() {
        showInfo("Booking", "Booking dipilih untuk Inten Villa (Card 2).");
    }

    @FXML
    private void handleBookingVilla3() {
        showInfo("Booking", "Booking dipilih untuk Gina Villa (Card 3).");
    }

    private void showInfo(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}