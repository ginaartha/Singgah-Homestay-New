import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Alert;
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

public class HasilPencarianController {

    @FXML private HBox authButtonsBox;
    @FXML private Button profileButton;
    @FXML private Label filterSummaryLabel;
    @FXML private FlowPane resultsContainer;

    private static class VillaItem {
        String name;
        String area;
        String regency;
        int maxGuests;
        String facilities;
        String priceText;
        String imagePath;

        VillaItem(String name, String area, String regency, int maxGuests, String facilities, String priceText, String imagePath) {
            this.name = name;
            this.area = area;
            this.regency = regency;
            this.maxGuests = maxGuests;
            this.facilities = facilities;
            this.priceText = priceText;
            this.imagePath = imagePath;
        }
    }

    @FXML
    public void initialize() {
        boolean isLoggedIn = UserSession.isLoggedIn();
        if (authButtonsBox != null && profileButton != null) {
            authButtonsBox.setVisible(!isLoggedIn);
            authButtonsBox.setManaged(!isLoggedIn);

            profileButton.setVisible(isLoggedIn);
            profileButton.setManaged(isLoggedIn);
        }

        String selectedLoc = UserSession.getSearchLocation();
        String selectedDate = UserSession.getSearchDate();
        String selectedGuests = UserSession.getSearchGuests();

        updateFilterSubtitle(selectedLoc, selectedDate, selectedGuests);
        loadFilteredVillas(selectedLoc, selectedGuests);
    }

    private void updateFilterSubtitle(String loc, String date, String guests) {
        List<String> parts = new ArrayList<>();
        if (loc != null && !loc.isEmpty()) parts.add("Lokasi: " + loc);
        if (date != null && !date.isEmpty()) parts.add("Tanggal: " + date);
        if (guests != null && !guests.isEmpty()) parts.add("Minimal Tamu: " + guests);

        if (parts.isEmpty()) {
            filterSummaryLabel.setText("Menampilkan semua villa yang tersedia di Bali");
        } else {
            filterSummaryLabel.setText("Filter aktif — " + String.join("  •  ", parts));
        }
    }

    private void loadFilteredVillas(String selectedLoc, String selectedGuests) {
        resultsContainer.getChildren().clear();

        List<VillaItem> allVillas = List.of(
            new VillaItem("Dhyana Villa", "Klungkung, Bali", "Klungkung", 2, "2 tamu • Kamar mandi dalam • Wi-Fi", "Rp 250.000,00/malam", "/images/DhyanaVilla.png"),
            new VillaItem("Inten Villa", "Tabanan, Bali", "Tabanan", 3, "3 tamu • Kamar mandi dalam • Wi-Fi", "Rp 250.000,00/malam", "/images/DhyanaVilla.png"),
            new VillaItem("Gina Villa", "Gianyar, Bali", "Gianyar", 4, "4 tamu • Kamar mandi dalam • Wi-Fi", "Rp 250.000,00/malam", "/images/DhyanaVilla.png")
        );

        int requiredGuests = parseGuestCount(selectedGuests);
        int matchCount = 0;

        for (VillaItem villa : allVillas) {
            boolean matchesLocation = (selectedLoc == null || selectedLoc.isEmpty() || villa.regency.equalsIgnoreCase(selectedLoc));
            boolean matchesGuests = (requiredGuests == 0 || villa.maxGuests >= requiredGuests);

            if (matchesLocation && matchesGuests) {
                resultsContainer.getChildren().add(createVillaCard(villa));
                matchCount++;
            }
        }

        if (matchCount == 0) {
            Label emptyLabel = new Label("Belum ada villa yang sesuai dengan kriteria pencarianmu. Coba pilih kabupaten atau jumlah tamu lain.");
            emptyLabel.setStyle("-fx-text-fill: white; -fx-font-size: 15px; -fx-padding: 20 0 0 0;");
            resultsContainer.getChildren().add(emptyLabel);
        }
    }

    private int parseGuestCount(String guestText) {
        if (guestText == null || guestText.isEmpty()) return 0;
        if (guestText.startsWith("1")) return 1;
        if (guestText.startsWith("2")) return 2;
        if (guestText.startsWith("3")) return 3;
        if (guestText.startsWith("4")) return 4;
        if (guestText.startsWith("5")) return 5;
        return 0;
    }

    private VBox createVillaCard(VillaItem villa) {
        VBox card = new VBox();
        card.setPrefWidth(300.0);
        card.setStyle("-fx-background-color: white; -fx-background-radius: 16; -fx-effect: dropshadow(gaussian, rgba(0,0,0,0.2), 12, 0.1, 0, 3);");

        StackPane imageWrapper = new StackPane();
        imageWrapper.setMinHeight(170.0);
        imageWrapper.setPrefHeight(170.0);
        imageWrapper.setStyle("-fx-background-color: #4A6F4D; -fx-background-radius: 16 16 0 0;");

        ImageView imageView = new ImageView();
        imageView.setFitHeight(170.0);
        imageView.setFitWidth(300.0);
        imageView.setPickOnBounds(true);
        imageView.setPreserveRatio(false);
        try {
            imageView.setImage(new Image(getClass().getResourceAsStream(villa.imagePath)));
        } catch (Exception e) {
            // Fallback jika gambar belum ada
        }
        imageWrapper.getChildren().add(imageView);

        VBox body = new VBox(6.0);
        body.setPadding(new Insets(14.0, 18.0, 20.0, 18.0));

        Label areaLabel = new Label(villa.area);
        areaLabel.setStyle("-fx-text-fill: #666666; -fx-font-size: 11px;");

        Label nameLabel = new Label(villa.name);
        nameLabel.setStyle("-fx-text-fill: #1A1A1A; -fx-font-size: 18px; -fx-font-weight: bold;");

        Label facLabel = new Label(villa.facilities);
        facLabel.setStyle("-fx-text-fill: #777777; -fx-font-size: 11px;");

        HBox bottomRow = new HBox(8.0);
        bottomRow.setAlignment(Pos.CENTER_LEFT);
        bottomRow.setPadding(new Insets(8.0, 0, 0, 0));

        Label priceLabel = new Label(villa.priceText);
        priceLabel.setStyle("-fx-text-fill: #B84A27; -fx-font-size: 14px; -fx-font-weight: bold;");

        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);

        Button bookingBtn = new Button("Booking");
        bookingBtn.setStyle("-fx-background-color: #245228; -fx-text-fill: white; -fx-font-size: 11px; -fx-font-weight: bold; -fx-background-radius: 20; -fx-padding: 6 16 6 16; -fx-cursor: hand;");
        bookingBtn.setOnAction(e -> {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Booking Villa");
            alert.setHeaderText(null);
            alert.setContentText("Booking dipilih untuk " + villa.name + " (" + villa.area + ").");
            alert.showAndWait();
        });

        bottomRow.getChildren().addAll(priceLabel, spacer, bookingBtn);
        body.getChildren().addAll(areaLabel, nameLabel, facLabel, bottomRow);
        card.getChildren().addAll(imageWrapper, body);

        return card;
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
    private void handleMyReservations() {
        if (!UserSession.isLoggedIn()) {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Masuk Diperlukan");
            alert.setHeaderText(null);
            alert.setContentText("Silakan masuk ke akun Anda terlebih dahulu untuk melihat Reservasi Saya.");
            alert.showAndWait();
            try {
                App.setRoot("SignInPage");
            } catch (IOException e) {
                e.printStackTrace();
            }
            return;
        }

        try {
            App.setRoot("ReservasiSaya");
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