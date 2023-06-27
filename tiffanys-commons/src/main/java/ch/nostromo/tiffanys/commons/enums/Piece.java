package ch.nostromo.tiffanys.commons.enums;

import ch.nostromo.tiffanys.commons.board.Board;
import ch.nostromo.tiffanys.commons.move.Move;
import ch.nostromo.tiffanys.commons.pieces.*;
import lombok.Getter;

import java.util.List;
@Getter
public enum Piece {

    //@formatter:off
    KING("K", new King()),
    QUEEN("Q", new Queen()),
    ROOK("R", new Rook()),
    BISHOP("B", new Bishop()),
    KNIGHT("N", new Knight()),
    PAWN("P", new Pawn());
    //@formatter:on

    private AbstractPiece ref;
    private String charCode;

    private Piece(String charCode, AbstractPiece ref) {
        this.charCode = charCode;
        this.ref = ref;
    }

    public void addPseudoLegalMoves(Board board, List<Move> moves, int boardIdx, GameColor colorToMove) {
        ref.addPseudoLegalMoves(board, moves, boardIdx, colorToMove);
    }

    public static Piece getPieceByCharCode(String description) {

        if (description.equalsIgnoreCase(KING.getCharCode())) {
            return Piece.KING;
        } else if (description.equalsIgnoreCase(QUEEN.getCharCode())) {
            return Piece.QUEEN;
        } else if (description.equalsIgnoreCase(ROOK.getCharCode())) {
            return Piece.ROOK;
        } else if (description.equalsIgnoreCase(BISHOP.getCharCode())) {
            return Piece.BISHOP;
        } else if (description.equalsIgnoreCase(PAWN.getCharCode())) {
            return Piece.PAWN;
        } else if (description.equalsIgnoreCase(KNIGHT.getCharCode())) {
            return Piece.KNIGHT;
        }

        throw new IllegalArgumentException("Unknown piece description: " + description);

    }

}
