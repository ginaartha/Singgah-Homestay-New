import java.io.IOException;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;

public class ReservasiSayaController {

    @FXML private HBox authButtonsBox;
    @FXML private Button profileButton;
    @FXML private VBox reservationsContainer;

    @FXML
    public void initialize() {
        // Proteksi: halaman Reservasi Saya hanya bisa diakses jika sudah login
        if (!UserSession.isLoggedIn()) {
            Platform.runLater(() -> {
                Alert alert = new Alert(Alert.AlertType.WARNING);
                alert.setTitle("Akses Terbatas");
                alert.setHeaderText(null);
                alert.setContentText("Silakan masuk terlebih dahulu untuk melihat Reservasi Saya.");
                alert.showAndWait();
                try {
                    App.setRoot("SignInPage");
                } catch (IOException e) {
                    e.printStackTrace();
                }
            });
            return;
        }

        if (authButtonsBox != null && profileButton != null) {
            authButtonsBox.setVisible(false);
            authButtonsBox.setManaged(false);
            profileButton.setVisible(true);
            profileButton.setManaged(true);
        }

        // Tambahkan pesanan baru dari session ke bagian paling atas
        for (int i = UserSession.getUserReservations().size() - 1; i >= 0; i--) {
            reservationsContainer.getChildren().add(0, createReservationCard(UserSession.getUserReservations().get(i)));
        }
    }

    private VBox createReservationCard(UserSession.ReservationRecord rec) {
        VBox card = new VBox(16.0);
        card.setStyle("-fx-background-color: white; -fx-background-radius: 16; -fx-effect: dropshadow(gaussian, rgba(0,0,0,0.22), 14, 0.1, 0, 4);");
        card.setPadding(new Insets(20.0, 28.0, 24.0, 28.0));

        // Header
        HBox header = new HBox();
        header.setAlignment(Pos.CENTER_LEFT);

        HBox orderIdBox = new HBox(4.0);
        Label prefix = new Label("No. pesanan");
        prefix.setStyle("-fx-text-fill: #666666; -fx-font-size: 12px;");
        Label code = new Label(rec.orderCode);
        code.setStyle("-fx-text-fill: #222222; -fx-font-size: 12px; -fx-font-weight: bold;");
        orderIdBox.getChildren().addAll(prefix, code);

        Region headerSpacer = new Region();
        HBox.setHgrow(headerSpacer, Priority.ALWAYS);

        Label statusBadge = new Label(rec.status);
        statusBadge.setStyle("-fx-background-color: #E8EDE8; -fx-text-fill: #245228; -fx-font-size: 11px; -fx-font-weight: bold; -fx-background-radius: 14; -fx-padding: 5 14 5 14;");

        header.getChildren().addAll(orderIdBox, headerSpacer, statusBadge);

        // Body
        HBox body = new HBox(22.0);
        body.setAlignment(Pos.CENTER_LEFT);

        StackPane imgBox = new StackPane();
        imgBox.setMinSize(195.0, 135.0);
        imgBox.setMaxSize(195.0, 135.0);
        imgBox.setStyle("-fx-background-color: #4A6F4D; -fx-background-radius: 12;");
        ImageView imgView = new ImageView();
        imgView.setFitWidth(195.0);
        imgView.setFitHeight(135.0);
        imgView.setPreserveRatio(false);
        try {
            imgView.setImage(new Image(getClass().getResourceAsStream(rec.imagePath)));
        } catch (Exception ignored) {
        }
        imgBox.getChildren().add(imgView);

        VBox details = new VBox(6.0);
        HBox.setHgrow(details, Priority.ALWAYS);

        Label locLabel = new Label(rec.location);
        locLabel.setStyle("-fx-text-fill: #666666; -fx-font-size: 11px;");

        Label nameLabel = new Label(rec.villaName);
        nameLabel.setStyle("-fx-text-fill: #1A1A1A; -fx-font-size: 20px; -fx-font-weight: bold;");

        Label roomLabel = new Label(rec.roomSummary);
        roomLabel.setStyle("-fx-text-fill: #666666; -fx-font-size: 12px;");

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
    private void handleHome() {
        try {
            App.setRoot("LandingPage");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void handleSearchRoom() {
        UserSession.setSearchFilter(null, null, null);
        try {
            App.setRoot("HasilPencarian");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void handleRegister() {
        try {
            App.setRoot("SignUpPage");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void handleLogin() {
        try {
            App.setRoot("SignInPage");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void handleProfileClick() {
        if (!UserSession.isLoggedIn()) {
            return;
        }
        try {
            if ("HOST".equalsIgnoreCase(UserSession.getRole())) {
                App.setRoot("HostProfilPage");
            } else {
                App.setRoot("TamuProfilPage");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}