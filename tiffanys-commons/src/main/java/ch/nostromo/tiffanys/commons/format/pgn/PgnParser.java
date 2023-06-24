package ch.nostromo.tiffanys.commons.format.pgn;

import ch.nostromo.tiffanys.commons.ChessGameState;
import ch.nostromo.tiffanys.commons.Side;
import ch.nostromo.tiffanys.commons.exception.PgnFormatException;
import ch.nostromo.tiffanys.commons.utils.StringScanner;
import lombok.Data;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Does parse a PGN move String, provides tags and moves including variants and comments.
 */
@Data
public class PgnParser {
    private static final Pattern ENDING_PATTERN = Pattern.compile("(\\*|1/2-1/2|1-0|0-1)\\s*\\z");

    private static final Pattern PREAMBLE_PATTERN = Pattern.compile("^(?>\\s*\\{[^}]*\\})+");

    private static final Pattern MOVE_TOKEN_PATTERN = Pattern.compile(
            "^(\\d++)?(\\.{1,3}+)?+([A-Za-z0-9\\-=+#]++)((?>[!?]|\\$\\d++)*+)$"
    );

    // Full pgn string
    private final String movesBody;

    Map<String, String> tags = new LinkedHashMap<>();

    /**
     * Create a pgn parser
     *
     * @param pgn the full pgn
     */
    public PgnParser(String pgn) {

        StringTokenizer pgnLineTokenizer = new StringTokenizer(pgn, "\n");

        StringBuilder pgnMovesBoddy = new StringBuilder();
        while (pgnLineTokenizer.hasMoreTokens()) {
            String line = pgnLineTokenizer.nextToken();

            if (line.startsWith("[")) {
                extractTag(line);
            } else if (!line.trim().isEmpty()) {
                pgnMovesBoddy.append(line);
                if (pgnLineTokenizer.hasMoreTokens()) {
                    pgnMovesBoddy.append("\n");
                }
            }
        }

        this.movesBody = pgnMovesBoddy.toString().trim();
    }

    private void extractTag(String line) {
        // Regex to capture key and value between [key "value"]
        Pattern pattern = Pattern.compile("\\[(\\w+)\\s+\"([^\"]*)\"]");
        Matcher matcher = pattern.matcher(line);

        if (matcher.find()) {
            String key = matcher.group(1);
            String value = matcher.group(2);
            tags.put(key, value);
        } else {
            throw new PgnFormatException("Invalid pgn tag format: " + line);
        }
    }

    /**
     * Does return the game state at the end of the given pgn
     *
     * @return ChessGameState
     */
    public ChessGameState extractChessGameState() {
        String tail = movesBody.stripTrailing();

        Matcher m = ENDING_PATTERN.matcher(tail);

        if (!m.find()) {
            return ChessGameState.GAME_OPEN;
        }

        return switch (m.group()) {
            case "1/2-1/2" -> ChessGameState.DRAW;
            case "1-0" -> ChessGameState.WHITE_WIN;
            case "0-1" -> ChessGameState.BLACK_WIN;
            case "*" -> ChessGameState.GAME_OPEN;
            default -> throw new IllegalStateException("Unexpected end of pgn: " + m.group());
        };
    }

    /**
     * Does return (possible) preamble comments in the pgn
     *
     * @return List of comments
     */
    public List<String> extractPreambleComments() {
        List<String> result = new ArrayList<>();
        Matcher commentMatcher = PREAMBLE_PATTERN.matcher(movesBody);

        if (commentMatcher.find()) {
            StringScanner stringScanner = new StringScanner(commentMatcher.group());
            while (stringScanner.skipWhitespace() && stringScanner.peek() == '{') {
                result.add(stringScanner.extractSequence('{', '}'));
            }
        }

        return result;
    }

    /**
     * Does return a list of pgn moves, including variants and comments.
     *
     * @return List of moves
     */
    public List<PgnMove> extractMoves() {
        List<PgnMove> result = new ArrayList<>();

        String movesPart = "";

        // Extract moves part
        Matcher moveMatcher = Pattern.compile(
                new StringBuilder().append("(?:^\\s*(?:\\{[^}]*\\}\\s*)*)").append("(.*?)").append("(?:\\s*(?:\\*|1/2-1/2|1-0|0-1)?\\s*)?$").toString(), Pattern.DOTALL).matcher(this.movesBody);

        if (moveMatcher.find()) {
            movesPart = moveMatcher.group(1).trim();
        }

        StringScanner stringScanner = new StringScanner(movesPart);

        int startMoveNumber = getStartMoveNumberFromString(movesPart);
        Side startingSide = getNextMoveColor(stringScanner);

        parseSequence(stringScanner, result, startMoveNumber, startingSide);

        return result;
    }

    /**
     * Does parse a sequence of characters, does find moves, beginning of variants and comments on moves.
     *
     * @param stringScanner   current string scanner
     * @param moves           list of moves of the current (or parent) sequence
     * @param startMoveNumber current move number
     * @param startingSide    starting side to be expected next (can be overridden during parsing)
     * @return returns the game state found (eg. in variants)
     */
    private ChessGameState parseSequence(StringScanner stringScanner, List<PgnMove> moves, int startMoveNumber, Side startingSide) {
        ChessGameState sequenceResult = ChessGameState.GAME_OPEN;

        Side expectedSide = startingSide;
        int currentMoveNumber = startMoveNumber;

        while (stringScanner.skipWhitespace()) {
            char current = stringScanner.peek();

            if (current == ')') {
                stringScanner.advance();
                return sequenceResult;
            }

            if (current == '(') {
                handleVariation(stringScanner, moves.getLast());
            } else if (current == '{') {
                handleComment(stringScanner, moves.getLast());
            } else {
                sequenceResult = handleMoveOrGameState(stringScanner, moves, sequenceResult, expectedSide, currentMoveNumber);

                if (!moves.isEmpty()) {
                    PgnMove lastMove = moves.getLast();
                    currentMoveNumber = lastMove.getMoveNumber() + (lastMove.getSide() == Side.BLACK ? 1 : 0);
                    expectedSide = lastMove.getSide().invert();
                }
            }
        }
        return sequenceResult;
    }

    private void handleComment(StringScanner stringScanner, PgnMove parentMove) {
        String comment = stringScanner.extractSequence('{', '}');
        parentMove.getComments().add(comment);
    }

    private ChessGameState handleMoveOrGameState(StringScanner stringScanner, List<PgnMove> moves,
                                                 ChessGameState currentResult, Side expectedSide, int currentMoveNumber) {
        ChessGameState inlineResult = extractPossibleSequenceChessGameState(stringScanner);
        if (inlineResult != null) {
            return inlineResult;
        }

        String moveToken = extractMoveToken(stringScanner);
        PgnMove move = parseMoveToken(moveToken, currentMoveNumber, expectedSide);
        if (move != null) {
            moves.add(move);
        }

        return currentResult;
    }

    /**
     * Does handle the beginning of a variant recursively, may lead to further sequence parsing.
     */
    private void handleVariation(StringScanner stringScanner, PgnMove parentMove) {
        stringScanner.advance();
        stringScanner.skipWhitespace();
        String variationComment = stringScanner.peek() == '{' ? stringScanner.extractSequence('{', '}') : null;
        List<PgnMove> variationMoves = new ArrayList<>();
        ChessGameState variationResult = parseSequence(stringScanner, variationMoves, parentMove.getMoveNumber(), parentMove.getSide());

        PgnMoveVariation variation = new PgnMoveVariation(variationComment, variationResult);
        variation.setMoves(variationMoves);
        parentMove.getVariations().add(variation);
    }

    /**
     * Does extract a move token from the scanner
     */
    private String extractMoveToken(StringScanner stringScanner) {
        stringScanner.skipWhitespace();
        StringBuilder result = new StringBuilder();
        while (stringScanner.hasMore() && !Character.isWhitespace(stringScanner.peek()) && !"{()".contains(String.valueOf(stringScanner.peek()))) {
            result.append(stringScanner.peek());
            stringScanner.advance();
        }
        return result.toString();
    }

    /**
     * Parses and creates a move, does return null if no move has been found in token
     */
    private PgnMove parseMoveToken(String token, int expectedMoveNumber, Side expectedSide) {
        Matcher matcher = MOVE_TOKEN_PATTERN.matcher(token);
        if (!matcher.find()) {
            return null;
        }

        int actualMoveNumber =
                matcher.group(1) != null ? Integer.parseInt(matcher.group(1)) : expectedMoveNumber;

        Side side = matcher.group(2) != null && matcher.group(2).length() >= 3
                ? Side.BLACK
                : expectedSide;

        return new PgnMove(
                actualMoveNumber,
                matcher.group(3),
                matcher.group(4).isEmpty() ? null : matcher.group(4),
                side
        );
    }

    /**
     * Tries to find a possible game state in the scanner, advances in scanner if found
     */
    private ChessGameState extractPossibleSequenceChessGameState(StringScanner stringScanner) {
        stringScanner.skipWhitespace();
        stringScanner.save();

        StringBuilder token = new StringBuilder();
        while (stringScanner.hasMore() && !Character.isWhitespace(stringScanner.peek()) && !"{()".contains(String.valueOf(stringScanner.peek()))) {
            token.append(stringScanner.peek());
            stringScanner.advance();
        }

        ChessGameState result = ChessGameState.isSanGameState(token.toString());

        // Rollback if not found.
        if (result == null && !token.isEmpty()) {
            stringScanner.rollBack();
        }
        return result;
    }

    /**
     * Returns next move color in scanner, does not advance scanner
     */
    private Side getNextMoveColor(StringScanner stringScanner) {

        stringScanner.save();
        stringScanner.skipWhitespace();

        while (stringScanner.hasMore() && Character.isDigit(stringScanner.peek())) {
            stringScanner.advance();
        }

        int dotCount = 0;

        while (stringScanner.hasMore() && stringScanner.peek() == '.') {
            dotCount++;
            stringScanner.advance();
        }

        stringScanner.rollBack();

        return dotCount >= 3 ? Side.BLACK : Side.WHITE;
    }

    /**
     * Return next move number in String
     */
    private int getStartMoveNumberFromString(String moves) {
        Matcher matcher = Pattern.compile("^(\\d+)\\.{1,3}").matcher(moves.trim());
        return matcher.find() ? Integer.parseInt(matcher.group(1)) : 1;
    }

}
