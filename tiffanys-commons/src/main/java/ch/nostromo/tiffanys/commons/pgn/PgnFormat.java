package ch.nostromo.tiffanys.commons.pgn;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.StringTokenizer;

@Getter
@Setter
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


    private void extractOptionalTag(String line) {
        StringTokenizer st = new StringTokenizer(line, "\"");
        String key = st.nextToken().substring(1).trim();
        String value = st.nextToken();
        if (value == null) {
            throw new IllegalArgumentException("Unknown pgn tag format: " + line);
        }

        optionalTags.put(key, value);

    }

    private String extractTagValue(String line) {
        StringTokenizer st = new StringTokenizer(line, "\"");
        st.nextToken();
        String result = st.nextToken();
        if (result == null) {
            throw new IllegalArgumentException("Unknown pgn tag format: " + line);
        }
        return result;
    }

    public PgnFormat(String pgn) {
        StringTokenizer pgnLineTokenizer = new StringTokenizer(pgn, "\n");

        this.pgnMoves = ""; // other may stay null
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
                pgnMoves += line;
                if (pgnLineTokenizer.hasMoreTokens()) {
                    pgnMoves += "\n";
                }
            }
        }

    }

    public String generatePgn() {
        StringBuilder result = new StringBuilder();

        result.append("[Event \"" + event + "\"]\n");
        result.append("[Site \"" + site + "\"]\n");
        result.append("[Date \"" + date + "\"]\n");
        result.append("[Round \"" + round + "\"]\n");
        result.append("[White \"" + whitePlayer + "\"]\n");
        result.append("[Black \"" + blackPlayer + "\"]\n");
        result.append("[Result \"" + this.result + "\"]\n");

        for (Map.Entry<String, String> entry : optionalTags.entrySet()) {
           result.append("[" + entry.getKey() + "\"" + entry.getValue() + "\"]\n");
        }

        result.append("\n");
        result.append(pgnMoves);

        return result.toString();

    }

    public String getStripedPgnMoves() {
        return pgnMoves.replaceAll("\\{.*?\\}", "");
    }

    @Override
    public String toString() {
        return generatePgn();
    }

}
