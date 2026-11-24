package controllers;
import java.io.IOException;

import app.App;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import models.UserSession;

public class HostSignInController {

    @FXML private TextField identifierField;
    @FXML private PasswordField passwordField;

    @FXML
    private void handleHostLoginSubmit() {
        String id = identifierField.getText().trim();
        String pass = passwordField.getText();

        if (id.isEmpty() || pass.isEmpty()) {
            showAlert(Alert.AlertType.WARNING, "Data Belum Lengkap", "Harap isi username/email dan password host.");
            return;
        }

        if (UserSession.authenticateWithRole(id, pass, "HOST")) {
            navigate("HostDashboard");
        } else {
            showAlert(Alert.AlertType.ERROR, "Login Gagal", "Akun Host tidak ditemukan atau password salah.");
        }
    }

    @FXML
    private void handleHostSignInSubmit() {
        handleHostLoginSubmit();
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
    private void handleLogoClick() {
        navigate("LandingPage");
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
            showAlert(Alert.AlertType.ERROR, "Gagal Membuka Halaman", fxml + ": " + e.getMessage());
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