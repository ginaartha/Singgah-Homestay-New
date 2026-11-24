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
            navigate("HostDashboard");
        } else {
            showAlert(Alert.AlertType.ERROR, "Gagal Masuk", "Akun Host tidak ditemukan atau password salah.");
        }
    }

    @FXML
    private void handleGoToHostSignUp() {
        navigate("HostSignUpPage");
    }

    @FXML
    private void handleGoToGuestSignIn() {
        navigate("SignInPage");
    }

    @FXML
    private void handleBackToLanding() {
        navigate("LandingPage");
    }

    // Kalau gagal membuka halaman, penyebabnya muncul di jendela dan di terminal
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

    private void showAlert(Alert.AlertType type, String title, String message) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}