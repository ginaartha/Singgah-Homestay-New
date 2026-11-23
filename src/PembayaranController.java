import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Locale;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;

public class PembayaranController {

    @FXML private Label bookingSummaryLabel;
    @FXML private Label totalAmountLabel;
    @FXML private Button tunaiBtn;
    @FXML private Button nonTunaiBtn;

    @FXML private VBox paymentDetailBox;
    @FXML private Label detailTitleLabel;
    @FXML private Label detailBodyLabel;
    @FXML private TextField senderNameField;
    @FXML private Button lanjutBtn;

    private String selectedMethod = null; // "TUNAI" atau "NONTUNAI"
    private boolean stepTwoShown = false;

    private static final String SELECTED_STYLE =
        "-fx-background-color: #245228; -fx-text-fill: white; -fx-font-weight: bold; -fx-font-size: 12px; -fx-border-color: #245228; -fx-border-width: 1.5; -fx-border-radius: 20; -fx-background-radius: 20; -fx-padding: 7 16 7 16; -fx-cursor: hand;";
    private static final String UNSELECTED_STYLE =
        "-fx-background-color: white; -fx-text-fill: #1B431E; -fx-font-weight: bold; -fx-font-size: 12px; -fx-border-color: #245228; -fx-border-width: 1.5; -fx-border-radius: 20; -fx-background-radius: 20; -fx-padding: 7 16 7 16; -fx-cursor: hand;";

    @FXML
    public void initialize() {
        // Proteksi: wajib login terlebih dahulu
        if (!UserSession.isLoggedIn()) {
            Platform.runLater(() -> {
                Alert alert = new Alert(Alert.AlertType.WARNING);
                alert.setTitle("Masuk Diperlukan");
                alert.setHeaderText(null);
                alert.setContentText("Silakan masuk ke akun Anda terlebih dahulu untuk melakukan pembayaran.");
                alert.showAndWait();
                try {
                    App.setRoot("SignInPage");
                } catch (IOException e) {
                    e.printStackTrace();
                }
            });
            return;
        }

        String villaName = UserSession.getSelectedVillaName() != null ? UserSession.getSelectedVillaName() : "Dhyana Villa";
        long nights = UserSession.getBookingNights() > 0 ? UserSession.getBookingNights() : 1;
        long total = UserSession.getBookingTotalAmount() > 0 ? UserSession.getBookingTotalAmount() : 250000;

        bookingSummaryLabel.setText(villaName + "  •  " + nights + " malam");
        totalAmountLabel.setText("Rp " + String.format("%,d,00", total).replace(',', '.'));
    }

    @FXML
    private void handleSelectTunai() {
        selectedMethod = "TUNAI";
        tunaiBtn.setStyle(SELECTED_STYLE);
        nonTunaiBtn.setStyle(UNSELECTED_STYLE);

        // Reset ke langkah 1 kalau user mengganti metode
        stepTwoShown = false;
        paymentDetailBox.setVisible(false);
        paymentDetailBox.setManaged(false);
        lanjutBtn.setText("Lanjut");
    }

    @FXML
    private void handleSelectNonTunai() {
        selectedMethod = "NONTUNAI";
        nonTunaiBtn.setStyle(SELECTED_STYLE);
        tunaiBtn.setStyle(UNSELECTED_STYLE);

        // Reset ke langkah 1 kalau user mengganti metode
        stepTwoShown = false;
        paymentDetailBox.setVisible(false);
        paymentDetailBox.setManaged(false);
        lanjutBtn.setText("Lanjut");
    }

    @FXML
    private void handleLanjut() {
        if (selectedMethod == null) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Metode Belum Dipilih");
            alert.setHeaderText(null);
            alert.setContentText("Silakan pilih metode pembayaran (Tunai atau Non-tunai) terlebih dahulu.");
            alert.showAndWait();
            return;
        }

        // Langkah 1: Tampilkan instruksi Tunai atau Rekening Pemilik (Non-tunai)
        if (!stepTwoShown) {
            stepTwoShown = true;
            paymentDetailBox.setVisible(true);
            paymentDetailBox.setManaged(true);

            if ("TUNAI".equals(selectedMethod)) {
                detailTitleLabel.setText("Pembayaran Tunai di Lokasi");
                detailBodyLabel.setText("Silakan lakukan pembayaran tunai langsung kepada pemilik villa saat proses check-in tiba.");
                senderNameField.setVisible(false);
                senderNameField.setManaged(false);
                lanjutBtn.setText("Konfirmasi Pesanan");
            } else {
                detailTitleLabel.setText("Transfer ke Rekening Pemilik Villa");
                detailBodyLabel.setText(
                    "Bank BCA  •  1234-567-890\n" +
                    "a.n. Pemilik " + (UserSession.getSelectedVillaName() != null ? UserSession.getSelectedVillaName() : "Villa Singgah") + "\n" +
                    "Harap transfer sesuai nominal total di atas."
                );
                senderNameField.setVisible(true);
                senderNameField.setManaged(true);
                lanjutBtn.setText("Sudah Transfer");
            }
            return;
        }

        // Langkah 2: Simpan ke Reservasi Saya dan pindah ke halaman ReservasiSaya
        saveReservationToSession();

        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Reservasi Berhasil");
        alert.setHeaderText(null);
        if ("TUNAI".equals(selectedMethod)) {
            alert.setContentText("Pesanan berhasil dibuat! Silakan bayar tunai saat check-in.");
        } else {
            alert.setContentText("Konfirmasi transfer diterima! Pesananmu telah masuk ke Reservasi Saya.");
        }
        alert.showAndWait();

        try {
            App.setRoot("ReservasiSaya");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void saveReservationToSession() {
        DateTimeFormatter fmt = DateTimeFormatter.ofPattern("dd MMM yyyy", new Locale("id", "ID"));
        LocalDate checkIn = UserSession.getBookingCheckIn() != null ? UserSession.getBookingCheckIn() : LocalDate.now().plusDays(1);
        LocalDate checkOut = UserSession.getBookingCheckOut() != null ? UserSession.getBookingCheckOut() : checkIn.plusDays(1);
        long nights = UserSession.getBookingNights() > 0 ? UserSession.getBookingNights() : 1;
        long total = UserSession.getBookingTotalAmount() > 0 ? UserSession.getBookingTotalAmount() : 250000;
        String guests = UserSession.getBookingGuests() != null ? UserSession.getBookingGuests() : "1 orang";

        String orderCode = "SGH-" + (100000 + (int) (Math.random() * 900000));
        String statusText = "TUNAI".equals(selectedMethod) ? "Bayar di Tempat" : "Terkonfirmasi";
        String paymentNote = "TUNAI".equals(selectedMethod) ? "Tunai • " + nights + " malam" : "Lunas • " + nights + " malam";

        UserSession.ReservationRecord record = new UserSession.ReservationRecord(
            orderCode,
            statusText,
            UserSession.getSelectedVillaName() != null ? UserSession.getSelectedVillaName() : "Dhyana Villa",
            UserSession.getSelectedVillaAddress() != null ? UserSession.getSelectedVillaAddress() : "Bali",
            "Kamar Deluxe  •  " + guests + "  •  1 kamar",
            checkIn.format(fmt),
            checkOut.format(fmt),
            nights + " malam",
            "Rp " + String.format("%,d", total).replace(',', '.'),
            paymentNote,
            UserSession.getSelectedVillaImage() != null ? UserSession.getSelectedVillaImage() : "/images/DhyanaVilla.png"
        );

        UserSession.addReservation(record);
    }

    @FXML
    private void handleBack() {
        try {
            App.setRoot("DetailVilla");
        } catch (IOException e) {
            e.printStackTrace();
        }
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
    private void handleMyReservations() {
        try {
            App.setRoot("ReservasiSaya");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void handleProfileClick() {
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