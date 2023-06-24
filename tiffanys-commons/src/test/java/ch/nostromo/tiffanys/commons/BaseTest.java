package ch.nostromo.tiffanys.commons;

import ch.nostromo.tiffanys.commons.move.Move;
import org.junit.jupiter.api.BeforeEach;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public abstract class BaseTest {


    @BeforeEach
    public void setUp() {
        System.setProperty("org.slf4j.simpleLogger.defaultLogLevel", "error");
    }


    public void assertGeneratedMoves(List<Move> generatedMoves, List<Move> expectedMoves) {
        for (Move expectedMove : expectedMoves) {
            if (!generatedMoves.remove(expectedMove)) {
                fail("Move : " + expectedMove + " not found!");
            }
        }
        assertTrue(generatedMoves.isEmpty());
    }

   public void assertEqualsIgnoringLineEndings(String expected, String actual, String testName) {
        String normalizedExpected = normalizeLineEndings(expected);
        String normalizedActual = normalizeLineEndings(actual);
        assertEquals(normalizedExpected, normalizedActual, testName);
    }

    public String normalizeLineEndings(String text) {
        return text == null ? null : text.replaceAll("\\r\\n?", "\n");
    }


}
