package ch.nostromo.tiffanys.commons.uci.utils;

import ch.nostromo.tiffanys.commons.Side;
import ch.nostromo.tiffanys.commons.board.Board;
import ch.nostromo.tiffanys.commons.board.Piece;
import ch.nostromo.tiffanys.commons.board.Square;
import ch.nostromo.tiffanys.commons.move.Castling;
import ch.nostromo.tiffanys.commons.move.Move;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

/**
 * Helper Class to translate moves between Default Move Objects and the uci protocol
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class UciMoveTranslator {


    /**
     * Translate a move to uci protocol. Supports castling and promotions
     */
    public static String moveToUci(Move move) {

        String result;
        if (move.getCastling() != null) {
            result = move.getCastling().getFromKing().getLowerCaseName();
            result += move.getCastling().getToKing().getLowerCaseName();
        } else {
            result = move.getFrom().getLowerCaseName();
            result += move.getTo().getLowerCaseName();
            if (move.isPromotion()) {
                result += move.getPromotion().getCharCode().toLowerCase();
            }
        }

        return result;
    }


    /**
     * Translate a uci protocol move to a move object. Board is provided for castling checks and promotion side.
     */
    public static Move uciToMove(String move, Board board) {
        String froms = move.substring(0, 2);
        String tos = move.substring(2, 4);

        Square from = Square.byName(froms);
        Square to = Square.byName(tos);


        Move toMove;
        if (move.length() > 4) {
            Piece pieceCode;

            // UCI is always lower case
            if (board.getPiece(from).getSide().equals(Side.WHITE)) {
                pieceCode = Piece.getPieceByCharCode(move.substring(4).toUpperCase());
            } else {
                pieceCode = Piece.getPieceByCharCode(move.substring(4).toLowerCase());
            }

            toMove = new Move(from, to, pieceCode);
        } else {
            toMove = new Move(from, to);
        }

        Castling castling = isUciCastling(board, toMove);
        if (castling != null) {
            toMove = new Move(castling);
        }

        return toMove;
    }

    /**
     * Check and translate castling when possible, returns null if it's not a valid castling move.
     */
    private static Castling isUciCastling(Board board, Move move) {

        if (board.isCastlingAllowed(Castling.WHITE_LONG) && move.getFrom().getBoardIdx() == Castling.WHITE_LONG.getFromKing().getBoardIdx() && move.getTo().getBoardIdx() == Castling.WHITE_LONG.getToKing().getBoardIdx()) {
            return Castling.WHITE_LONG;
        }

        if (board.isCastlingAllowed(Castling.WHITE_SHORT) && move.getFrom().getBoardIdx() == Castling.WHITE_SHORT.getFromKing().getBoardIdx() && move.getTo().getBoardIdx() == Castling.WHITE_SHORT.getToKing().getBoardIdx()) {
            return Castling.WHITE_SHORT;
        }

        if (board.isCastlingAllowed(Castling.BLACK_LONG) && move.getFrom().getBoardIdx() == Castling.BLACK_LONG.getFromKing().getBoardIdx() && move.getTo().getBoardIdx() == Castling.BLACK_LONG.getToKing().getBoardIdx()) {
            return Castling.BLACK_LONG;
        }

        if (board.isCastlingAllowed(Castling.BLACK_SHORT) && move.getFrom().getBoardIdx() == Castling.BLACK_SHORT.getFromKing().getBoardIdx() && move.getTo().getBoardIdx() == Castling.BLACK_SHORT.getToKing().getBoardIdx()) {
            return Castling.BLACK_SHORT;
        }

        return null;

    }

}
