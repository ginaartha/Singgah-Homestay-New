import java.io.IOException;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;

public class TamuProfilController {

    @FXML private TextField usernameField;
    @FXML private TextField fullNameField;
    @FXML private TextField emailField;
    @FXML private TextField phoneField;

    @FXML
    public void initialize() {
        // Proteksi: hanya bisa dilihat kalau sudah login
        if (!UserSession.isLoggedIn()) {
            Platform.runLater(() -> {
                try {
                    App.setRoot("SignInPage");
                } catch (IOException e) {
                    e.printStackTrace();
                }
            });
            return;
        }

        usernameField.setText(UserSession.getUsername() != null ? UserSession.getUsername() : "");
        fullNameField.setText(UserSession.getFullName() != null ? UserSession.getFullName() : "");
        emailField.setText(UserSession.getEmail() != null ? UserSession.getEmail() : "");
        phoneField.setText(UserSession.getPhone() != null ? UserSession.getPhone() : "");
    }

    @FXML
    private void handleBackToLanding() {
        try {
            App.setRoot("LandingPage");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void handleLogout() {
        UserSession.logout();
        try {
            App.setRoot("LandingPage");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}