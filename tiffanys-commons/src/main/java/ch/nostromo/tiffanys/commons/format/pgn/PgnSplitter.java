package ch.nostromo.tiffanys.commons.format.pgn;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

/**
 * Split multiple pgn games.
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class PgnSplitter {

    /**
     * Does split multiple pgn games found into single pgn strings
     */
    public static List<String> splitPgnCollection(String pgnContent) {
        List<String> pgnGames = new ArrayList<>();

        String[] lines = pgnContent.split("\n");

        StringBuilder currentGame = new StringBuilder();

        boolean inGame = false;
        int consecutiveEmptyLines = 0;

        for (String line : lines) {

            String trimmedLine = line.trim();

            if (trimmedLine.startsWith("[")) {
                // Start of a new game
                inGame = true;
                consecutiveEmptyLines = 0;
                currentGame.append(line).append("\n");

            } else if (trimmedLine.isEmpty()) {

                if (inGame) {

                    currentGame.append(line).append("\n");
                    consecutiveEmptyLines++;
                    if (consecutiveEmptyLines >= 2) {

                        // Add last game to list
                        pgnGames.add(currentGame.toString().trim());

                        currentGame = new StringBuilder();
                        inGame = false;
                        consecutiveEmptyLines = 0;
                    }
                }

            } else {
                consecutiveEmptyLines = 0;
                currentGame.append(line).append("\n");
            }
        }

        if (!currentGame.isEmpty()) {
            pgnGames.add(currentGame.toString().trim());
        }

        return pgnGames;
    }

}
