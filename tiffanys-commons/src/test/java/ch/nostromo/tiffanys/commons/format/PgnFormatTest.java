package ch.nostromo.tiffanys.commons.format;

import ch.nostromo.tiffanys.commons.BaseTest;
import ch.nostromo.tiffanys.commons.format.pgn.PgnSplitter;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.stream.Stream;


class PgnFormatTest extends BaseTest {

    @ParameterizedTest(name = "Game {index}: {0}")
    @MethodSource("providePgnGames")
    void testReconstruction(String testName, String pgn) {
        PgnFormat pgnFormat = new PgnFormat(pgn);
        assertEqualsIgnoringLineEndings(pgn, pgnFormat.toString(), testName);
    }

    private static Stream<org.junit.jupiter.params.provider.Arguments> providePgnGames() throws IOException {
        String pgnFile;

        try (var inputStream = PgnFormatTest.class.getClassLoader().getResourceAsStream("GameCollection.pgn")) {
            Assertions.assertNotNull(inputStream);
            pgnFile = new String(inputStream.readAllBytes(), StandardCharsets.UTF_8);
        }

        List<String> pgnGames = PgnSplitter.splitPgnCollection(pgnFile);

        return Stream.iterate(0, i -> i < pgnGames.size(), i -> i + 1)
                .map(i -> org.junit.jupiter.params.provider.Arguments.of("PGN Game " + (i + 1), pgnGames.get(i)
                ));
    }


}
