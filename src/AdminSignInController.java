import java.io.IOException;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

public class AdminSignInController {

    @FXML private TextField loginIdentifierField;
    @FXML private PasswordField loginPasswordField;

    @FXML
    private void handleAdminSignInSubmit() {
        String identifier = loginIdentifierField.getText().trim();
        String password = loginPasswordField.getText();

        if (identifier.isEmpty() || password.isEmpty()) {
            showAlert(Alert.AlertType.WARNING, "Input Kosong", "Masukkan username/email dan password admin.");
            return;
        }

        if (UserSession.authenticateWithRole(identifier, password, "ADMIN")) {
            showAlert(Alert.AlertType.INFORMATION, "Login Admin Berhasil", "Selamat datang, " + UserSession.getFullName() + "!");
            try {
                App.setRoot("LandingPage");
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            showAlert(Alert.AlertType.ERROR, "Akses Ditolak", "Username/email atau password admin salah.");
        }
    }

    @FXML
    private void handleBackToLanding() {
        try {
            App.setRoot("LandingPage");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void showAlert(Alert.AlertType type, String title, String message) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}