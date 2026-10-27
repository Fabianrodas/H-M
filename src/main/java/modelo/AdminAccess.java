package modelo;

public final class AdminAccess {

    private AdminAccess() {
    }

    public static boolean canAccessDatabase(String userEmail, String adminEmail) {
        return userEmail != null
                && adminEmail != null
                && !userEmail.trim().isEmpty()
                && !adminEmail.trim().isEmpty()
                && userEmail.trim().equalsIgnoreCase(adminEmail.trim());
    }

    public static boolean currentUserCanAccessDatabase() {
        Cliente currentUser = ClienteSingleton.getInstance().getCliente();
        if (currentUser == null) {
            return false;
        }
        try {
            return canAccessDatabase(currentUser.getCorreo(), DatabaseConfig.loadDefault().getAdminEmail());
        } catch (IllegalStateException exception) {
            return false;
        }
    }
}
