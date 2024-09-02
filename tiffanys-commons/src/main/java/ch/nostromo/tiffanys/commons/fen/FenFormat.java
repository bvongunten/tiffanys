package ch.nostromo.tiffanys.commons.fen;

import lombok.Data;

import java.util.Objects;
import java.util.StringTokenizer;

@Data
public class FenFormat {

    public static final FenFormat INITIAL_FEN = new FenFormat("rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR w KQkq - 0 1");

    protected String position;
    protected String colorToMove;
    protected String castling;
    protected String enPassant;
    protected Integer halfMoveClock;
    protected Integer moveNr;

    public FenFormat(String position, String colorToMove, String castling, String enPassant, Integer halfMoveClock, Integer moveNr) {
        this.position = position;
        this.castling = castling;
        this.enPassant = enPassant;
        this.colorToMove = colorToMove;
        this.halfMoveClock = halfMoveClock;
        this.moveNr = moveNr;
    }

    public FenFormat(String fen) {
        StringTokenizer fenTokenizer = new StringTokenizer(fen, " ");
        position = fenTokenizer.nextToken();
        colorToMove = fenTokenizer.nextToken();
        castling = fenTokenizer.nextToken();
        enPassant = fenTokenizer.nextToken();
        halfMoveClock = Integer.valueOf(fenTokenizer.nextToken());
        moveNr = Integer.valueOf(fenTokenizer.nextToken());
    }

    @Override
    public String toString() {
        return position + " " + colorToMove + " " + castling + " " + enPassant + " " + halfMoveClock + " " + moveNr;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        FenFormat fenFormat = (FenFormat) o;

        if (!Objects.equals(position, fenFormat.position)) return false;
        if (!Objects.equals(colorToMove, fenFormat.colorToMove))
            return false;
        if (!Objects.equals(castling, fenFormat.castling)) return false;
        if (!Objects.equals(enPassant, fenFormat.enPassant)) return false;
        if (!Objects.equals(halfMoveClock, fenFormat.halfMoveClock))
            return false;
        return Objects.equals(moveNr, fenFormat.moveNr);
    }

    @Override
    public int hashCode() {
        int result = position != null ? position.hashCode() : 0;
        result = 31 * result + (colorToMove != null ? colorToMove.hashCode() : 0);
        result = 31 * result + (castling != null ? castling.hashCode() : 0);
        result = 31 * result + (enPassant != null ? enPassant.hashCode() : 0);
        result = 31 * result + (halfMoveClock != null ? halfMoveClock.hashCode() : 0);
        result = 31 * result + (moveNr != null ? moveNr.hashCode() : 0);
        return result;
    }
}
