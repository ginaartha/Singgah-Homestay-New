import java.io.File;
import java.io.IOException;
import java.util.List;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;

public class HostDashboardController {

    @FXML private ScrollPane mainScrollPane;
    @FXML private Button navKelolaBtn;

    @FXML private Label welcomeHostLabel;
    @FXML private Label statTotalVillaLabel;
    @FXML private Label statTotalBookingLabel;

    @FXML private VBox kelolaSection;
    @FXML private Label villaSectionTitle;
    @FXML private Button toggleFormBtn;
    @FXML private VBox villaFormCard;
    @FXML private Label formTitleLabel;
    @FXML private TextField villaNameField;
    @FXML private ComboBox<String> regencyComboBox;
    @FXML private TextField maxGuestsField;
    @FXML private TextField priceField;
    @FXML private TextField addressField;
    @FXML private TextField facilitiesField;
    @FXML private Label selectedImageLabel;
    @FXML private Button clearImageBtn;
    @FXML private Button saveVillaBtn;
    @FXML private FlowPane hostVillasContainer;

    @FXML private VBox reservasiSection;
    @FXML private FlowPane bookedRoomsSummaryPane;
    @FXML private VBox reservationsContainer;

    private int editingVillaIndex = -1;
    private String selectedImagePath = "";

    @FXML
    public void initialize() {
        if (!UserSession.isLoggedIn() || !"HOST".equalsIgnoreCase(UserSession.getRole())) {
            Platform.runLater(() -> {
                try {
                    App.setRoot("HostSignInPage");
                } catch (IOException e) {
                    e.printStackTrace();
                }
            });
            return;
        }

        String hostName = UserSession.getFullName() != null ? UserSession.getFullName() : UserSession.getUsername();
        String rekInfo = UserSession.hasHostBankDetails()
            ? " | Rekening: " + UserSession.getHostBankName() + " (" + UserSession.getHostBankAccount() + ")"
            : " | Rekening belum diatur";
        welcomeHostLabel.setText("Halo Host " + hostName + rekInfo);

        regencyComboBox.setItems(FXCollections.observableArrayList(
            "Badung", "Bangli", "Buleleng", "Denpasar",
            "Gianyar", "Jembrana", "Karangasem", "Klungkung", "Tabanan"
        ));

        refreshAll();
    }

    private void refreshAll() {
        refreshVillas();
        refreshReservations();
    }

    @FXML
    private void handleLogoClick() {
        handleShowKelolaHomestay();
        refreshAll();
    }

    @FXML
    private void handleShowKelolaHomestay() {
        if (mainScrollPane != null) {
            mainScrollPane.setVvalue(0.0);
        }
    }

    @FXML
    private void handleChooseImage() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Pilih Foto Homestay / Kamar");
        fileChooser.getExtensionFilters().addAll(
            new FileChooser.ExtensionFilter("File Gambar", "*.png", "*.jpg", "*.jpeg", "*.webp")
        );

        File file = fileChooser.showOpenDialog(mainScrollPane.getScene().getWindow());
        if (file != null) {
            selectedImagePath = file.toURI().toString();
            selectedImageLabel.setText(file.getName());
            clearImageBtn.setVisible(true);
            clearImageBtn.setManaged(true);
        }
    }

    @FXML
    private void handleClearImage() {
        selectedImagePath = "";
        selectedImageLabel.setText("Belum ada foto dipilih (tampil polosan)");
        clearImageBtn.setVisible(false);
        clearImageBtn.setManaged(false);
    }

    @FXML
    private void handleOpenAddForm() {
        // Host hanya dapat menambahkan kamar baru jika sudah memiliki rekening bank di profil
        if (!UserSession.hasHostBankDetails()) {
            showAlert(Alert.AlertType.WARNING, "Rekening Bank Wajib Diisi",
                "Anda harus menyimpan Nama Bank dan Nomor Rekening di halaman Profil terlebih dahulu sebelum dapat menambahkan listing kamar baru.");
            handleProfileClick();
            return;
        }

        editingVillaIndex = -1;
        formTitleLabel.setText("Tambah Listing Homestay Baru");
        saveVillaBtn.setText("Simpan Listing");

        villaNameField.clear();
        regencyComboBox.setValue(null);
        maxGuestsField.clear();
        priceField.clear();
        addressField.clear();
        facilitiesField.clear();
        handleClearImage();

        villaFormCard.setVisible(true);
        villaFormCard.setManaged(true);
    }

    private void openEditForm(int index, UserSession.VillaData v) {
        editingVillaIndex = index;
        formTitleLabel.setText("Edit Kamar & Detail — " + v.name);
        saveVillaBtn.setText("Simpan Perubahan");

        villaNameField.setText(v.name);
        regencyComboBox.setValue(v.regency);
        maxGuestsField.setText(String.valueOf(v.maxGuests));
        priceField.setText(String.valueOf(v.pricePerNight));
        addressField.setText(v.fullAddress);

        String cleanFac = v.facilities;
        int bulletIdx = cleanFac.indexOf("•");
        if (bulletIdx != -1 && bulletIdx + 1 < cleanFac.length()) {
            cleanFac = cleanFac.substring(bulletIdx + 1).trim();
        }
        facilitiesField.setText(cleanFac);

        if (v.imagePath != null && !v.imagePath.isEmpty()) {
            selectedImagePath = v.imagePath;
            String fileName = v.imagePath.substring(v.imagePath.lastIndexOf('/') + 1);
            selectedImageLabel.setText("Foto terpasang: " + fileName);
            clearImageBtn.setVisible(true);
            clearImageBtn.setManaged(true);
        } else {
            handleClearImage();
        }

        villaFormCard.setVisible(true);
        villaFormCard.setManaged(true);
        if (mainScrollPane != null) {
            mainScrollPane.setVvalue(0.2);
        }
    }

    @FXML
    private void handleCloseForm() {
        editingVillaIndex = -1;
        handleClearImage();
        villaFormCard.setVisible(false);
        villaFormCard.setManaged(false);
    }

    @FXML
    private void handleSaveVilla() {
        if (editingVillaIndex < 0 && !UserSession.hasHostBankDetails()) {
            showAlert(Alert.AlertType.WARNING, "Rekening Bank Wajib Diisi",
                "Harap simpan Nama Bank dan Nomor Rekening di Profil Host sebelum menambah listing baru.");
            handleProfileClick();
            return;
        }

        String name = villaNameField.getText().trim();
        String regency = regencyComboBox.getValue();
        String guestsStr = maxGuestsField.getText().trim();
        String priceStr = priceField.getText().trim();
        String address = addressField.getText().trim();
        String facilities = facilitiesField.getText().trim();

        if (name.isEmpty() || regency == null || guestsStr.isEmpty() || priceStr.isEmpty() || address.isEmpty()) {
            showAlert(Alert.AlertType.WARNING, "Form Belum Lengkap", "Harap isi nama homestay, kabupaten, kapasitas tamu, harga, dan alamat.");
            return;
        }

        int maxGuests;
        int price;
        try {
            maxGuests = Integer.parseInt(guestsStr);
            price = Integer.parseInt(priceStr);
        } catch (NumberFormatException e) {
            showAlert(Alert.AlertType.ERROR, "Format Angka Salah", "Maksimal tamu dan harga harus berupa angka.");
            return;
        }

        String formattedFacilities = maxGuests + " tamu • " + (facilities.isEmpty() ? "Kamar mandi dalam • Wi-Fi" : facilities);
        String formattedPrice = "Rp " + String.format("%,d,00", price).replace(',', '.') + "/malam";

        UserSession.VillaData villaData = new UserSession.VillaData(
            name,
            regency + ", Bali",
            regency,
            address,
            maxGuests,
            formattedFacilities,
            price,
            formattedPrice,
            selectedImagePath
        );

        if (editingVillaIndex >= 0) {
            UserSession.updateVilla(editingVillaIndex, villaData);
            handleCloseForm();
            refreshVillas();
            showAlert(Alert.AlertType.INFORMATION, "Kamar Diperbarui", "Perubahan pada " + name + " berhasil disimpan!");
        } else {
            UserSession.addVilla(villaData);
            handleCloseForm();
            refreshVillas();
            showAlert(Alert.AlertType.INFORMATION, "Listing Ditambahkan", name + " berhasil ditambahkan ke daftar homestay!");
        }
    }

    private Image loadVillaImage(String path) {
        if (path == null || path.isEmpty()) {
            return null;
        }
        try {
            if (path.startsWith("file:") || path.startsWith("http:") || path.startsWith("https:")) {
                return new Image(path);
            }
            var stream = getClass().getResourceAsStream(path);
            return stream != null ? new Image(stream) : null;
        } catch (Exception e) {
            return null;
        }
    }

    private void refreshVillas() {
        hostVillasContainer.getChildren().clear();
        List<UserSession.VillaData> villas = UserSession.getAllVillas();
        villaSectionTitle.setText("Kelola Homestay & Daftar Kamar (" + villas.size() + ")");
        if (statTotalVillaLabel != null) {
            statTotalVillaLabel.setText(String.valueOf(villas.size()));
        }

        for (int i = 0; i < villas.size(); i++) {
            final int idx = i;
            final UserSession.VillaData v = villas.get(i);

            VBox card = new VBox();
            card.setPrefWidth(300.0);
            card.setStyle("-fx-background-color: white; -fx-background-radius: 16; -fx-effect: dropshadow(gaussian, rgba(0,0,0,0.2), 12, 0.1, 0, 3);");

            StackPane imgWrap = new StackPane();
            imgWrap.setMinHeight(170.0);
            imgWrap.setPrefHeight(170.0);
            imgWrap.setStyle("-fx-background-color: #4A6F4D; -fx-background-radius: 16 16 0 0;");

            Image loadedImg = loadVillaImage(v.imagePath);
            if (loadedImg != null) {
                ImageView iv = new ImageView(loadedImg);
                iv.setFitWidth(300.0);
                iv.setFitHeight(170.0);
                iv.setPreserveRatio(false);
                imgWrap.getChildren().add(iv);
            }

            VBox body = new VBox(6.0);
            body.setPadding(new Insets(14.0, 18.0, 20.0, 18.0));

            Label areaLbl = new Label(v.area);
            areaLbl.setStyle("-fx-text-fill: #666666; -fx-font-size: 11px;");

            Label nameLbl = new Label(v.name);
            nameLbl.setStyle("-fx-text-fill: #1A1A1A; -fx-font-size: 18px; -fx-font-weight: bold;");

            Label facLbl = new Label(v.facilities);
            facLbl.setStyle("-fx-text-fill: #777777; -fx-font-size: 11px;");

            HBox bottomRow = new HBox(8.0);
            bottomRow.setAlignment(Pos.CENTER_LEFT);
            bottomRow.setPadding(new Insets(8.0, 0, 0, 0));

            Label priceLbl = new Label(v.priceText);
            priceLbl.setStyle("-fx-text-fill: #B84A27; -fx-font-size: 14px; -fx-font-weight: bold;");

            Region spacer = new Region();
            HBox.setHgrow(spacer, Priority.ALWAYS);

            Button editBtn = new Button("Edit Kamar");
            editBtn.setStyle("-fx-background-color: #245228; -fx-text-fill: white; -fx-font-size: 11px; -fx-font-weight: bold; -fx-background-radius: 20; -fx-padding: 6 14 6 14; -fx-cursor: hand;");
            editBtn.setOnAction(e -> openEditForm(idx, v));

            bottomRow.getChildren().addAll(priceLbl, spacer, editBtn);
            body.getChildren().addAll(areaLbl, nameLbl, facLbl, bottomRow);
            card.getChildren().addAll(imgWrap, body);
            hostVillasContainer.getChildren().add(card);
        }
    }

    private void refreshReservations() {
        bookedRoomsSummaryPane.getChildren().clear();
        reservationsContainer.getChildren().clear();

        List<UserSession.ReservationRecord> records = UserSession.getUserReservations();
        if (statTotalBookingLabel != null) {
            statTotalBookingLabel.setText(String.valueOf(records.size()));
        }

        if (records.isEmpty()) {
            Label emptyChip = new Label("Belum ada kamar yang dibooking saat ini.");
            emptyChip.setStyle("-fx-text-fill: rgba(255,255,255,0.85); -fx-font-size: 12px;");
            bookedRoomsSummaryPane.getChildren().add(emptyChip);

            VBox emptyBox = new VBox(6.0);
            emptyBox.setStyle("-fx-background-color: rgba(255,255,255,0.08); -fx-background-radius: 14; -fx-padding: 24;");
            Label emptyTitle = new Label("Belum ada reservasi masuk");
            emptyTitle.setStyle("-fx-text-fill: white; -fx-font-size: 15px; -fx-font-weight: bold;");
            Label emptySub = new Label("Pesanan tamu yang masuk akan otomatis muncul di sini beserta progres dan detail kamarnya.");
            emptySub.setStyle("-fx-text-fill: rgba(255,255,255,0.8); -fx-font-size: 12px;");
            emptyBox.getChildren().addAll(emptyTitle, emptySub);
            reservationsContainer.getChildren().add(emptyBox);
            return;
        }

        for (UserSession.ReservationRecord rec : records) {
            Label roomChip = new Label("• " + rec.villaName + " — " + rec.roomSummary + " (" + rec.checkInText + " s/d " + rec.checkOutText + ") [" + rec.status + "]");
            roomChip.setStyle("-fx-background-color: white; -fx-text-fill: #1B431E; -fx-font-size: 12px; -fx-font-weight: bold; -fx-background-radius: 20; -fx-padding: 6 14 6 14;");
            bookedRoomsSummaryPane.getChildren().add(roomChip);

            reservationsContainer.getChildren().add(createReservationCard(rec));
        }
    }

    private VBox createReservationCard(UserSession.ReservationRecord rec) {
        VBox card = new VBox(16.0);
        card.setStyle("-fx-background-color: white; -fx-background-radius: 16; -fx-effect: dropshadow(gaussian, rgba(0,0,0,0.22), 14, 0.1, 0, 4);");
        card.setPadding(new Insets(20.0, 28.0, 24.0, 28.0));

        HBox header = new HBox(10.0);
        header.setAlignment(Pos.CENTER_LEFT);

        HBox orderIdBox = new HBox(4.0);
        Label prefix = new Label("No. pesanan ");
        prefix.setStyle("-fx-text-fill: #666666; -fx-font-size: 12px;");
        Label code = new Label(rec.orderCode);
        code.setStyle("-fx-text-fill: #222222; -fx-font-size: 12px; -fx-font-weight: bold;");
        orderIdBox.getChildren().addAll(prefix, code);

        Region headerSpacer = new Region();
        HBox.setHgrow(headerSpacer, Priority.ALWAYS);

        Label statusBadge = new Label("Progres: " + rec.status);
        statusBadge.setStyle("-fx-background-color: #E8EDE8; -fx-text-fill: #245228; -fx-font-size: 11px; -fx-font-weight: bold; -fx-background-radius: 14; -fx-padding: 5 14 5 14;");

        Button nextProgressBtn = new Button("Update Progres");
        nextProgressBtn.setStyle("-fx-background-color: #F2A007; -fx-text-fill: white; -fx-font-size: 11px; -fx-font-weight: bold; -fx-background-radius: 14; -fx-padding: 5 12 5 12; -fx-cursor: hand;");
        nextProgressBtn.setOnAction(e -> {
            if ("Terkonfirmasi".equalsIgnoreCase(rec.status) || "Bayar di Tempat".equalsIgnoreCase(rec.status)) {
                rec.status = "Check-in / Berjalan";
            } else if ("Check-in / Berjalan".equalsIgnoreCase(rec.status)) {
                rec.status = "Selesai";
            } else {
                rec.status = "Terkonfirmasi";
            }
            refreshReservations();
        });

        header.getChildren().addAll(orderIdBox, headerSpacer, statusBadge, nextProgressBtn);

        HBox body = new HBox(22.0);
        body.setAlignment(Pos.CENTER_LEFT);

        StackPane imgBox = new StackPane();
        imgBox.setMinSize(195.0, 135.0);
        imgBox.setMaxSize(195.0, 135.0);
        imgBox.setStyle("-fx-background-color: #4A6F4D; -fx-background-radius: 12;");

        Image loadedImg = loadVillaImage(rec.imagePath);
        if (loadedImg != null) {
            ImageView imgView = new ImageView(loadedImg);
            imgView.setFitWidth(195.0);
            imgView.setFitHeight(135.0);
            imgView.setPreserveRatio(false);
            imgBox.getChildren().add(imgView);
        }

        VBox details = new VBox(6.0);
        HBox.setHgrow(details, Priority.ALWAYS);

        Label locLabel = new Label(rec.location);
        locLabel.setStyle("-fx-text-fill: #666666; -fx-font-size: 11px;");

        Label nameLabel = new Label(rec.villaName);
        nameLabel.setStyle("-fx-text-fill: #1A1A1A; -fx-font-size: 20px; -fx-font-weight: bold;");

        Label roomLabel = new Label("Kamar dibooking: " + rec.roomSummary);
        roomLabel.setStyle("-fx-text-fill: #245228; -fx-font-size: 12px; -fx-font-weight: bold;");

        HBox datesRow = new HBox(28.0);
        datesRow.setPadding(new Insets(8.0, 0, 0, 0));

        datesRow.getChildren().addAll(
            createDateColumn("Check-in", rec.checkInText),
            createDateColumn("Check-out", rec.checkOutText),
            createDateColumn("Durasi", rec.durationText)
        );

        details.getChildren().addAll(locLabel, nameLabel, roomLabel, datesRow);

        VBox rightPay = new VBox(4.0);
        rightPay.setAlignment(Pos.CENTER_RIGHT);

        Label payTitle = new Label("Total pembayaran");
        payTitle.setStyle("-fx-text-fill: #777777; -fx-font-size: 11px;");

        Label totalLabel = new Label(rec.totalPriceText);
        totalLabel.setStyle("-fx-text-fill: #B84A27; -fx-font-size: 20px; -fx-font-weight: bold;");

        Label noteLabel = new Label(rec.paymentNote);
        noteLabel.setStyle("-fx-text-fill: #888888; -fx-font-size: 11px;");

        rightPay.getChildren().addAll(payTitle, totalLabel, noteLabel);

        body.getChildren().addAll(imgBox, details, rightPay);
        card.getChildren().addAll(header, body);
        return card;
    }

    private VBox createDateColumn(String title, String value) {
        VBox col = new VBox(2.0);
        Label t = new Label(title);
        t.setStyle("-fx-text-fill: #888888; -fx-font-size: 11px;");
        Label v = new Label(value);
        v.setStyle("-fx-text-fill: #1A1A1A; -fx-font-size: 13px; -fx-font-weight: bold;");
        col.getChildren().addAll(t, v);
        return col;
    }

    @FXML
    private void handleProfileClick() {
        try {
            App.setRoot("HostProfilPage");
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