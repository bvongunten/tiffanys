package ch.nostromo.tiffanys.commons.move;

import ch.nostromo.tiffanys.commons.Side;
import ch.nostromo.tiffanys.commons.board.Square;
import lombok.Getter;

import static ch.nostromo.tiffanys.commons.board.Square.*;

/**
 * Castling moves including all affected squares
 */
@Getter
public enum Castling {

    // @formatter:off
    WHITE_LONG(Side.WHITE, E1, C1, A1, D1, new Square[] { B1, C1, D1 }, new Square[] { C1, D1 , E1 }, "O-O-O"),
    WHITE_SHORT(Side.WHITE, E1, G1, H1, F1, new Square[] { F1, G1 }, new Square[] { E1, F1, G1 }, "O-O"),
    BLACK_LONG(Side.BLACK, E8, C8, A8, D8, new Square[] { B8, C8, D8 }, new Square[] { C8, D8, E8 }, "O-O-O"),
    BLACK_SHORT(Side.BLACK, E8, G8, H8, F8, new Square[] { F8, G8 }, new Square[] { E8, F8, G8 }, "O-O");
    // @formatter:on

    private final Side side;
    private final Square fromKing;
    private final Square toKing;
    private final Square fromRook;
    private final Square toRook;
    private final Square[] mustBeEmpty;
    private final Square[] mustNotBeCheck;
    private final String annotation;

    Castling(Side side, Square fromKing, Square toKing, Square fromRook, Square toRook, Square[] mustBeEmpty, Square[] mustNotBeCheck, String annotation) {
        this.side = side;
        this.fromKing = fromKing;
        this.toKing = toKing;
        this.fromRook = fromRook;
        this.toRook = toRook;
        this.mustBeEmpty = mustBeEmpty;
        this.mustNotBeCheck = mustNotBeCheck;
        this.annotation = annotation;
    }


}
