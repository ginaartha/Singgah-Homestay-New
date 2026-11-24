import java.io.IOException;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;

public class SignInController {

    @FXML private TextField loginIdentifierField;
    @FXML private PasswordField loginPasswordField;

    @FXML
    private void handleSignInSubmit() {
        String identifier = loginIdentifierField.getText().trim();
        String password = loginPasswordField.getText();

        if (identifier.isEmpty() || password.isEmpty()) {
            showAlert(Alert.AlertType.WARNING, "Input Kosong", "Harap masukkan username/email dan kata sandi.");
            return;
        }

        if (UserSession.authenticateWithRole(identifier, password, "GUEST") ||
            UserSession.authenticateWithRole(identifier, password, "HOST") ||
            UserSession.authenticateWithRole(identifier, password, "ADMIN")) {

            String role = UserSession.getRole();
            showAlert(Alert.AlertType.INFORMATION, "Login Berhasil", "Selamat datang kembali, " + UserSession.getFullName() + "!");

            if ("HOST".equalsIgnoreCase(role)) {
                navigate("HostDashboard");
            } else if ("ADMIN".equalsIgnoreCase(role)) {
                navigate("AdminDashboard");
            } else {
                navigate("LandingPage");
            }
        } else {
            showAlert(Alert.AlertType.ERROR, "Login Gagal", "Username/email tidak ditemukan atau kata sandi salah.");
        }
    }

    // Dipasang di HBox navbar: klik 3x di area kosong navbar -> AdminSignInPage.
    // Klik 1x pada logo/teks ditangani handleBackToLanding (event itu juga naik ke sini
    // dengan clickCount 1, jadi tidak memicu apa-apa dan tidak ada navigasi ganda).
    @FXML
    private void handleLogoClick(MouseEvent event) {
        if (event.getClickCount() == 3) {
            navigate("AdminSignInPage");
        }
    }

    @FXML
    private void handleGoToSignUp() {
        navigate("SignUpPage");
    }

    @FXML
    private void handleGoToHostSignIn() {
        navigate("HostSignInPage");
    }

    @FXML
    private void handleGoToHostSignUp() {
        navigate("HostSignUpPage");
    }

    @FXML
    private void handleGoToAdminSignIn() {
        navigate("AdminSignInPage");
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