package controllers;

import app.App;
import java.io.IOException;
import java.util.List;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import models.UserSession;

public class KelolaReservasiController {

    @FXML private Label totalPaidReservationsLabel;
    @FXML private FlowPane bookedRoomsSummaryPane;
    @FXML private VBox reservationsContainer;

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

        refreshPaidReservations();
    }

    private void refreshPaidReservations() {
        bookedRoomsSummaryPane.getChildren().clear();
        reservationsContainer.getChildren().clear();

        List<UserSession.ReservationRecord> paidRecords = UserSession.getPaidReservations();
        if (totalPaidReservationsLabel != null) {
            totalPaidReservationsLabel.setText(String.valueOf(paidRecords.size()));
        }

        if (paidRecords.isEmpty()) {
            Label emptyChip = new Label("Belum ada kamar yang direservasi & dibayar saat ini.");
            emptyChip.setStyle("-fx-text-fill: rgba(255,255,255,0.85); -fx-font-size: 12px;");
            bookedRoomsSummaryPane.getChildren().add(emptyChip);

            VBox emptyBox = new VBox(6.0);
            emptyBox.setStyle("-fx-background-color: rgba(255,255,255,0.08); -fx-background-radius: 14; -fx-padding: 26;");
            Label emptyTitle = new Label("Belum ada reservasi masuk");
            emptyTitle.setStyle("-fx-text-fill: white; -fx-font-size: 16px; -fx-font-weight: bold;");
            Label emptySub = new Label("Reservasi akan otomatis muncul di halaman ini setelah tamu menyelesaikan pembayaran.");
            emptySub.setStyle("-fx-text-fill: rgba(255,255,255,0.8); -fx-font-size: 12px;");
            emptyBox.getChildren().addAll(emptyTitle, emptySub);
            reservationsContainer.getChildren().add(emptyBox);
            return;
        }

        for (UserSession.ReservationRecord rec : paidRecords) {
            String guestTag = (rec.guestUsername != null && !rec.guestUsername.isEmpty())
                ? "@" + rec.guestUsername
                : "Tamu";

            Label roomChip = new Label(
                "• " + rec.villaName + " — " + guestTag + " (" + rec.checkInText + " s/d " + rec.checkOutText + ")"
            );
            roomChip.setStyle("-fx-background-color: white; -fx-text-fill: #1B431E; -fx-font-size: 12px; -fx-font-weight: bold; -fx-background-radius: 20; -fx-padding: 6 14 6 14;");
            bookedRoomsSummaryPane.getChildren().add(roomChip);

            reservationsContainer.getChildren().add(createReservationCard(rec));
        }
    }

    private Image loadVillaImage(String path) {
        String target = (path != null && !path.isEmpty()) ? path : "/images/DhyanaVilla.png";
        try {
            if (target.startsWith("file:") || target.startsWith("http:") || target.startsWith("https:")) {
                return new Image(target);
            }
            var stream = getClass().getResourceAsStream(target);
            return stream != null ? new Image(stream) : null;
        } catch (Exception e) {
            return null;
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

        String usernameDisplay = (rec.guestUsername != null && !rec.guestUsername.isEmpty())
            ? "@" + rec.guestUsername
            : "-";
        String guestFullNameDisplay = (rec.guestFullName != null && !rec.guestFullName.isEmpty())
            ? " (" + rec.guestFullName + ")"
            : "";
        Label guestBadge = new Label("Tamu: " + usernameDisplay + guestFullNameDisplay);
        guestBadge.setStyle("-fx-background-color: #FFF4DF; -fx-text-fill: #8A5300; -fx-font-size: 11px; -fx-font-weight: bold; -fx-background-radius: 14; -fx-padding: 5 12 5 12;");

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
            refreshPaidReservations();
        });

        header.getChildren().addAll(orderIdBox, guestBadge, headerSpacer, statusBadge, nextProgressBtn);

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

        Label roomLabel = new Label("Kamar direservasi: " + rec.roomSummary);
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
        noteLabel.setStyle("-fx-text-fill: #245228; -fx-font-size: 11px; -fx-font-weight: bold;");

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
    private void handleGoToKelolaHomestay() {
        try {
            App.setRoot("HostDashboard");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void handleProfileClick() {
        try {
            App.setRoot("HostProfilPage");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}