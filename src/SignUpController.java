import java.io.IOException;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

public class SignUpController {

    @FXML private TextField usernameField;
    @FXML private TextField fullNameField;
    @FXML private TextField emailField;
    @FXML private PasswordField passwordField;
    @FXML private TextField phoneField;

    @FXML
    private void handleSignUpSubmit() {
        String username = usernameField.getText().trim();
        String fullName = fullNameField.getText().trim();
        String email = emailField.getText().trim();
        String password = passwordField.getText();
        String phone = phoneField.getText().trim();

        if (username.isEmpty() || fullName.isEmpty() || email.isEmpty() || password.isEmpty() || phone.isEmpty()) {
            showAlert(Alert.AlertType.WARNING, "Form Belum Lengkap", "Harap isi semua kolom pendaftaran.");
            return;
        }

        if (password.length() < 8) {
            showAlert(Alert.AlertType.WARNING, "Password Terlalu Pendek", "Password harus minimal 8 karakter.");
            return;
        }

        boolean created = UserSession.registerUser(username, fullName, email, password, phone);
        if (!created) {
            showAlert(Alert.AlertType.ERROR, "Pendaftaran Gagal", "Username atau email sudah terdaftar. Gunakan yang lain.");
            return;
        }

        showAlert(Alert.AlertType.INFORMATION, "Pendaftaran Berhasil", "Akun berhasil dibuat! Silakan masuk.");
        navigate("SignInPage");
    }

    @FXML
    private void handleGoToSignIn() {
        navigate("SignInPage");
    }

    @FXML
    private void handleGoToHostSignUp() {
        navigate("HostSignUpPage");
    }

    @FXML
    private void handleBackToLanding() {
        navigate("LandingPage");
    }

    private void navigate(String fxml) {
        try {
            App.setRoot(fxml);
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