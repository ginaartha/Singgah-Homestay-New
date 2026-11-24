package controllers;
import java.io.IOException;

import app.App;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.layout.HBox;
import models.UserSession;

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
        updateNavbarState();
    }

    private void updateNavbarState() {
        boolean loggedIn = UserSession.isLoggedIn();
        if (authButtonsBox != null) {
            authButtonsBox.setVisible(!loggedIn);
            authButtonsBox.setManaged(!loggedIn);
        }
        if (profileButton != null) {
            profileButton.setVisible(loggedIn);
            profileButton.setManaged(loggedIn);
        }
    }

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
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Akses Terbatas");
            alert.setHeaderText(null);
            alert.setContentText("Silakan masuk terlebih dahulu untuk melihat Reservasi Saya.");
            alert.showAndWait();
            handleLogin();
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
            handleLogin();
            return;
        }
        if ("HOST".equalsIgnoreCase(UserSession.getRole())) {
            navigate("HostProfilPage");
        } else if ("ADMIN".equalsIgnoreCase(UserSession.getRole())) {
            navigate("AdminDashboard");
        } else {
            navigate("TamuProfilPage");
        }
    }

    @FXML
    private void handleCheckAvailability() {
        String loc = (locationComboBox != null) ? locationComboBox.getValue() : null;
        String date = (datePicker != null && datePicker.getValue() != null) ? datePicker.getValue().toString() : null;
        String guests = (guestComboBox != null) ? guestComboBox.getValue() : null;

        UserSession.setSearchFilter(loc, date, guests);
        navigate("HasilPencarian");
    }

    @FXML
    private void handleBookingVilla1() {
        openVillaDetail(0, "Dhyana Villa", "Jl. Menuju Hatimu, No. 01, Kec. Klungkung, Kab. Klungkung, Bali",
            "2 tamu • Kamar mandi dalam • Wi-Fi", 250000, "");
    }

    @FXML
    private void handleBookingVilla2() {
        openVillaDetail(1, "Inten Villa", "Jl. Tanah Lot No. 12, Kec. Kediri, Kab. Tabanan, Bali",
            "3 tamu • Kamar mandi dalam • Wi-Fi", 250000, "");
    }

    @FXML
    private void handleBookingVilla3() {
        openVillaDetail(2, "Gina Villa", "Jl. Raya Ubud No. 88, Kec. Ubud, Kab. Gianyar, Bali",
            "4 tamu • Kamar mandi dalam • Wi-Fi", 250000, "");
    }

    private void openVillaDetail(int index, String fallbackName, String fallbackAddr,
                                 String fallbackFac, int fallbackPrice, String fallbackImg) {
        if (UserSession.getAllVillas().size() > index) {
            UserSession.VillaData v = UserSession.getAllVillas().get(index);
            UserSession.setSelectedVilla(v.name, v.fullAddress, v.facilities, v.pricePerNight, v.imagePath);
        } else {
            UserSession.setSelectedVilla(fallbackName, fallbackAddr, fallbackFac, fallbackPrice, fallbackImg);
        }
        navigate("DetailVilla");
    }

    private void navigate(String fxml) {
        try {
            App.setRoot(fxml);
        } catch (IOException e) {
            e.printStackTrace();
            String detail = String.valueOf(e.getMessage());
            if (e.getCause() != null) {
                detail += "\n\nPenyebab: " + e.getCause();
            }
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Gagal membuka halaman");
            alert.setHeaderText(fxml);
            alert.setContentText(detail);
            alert.showAndWait();
        }
    }
}