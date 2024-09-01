package ch.nostromo.tiffanys.commons.move;

import ch.nostromo.tiffanys.commons.board.BoardCoordinates;
import ch.nostromo.tiffanys.commons.enums.Castling;
import ch.nostromo.tiffanys.commons.enums.Piece;
import lombok.Data;

@Data
public class Move {

    private BoardCoordinates from = null;
    private BoardCoordinates to = null;

    private Piece promotion = null;
    private Castling castling = null;

    MoveAttributes moveAttributes = null;

    public Move(BoardCoordinates from, BoardCoordinates to) {
        this.from = from;
        this.to = to;
    }

    public Move(BoardCoordinates from, BoardCoordinates to, Piece promotion) {
        this(from, to);
        this.promotion = promotion;
    }

    public Move(Castling castling) {
        this.castling = castling;
    }

    public boolean isPromotion() {
        return promotion != null;
    }

    public boolean isCastling() {
        return castling != null;
    }

    @Override
    public String toString() {
        String result = "Move [" + MoveTranslator.moveToString(this) + "]";

        if (this.moveAttributes != null) {
            result += " " + moveAttributes.toString();
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
        if (to != other.to)
            return false;
        return true;
    }

}
