package modelo;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

class PasswordServiceTest {

    @Test
    void hashesVerifyWithRandomSalt() {
        char[] password = "mock-customer-password".toCharArray();

        String first = PasswordService.hash(password);
        String second = PasswordService.hash(password);

        assertNotEquals(first, second);
        assertTrue(PasswordService.verify(password, first).isValid());
        assertTrue(PasswordService.verify(password, second).isValid());
    }

    @Test
    void rejectsWrongPassword() {
        String stored = PasswordService.hash("correct-mock-password".toCharArray());

        assertFalse(PasswordService.verify("wrong-mock-password".toCharArray(), stored).isValid());
    }

    @Test
    void encodedValueFitsExistingColumn() {
        String stored = PasswordService.hash("column-length-check".toCharArray());

        assertTrue(stored.length() <= 100);
    }

    @Test
    void verifiesLegacyHexAndFlagsUpgrade() {
        String legacy = toLegacyHex("legacy-mock-password");

        PasswordService.VerificationResult result = PasswordService.verify("legacy-mock-password".toCharArray(), legacy);

        assertTrue(result.isValid());
        assertTrue(result.needsUpgrade());
        assertFalse(result.mustChangePassword());
    }

    @Test
    void adminPasswordChangeRequirementSurvivesLoginVerification() {
        String stored = PasswordService.hashForPasswordChangeRequired("legacy-mock-password".toCharArray());

        assertTrue(stored.length() <= PasswordService.MAX_STORED_LENGTH);
        PasswordService.VerificationResult result = PasswordService.verify("legacy-mock-password".toCharArray(), stored);

        assertTrue(result.isValid());
        assertFalse(result.needsUpgrade());
        assertTrue(result.mustChangePassword());
    }

    @Test
    void upgradesLegacyAdministratorPasswordWithChangeRequiredMarker() {
        char[] password = "legacy-admin-mock-password".toCharArray();
        String legacy = toLegacyHex(new String(password));

        String upgraded = PasswordService.upgradeLegacyPassword(legacy, true);
        PasswordService.VerificationResult result = PasswordService.verify(password, upgraded);

        assertTrue(result.isValid());
        assertTrue(result.mustChangePassword());
    }

    @Test
    void displayMaskDoesNotReturnStoredPassword() {
        String stored = "pbkdf2-sha256$stored-test-hash";

        String displayed = PasswordService.maskForDisplay(stored);

        assertEquals("••••••••", displayed);
        assertNotEquals(stored, displayed);
    }

    private String toLegacyHex(String password) {
        StringBuilder encoded = new StringBuilder();
        for (char character : password.toCharArray()) {
            encoded.append(String.format("%02x", (int) character));
        }
        return encoded.toString();
    }
}
