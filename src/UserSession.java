public class UserSession {
    private static String username;
    private static String fullName;
    private static String email;
    private static String password;
    private static String phone;
    private static String role = "GUEST"; // "GUEST", "HOST", atau "ADMIN"
    private static boolean loggedIn = false;

    // Akun Admin bawaan (pre-seeded, tanpa halaman Sign Up publik)
    private static final String ADMIN_USERNAME = "admin";
    private static final String ADMIN_EMAIL = "admin@singgah.com";
    private static final String ADMIN_PASSWORD = "admin123";
    private static final String ADMIN_FULLNAME = "Super Admin Singgah";

    // Daftar Tamu (Guest)
    public static void registerUser(String username, String fullName, String email, String password, String phone) {
        registerAccount(username, fullName, email, password, phone, "GUEST");
    }

    // Daftar Guest atau Host
    public static void registerAccount(String username, String fullName, String email, String password, String phone, String role) {
        UserSession.username = username;
        UserSession.fullName = fullName;
        UserSession.email = email;
        UserSession.password = password;
        UserSession.phone = phone;
        UserSession.role = role;
    }

    // Login Tamu (Guest)
    public static boolean authenticate(String identifier, String inputPassword) {
        return authenticateWithRole(identifier, inputPassword, "GUEST");
    }

    // Login berdasarkan Role ("GUEST", "HOST", atau "ADMIN")
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

    public static boolean isLoggedIn() {
        return loggedIn;
    }

    public static String getUsername() {
        return username;
    }

    public static String getFullName() {
        return fullName;
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