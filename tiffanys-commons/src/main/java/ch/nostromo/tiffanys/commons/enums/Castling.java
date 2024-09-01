package ch.nostromo.tiffanys.commons.enums;

import ch.nostromo.tiffanys.commons.board.BoardCoordinates;
import lombok.Getter;

import static ch.nostromo.tiffanys.commons.board.BoardCoordinates.*;


@Getter
public enum Castling {

    // @formatter:off
    WHITE_LONG(GameColor.WHITE, E1, C1, A1, D1, new BoardCoordinates[] { B1, C1, D1 }, new BoardCoordinates[] { C1, D1 , E1 }, "O-O-O"),
    WHITE_SHORT(GameColor.WHITE, E1, G1, H1, F1, new BoardCoordinates[] { F1, G1 }, new BoardCoordinates[] { E1, F1, G1 }, "O-O"),
    BLACK_LONG(GameColor.BLACK, E8, C8, A8, D8, new BoardCoordinates[] { B8, C8, D8 }, new BoardCoordinates[] { C8, D8, E8 }, "O-O-O"),
    BLACK_SHORT(GameColor.BLACK, E8, G8, H8, F8, new BoardCoordinates[] { F8, G8 }, new BoardCoordinates[] { E8, F8, G8 }, "O-O");
    // @formatter:on

    private GameColor colorToMove;
    private final BoardCoordinates fromRook;
    private final BoardCoordinates toRook;
    private final BoardCoordinates fromKing;
    private final BoardCoordinates toKing;
    private final BoardCoordinates[] mustBeEmpty;
    private final BoardCoordinates[] mustNotBeCheck;
    private String annotation;

    private Castling(GameColor colorToMove, BoardCoordinates fromKing, BoardCoordinates toKing, BoardCoordinates fromRook, BoardCoordinates toRook, BoardCoordinates[] mustBeEmpty, BoardCoordinates[] mustNotBeCheck, String annotation) {
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
