import java.io.IOException;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

public class HostSignInController {

    @FXML private TextField loginIdentifierField;
    @FXML private PasswordField loginPasswordField;

    @FXML
    private void handleHostSignInSubmit() {
        String identifier = loginIdentifierField.getText().trim();
        String password = loginPasswordField.getText();

        if (identifier.isEmpty() || password.isEmpty()) {
            showAlert(Alert.AlertType.WARNING, "Input Kosong", "Masukkan username/email dan password host.");
            return;
        }

        if (UserSession.authenticateWithRole(identifier, password, "HOST")) {
            showAlert(Alert.AlertType.INFORMATION, "Login Host Berhasil", "Selamat datang kembali, Host " + UserSession.getFullName() + "!");
            try {
                App.setRoot("LandingPage");
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            showAlert(Alert.AlertType.ERROR, "Gagal Masuk", "Akun Host tidak ditemukan atau password salah.");
        }
    }

    @FXML
    private void handleGoToHostSignUp() {
        try {
            App.setRoot("HostSignUpPage");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void handleGoToGuestSignIn() {
        try {
            App.setRoot("SignInPage");
        } catch (IOException e) {
            e.printStackTrace();
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