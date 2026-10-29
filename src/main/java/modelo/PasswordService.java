package modelo;

import java.nio.charset.StandardCharsets;
import java.security.GeneralSecurityException;
import java.security.MessageDigest;
import java.security.SecureRandom;
import java.util.Arrays;
import java.util.Base64;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;

/** Hashes account passwords and verifies hashes created by older H-M versions. */
public final class PasswordService {

    public static final int MAX_STORED_LENGTH = 100;
    private static final int ITERATIONS = 600_000;
    private static final int SALT_BYTES = 16;
    private static final int HASH_BYTES = 32;
    private static final String HASH_PREFIX = "pbkdf2-sha256";
    private static final String CHANGE_REQUIRED_PREFIX = "pbkdf2-sha256-required";
    private static final String DISPLAY_MASK = "••••••••";
    private static final SecureRandom RANDOM = new SecureRandom();

    private PasswordService() {
    }

    public static String hash(char[] password) {
        return hash(password, false);
    }

    public static String hashForPasswordChangeRequired(char[] password) {
        return hash(password, true);
    }

    private static String hash(char[] password, boolean changeRequired) {
        if (password == null || password.length == 0) {
            throw new IllegalArgumentException("Password cannot be empty.");
        }
        byte[] salt = new byte[SALT_BYTES];
        RANDOM.nextBytes(salt);
        byte[] derived = derive(password, salt, ITERATIONS, HASH_BYTES * 8);
        String prefix = changeRequired ? CHANGE_REQUIRED_PREFIX : HASH_PREFIX;
        String encoded = prefix + "$" + ITERATIONS + "$"
                + Base64.getEncoder().encodeToString(salt) + "$"
                + Base64.getEncoder().encodeToString(derived);
        Arrays.fill(derived, (byte) 0);
        if (encoded.length() > MAX_STORED_LENGTH) {
            throw new IllegalStateException("The password hash does not fit in the current column.");
        }
        return encoded;
    }

    public static VerificationResult verify(char[] password, String storedValue) {
        if (password == null || storedValue == null || password.length == 0) {
            return VerificationResult.invalid();
        }
        if (storedValue.startsWith(HASH_PREFIX + "$")) {
            return verifyPbkdf2(password, storedValue, false);
        }
        if (storedValue.startsWith(CHANGE_REQUIRED_PREFIX + "$")) {
            return verifyPbkdf2(password, storedValue, true);
        }
        if (!isLegacyHex(storedValue)) {
            return VerificationResult.invalid();
        }

        String legacyPassword = decodeLegacyHex(storedValue);
        byte[] submitted = new String(password).getBytes(StandardCharsets.UTF_8);
        byte[] expected = legacyPassword.getBytes(StandardCharsets.UTF_8);
        return MessageDigest.isEqual(submitted, expected)
                ? VerificationResult.legacyMatch()
                : VerificationResult.invalid();
    }

    public static boolean isLegacyHex(String storedValue) {
        return storedValue != null
                && !storedValue.isEmpty()
                && (storedValue.length() % 2 == 0)
                && storedValue.matches("(?i)[0-9a-f]+");
    }

    public static String upgradeLegacyPassword(String storedValue, boolean requirePasswordChange) {
        if (!isLegacyHex(storedValue)) {
            throw new IllegalArgumentException("The value is not in the legacy password format.");
        }
        char[] legacyPassword = decodeLegacyHex(storedValue).toCharArray();
        try {
            return requirePasswordChange
                    ? hashForPasswordChangeRequired(legacyPassword)
                    : hash(legacyPassword);
        } finally {
            Arrays.fill(legacyPassword, '\0');
        }
    }

    public static boolean requiresPasswordChange(String storedValue) {
        return storedValue != null && storedValue.startsWith(CHANGE_REQUIRED_PREFIX + "$");
    }

    public static String maskForDisplay(String storedValue) {
        return DISPLAY_MASK;
    }

    private static VerificationResult verifyPbkdf2(char[] password, String storedValue, boolean changeRequired) {
        try {
            String[] fields = storedValue.split("\\$", -1);
            if (fields.length != 4) {
                return VerificationResult.invalid();
            }
            int iterations = Integer.parseInt(fields[1]);
            if (iterations != ITERATIONS) {
                return VerificationResult.invalid();
            }
            byte[] salt = Base64.getDecoder().decode(fields[2]);
            byte[] expected = Base64.getDecoder().decode(fields[3]);
            if (salt.length != SALT_BYTES || expected.length != HASH_BYTES) {
                return VerificationResult.invalid();
            }
            byte[] submitted = derive(password, salt, iterations, expected.length * 8);
            boolean valid = MessageDigest.isEqual(submitted, expected);
            Arrays.fill(submitted, (byte) 0);
            return valid
                    ? VerificationResult.passwordHashMatch(changeRequired)
                    : VerificationResult.invalid();
        } catch (IllegalArgumentException exception) {
            return VerificationResult.invalid();
        }
    }

    private static byte[] derive(char[] password, byte[] salt, int iterations, int keyLengthBits) {
        PBEKeySpec keySpec = new PBEKeySpec(password, salt, iterations, keyLengthBits);
        try {
            return SecretKeyFactory.getInstance("PBKDF2WithHmacSHA256")
                    .generateSecret(keySpec)
                    .getEncoded();
        } catch (GeneralSecurityException exception) {
            throw new IllegalStateException("Could not secure the password.", exception);
        } finally {
            keySpec.clearPassword();
        }
    }

    private static String decodeLegacyHex(String encoded) {
        StringBuilder decoded = new StringBuilder(encoded.length() / 2);
        for (int index = 0; index < encoded.length(); index += 2) {
            int character = Integer.parseInt(encoded.substring(index, index + 2), 16);
            decoded.append((char) character);
        }
        return decoded.toString();
    }

    public static final class VerificationResult {
        private final boolean valid;
        private final boolean needsUpgrade;
        private final boolean mustChangePassword;

        private VerificationResult(boolean valid, boolean needsUpgrade, boolean mustChangePassword) {
            this.valid = valid;
            this.needsUpgrade = needsUpgrade;
            this.mustChangePassword = mustChangePassword;
        }

        private static VerificationResult invalid() {
            return new VerificationResult(false, false, false);
        }

        private static VerificationResult legacyMatch() {
            return new VerificationResult(true, true, false);
        }

        private static VerificationResult passwordHashMatch(boolean mustChangePassword) {
            return new VerificationResult(true, false, mustChangePassword);
        }

        public boolean isValid() {
            return valid;
        }

        public boolean needsUpgrade() {
            return needsUpgrade;
        }

        public boolean mustChangePassword() {
            return mustChangePassword;
        }
    }
}
