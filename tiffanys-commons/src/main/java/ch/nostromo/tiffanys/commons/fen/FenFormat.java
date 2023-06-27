package ch.nostromo.tiffanys.commons.fen;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.util.StringTokenizer;

@Data
public class FenFormat {

    public static final String INITIAL_BOARD = "rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR w KQkq - 0 1";

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


}
