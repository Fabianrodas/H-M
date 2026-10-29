package proyecto.hm;

import java.util.Locale;

/** Converts legacy values to English labels for display in the interface. */
final class UiText {

    private UiText() {
    }

    static String gender(String value) {
        if (value == null || value.isBlank()) {
            return value;
        }

        return switch (value.trim().toLowerCase(Locale.ROOT)) {
            case "hombre", "male", "masculino" -> "Male";
            case "mujer", "female", "femenino" -> "Female";
            case "unisex" -> "Unisex";
            default -> value;
        };
    }
}
