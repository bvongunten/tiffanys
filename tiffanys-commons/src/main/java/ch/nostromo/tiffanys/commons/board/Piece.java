package ch.nostromo.tiffanys.commons.board;

import ch.nostromo.tiffanys.commons.ChessGameException;
import ch.nostromo.tiffanys.commons.Side;
import ch.nostromo.tiffanys.commons.move.Move;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

/**
 * Piece contains a Type, Side and character Code
 */
@Getter
@AllArgsConstructor
public enum Piece {

    //@formatter:off
    WHITE_KING(PieceType.KING, Side.WHITE, "K"),
    WHITE_QUEEN(PieceType.QUEEN, Side.WHITE, "Q"),
    WHITE_ROOK(PieceType.ROOK, Side.WHITE, "R"),
    WHITE_BISHOP(PieceType.BISHOP, Side.WHITE, "B"),
    WHITE_KNIGHT(PieceType.KNIGHT, Side.WHITE, "N"),
    WHITE_PAWN(PieceType.PAWN, Side.WHITE, "P"),

    BLACK_KING(PieceType.KING, Side.BLACK, "k"),
    BLACK_QUEEN(PieceType.QUEEN, Side.BLACK, "q"),
    BLACK_ROOK(PieceType.ROOK, Side.BLACK, "r"),
    BLACK_BISHOP(PieceType.BISHOP, Side.BLACK, "b"),
    BLACK_KNIGHT(PieceType.KNIGHT, Side.BLACK, "n"),
    BLACK_PAWN(PieceType.PAWN, Side.BLACK, "p");
    //@formatter:on

    private final PieceType pieceType;
    private final Side side;
    private final String charCode;

    /**
     * Does return a list of pseudo legal moves of this pieceType on a given board and board idx / field
     */
    public List<Move> getPseudoLegalMoves(Board board, int boardIdx) {
        return pieceType.getMoveGenerator().getPseudoLegalMoves(board, boardIdx, side);
    }

    /**
     * Does return a Piece for given character code
     */
    public static Piece getPieceByCharCode(String charCode) {

        for (Piece piece : Piece.values()) {
            if (piece.getCharCode().equals(charCode)) {
                return piece;
            }
        }

        throw new ChessGameException("Unknown piece char code: " + charCode);

    }

}
