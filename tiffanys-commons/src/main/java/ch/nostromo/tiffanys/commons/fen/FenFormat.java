package ch.nostromo.tiffanys.commons.fen;

import lombok.Getter;
import lombok.Setter;

import java.util.StringTokenizer;

@Getter
@Setter
public class FenFormat {

    public static final String INITIAL_BOARD = "rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR w KQkq - 0 1";

	
    protected String colorToMove;
    protected Integer halfMoveClock;
    protected Integer moveNr;
    protected String position;
    protected String castling;
    protected String enPassant;

    public FenFormat(String position, String castling, String enPassant) {
        this.position = position;
        this.castling = castling;
        this.enPassant = enPassant;
    }

    public FenFormat(String position, String colorToMove, String castling, String enPassant, Integer halfMoveClock,
            Integer moveNr) {
        this(position, castling, enPassant);
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

    public String generateFen() {
        return position + " " + colorToMove + " " + castling + " " + enPassant + " " + halfMoveClock + " " + moveNr;
    }

    @Override
    public String toString() {
        return generateFen();
    }

    public boolean equalsPosition(FenFormat otherFen) {
        return getPosition().equals(otherFen.getPosition());
    }

}
