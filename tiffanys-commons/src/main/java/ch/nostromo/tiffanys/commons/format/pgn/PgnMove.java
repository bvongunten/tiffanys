package ch.nostromo.tiffanys.commons.format.pgn;

import ch.nostromo.tiffanys.commons.Side;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

/**
 * PGN Move found during parsing
 */
@Data
public class PgnMove {
    private int moveNumber;
    private String sanMove;
    private String nag;
    private Side side;

    private List<String> comments = new ArrayList<>();
    private List<PgnMoveVariation> variations = new ArrayList<>();

    public PgnMove(int moveNumber, String sanMove, String nag, Side side) {
        this.moveNumber = moveNumber;
        this.sanMove = sanMove;
        this.nag = nag;
        this.side = side;
    }

}