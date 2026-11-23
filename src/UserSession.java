public class UserSession {
    private static String username;
    private static String fullName;
    private static String email;
    private static String password;
    private static String phone;
    private static boolean loggedIn = false;

    public static void registerUser(String username, String fullName, String email, String password, String phone) {
        UserSession.username = username;
        UserSession.fullName = fullName;
        UserSession.email = email;
        UserSession.password = password;
        UserSession.phone = phone;
    }

    public static boolean authenticate(String identifier, String inputPassword) {
        if (username == null || password == null) {
            return false;
        }
        boolean idMatches = identifier.equalsIgnoreCase(username) || identifier.equalsIgnoreCase(email);
        if (idMatches && password.equals(inputPassword)) {
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

    public static void logout() {
        loggedIn = false;
    }
}