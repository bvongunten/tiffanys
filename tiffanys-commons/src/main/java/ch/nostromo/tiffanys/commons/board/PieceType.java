package ch.nostromo.tiffanys.commons.board;

import ch.nostromo.tiffanys.commons.move.movegenerator.*;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * PieceType contains a specific move generator reference
 */
@Getter
@AllArgsConstructor
public enum PieceType {

    //@formatter:off
    KING(new KingMoveGenerator()),
    QUEEN(new QueenMoveGenerator()),
    ROOK(new RookMoveGenerator()),
    BISHOP(new BishopMoveGenerator()),
    KNIGHT(new KnightMoveGenerator()),
    PAWN(new PawnMoveGenerator());
    //@formatter:on

    private final AbstractMoveGenerator moveGenerator;


}
