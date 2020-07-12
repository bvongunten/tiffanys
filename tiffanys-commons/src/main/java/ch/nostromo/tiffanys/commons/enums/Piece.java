package ch.nostromo.tiffanys.commons.enums;

import java.util.List;

import ch.nostromo.tiffanys.commons.board.Board;
import ch.nostromo.tiffanys.commons.move.Move;
import ch.nostromo.tiffanys.commons.pieces.AbstractPiece;
import ch.nostromo.tiffanys.commons.pieces.Bishop;
import ch.nostromo.tiffanys.commons.pieces.King;
import ch.nostromo.tiffanys.commons.pieces.Knight;
import ch.nostromo.tiffanys.commons.pieces.Pawn;
import ch.nostromo.tiffanys.commons.pieces.Queen;
import ch.nostromo.tiffanys.commons.pieces.Rook;

public enum Piece {

    //@formatter:off
    KING("K", new King()), 
    QUEEN("Q", new Queen()), 
    ROOK("R", new Rook()), 
    BISHOP("B", new Bishop()), 
    KNIGHT("N", new Knight()), 
    PAWN("P", new Pawn());
    //@formatter:on

    private AbstractPiece ref;
    private String charCode;

    private Piece(String charCode, AbstractPiece ref) {
        this.charCode = charCode;
        this.ref = ref;
    }

    public String getPieceCharCode() {
        return charCode;
    }

    public void addPseudoLegalMoves(Board board, List<Move> moves, int boardIdx, GameColor colorToMove) {
        ref.addPseudoLegalMoves(board, moves, boardIdx, colorToMove);
    }

    public static Piece getPieceByCharCode(String description) {

        if (description.equalsIgnoreCase(KING.getPieceCharCode())) {
            return Piece.KING;
        } else if (description.equalsIgnoreCase(QUEEN.getPieceCharCode())) {
            return Piece.QUEEN;
        } else if (description.equalsIgnoreCase(ROOK.getPieceCharCode())) {
            return Piece.ROOK;
        } else if (description.equalsIgnoreCase(BISHOP.getPieceCharCode())) {
            return Piece.BISHOP;
        } else if (description.equalsIgnoreCase(PAWN.getPieceCharCode())) {
            return Piece.PAWN;
        } else if (description.equalsIgnoreCase(KNIGHT.getPieceCharCode())) {
            return Piece.KNIGHT;
        }

        throw new IllegalArgumentException("Unknown piece description: " + description);

    }

    public static GameColor getPieceColorByCharCode(String description) {
        switch (description) {
            case "p":
            case "n":
            case "b":
            case "r":
            case "q":
            case "k": {
                return GameColor.BLACK;
            }
            case "P":
            case "N":
            case "B":
            case "R":
            case "Q":
            case "K": {
                return GameColor.WHITE;
            }
        }

        throw new IllegalArgumentException("Unknown piece description: " + description);
    }
}
