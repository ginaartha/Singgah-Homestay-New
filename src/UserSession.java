import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class UserSession {
    private static String username;
    private static String fullName;
    private static String email;
    private static String password;
    private static String phone;
    private static String role = "GUEST"; // "GUEST", "HOST", atau "ADMIN"
    private static boolean loggedIn = false;

    // Filter pencarian dari LandingPage ke HasilPencarian
    private static String searchLocation;
    private static String searchDate;
    private static String searchGuests;

    // Data villa yang dipilih untuk DetailVilla
    private static String selectedVillaName;
    private static String selectedVillaAddress;
    private static String selectedVillaFacilities;
    private static int selectedVillaPrice;
    private static String selectedVillaImage;

    // Detail booking aktif menuju halaman Pembayaran
    private static LocalDate bookingCheckIn;
    private static LocalDate bookingCheckOut;
    private static String bookingGuests;
    private static long bookingNights;
    private static long bookingTotalAmount;

    // Daftar reservasi milik user
    public static class ReservationRecord {
        public String orderCode;
        public String status;
        public String villaName;
        public String location;
        public String roomSummary;
        public String checkInText;
        public String checkOutText;
        public String durationText;
        public String totalPriceText;
        public String paymentNote;
        public String imagePath;

        public ReservationRecord(String orderCode, String status, String villaName, String location,
                                 String roomSummary, String checkInText, String checkOutText,
                                 String durationText, String totalPriceText, String paymentNote, String imagePath) {
            this.orderCode = orderCode;
            this.status = status;
            this.villaName = villaName;
            this.location = location;
            this.roomSummary = roomSummary;
            this.checkInText = checkInText;
            this.checkOutText = checkOutText;
            this.durationText = durationText;
            this.totalPriceText = totalPriceText;
            this.paymentNote = paymentNote;
            this.imagePath = imagePath;
        }
    }

    private static final List<ReservationRecord> userReservations = new ArrayList<>();

    // Akun Admin bawaan
    private static final String ADMIN_USERNAME = "admin";
    private static final String ADMIN_EMAIL = "admin@singgah.com";
    private static final String ADMIN_PASSWORD = "admin123";
    private static final String ADMIN_FULLNAME = "Super Admin Singgah";

    public static void registerUser(String username, String fullName, String email, String password, String phone) {
        registerAccount(username, fullName, email, password, phone, "GUEST");
    }

    public static void registerAccount(String username, String fullName, String email, String password, String phone, String role) {
        UserSession.username = username;
        UserSession.fullName = fullName;
        UserSession.email = email;
        UserSession.password = password;
        UserSession.phone = phone;
        UserSession.role = role;
    }

    public static boolean authenticate(String identifier, String inputPassword) {
        return authenticateWithRole(identifier, inputPassword, "GUEST");
    }

    public static boolean authenticateWithRole(String identifier, String inputPassword, String requiredRole) {
        if ("ADMIN".equalsIgnoreCase(requiredRole)) {
            boolean adminIdMatch = identifier.equalsIgnoreCase(ADMIN_USERNAME) || identifier.equalsIgnoreCase(ADMIN_EMAIL);
            if (adminIdMatch && ADMIN_PASSWORD.equals(inputPassword)) {
                UserSession.username = ADMIN_USERNAME;
                UserSession.fullName = ADMIN_FULLNAME;
                UserSession.email = ADMIN_EMAIL;
                UserSession.role = "ADMIN";
                loggedIn = true;
                return true;
            }
            return false;
        }

        if (username == null || password == null) {
            return false;
        }
        boolean idMatches = identifier.equalsIgnoreCase(username) || identifier.equalsIgnoreCase(email);
        boolean roleMatches = requiredRole.equalsIgnoreCase(role);
        if (idMatches && password.equals(inputPassword) && roleMatches) {
            loggedIn = true;
            return true;
        }
        return false;
    }

    public static void setSearchFilter(String location, String date, String guests) {
        UserSession.searchLocation = location;
        UserSession.searchDate = date;
        UserSession.searchGuests = guests;
    }

    public static String getSearchLocation() {
        return searchLocation;
    }

    public static String getSearchDate() {
        return searchDate;
    }

    public static String getSearchGuests() {
        return searchGuests;
    }

    public static void setSelectedVilla(String name, String address, String facilities, int price, String imagePath) {
        UserSession.selectedVillaName = name;
        UserSession.selectedVillaAddress = address;
        UserSession.selectedVillaFacilities = facilities;
        UserSession.selectedVillaPrice = price;
        UserSession.selectedVillaImage = imagePath;
    }

    public static String getSelectedVillaName() {
        return selectedVillaName;
    }

    public static String getSelectedVillaAddress() {
        return selectedVillaAddress;
    }

    public static String getSelectedVillaFacilities() {
        return selectedVillaFacilities;
    }

    public static int getSelectedVillaPrice() {
        return selectedVillaPrice;
    }

    public static String getSelectedVillaImage() {
        return selectedVillaImage;
    }

    public static void setActiveBooking(LocalDate checkIn, LocalDate checkOut, String guests, long nights, long totalAmount) {
        UserSession.bookingCheckIn = checkIn;
        UserSession.bookingCheckOut = checkOut;
        UserSession.bookingGuests = guests;
        UserSession.bookingNights = nights;
        UserSession.bookingTotalAmount = totalAmount;
    }

    public static LocalDate getBookingCheckIn() {
        return bookingCheckIn;
    }

    public static LocalDate getBookingCheckOut() {
        return bookingCheckOut;
    }

    public static String getBookingGuests() {
        return bookingGuests;
    }

    public static long getBookingNights() {
        return bookingNights;
    }

    public static long getBookingTotalAmount() {
        return bookingTotalAmount;
    }

    public static void addReservation(ReservationRecord record) {
        userReservations.add(0, record); // Pesanan terbaru ditaruh paling atas
    }

    public static List<ReservationRecord> getUserReservations() {
        return userReservations;
    }

    public static boolean isLoggedIn() {
        return loggedIn;
    }

    public static String getUsername() {
        return username;
    }

    public static String getFullName() {
        return fullName;
    }

    public static String getEmail() {
        return email;
    }

    public static String getPhone() {
        return phone;
    }

    public static String getRole() {
        return role;
    }

    public static void logout() {
        loggedIn = false;
    }
}