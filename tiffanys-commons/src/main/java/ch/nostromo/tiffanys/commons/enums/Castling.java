package ch.nostromo.tiffanys.commons.enums;

import lombok.Getter;

import static ch.nostromo.tiffanys.commons.enums.Coordinates.*;


@Getter
public enum Castling {

    // @formatter:off
    WHITE_LONG(GameColor.WHITE, E1, C1, A1, D1, new Coordinates[] { B1, C1, D1 }, new Coordinates[] { C1, D1 , E1 }, "O-O-O"),
    WHITE_SHORT(GameColor.WHITE, E1, G1, H1, F1, new Coordinates[] { F1, G1 }, new Coordinates[] { E1, F1, G1 }, "O-O"),
    BLACK_LONG(GameColor.BLACK, E8, C8, A8, D8, new Coordinates[] { B8, C8, D8 }, new Coordinates[] { C8, D8, E8 }, "O-O-O"),
    BLACK_SHORT(GameColor.BLACK, E8, G8, H8, F8, new Coordinates[] { F8, G8 }, new Coordinates[] { E8, F8, G8 }, "O-O");
    // @formatter:on

    private GameColor colorToMove;
    private final Coordinates fromKing;
    private final Coordinates toKing;
    private final Coordinates fromRook;
    private final Coordinates toRook;
    private final Coordinates[] mustBeEmpty;
    private final Coordinates[] mustNotBeCheck;
    private String annotation;

    private Castling(GameColor colorToMove, Coordinates fromKing, Coordinates toKing, Coordinates fromRook, Coordinates toRook, Coordinates[] mustBeEmpty, Coordinates[] mustNotBeCheck, String annotation) {
        this.colorToMove = colorToMove;
        this.fromKing = fromKing;
        this.toKing = toKing;
        this.fromRook = fromRook;
        this.toRook = toRook;
        this.mustBeEmpty = mustBeEmpty;
        this.mustNotBeCheck = mustNotBeCheck;
        this.annotation = annotation;
    }


}
