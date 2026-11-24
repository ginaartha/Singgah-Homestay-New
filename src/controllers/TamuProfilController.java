package controllers;
import java.io.IOException;

import app.App;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import models.UserSession;

public class TamuProfilController {

    @FXML private TextField usernameField;
    @FXML private TextField fullNameField;
    @FXML private TextField emailField;
    @FXML private TextField phoneField;

    @FXML private Button editProfileBtn;
    @FXML private Button cancelEditBtn;
    @FXML private Button saveProfileBtn;
    @FXML private Button backBtn;
    @FXML private Button logoutBtn;

    @FXML
    public void initialize() {
        if (!UserSession.isLoggedIn()) {
            Platform.runLater(() -> {
                try {
                    App.setRoot("SignInPage");
                } catch (IOException e) {
                    e.printStackTrace();
                }
            });
            return;
        }

        loadUserData();
        setEditMode(false);
    }

    private void loadUserData() {
        usernameField.setText(UserSession.getUsername());
        fullNameField.setText(UserSession.getFullName());
        emailField.setText(UserSession.getEmail());
        phoneField.setText(UserSession.getPhone());
    }

    private void setEditMode(boolean editable) {
        fullNameField.setEditable(editable);
        emailField.setEditable(editable);
        phoneField.setEditable(editable);

        String borderColor = editable ? "#F2A007" : "#8BA38D";
        String style = "-fx-background-color: white; -fx-border-color: " + borderColor + "; -fx-border-radius: 6; -fx-background-radius: 6; -fx-padding: 8 12 8 12; -fx-font-size: 13px; -fx-text-fill: #333333;";
        fullNameField.setStyle(style);
        emailField.setStyle(style);
        phoneField.setStyle(style);

        editProfileBtn.setVisible(!editable);
        editProfileBtn.setManaged(!editable);
        backBtn.setVisible(!editable);
        backBtn.setManaged(!editable);
        logoutBtn.setVisible(!editable);
        logoutBtn.setManaged(!editable);

        cancelEditBtn.setVisible(editable);
        cancelEditBtn.setManaged(editable);
        saveProfileBtn.setVisible(editable);
        saveProfileBtn.setManaged(editable);
    }

    @FXML
    private void handleEnableEdit() {
        setEditMode(true);
    }

    @FXML
    private void handleCancelEdit() {
        loadUserData();
        setEditMode(false);
    }

    @FXML
    private void handleSaveProfile() {
        String fullName = fullNameField.getText().trim();
        String email = emailField.getText().trim();
        String phone = phoneField.getText().trim();

        if (fullName.isEmpty() || email.isEmpty() || phone.isEmpty()) {
            showAlert(Alert.AlertType.WARNING, "Data Belum Lengkap", "Nama Lengkap, Email, dan Nomor HP tidak boleh kosong.");
            return;
        }

        boolean ok = UserSession.updateCurrentUserProfile(fullName, email, phone, null, null);
        if (!ok) {
            showAlert(Alert.AlertType.ERROR, "Gagal Menyimpan", "Email sudah digunakan oleh akun lain.");
            return;
        }

        loadUserData();
        setEditMode(false);
        showAlert(Alert.AlertType.INFORMATION, "Profil Diperbarui", "Data profil Anda berhasil disimpan.");
    }

    @FXML
    private void handleBackToLanding() {
        try {
            App.setRoot("LandingPage");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void handleLogout() {
        UserSession.logout();
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