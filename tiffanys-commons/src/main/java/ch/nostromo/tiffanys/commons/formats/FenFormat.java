package ch.nostromo.tiffanys.commons.formats;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.StringTokenizer;

/**
 * FEN splits and contains all fen parameters
 */
@Data
@EqualsAndHashCode
public class FenFormat {

    public static final FenFormat INITIAL_FEN = new FenFormat("rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR w KQkq - 0 1");

    protected String position;
    protected String sideToMove;
    protected String castling;
    protected String enPassant;
    protected Integer halfMoveClock;
    protected Integer moveNr;

    public FenFormat(String position, String sideToMove, String castling, String enPassant, Integer halfMoveClock, Integer moveNr) {
        this.position = position;
        this.castling = castling;
        this.enPassant = enPassant;
        this.sideToMove = sideToMove;
        this.halfMoveClock = halfMoveClock;
        this.moveNr = moveNr;
    }

    public FenFormat(String fen) {
        StringTokenizer fenTokenizer = new StringTokenizer(fen, " ");
        position = fenTokenizer.nextToken();
        sideToMove = fenTokenizer.nextToken();
        castling = fenTokenizer.nextToken();
        enPassant = fenTokenizer.nextToken();
        halfMoveClock = Integer.valueOf(fenTokenizer.nextToken());
        moveNr = Integer.valueOf(fenTokenizer.nextToken());
    }

    @Override
    public String toString() {
        return position + " " + sideToMove + " " + castling + " " + enPassant + " " + halfMoveClock + " " + moveNr;
    }

}
