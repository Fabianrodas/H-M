package modelo;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

class AdminAccessTest {

    @Test
    void doesNotRecognizeDifferentOrMissingAdminEmails() {
        assertFalse(AdminAccess.isConfiguredAdmin("customer@example.test", "admin@example.test"));
        assertFalse(AdminAccess.isConfiguredAdmin(null, "admin@example.test"));
        assertFalse(AdminAccess.isConfiguredAdmin("admin@example.test", ""));
    }

    @Test
    void recognizesConfiguredAdminWithoutCaseSensitivity() {
        assertTrue(AdminAccess.isConfiguredAdmin("Admin@example.test", "admin@example.test"));
    }
}
