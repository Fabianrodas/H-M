package modelo;

import static org.junit.jupiter.api.Assertions.assertNotNull;

import org.junit.jupiter.api.Test;

class DefaultProfileImageResourceTest {

    @Test
    void defaultProfileImageIsPackagedOnTheApplicationClasspath() {
        assertNotNull(getClass().getResource("/imagen_usuario/default_user.jpg"),
                "The account screen's default profile image must be included in the application resources.");
    }
}