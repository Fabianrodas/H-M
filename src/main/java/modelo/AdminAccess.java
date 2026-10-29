package modelo;

public final class AdminAccess {

    private AdminAccess() {
    }

    public static boolean isConfiguredAdmin(String userEmail, String adminEmail) {
        return userEmail != null
                && adminEmail != null
                && !userEmail.trim().isEmpty()
                && !adminEmail.trim().isEmpty()
                && userEmail.trim().equalsIgnoreCase(adminEmail.trim());
    }
}
