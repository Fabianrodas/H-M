package modelo;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

class AdminAccessTest {

    @Test
    void deniesNonAdminMenuAndDirectRoute() {
        assertFalse(AdminAccess.canAccessDatabase("customer@example.test", "admin@example.test"));
        assertFalse(AdminAccess.canAccessDatabase(null, "admin@example.test"));
        assertFalse(AdminAccess.canAccessDatabase("admin@example.test", ""));
    }

    @Test
    void allowsConfiguredAdminWithoutCaseSensitivity() {
        assertTrue(AdminAccess.canAccessDatabase("Admin@example.test", "admin@example.test"));
    }
}
