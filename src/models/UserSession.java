package models;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class UserSession {

    public static class Account {
        public String username;
        public String fullName;
        public String email;
        public String password;
        public String phone;
        public String role;
        public String bankName;
        public String bankAccount;

        public Account(String username, String fullName, String email, String password, String phone, String role) {
            this(username, fullName, email, password, phone, role, "", "");
        }

        public Account(String username, String fullName, String email, String password, String phone, String role,
                       String bankName, String bankAccount) {
            this.username = username;
            this.fullName = fullName;
            this.email = email;
            this.password = password;
            this.phone = phone;
            this.role = role;
            this.bankName = bankName != null ? bankName : "";
            this.bankAccount = bankAccount != null ? bankAccount : "";
        }
    }

    private static final Map<String, Account> accounts = new HashMap<>();
    private static Account currentUser = null;
    private static boolean loggedIn = false;

    static {
        accounts.put("admin", new Account(
            "admin",
            "Super Admin Singgah",
            "admin@singgah.com",
            "admin123",
            "081234567890",
            "ADMIN"
        ));
    }

    private static String searchLocation;
    private static String searchDate;
    private static String searchGuests;

    private static String selectedVillaName;
    private static String selectedVillaAddress;
    private static String selectedVillaFacilities;
    private static int selectedVillaPrice;
    private static String selectedVillaImage;

    private static LocalDate bookingCheckIn;
    private static LocalDate bookingCheckOut;
    private static String bookingGuests;
    private static long bookingNights;
    private static long bookingTotalAmount;

    public static class VillaData {
        public String name;
        public String area;
        public String regency;
        public String fullAddress;
        public int maxGuests;
        public String facilities;
        public int pricePerNight;
        public String priceText;
        public String imagePath;

        public VillaData(String name, String area, String regency, String fullAddress, int maxGuests,
                         String facilities, int pricePerNight, String priceText, String imagePath) {
            this.name = name;
            this.area = area;
            this.regency = regency;
            this.fullAddress = fullAddress;
            this.maxGuests = maxGuests;
            this.facilities = facilities;
            this.pricePerNight = pricePerNight;
            this.priceText = priceText;
            this.imagePath = imagePath;
        }
    }

    private static final List<VillaData> allVillas = new ArrayList<>(List.of(
        new VillaData("Dhyana Villa", "Klungkung, Bali", "Klungkung", "Jl. Menuju Hatimu, No. 01, Kec. Klungkung, Kab. Klungkung, Bali", 2, "2 tamu • Kamar mandi dalam • Wi-Fi", 250000, "Rp 250.000,00/malam", ""),
        new VillaData("Inten Villa", "Tabanan, Bali", "Tabanan", "Jl. Tanah Lot No. 12, Kec. Kediri, Kab. Tabanan, Bali", 3, "3 tamu • Kamar mandi dalam • Wi-Fi", 250000, "Rp 250.000,00/malam", ""),
        new VillaData("Gina Villa", "Gianyar, Bali", "Gianyar", "Jl. Raya Ubud No. 88, Kec. Ubud, Kab. Gianyar, Bali", 4, "4 tamu • Kamar mandi dalam • Wi-Fi", 250000, "Rp 250.000,00/malam", "")
    ));

    public static List<VillaData> getAllVillas() {
        return allVillas;
    }

    public static void addVilla(VillaData villa) {
        allVillas.add(villa);
    }

    public static void updateVilla(int index, VillaData updated) {
        if (index >= 0 && index < allVillas.size()) {
            allVillas.set(index, updated);
        }
    }

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
        public String guestUsername;
        public String guestFullName;
        public boolean isPaid;

        public ReservationRecord(String orderCode, String status, String villaName, String location,
                                 String roomSummary, String checkInText, String checkOutText,
                                 String durationText, String totalPriceText, String paymentNote, String imagePath) {
            this(orderCode, status, villaName, location, roomSummary, checkInText, checkOutText,
                 durationText, totalPriceText, paymentNote, imagePath, "", "", true);
        }

        public ReservationRecord(String orderCode, String status, String villaName, String location,
                                 String roomSummary, String checkInText, String checkOutText,
                                 String durationText, String totalPriceText, String paymentNote, String imagePath,
                                 String guestUsername, String guestFullName, boolean isPaid) {
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
            this.guestUsername = guestUsername != null ? guestUsername : "";
            this.guestFullName = guestFullName != null ? guestFullName : "";
            this.isPaid = isPaid;
        }
    }

    private static final List<ReservationRecord> userReservations = new ArrayList<>();

    public static boolean isUsernameOrEmailTaken(String username, String email) {
        String key = username.trim().toLowerCase();
        if (accounts.containsKey(key)) {
            return true;
        }
        for (Account acc : accounts.values()) {
            if (acc.email != null && acc.email.equalsIgnoreCase(email.trim())) {
                return true;
            }
        }
        return false;
    }

    public static boolean registerUser(String username, String fullName, String email, String password, String phone) {
        return registerAccount(username, fullName, email, password, phone, "GUEST", "", "");
    }

    public static boolean registerAccount(String username, String fullName, String email, String password, String phone, String role) {
        return registerAccount(username, fullName, email, password, phone, role, "", "");
    }

    public static boolean registerAccount(String username, String fullName, String email, String password, String phone,
                                          String role, String bankName, String bankAccount) {
        if (isUsernameOrEmailTaken(username, email)) {
            return false;
        }
        Account newAcc = new Account(
            username.trim(),
            fullName.trim(),
            email.trim(),
            password,
            phone.trim(),
            role,
            bankName != null ? bankName.trim() : "",
            bankAccount != null ? bankAccount.trim() : ""
        );
        accounts.put(username.trim().toLowerCase(), newAcc);
        return true;
    }

    public static boolean authenticate(String identifier, String inputPassword) {
        return authenticateWithRole(identifier, inputPassword, "GUEST");
    }

    public static boolean authenticateWithRole(String identifier, String inputPassword, String requiredRole) {
        if (identifier == null || inputPassword == null) {
            return false;
        }
        String id = identifier.trim();
        for (Account acc : accounts.values()) {
            boolean idMatches = acc.username.equalsIgnoreCase(id) || acc.email.equalsIgnoreCase(id);
            boolean roleMatches = requiredRole.equalsIgnoreCase(acc.role);
            if (idMatches && acc.password.equals(inputPassword) && roleMatches) {
                currentUser = acc;
                loggedIn = true;
                return true;
            }
        }
        return false;
    }

    public static boolean isLoggedIn() {
        return loggedIn && currentUser != null;
    }

    public static String getUsername() {
        return currentUser != null ? currentUser.username : null;
    }

    public static String getFullName() {
        return currentUser != null ? currentUser.fullName : null;
    }

    public static String getEmail() {
        return currentUser != null ? currentUser.email : null;
    }

    public static String getPhone() {
        return currentUser != null ? currentUser.phone : null;
    }

    public static String getRole() {
        return currentUser != null ? currentUser.role : "GUEST";
    }

    public static String getHostBankName() {
        return (currentUser != null && currentUser.bankName != null) ? currentUser.bankName : "";
    }

    public static String getHostBankAccount() {
        return (currentUser != null && currentUser.bankAccount != null) ? currentUser.bankAccount : "";
    }

    public static boolean hasHostBankDetails() {
        return !getHostBankName().trim().isEmpty() && !getHostBankAccount().trim().isEmpty();
    }

    public static boolean updateCurrentUserProfile(String fullName, String email, String phone,
                                                   String bankName, String bankAccount) {
        if (currentUser == null) {
            return false;
        }
        String trimmedEmail = email.trim();
        for (Account acc : accounts.values()) {
            if (acc != currentUser && acc.email != null && acc.email.equalsIgnoreCase(trimmedEmail)) {
                return false;
            }
        }
        currentUser.fullName = fullName.trim();
        currentUser.email = trimmedEmail;
        currentUser.phone = phone.trim();
        if (bankName != null) {
            currentUser.bankName = bankName.trim();
        }
        if (bankAccount != null) {
            currentUser.bankAccount = bankAccount.trim();
        }
        return true;
    }

    public static void logout() {
        currentUser = null;
        loggedIn = false;
    }

    public static void setSearchFilter(String location, String date, String guests) {
        UserSession.searchLocation = location;
        UserSession.searchDate = date;
        UserSession.searchGuests = guests;
    }

    public static String getSearchLocation() { return searchLocation; }
    public static String getSearchDate() { return searchDate; }
    public static String getSearchGuests() { return searchGuests; }

    public static void setSelectedVilla(String name, String address, String facilities, int price, String imagePath) {
        UserSession.selectedVillaName = name;
        UserSession.selectedVillaAddress = address;
        UserSession.selectedVillaFacilities = facilities;
        UserSession.selectedVillaPrice = price;
        UserSession.selectedVillaImage = imagePath;
    }

    public static String getSelectedVillaName() { return selectedVillaName; }
    public static String getSelectedVillaAddress() { return selectedVillaAddress; }
    public static String getSelectedVillaFacilities() { return selectedVillaFacilities; }
    public static int getSelectedVillaPrice() { return selectedVillaPrice; }
    public static String getSelectedVillaImage() { return selectedVillaImage; }

    public static void setActiveBooking(LocalDate checkIn, LocalDate checkOut, String guests, long nights, long totalAmount) {
        UserSession.bookingCheckIn = checkIn;
        UserSession.bookingCheckOut = checkOut;
        UserSession.bookingGuests = guests;
        UserSession.bookingNights = nights;
        UserSession.bookingTotalAmount = totalAmount;
    }

    public static LocalDate getBookingCheckIn() { return bookingCheckIn; }
    public static LocalDate getBookingCheckOut() { return bookingCheckOut; }
    public static String getBookingGuests() { return bookingGuests; }
    public static long getBookingNights() { return bookingNights; }
    public static long getBookingTotalAmount() { return bookingTotalAmount; }

    public static void addReservation(ReservationRecord record) {
        userReservations.add(0, record);
    }

    public static List<ReservationRecord> getUserReservations() {
        return userReservations;
    }

    public static List<ReservationRecord> getPaidReservations() {
        List<ReservationRecord> paid = new ArrayList<>();
        for (ReservationRecord r : userReservations) {
            if (r.isPaid) {
                paid.add(r);
            }
        }
        return paid;
    }
}