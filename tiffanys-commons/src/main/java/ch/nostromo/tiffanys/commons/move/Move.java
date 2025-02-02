package ch.nostromo.tiffanys.commons.move;

import ch.nostromo.tiffanys.commons.board.Piece;
import ch.nostromo.tiffanys.commons.board.Square;
import lombok.Data;

/**
 * Move class
 */
@Data
public class Move {

    // From square
    private Square from = null;

    // To square
    private Square to = null;

    // Possible Promotion piece
    private Piece promotion = null;

    // Possible Castling
    private Castling castling = null;

    // Engine attributes
    MoveAttributes moveAttributes = null;

    /**
     * Regular move constructor
     */
    public Move(Square from, Square to) {
        this.from = from;
        this.to = to;
    }

    /**
     * Pawn promotion move
     */
    public Move(Square from, Square to, Piece promotion) {
        this(from, to);
        this.promotion = promotion;
    }

    /**
     * Castling move
     */
    public Move(Castling castling) {
        this.castling = castling;
    }

    /**
     * Returns true if it is a promotion move
     */
    public boolean isPromotion() {
        return promotion != null;
    }

    /**
     * Returns true if it is a castling move
     */
    public boolean isCastling() {
        return castling != null;
    }

    /**
     * Returns a simple move description (generateMoveDetailString) and move attributes, if present
     */
    @Override
    public String toString() {
        String result = "Move [" + generateMoveDetailString() + "]";

        if (this.moveAttributes != null) {
            result += " " + moveAttributes;
        }

        return result;
    }


    /**
     * Returns a move simple description
     */
    public String generateMoveDetailString() {

        String result;
        if (Castling.WHITE_LONG.equals(getCastling()) || Castling.BLACK_LONG.equals(getCastling())) {
            result = "O-O-O";
        } else if (Castling.WHITE_SHORT.equals(getCastling()) || Castling.BLACK_SHORT.equals(getCastling())) {
            result = "O-O";
        } else {
            result = getFrom().getLowerCaseName();
            result += "-";
            result += getTo().getLowerCaseName();
            if (isPromotion()) {
                result += getPromotion().getCharCode();
            }
        }

        return result;
    }


    @Override
    public int hashCode() {
        int result = from != null ? from.hashCode() : 0;
        result = 31 * result + (to != null ? to.hashCode() : 0);
        result = 31 * result + (promotion != null ? promotion.hashCode() : 0);
        result = 31 * result + (castling != null ? castling.hashCode() : 0);
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        Move other = (Move) obj;
        if (castling != other.castling)
            return false;
        if (from != other.from)
            return false;
        if (promotion != other.promotion)
            return false;
        return to == other.to;
    }

}
