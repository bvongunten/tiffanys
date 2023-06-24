package ch.nostromo.tiffanys.commons.format.pgn;


import ch.nostromo.tiffanys.commons.ChessGameState;
import ch.nostromo.tiffanys.commons.Side;
import ch.nostromo.tiffanys.commons.format.PgnFormat;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.util.Map;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class PgnWriter {

    public static String createPgn(PgnFormat pgnFormat) {

        StringBuilder resultString = new StringBuilder();

        // Export tags
        resultString.append(createTagLine("Event", pgnFormat.getChessGameInformation().getEvent()));
        resultString.append(createTagLine("Site", pgnFormat.getChessGameInformation().getSite()));
        resultString.append(createTagLine("Date", pgnFormat.getChessGameInformation().getDate()));
        resultString.append(createTagLine("Round", pgnFormat.getChessGameInformation().getRound()));
        resultString.append(createTagLine("White", pgnFormat.getChessGameInformation().getWhite()));
        resultString.append(createTagLine("Black", pgnFormat.getChessGameInformation().getBlack()));

        resultString.append(createTagLine("Result", pgnFormat.getChessGameInformation().getResult()));

        for (Map.Entry<String, String> entry : pgnFormat.getChessGameInformation().getOptionalTags().entrySet()) {
            resultString.append(createTagLine(entry.getKey(), entry.getValue()));
        }

        resultString.append("\n");

        // If we have parsed moves, reconstruct from them; otherwise use original pgnMoves
        resultString.append(reconstructMoves(pgnFormat));


        return resultString.toString();

    }

    /**
     * Does create a single tag line like [key "value"]
     */
    private static String createTagLine(String key, String value) {
        if (value == null) {
            return "";
        } else {
            return "[" + key + " \"" + value + "\"]\n";
        }

    }

    /**
     * Reconstructs the PGN moves string from the parsed move structure with line wrapping.
     */
    private static String reconstructMoves(PgnFormat pgnFormat) {
        StringBuilder movesString = new StringBuilder();

        // Add preamble comments if any
        if (pgnFormat.getChessGameInformation().getPreambleComments() != null && !pgnFormat.getChessGameInformation().getPreambleComments().isEmpty()) {
            for (String comment : pgnFormat.getChessGameInformation().getPreambleComments()) {
                movesString.append("{").append(comment).append("} ");
            }
        }

        MoveFormattingState state = new MoveFormattingState();

        for (int i = 0; i < pgnFormat.getMoves().size(); i++) {
            if (i > 0) {
                movesString.append(" ");
            }

            PgnMove move = pgnFormat.getMoves().get(i);
            movesString.append(formatMove(move, i, state));

            // Add variations if present
            if (move.getVariations() != null && !move.getVariations().isEmpty()) {
                for (PgnMoveVariation variation : move.getVariations()) {
                    movesString.append(" ").append(reconstructVariation(variation));
                }
                state.afterVariation = true;
            }
        }

        movesString.append(" ").append(pgnFormat.getChessGameInformation().getChessGameState().getGetSanResult());
        return movesString.toString();
    }



    /**
     * Reconstructs a variation string from a PgnVariation object.
     *
     * @param variation The variation to reconstruct
     */
    private static String reconstructVariation(PgnMoveVariation variation) {
        StringBuilder varSb = new StringBuilder();
        varSb.append("(");

        // Add variation comment if present
        if (variation.getComment() != null && !variation.getComment().isEmpty()) {
            varSb.append("{ ").append(variation.getComment()).append(" } ");
        }

        MoveFormattingState state = new MoveFormattingState();

        for (int i = 0; i < variation.getMoves().size(); i++) {
            if (i > 0) {
                varSb.append(" ");
            }

            PgnMove move = variation.getMoves().get(i);
            varSb.append(formatMove(move, i, state));

            // Recursively add nested variations
            if (move.getVariations() != null && !move.getVariations().isEmpty()) {
                for (PgnMoveVariation nestedVar : move.getVariations()) {
                    varSb.append(" ").append(reconstructVariation(nestedVar));
                }
                state.afterVariation = true;
            }
        }

        // Add variation result if present and meaningful
        if (variation.getResult() != ChessGameState.GAME_OPEN) {
            varSb.append(" ").append(variation.getResult().getGetSanResult());
        }

        varSb.append(")");
        return varSb.toString();
    }

    /**
     * Formats a single PGN move with its number, NAG, and comments.
     *
     * @param move  The move to format
     * @param index The index in the move list
     * @param state The formatting state tracking context
     * @return The formatted move string
     */
    private static String formatMove(PgnMove move, int index, MoveFormattingState state) {
        StringBuilder moveStr = new StringBuilder();

        // Add move number
        if (move.getSide().equals(Side.WHITE)) {
            moveStr.append(move.getMoveNumber()).append(". ");
            state.lastWasWhite = true;
            state.afterVariation = false;
            state.afterComment = false;
        } else {
            if (index == 0 || !state.lastWasWhite || state.afterVariation || state.afterComment) {
                moveStr.append(move.getMoveNumber()).append("... ");
            }
            state.lastWasWhite = false;
            state.afterVariation = false;
            state.afterComment = false;
        }

        // Add the move itself
        moveStr.append(move.getSanMove());

        // Add NAG if present
        if (move.getNag() != null && !move.getNag().isEmpty()) {
            moveStr.append(move.getNag());
        }

        // Add comments if present
        for (String comment : move.getComments()) {
            moveStr.append(" { ").append(comment).append(" }");
            state.afterComment = true;
        }


        return moveStr.toString();
    }

    /**
     * Helper class to track formatting state across moves.
     */
    private static class MoveFormattingState {
        boolean lastWasWhite = false;
        boolean afterVariation = false;
        boolean afterComment = false;
    }


}
