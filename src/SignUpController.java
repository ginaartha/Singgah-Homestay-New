import java.io.IOException;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

public class SignUpController {

    @FXML private TextField usernameField;
    @FXML private TextField fullNameField;
    @FXML private TextField emailField;
    @FXML private TextField phoneField;
    @FXML private PasswordField passwordField;

    @FXML
    private void handleRegisterSubmit() {
        String username = usernameField.getText().trim();
        String fullName = fullNameField.getText().trim();
        String email = emailField.getText().trim();
        String phone = phoneField.getText().trim();
        String password = passwordField.getText();

        if (username.isEmpty() || fullName.isEmpty() || email.isEmpty() || phone.isEmpty() || password.isEmpty()) {
            showAlert(Alert.AlertType.WARNING, "Data Belum Lengkap", "Harap isi semua kolom pendaftaran.");
            return;
        }

        if (password.length() < 6) {
            showAlert(Alert.AlertType.WARNING, "Password Terlalu Pendek", "Password minimal 6 karakter.");
            return;
        }

        boolean success = UserSession.registerUser(username, fullName, email, password, phone);
        if (!success) {
            showAlert(Alert.AlertType.ERROR, "Registrasi Gagal", "Username atau email sudah terdaftar.");
            return;
        }

        showAlert(Alert.AlertType.INFORMATION, "Registrasi Berhasil", "Akun berhasil dibuat! Silakan masuk menggunakan akun Anda.");
        handleGoToSignIn();
    }

    @FXML
    private void handleSignUpSubmit() {
        handleRegisterSubmit();
    }

    @FXML
    private void handleGoToSignIn() {
        navigate("SignInPage");
    }

    @FXML
    private void handleLogin() {
        navigate("SignInPage");
    }

    @FXML
    private void handleHostRegister() {
        navigate("HostSignUpPage");
    }

    @FXML
    private void handleGoToHostSignUp() {
        navigate("HostSignUpPage");
    }

    @FXML
    private void handleBackToLanding() {
        navigate("LandingPage");
    }

    @FXML
    private void handleHome() {
        navigate("LandingPage");
    }

    private void navigate(String fxml) {
        try {
            App.setRoot(fxml);
        } catch (IOException e) {
            e.printStackTrace();
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Gagal membuka halaman");
            alert.setHeaderText(fxml);
            alert.setContentText(String.valueOf(e.getMessage()));
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