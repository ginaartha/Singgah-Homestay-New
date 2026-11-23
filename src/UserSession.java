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