package ch.nostromo.tiffanys.commons.formats;

import ch.nostromo.tiffanys.commons.ChessGameException;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.StringTokenizer;

/**
 * PGN Format can pre-parse a PGN String (Tags & moves string).
 */
@Data
@AllArgsConstructor
public class PgnFormat {

    // 7 Tag roster
    String event;
    String site;
    String date;
    String round;
    String whitePlayer;
    String blackPlayer;
    String result;

    String pgnMoves;

    // Optional Tags
    Map<String, String> optionalTags = new LinkedHashMap<>();


    public PgnFormat(String pgn) {
        StringTokenizer pgnLineTokenizer = new StringTokenizer(pgn, "\n");

        StringBuilder movesSb = new StringBuilder();
        while (pgnLineTokenizer.hasMoreTokens()) {
            String line = pgnLineTokenizer.nextToken();

            if (line.startsWith("[Event")) {
                event = extractTagValue(line);
            } else if (line.startsWith("[Site")) {
                site = extractTagValue(line);
            } else if (line.startsWith("[Date")) {
                date = extractTagValue(line);
            } else if (line.startsWith("[Round")) {
                round = extractTagValue(line);
            } else if (line.startsWith("[White ")) {
                whitePlayer = extractTagValue(line);
            } else if (line.startsWith("[Black ")) {
                blackPlayer = extractTagValue(line);
            } else if (line.startsWith("[Result")) {
                result = extractTagValue(line);
            } else if (line.startsWith("[")) {
                extractOptionalTag(line);
            } else if (!line.trim().isEmpty()) {
                movesSb.append(line);
                if (pgnLineTokenizer.hasMoreTokens()) {
                    movesSb.append("\n");
                }
            }

            this.pgnMoves = movesSb.toString();
        }

    }


    private void extractOptionalTag(String line) {
        StringTokenizer st = new StringTokenizer(line, "\"");
        String key = st.nextToken().substring(1).trim();
        String value = st.nextToken();
        if (value == null) {
            throw new ChessGameException("Unknown pgn tag format: " + line);
        }

        optionalTags.put(key, value);

    }

    private String extractTagValue(String line) {
        StringTokenizer st = new StringTokenizer(line, "\"");
        st.nextToken();
        String result = st.nextToken();
        if (result == null) {
            throw new ChessGameException("Unknown pgn tag format: " + line);
        }
        return result;
    }


    public String getStripedPgnMoves() {
        return pgnMoves.replaceAll("\\{.*?}", "");
    }

    private String exportTag(String key, String value) {
        if (value == null) {
            return "";
        } else {
            return "[" + key + " \"" + value + "\"]\n";
        }

    }

    @Override
    public String toString() {
        StringBuilder resultSb = new StringBuilder();

        resultSb.append(exportTag("Event", event));
        resultSb.append(exportTag("Site", site));
        resultSb.append(exportTag("Date", date));
        resultSb.append(exportTag("Round", round));
        resultSb.append(exportTag("White", whitePlayer));
        resultSb.append(exportTag("Black", blackPlayer));
        resultSb.append(exportTag("Result", result));

        for (Map.Entry<String, String> entry : optionalTags.entrySet()) {
            resultSb.append(exportTag(entry.getKey(), entry.getValue()));
        }

        resultSb.append("\n");
        resultSb.append(pgnMoves);

        return resultSb.toString();
    }

}
