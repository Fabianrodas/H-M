package modelo;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import org.junit.jupiter.api.Test;

class PostgreSqlSeedTest {

    private static final Pattern INSERT = Pattern.compile("^INSERT INTO\\s+([^\\s(]+)(.*)$");

    @Test
    void preservesOriginalInsertValuesAndOrder() throws IOException {
        Path originalDump = Path.of("dump.sql");
        Path postgresSeed = Path.of("database", "postgresql", "seed.sql");

        assertTrue(Files.isRegularFile(postgresSeed), "PostgreSQL seed.sql must exist");

        List<String> original = readInserts(originalDump);
        List<String> migrated = readInserts(postgresSeed);

        assertEquals(6, original.size(), "The source dump must retain its six INSERT statements");
        assertEquals(original, migrated, "Seed statements must preserve values and order");
    }

    private static List<String> readInserts(Path path) throws IOException {
        return Files.readAllLines(path).stream()
                .filter(line -> line.startsWith("INSERT INTO"))
                .map(PostgreSqlSeedTest::normalizeTableIdentifier)
                .collect(Collectors.toList());
    }

    private static String normalizeTableIdentifier(String statement) {
        Matcher matcher = INSERT.matcher(statement);
        if (!matcher.matches()) {
            throw new AssertionError("Unexpected INSERT format: " + statement);
        }
        String table = matcher.group(1).replace("`", "").toLowerCase();
        return "INSERT INTO " + table + matcher.group(2);
    }
}
