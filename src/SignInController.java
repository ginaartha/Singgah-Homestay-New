import java.io.IOException;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

public class SignInController {

    @FXML private TextField usernameField;
    @FXML private TextField loginIdentifierField;
    @FXML private PasswordField passwordField;
    @FXML private PasswordField loginPasswordField;

    @FXML
    private void handleLoginSubmit() {
        TextField idInput = (loginIdentifierField != null) ? loginIdentifierField : usernameField;
        PasswordField passInput = (loginPasswordField != null) ? loginPasswordField : passwordField;

        String identifier = (idInput != null) ? idInput.getText().trim() : "";
        String password = (passInput != null) ? passInput.getText() : "";

        if (identifier.isEmpty() || password.isEmpty()) {
            showAlert(Alert.AlertType.WARNING, "Data Belum Lengkap", "Harap masukkan username/email dan password.");
            return;
        }

        if (UserSession.authenticateWithRole(identifier, password, "ADMIN")) {
            navigate("AdminDashboard");
            return;
        }

        if (UserSession.authenticateWithRole(identifier, password, "GUEST")) {
            navigate("LandingPage");
            return;
        }

        showAlert(Alert.AlertType.ERROR, "Login Gagal", "Username/email atau password salah, atau akun terdaftar sebagai Host.");
    }

    @FXML
    private void handleSignInSubmit() {
        handleLoginSubmit();
    }

    @FXML
    private void handleGoToSignUp() {
        navigate("SignUpPage");
    }

    @FXML
    private void handleRegister() {
        navigate("SignUpPage");
    }

    @FXML
    private void handleHostLogin() {
        navigate("HostSignInPage");
    }

    @FXML
    private void handleGoToHostSignIn() {
        navigate("HostSignInPage");
    }

    @FXML
    private void handleHostRegister() {
        navigate("HostSignUpPage");
    }

    @FXML
    private void handleLogoClick() {
        navigate("LandingPage");
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