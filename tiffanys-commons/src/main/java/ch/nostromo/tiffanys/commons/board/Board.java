package ch.nostromo.tiffanys.commons.board;

import ch.nostromo.tiffanys.commons.enums.Coordinates;
import ch.nostromo.tiffanys.commons.enums.Castling;
import ch.nostromo.tiffanys.commons.enums.FieldType;
import ch.nostromo.tiffanys.commons.enums.GameColor;
import ch.nostromo.tiffanys.commons.enums.Piece;
import ch.nostromo.tiffanys.commons.fen.FenFormat;
import ch.nostromo.tiffanys.commons.move.Move;
import lombok.Data;

import java.util.StringTokenizer;
import java.util.logging.Logger;

@Data
public class Board implements Cloneable {

    Logger logger = Logger.getLogger(getClass().getName());

    BoardField[] boardFields = new BoardField[120];

    Coordinates enPassantField;

    boolean castlingWhiteShortAllowed;
    boolean castlingBlackShortAllowed;
    boolean castlingWhiteLongAllowed;
    boolean castlingBlackLongAllowed;

    public Board() {
        this(FenFormat.INITIAL_FEN);
    }

    public Board(FenFormat fenFormat) {

        logger.fine("Creating board for fen: " + fenFormat.toString());

        initializeFields();
        initializeBoard(fenFormat);

        logger.fine("Board created: \n" + this.toString());
    }

    private void initializeFields() {
        // Reset
        for (int i = 0; i < boardFields.length; i++) {
            boardFields[i] = new BoardField(FieldType.VOID);
        }

        // Add legal fields
        for (Coordinates coordinates : Coordinates.values()) {
            boardFields[coordinates.getIdx()] = new BoardField(FieldType.LEGAL);
        }
    }

    private void initializeBoard(FenFormat fenFormat) {

        // Set position
        String position = fenFormat.getPosition();

        StringTokenizer positionTokenizer = new StringTokenizer(position, "/");

        int currLineIdx = 9;
        while (positionTokenizer.hasMoreTokens()) {
            String line = positionTokenizer.nextToken();
            int fieldIdx = 1;
            for (int i = 0; i < line.length(); i++) {
                char c = line.charAt(i);
                if (Character.isDigit(c)) {
                    fieldIdx += Integer.parseInt(line.substring(i, i + 1));
                } else {

                    int toBePlacedId = currLineIdx * 10 + fieldIdx;

                    Piece piece = Piece.getPieceByCharCode(Character.toString(c));
                    GameColor color = Character.isUpperCase(c) ? GameColor.WHITE : GameColor.BLACK;

                    setPieceAndColor(toBePlacedId, piece, color);

                    fieldIdx++;
                }
            }
            currLineIdx--;

        }

        // Castling
        String castling = fenFormat.getCastling();
        this.castlingWhiteLongAllowed = castling.indexOf("Q") >= 0;
        this.castlingWhiteShortAllowed = castling.indexOf("K") >= 0;
        this.castlingBlackLongAllowed = castling.indexOf("q") >= 0;
        this.castlingBlackShortAllowed = castling.indexOf("k") >= 0;

        // Ep field
        String epField = fenFormat.getEnPassant();
        if (!epField.equals("-")) {
            this.enPassantField = Coordinates.byIdx(BoardUtil.coordToField(epField));
        } else {
            this.enPassantField = null;
        }

    }


    public String getFenPosition() {
        String position = "";
        for (int n = 9; n >= 2; n--) {
            String line = "";
            int sinceLast = 0;
            for (int i = n * 10 + 1; i <= n * 10 + 8; i++) {
                if (boardFields[i].getPiece() == null) {
                    sinceLast++;
                } else {
                    if (sinceLast > 0) {
                        line += sinceLast;
                        sinceLast = 0;
                    }
                    if (boardFields[i].getPieceColor() == GameColor.WHITE) {
                        line += boardFields[i].getPiece().getCharCode().toUpperCase();
                    } else {
                        line += boardFields[i].getPiece().getCharCode().toLowerCase();
                    }
                }
            }
            if (sinceLast > 0) {
                line += sinceLast;
            }
            position += line;
            if (n > 2) {
                position += "/";
            }
        }

        return position;
    }

    public String getFenCastling() {
        String castling = "";
        if (!castlingWhiteLongAllowed && !castlingWhiteShortAllowed && !castlingBlackLongAllowed
                && !castlingBlackShortAllowed) {
            castling = "-";
        } else {
            if (castlingWhiteLongAllowed) {
                castling += "K";
            }
            if (castlingWhiteShortAllowed) {
                castling += "Q";
            }
            if (castlingBlackLongAllowed) {
                castling += "k";
            }
            if (castlingBlackShortAllowed) {
                castling += "q";
            }
        }

        return castling;
    }

    public String getFenEnpassant() {
        String enPassant = "";
        if (enPassantField != null) {
            enPassant = enPassantField.nameLowerCase();
        } else {
            enPassant = "-";
        }

        return enPassant;
    }


    public int getPieceCount() {
        int pieceCount = 0;
        for (BoardField boardField : boardFields) {
            if (boardField.getPiece() != null) {
                pieceCount++;
            }
        }
        return pieceCount;
    }


    public boolean isCastlingAllowed(Castling castling) {
        switch (castling) {
            case WHITE_LONG:
                return castlingWhiteLongAllowed;
            case WHITE_SHORT:
                return castlingWhiteShortAllowed;
            case BLACK_LONG:
                return castlingBlackLongAllowed;
            case BLACK_SHORT:
                return castlingBlackShortAllowed;
        }

        throw new IllegalArgumentException("Unknown Castling enum val: " + castling);

    }

    public void setCastlingAllowed(Castling castling, boolean flag) {
        switch (castling) {
            case WHITE_LONG: {
                castlingWhiteLongAllowed = flag;
                return;
            }
            case WHITE_SHORT: {
                castlingWhiteShortAllowed = flag;
                return;
            }
            case BLACK_LONG: {
                castlingBlackLongAllowed = flag;
                return;
            }
            case BLACK_SHORT: {
                castlingBlackShortAllowed = flag;
                return;
            }
        }

        throw new IllegalArgumentException("Unknown Castling enum val: " + castling);

    }

    public GameColor getPieceColor(int idx) {
        if (boardFields[idx].getPiece() == null) {
            throw new IllegalArgumentException("No piece at idx: " + idx);
        }
        return boardFields[idx].getPieceColor();
    }

    public boolean containsPiece(int idx) {
        return boardFields[idx].getPiece() != null;
    }

    public boolean isVoid(int idx) {
        return boardFields[idx].getType() == FieldType.VOID;
    }

    public boolean isPieceAndColor(int idx, Piece piece, GameColor color) {
        return boardFields[idx].getPieceColor() == color && boardFields[idx].getPiece() == piece;
    }

    private void setPieceAndColor(int idx, Piece piece, GameColor color) {
        boardFields[idx].setPiece(piece);
        boardFields[idx].setPieceColor(color);
    }

    private void movePiece(int from, int to) {
        boardFields[to].setPiece(boardFields[from].getPiece());
        boardFields[to].setPieceColor(boardFields[from].getPieceColor());

        boardFields[from].setPiece(null);
        boardFields[from].setPieceColor(null);
    }

    public void applyMove(Move move, GameColor colorToMove) {

        // logger.finer("Applying move: " + move.toString());

        // 7 this.enPassantField = Integer.MIN_VALUE;

        if (move.getPromotion() != null) {
            // promotion

            setPieceAndColor(move.getTo().getIdx(), move.getPromotion(), colorToMove);
            boardFields[move.getFrom().getIdx()].setPiece(null);
            boardFields[move.getFrom().getIdx()].setPieceColor(null);

            this.enPassantField = null;

        } else if (move.getCastling() != null) {
            // castling

            switch (move.getCastling()) {
                case WHITE_SHORT:
                case WHITE_LONG: {
                    castlingWhiteLongAllowed = false;
                    castlingWhiteShortAllowed = false;
                    break;
                }
                case BLACK_SHORT:
                case BLACK_LONG: {
                    castlingBlackLongAllowed = false;
                    castlingBlackShortAllowed = false;
                    break;
                }
            }

            movePiece(move.getCastling().getFromRook().getIdx(), move.getCastling().getToRook().getIdx());
            movePiece(move.getCastling().getFromKing().getIdx(), move.getCastling().getToKing().getIdx());

            this.enPassantField = null;

        } else if (boardFields[move.getFrom().getIdx()].getPiece() == Piece.PAWN && enPassantField != null && move.getTo().getIdx() == enPassantField.getIdx()) {
            // en passant

            movePiece(move.getFrom().getIdx(), move.getTo().getIdx());
            int epIdx = move.getTo().getIdx() + (colorToMove.getCalculationModificator() * 10) * -1;
            boardFields[epIdx].setPiece(null);
            boardFields[epIdx].setPieceColor(null);

            this.enPassantField = null;

        } else {

            // Castling destroyed by movement of Rook / King or movement on Rook
            // field (iE rook hit)
            if (move.getTo().getIdx() == Castling.WHITE_LONG.getFromRook().getIdx() || move.getFrom().getIdx() == Castling.WHITE_LONG.getFromRook().getIdx()
                    || move.getFrom().getIdx() == Castling.WHITE_LONG.getFromKing().getIdx()) {
                castlingWhiteLongAllowed = false;
            }

            if (move.getTo().getIdx() == Castling.WHITE_SHORT.getFromRook().getIdx()
                    || move.getFrom().getIdx() == Castling.WHITE_SHORT.getFromRook().getIdx()
                    || move.getFrom().getIdx() == Castling.WHITE_SHORT.getFromKing().getIdx()) {
                castlingWhiteShortAllowed = false;
            }

            if (move.getTo().getIdx() == Castling.BLACK_LONG.getFromRook().getIdx() || move.getFrom().getIdx() == Castling.BLACK_LONG.getFromRook().getIdx()
                    || move.getFrom().getIdx() == Castling.BLACK_LONG.getFromKing().getIdx()) {
                castlingBlackLongAllowed = false;
            }

            if (move.getTo().getIdx() == Castling.BLACK_SHORT.getFromRook().getIdx()
                    || move.getFrom().getIdx() == Castling.BLACK_SHORT.getFromRook().getIdx()
                    || move.getFrom().getIdx() == Castling.BLACK_SHORT.getFromKing().getIdx()) {
                castlingBlackShortAllowed = false;
            }

            // En Passant field to be set?
            if (boardFields[move.getFrom().getIdx()].getPiece() == Piece.PAWN && Math.abs(move.getFrom().getIdx() - move.getTo().getIdx()) == 20) {
                this.enPassantField = Coordinates.byIdx(move.getTo().getIdx() + (colorToMove.getCalculationModificator() * 10) * -1);
            } else {
                this.enPassantField = null;
            }

            // Move piece
            movePiece(move.getFrom().getIdx(), move.getTo().getIdx());

        }

    }

    @Override
    public Board clone() {
        Board clone;
        try {
            clone = (Board) super.clone();
        } catch (CloneNotSupportedException e) {
            throw new IllegalStateException("Unable to clone board", e);
        }

        // Clone fields
        clone.boardFields = new BoardField[boardFields.length];

        for (int i = 0; i < boardFields.length; i++) {
            clone.boardFields[i] = boardFields[i].clone();
        }

        return clone;
    }

    @Override
    public String toString() {
        return BoardUtil.dumpBoard(this);
    }

}
