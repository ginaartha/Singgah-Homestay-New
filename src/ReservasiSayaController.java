import java.io.IOException;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

public class ReservasiSayaController {

    @FXML private HBox authButtonsBox;
    @FXML private Button profileButton;
    @FXML private VBox reservationsContainer;

    @FXML
    public void initialize() {
        // Proteksi: halaman Reservasi Saya hanya bisa diakses jika sudah login
        if (!UserSession.isLoggedIn()) {
            Platform.runLater(() -> {
                Alert alert = new Alert(Alert.AlertType.WARNING);
                alert.setTitle("Akses Terbatas");
                alert.setHeaderText(null);
                alert.setContentText("Silakan masuk terlebih dahulu untuk melihat Reservasi Saya.");
                alert.showAndWait();
                try {
                    App.setRoot("SignInPage");
                } catch (IOException e) {
                    e.printStackTrace();
                }
            });
            return;
        }

        if (authButtonsBox != null && profileButton != null) {
            authButtonsBox.setVisible(false);
            authButtonsBox.setManaged(false);
            profileButton.setVisible(true);
            profileButton.setManaged(true);
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