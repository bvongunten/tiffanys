package ch.nostromo.tiffanys.commons.opening;

import ch.nostromo.tiffanys.commons.board.Board;
import ch.nostromo.tiffanys.commons.board.Piece;
import ch.nostromo.tiffanys.commons.board.Square;
import ch.nostromo.tiffanys.commons.format.FenFormat;
import ch.nostromo.tiffanys.commons.move.Castling;
import ch.nostromo.tiffanys.commons.move.Move;
import lombok.Data;

/**
 * Raw opening book move, provides a Tiffany's Move object conversion.
 */
@Data
public class OpeningBookMove implements Comparable<OpeningBookMove> {

    private short move;
    private int weight;

    private Square from;
    private Square to;
    private Piece promotionPiece;

    public OpeningBookMove(short move, int weight, int fromRank, int fromFile, int toRank, int toFile, int promotionPiece) {
        this.move = move;
        this.weight = weight;

        this.from =  getSquareByRankAndFile(fromRank, fromFile);
        this.to = getSquareByRankAndFile(toRank, toFile);

        this.promotionPiece = getPromotionPieceByRankAndType(toRank, promotionPiece);


    }


    private Square getSquareByRankAndFile(int rank, int file) {
        int tiffRank = rank * 10 + 20;
        int tiffFile = file + 1;

        return Square.byBoardIdx(tiffRank + tiffFile);
    }



    public Move getMove(FenFormat fenFormat) {

        Move result = new Move();
        result.setFrom(getFrom());
        result.setTo(getTo());
        result.setPromotion(getPromotionPiece());


        // Castling? Abort ;)
        Board board = new Board(fenFormat);
        Castling castling = isCastling(board, result);
        if (castling != null) {
            result = new Move(castling);
        }


        return result;
    }


    private Castling isCastling(Board board, Move move) {

        if (board.isCastlingAllowed(Castling.WHITE_LONG) && move.getFrom().equals(Square.E1) && move.getTo().equals(Square.A1)) {
            return Castling.WHITE_LONG;
        }

        if (board.isCastlingAllowed(Castling.WHITE_SHORT) && move.getFrom().equals(Square.E1) && move.getTo().equals(Square.H1)) {
            return Castling.WHITE_SHORT;
        }

        if (board.isCastlingAllowed(Castling.BLACK_LONG) && move.getFrom().equals(Square.E8) && move.getTo().equals(Square.A8)) {
            return Castling.BLACK_LONG;
        }

        if (board.isCastlingAllowed(Castling.BLACK_SHORT) && move.getFrom().equals(Square.E8) && move.getTo().equals(Square.H8)) {
            return Castling.BLACK_SHORT;
        }

        return null;

    }

    private Piece getPromotionPieceByRankAndType(int toRank, int pieceType) {
        if (pieceType == 0) {
            return null;
        }

        if (toRank > 0) {
            return switch (pieceType) {
                case 1 -> Piece.WHITE_KNIGHT;
                case 2 -> Piece.WHITE_BISHOP;
                case 3 -> Piece.WHITE_ROOK;
                case 4 -> Piece.WHITE_QUEEN;
                default -> throw new IllegalArgumentException("Unknown polyglot piece type: " + pieceType);
            };
        } else {
            return switch (pieceType) {
                case 1 -> Piece.BLACK_KNIGHT;
                case 2 -> Piece.BLACK_BISHOP;
                case 3 -> Piece.BLACK_ROOK;
                case 4 -> Piece.BLACK_QUEEN;
                default -> throw new IllegalArgumentException("Unknown polyglot piece type: " + pieceType);
            };
        }
    }

    @Override
    public int compareTo(OpeningBookMove other) {
        return other.weight - this.weight;
    }


}
