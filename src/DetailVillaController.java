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

public class DetailVillaController {

    @FXML private HBox authButtonsBox;
    @FXML private Button profileButton;

    @FXML private ImageView mainPhotoView;
    @FXML private ImageView subPhoto1View;
    @FXML private ImageView subPhoto2View;
    @FXML private ImageView subPhoto3View;

    @FXML private Label villaNameLabel;
    @FXML private Label villaAddressLabel;
    @FXML private Label villaPriceLabel;
    @FXML private Label villaFacilitiesLabel;

    @FXML private DatePicker checkInPicker;
    @FXML private DatePicker checkOutPicker;
    @FXML private ComboBox<String> guestCountComboBox;

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

        // Load data villa yang dipilih dari UserSession
        String name = UserSession.getSelectedVillaName();
        String address = UserSession.getSelectedVillaAddress();
        String facilities = UserSession.getSelectedVillaFacilities();
        String imagePath = UserSession.getSelectedVillaImage();
        int price = UserSession.getSelectedVillaPrice();

        if (name != null && !name.isEmpty()) {
            villaNameLabel.setText(name);
        }
        if (address != null && !address.isEmpty()) {
            villaAddressLabel.setText(address);
        }
        if (facilities != null && !facilities.isEmpty()) {
            villaFacilitiesLabel.setText("Fasilitas: " + facilities);
        }
        if (price > 0) {
            this.pricePerNight = price;
            villaPriceLabel.setText(String.format("Rp%,d,00", price).replace(',', '.'));
        }
        if (imagePath != null && !imagePath.isEmpty()) {
            try {
                Image img = new Image(getClass().getResourceAsStream(imagePath));
                mainPhotoView.setImage(img);
                subPhoto1View.setImage(img);
                subPhoto2View.setImage(img);
                subPhoto3View.setImage(img);
            } catch (Exception e) {
                // Fallback ke gambar default di FXML
            }
        }

        // Default tanggal check-in & check-out
        LocalDate defaultCheckIn = LocalDate.now().plusDays(1);
        if (UserSession.getSearchDate() != null && !UserSession.getSearchDate().isEmpty()) {
            try {
                defaultCheckIn = LocalDate.parse(UserSession.getSearchDate());
            } catch (Exception ignored) {
            }
        }
        checkInPicker.setValue(defaultCheckIn);
        checkOutPicker.setValue(defaultCheckIn.plusDays(1));

        // Pilihan jumlah tamu
        guestCountComboBox.setItems(FXCollections.observableArrayList(
            "1 orang", "2 orang", "3 orang", "4 orang", "5 orang"
        ));
        guestCountComboBox.setValue("1 orang");
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

        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Konfirmasi Pilih Kamar");
        alert.setHeaderText(villaNameLabel.getText());
        alert.setContentText(
            "Check-in: " + checkIn + "\n" +
            "Check-out: " + checkOut + " (" + nights + " malam)\n" +
            "Tamu: " + guests + "\n" +
            "Total: Rp " + String.format("%,d", total).replace(',', '.')
        );
        alert.showAndWait();
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
            if ("HOST".equalsIgnoreCase(UserSession.getRole())) {
                App.setRoot("HostProfilPage");
            } else {
                App.setRoot("TamuProfilPage");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
