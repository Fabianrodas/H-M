package modelo;

import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;
import org.junit.jupiter.api.Test;

class ProjectNamespaceTest {

    @Test
    void sourceAndResourcesNoLongerUseTheLegacyProjectNamespace() throws IOException {
        Path root = Path.of(System.getProperty("user.dir"));
        Path sourceRoot = root.resolve("src");
        String legacyNamespace = "proyecto." + "tienda" + "proyecto";
        String legacyPath = "proyecto/" + "tienda" + "proyecto";
        List<Path> staleFiles = new ArrayList<>();

        try (Stream<Path> files = Files.walk(sourceRoot)) {
            files.filter(Files::isRegularFile)
                    .filter(path -> path.toString().endsWith(".java") || path.toString().endsWith(".fxml"))
                    .forEach(path -> {
                        String relativePath = root.relativize(path).toString().replace('\\', '/');
                        try {
                            String contents = Files.readString(path, StandardCharsets.UTF_8);
                            if (relativePath.contains(legacyPath) || contents.contains(legacyNamespace)) {
                                staleFiles.add(path);
                            }
                        } catch (IOException exception) {
                            throw new java.io.UncheckedIOException(exception);
                        }
                    });
        }

        assertTrue(staleFiles.isEmpty(),
                () -> "H-M still contains " + staleFiles.size() + " files with the old namespace.");
        assertTrue(Files.exists(sourceRoot.resolve("main/java/proyecto/hm/App.java")));
        assertTrue(Files.exists(sourceRoot.resolve("main/resources/proyecto/hm/tienda.fxml")));
        assertTrue(Files.readString(root.resolve("pom.xml"), StandardCharsets.UTF_8)
                .contains("<mainClass>proyecto.hm.Main</mainClass>"));
        assertTrue(Files.readString(sourceRoot.resolve("main/java/module-info.java"), StandardCharsets.UTF_8)
                .contains("module proyecto.hm"));
    }
}