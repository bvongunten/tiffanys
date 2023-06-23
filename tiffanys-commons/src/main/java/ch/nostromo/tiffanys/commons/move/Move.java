package ch.nostromo.tiffanys.commons.move;

import ch.nostromo.tiffanys.commons.board.BoardUtil;
import ch.nostromo.tiffanys.commons.enums.Castling;
import ch.nostromo.tiffanys.commons.enums.Piece;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Move {

    private int from;
    private int to;

    private Piece promotion = null;
    private Castling castling = null;

    MoveAttributes moveAttributes = null;

    public Move(int from, int to) {
        this.from = from;
        this.to = to;
    }

    public Move(int from, int to, Piece promotion) {
        this(from, to);
        this.promotion = promotion;
    }

    public Move(String from, String to) {
        this(BoardUtil.coordToField(from), BoardUtil.coordToField(to));
    }

    public Move(String from, String to, Piece promotion) {
        this(BoardUtil.coordToField(from), BoardUtil.coordToField(to));
        this.promotion = promotion;
    }

    public Move(Castling castling) {
        this.castling = castling;
    }

    public String getFromCoord() {
        return BoardUtil.fieldToCoord(from);
    }

    public String getToCoord() {
        return BoardUtil.fieldToCoord(to);
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
        final int prime = 31;
        int result = 1;
        result = prime * result + ((castling == null) ? 0 : castling.hashCode());
        result = prime * result + from;
        result = prime * result + ((promotion == null) ? 0 : promotion.hashCode());
        result = prime * result + to;
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
