package ch.nostromo.tiffanys.commons.format.pgn;

import ch.nostromo.tiffanys.commons.ChessGameState;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

/**
 * Pgn move variation found during parsing
 */
@Data
public class PgnMoveVariation {
    private String comment;
    private ChessGameState result;

    private List<PgnMove> moves = new ArrayList<>();

    public PgnMoveVariation(String comment, ChessGameState result) {
        this.comment = comment;
        this.result = result;
    }


}
