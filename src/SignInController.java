import java.io.IOException;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

public class SignInController {

    @FXML private TextField loginIdentifierField;
    @FXML private PasswordField loginPasswordField;

    @FXML
    private void handleSignInSubmit() {
        String identifier = loginIdentifierField.getText().trim();
        String password = loginPasswordField.getText();

        if (identifier.isEmpty() || password.isEmpty()) {
            showAlert(Alert.AlertType.WARNING, "Input Kosong", "Masukkan username/email dan password Anda.");
            return;
        }

        if (UserSession.authenticate(identifier, password)) {
            try {
                App.setRoot("LandingPage");
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            showAlert(Alert.AlertType.ERROR, "Gagal Masuk", "Username/email atau password salah. Pastikan Anda sudah mendaftar.");
        }
    }

    @FXML
    private void handleGoToSignUp() {
        try {
            App.setRoot("SignUpPage");
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