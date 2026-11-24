package controllers;
import app.App;
import java.io.IOException;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import models.UserSession;

public class DetailVillaController {

    @FXML private HBox authButtonsBox;
    @FXML private Button profileButton;

    @FXML private ImageView mainPhotoView;
    @FXML private ImageView subPhoto1View;
    @FXML private ImageView subPhoto2View;
    @FXML private ImageView subPhoto3View;

    @FXML private Label villaNameLabel;
    @FXML private Label villaAddressLabel;
    @FXML private Label villaFacilitiesLabel;
    @FXML private Label villaPriceLabel;

    @FXML private DatePicker checkInPicker;
    @FXML private DatePicker checkOutPicker;
    @FXML private ComboBox<String> guestCountComboBox;
    @FXML private Label totalPriceLabel;

    private int pricePerNight = 250000;

    @FXML
    public void initialize() {
        boolean isLoggedIn = UserSession.isLoggedIn();
        if (authButtonsBox != null && profileButton != null) {
            authButtonsBox.setVisible(!isLoggedIn);
            authButtonsBox.setManaged(!isLoggedIn);

            profileButton.setVisible(isLoggedIn);
            profileButton.setManaged(isLoggedIn);
        }

        if (UserSession.getSelectedVillaName() != null) {
            villaNameLabel.setText(UserSession.getSelectedVillaName());
            villaAddressLabel.setText(UserSession.getSelectedVillaAddress());
            villaFacilitiesLabel.setText("Fasilitas: " + UserSession.getSelectedVillaFacilities());
            pricePerNight = UserSession.getSelectedVillaPrice();
            villaPriceLabel.setText("Rp" + String.format("%,d,00", pricePerNight).replace(',', '.'));

            Image img = loadImage(UserSession.getSelectedVillaImage());
            if (img != null) {
                if (mainPhotoView != null) mainPhotoView.setImage(img);
                if (subPhoto1View != null) subPhoto1View.setImage(img);
                if (subPhoto2View != null) subPhoto2View.setImage(img);
                if (subPhoto3View != null) subPhoto3View.setImage(img);
            }
        }

        guestCountComboBox.setItems(FXCollections.observableArrayList(
            "1 orang", "2 orang", "3 orang", "4 orang", "5 orang"
        ));
        guestCountComboBox.setValue("1 orang");

        checkInPicker.setValue(LocalDate.now().plusDays(1));
        checkOutPicker.setValue(LocalDate.now().plusDays(3));

        checkInPicker.valueProperty().addListener((obs, oldVal, newVal) -> updateTotalPrice());
        checkOutPicker.valueProperty().addListener((obs, oldVal, newVal) -> updateTotalPrice());
        updateTotalPrice();
    }

    private Image loadImage(String path) {
        if (path == null || path.isEmpty()) {
            return null;
        }
        try {
            if (path.startsWith("file:") || path.startsWith("http:") || path.startsWith("https:")) {
                return new Image(path);
            }
            var stream = getClass().getResourceAsStream(path);
            return stream != null ? new Image(stream) : null;
        } catch (Exception e) {
            return null;
        }
    }

    private void updateTotalPrice() {
        if (totalPriceLabel == null) {
            return;
        }
        LocalDate in = checkInPicker.getValue();
        LocalDate out = checkOutPicker.getValue();
        if (in != null && out != null && out.isAfter(in)) {
            long nights = ChronoUnit.DAYS.between(in, out);
            long total = nights * pricePerNight;
            totalPriceLabel.setText("Rp " + String.format("%,d,00", total).replace(',', '.') + " (" + nights + " malam)");
        } else {
            totalPriceLabel.setText("Rp " + String.format("%,d,00", pricePerNight).replace(',', '.') + " (1 malam)");
        }
    }

    @FXML
    private void handlePilihKamar() {
        LocalDate checkIn = checkInPicker.getValue();
        LocalDate checkOut = checkOutPicker.getValue();
        String guests = guestCountComboBox.getValue();

        if (checkIn == null || checkOut == null || !checkOut.isAfter(checkIn)) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Tanggal Tidak Valid");
            alert.setHeaderText(null);
            alert.setContentText("Tanggal Check-out harus setelah tanggal Check-in.");
            alert.showAndWait();
            return;
        }

        if (!UserSession.isLoggedIn()) {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Masuk Diperlukan");
            alert.setHeaderText(null);
            alert.setContentText("Silakan masuk ke akun Anda terlebih dahulu untuk melanjutkan pemesanan.");
            alert.showAndWait();
            try {
                App.setRoot("SignInPage");
            } catch (IOException e) {
                e.printStackTrace();
            }
            return;
        }

        long nights = ChronoUnit.DAYS.between(checkIn, checkOut);
        long total = nights * pricePerNight;

        UserSession.setActiveBooking(checkIn, checkOut, guests, nights, total);

        try {
            App.setRoot("Pembayaran");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void handleHome() {
        try {
            App.setRoot("LandingPage");
        } catch (IOException e) {
            e.printStackTrace();
        }
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
        if (!UserSession.isLoggedIn()) {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Masuk Diperlukan");
            alert.setHeaderText(null);
            alert.setContentText("Silakan masuk ke akun Anda terlebih dahulu untuk melihat Reservasi Saya.");
            alert.showAndWait();
            try {
                App.setRoot("SignInPage");
            } catch (IOException e) {
                e.printStackTrace();
            }
            return;
        }

        try {
            App.setRoot("ReservasiSaya");
        } catch (IOException e) {
            e.printStackTrace();
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
            String role = UserSession.getRole();
            if ("HOST".equalsIgnoreCase(role)) {
                App.setRoot("HostProfilPage");
            } else {
                App.setRoot("TamuProfilPage");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}