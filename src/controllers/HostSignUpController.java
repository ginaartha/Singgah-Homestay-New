package controllers;
import java.io.IOException;

import app.App;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import models.UserSession;

public class HostSignUpController {

    @FXML private TextField usernameField;
    @FXML private TextField fullNameField;
    @FXML private TextField emailField;
    @FXML private PasswordField passwordField;
    @FXML private TextField phoneField;
    @FXML private TextField bankNameField;
    @FXML private TextField bankAccountField;

    @FXML
    private void handleHostRegisterSubmit() {
        String username = usernameField.getText().trim();
        String fullName = fullNameField.getText().trim();
        String email = emailField.getText().trim();
        String password = passwordField.getText();
        String phone = phoneField.getText().trim();
        String bankName = bankNameField.getText().trim();
        String bankAccount = bankAccountField.getText().trim();

        if (username.isEmpty() || fullName.isEmpty() || email.isEmpty() || password.isEmpty()
                || phone.isEmpty() || bankName.isEmpty() || bankAccount.isEmpty()) {
            showAlert(Alert.AlertType.WARNING, "Data Belum Lengkap",
                "Harap isi semua kolom termasuk Nama Bank dan Nomor Rekening.");
            return;
        }

        if (password.length() < 8) {
            showAlert(Alert.AlertType.WARNING, "Password Terlalu Pendek",
                "Password minimal 8 karakter.");
            return;
        }

        if (!bankAccount.matches("\\d+")) {
            showAlert(Alert.AlertType.WARNING, "Format Rekening Salah",
                "Nomor rekening harus berupa angka.");
            return;
        }

        boolean success = UserSession.registerAccount(
            username, fullName, email, password, phone, "HOST", bankName, bankAccount
        );

        if (!success) {
            showAlert(Alert.AlertType.ERROR, "Registrasi Gagal",
                "Username atau email sudah terdaftar.");
            return;
        }

        showAlert(Alert.AlertType.INFORMATION, "Registrasi Host Berhasil",
            "Akun Host dan rekening bank berhasil didaftarkan! Silakan masuk.");
        handleGoToHostSignIn();
    }

    @FXML
    private void handleGoToHostSignIn() {
        navigate("HostSignInPage");
    }

    @FXML
    private void handleGoToGuestSignUp() {
        navigate("SignUpPage");
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