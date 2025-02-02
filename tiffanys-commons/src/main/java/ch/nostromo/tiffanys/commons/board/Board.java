package ch.nostromo.tiffanys.commons.board;

import ch.nostromo.tiffanys.commons.ChessGameException;
import ch.nostromo.tiffanys.commons.Side;
import ch.nostromo.tiffanys.commons.formats.FenFormat;
import ch.nostromo.tiffanys.commons.move.Castling;
import ch.nostromo.tiffanys.commons.move.Move;
import ch.nostromo.tiffanys.commons.move.movegenerator.KingMoveGenerator;
import lombok.Data;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.StringTokenizer;
import java.util.logging.Logger;

/**
 * Board can be constructed by a fen. It handles squares, enPassant field and castling rights but is independent of
 * the current side to play. Provides helper methods.
 */
@Data
public class Board implements Cloneable {

    Logger logger = Logger.getLogger(getClass().getName());

    // Board Squares (null are border fields for move genration
    BoardSquare[] boardSquares = new BoardSquare[120];

    // Current en passant field
    Square enPassantField;

    // Legal / remaining castle moves
    boolean castlingWhiteShortAllowed;
    boolean castlingBlackShortAllowed;
    boolean castlingWhiteLongAllowed;
    boolean castlingBlackLongAllowed;

    /**
     * Create a standard chess board
     */
    public Board() {
        this(FenFormat.INITIAL_FEN);
    }

    /**
     * Create a chess board by given fen
     */
    public Board(FenFormat fenFormat) {
        logger.fine("Creating board for fen: " + fenFormat.toString());

        initializeBoard(fenFormat);

        logger.fine("Board created: \n" + this);
    }

    // ****************************************************************************************************************************************
    // *** FEN
    // ****************************************************************************************************************************************


    /**
     * Initialize board by fen, including castling flags and en passant field
     */
    private void initializeBoard(FenFormat fenFormat) {
        // Reset
        Arrays.fill(boardSquares, null);

        // Add legal fields
        for (Square square : Square.values()) {
            boardSquares[square.getBoardIdx()] = new BoardSquare();
        }

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
                    setPiece(toBePlacedId, piece);

                    fieldIdx++;
                }
            }
            currLineIdx--;

        }

        // Castling
        String castling = fenFormat.getCastling();
        this.castlingWhiteLongAllowed = castling.contains("Q");
        this.castlingWhiteShortAllowed = castling.contains("K");
        this.castlingBlackLongAllowed = castling.contains("q");
        this.castlingBlackShortAllowed = castling.contains("k");

        // Ep field
        String epField = fenFormat.getEnPassant();
        if (!epField.equals("-")) {
            this.enPassantField = Square.byName(epField);
        } else {
            this.enPassantField = null;
        }

    }

    /**
     * Returns partial fen string - position
     */
    public String getFenPosition() {
        String position = "";
        for (int n = 9; n >= 2; n--) {
            String line = "";
            int sinceLast = 0;
            for (int i = n * 10 + 1; i <= n * 10 + 8; i++) {
                if (boardSquares[i].getPiece() == null) {
                    sinceLast++;
                } else {
                    if (sinceLast > 0) {
                        line += sinceLast;
                        sinceLast = 0;
                    }
                    if (boardSquares[i].getPiece().getSide() == Side.WHITE) {
                        line += boardSquares[i].getPiece().getCharCode().toUpperCase();
                    } else {
                        line += boardSquares[i].getPiece().getCharCode().toLowerCase();
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

    /**
     * Returns partial fen string - legal castling
     */
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

    /**
     * Returns partial fen string - en passant
     */
    public String getFenEnPassant() {
        String enPassant = "";
        if (enPassantField != null) {
            enPassant = enPassantField.getLowerCaseName();
        } else {
            enPassant = "-";
        }

        return enPassant;
    }


    // ****************************************************************************************************************************************
    // *** Support
    // ****************************************************************************************************************************************


    /**
     * Returns the current amount of pieces on the board independent of side
     */
    public int getPieceCount() {
        int pieceCount = 0;
        for (BoardSquare boardSquare : boardSquares) {
            if (boardSquare != null && boardSquare.getPiece() != null) {
                pieceCount++;
            }
        }
        return pieceCount;
    }

    /**
     * Returns true if a given castling is allowed
     */
    public boolean isCastlingAllowed(Castling castling) {
        return switch (castling) {
            case WHITE_LONG -> castlingWhiteLongAllowed;
            case WHITE_SHORT -> castlingWhiteShortAllowed;
            case BLACK_LONG -> castlingBlackLongAllowed;
            case BLACK_SHORT -> castlingBlackShortAllowed;
        };
    }

    /**
     * Does return the piece on a given square. Throws a ChessGameException if no piece is present
     */
    public Piece getPiece(Square square) {
        if (boardSquares[square.getBoardIdx()].getPiece() == null) {
            throw new ChessGameException("No piece at square: " + square);
        }
        return boardSquares[square.getBoardIdx()].getPiece();
    }

    /**
     * Returns the side of a piece on the given square. Throws a ChessGameException if no piece is present
     */
    public Side getPieceSide(Square square) {
        return getPiece(square).getSide();
    }

    /**
     * Returns the PieceType of a piece on the given square. Throws a ChessGameException if no piece is present
     */
    public PieceType getPieceType(Square square) {
        return getPiece(square).getPieceType();
    }

    /**
     * Returns true if a given square is empty
     */
    public boolean isEmptySquare(Square square) {
        return boardSquares[square.getBoardIdx()].getPiece() == null;
    }


    /**
     * Returns true if given square contains a piece by given side
     */
    public boolean containsPieceOfSide(Square square, Side side) {
        return !isEmptySquare(square) && getPieceSide(square) == side;
    }


    /**
     * Returns true if given board idx / field contains a given PieceType of given side
     */
    public boolean containsPieceOfSide(int boardIdx, PieceType pieceType, Side side) {
        return !isBorder(boardIdx) && boardSquares[boardIdx].getPiece() != null && boardSquares[boardIdx].getPiece().getSide() == side && boardSquares[boardIdx].getPiece().getPieceType() == pieceType;
    }

    /**
     * Returns true if given board idx / field is part of the border
     */
    public boolean isBorder(int idx) {
        return boardSquares[idx] == null;
    }


    /**
     * Returns tru eif given board idx / field is not part of the border, empty or contains an opposite side piece
     */
    public boolean isPseudoLegalMoveToField(int boardIdx, Side sideToMove) {
        if (isBorder(boardIdx)) {
            return false;
        }

        if (boardSquares[boardIdx].getPiece() == null) {
            return true;
        }

        return boardSquares[boardIdx].getPiece().getSide() == sideToMove.invert();
    }


    // ****************************************************************************************************************************************
    // *** Move  & Rules
    // ****************************************************************************************************************************************


    /**
     * Set a given piece on the given board idx
     */
    private void setPiece(int idx, Piece piece) {
        boardSquares[idx].setPiece(piece);
    }

    /**
     * Move a piece from a given from to a given to board idx
     */
    private void movePiece(int from, int to) {
        boardSquares[to].setPiece(boardSquares[from].getPiece());

        boardSquares[from].setPiece(null);
    }

    /**
     * Does apply a move on the board, updates enPassant fields and castling if affected
     */
    public void applyMove(Move move, Side sideToMove) {

        if (move.getPromotion() != null) {
            // promotion

            setPiece(move.getTo().getBoardIdx(), move.getPromotion());
            boardSquares[move.getFrom().getBoardIdx()].setPiece(null);

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

            movePiece(move.getCastling().getFromRook().getBoardIdx(), move.getCastling().getToRook().getBoardIdx());
            movePiece(move.getCastling().getFromKing().getBoardIdx(), move.getCastling().getToKing().getBoardIdx());

            this.enPassantField = null;

        } else if (boardSquares[move.getFrom().getBoardIdx()].getPiece().getPieceType() == PieceType.PAWN && enPassantField != null && move.getTo().getBoardIdx() == enPassantField.getBoardIdx()) {
            // en passant

            movePiece(move.getFrom().getBoardIdx(), move.getTo().getBoardIdx());
            int epIdx = move.getTo().getBoardIdx() + (sideToMove.getCalculationModifier() * 10) * -1;
            boardSquares[epIdx].setPiece(null);

            this.enPassantField = null;

        } else {

            // Castling destroyed by movement of Rook / King or movement on Rook
            // field (iE rook hit)
            if (move.getTo().getBoardIdx() == Castling.WHITE_LONG.getFromRook().getBoardIdx() || move.getFrom().getBoardIdx() == Castling.WHITE_LONG.getFromRook().getBoardIdx()
                    || move.getFrom().getBoardIdx() == Castling.WHITE_LONG.getFromKing().getBoardIdx()) {
                castlingWhiteLongAllowed = false;
            }

            if (move.getTo().getBoardIdx() == Castling.WHITE_SHORT.getFromRook().getBoardIdx()
                    || move.getFrom().getBoardIdx() == Castling.WHITE_SHORT.getFromRook().getBoardIdx()
                    || move.getFrom().getBoardIdx() == Castling.WHITE_SHORT.getFromKing().getBoardIdx()) {
                castlingWhiteShortAllowed = false;
            }

            if (move.getTo().getBoardIdx() == Castling.BLACK_LONG.getFromRook().getBoardIdx() || move.getFrom().getBoardIdx() == Castling.BLACK_LONG.getFromRook().getBoardIdx()
                    || move.getFrom().getBoardIdx() == Castling.BLACK_LONG.getFromKing().getBoardIdx()) {
                castlingBlackLongAllowed = false;
            }

            if (move.getTo().getBoardIdx() == Castling.BLACK_SHORT.getFromRook().getBoardIdx()
                    || move.getFrom().getBoardIdx() == Castling.BLACK_SHORT.getFromRook().getBoardIdx()
                    || move.getFrom().getBoardIdx() == Castling.BLACK_SHORT.getFromKing().getBoardIdx()) {
                castlingBlackShortAllowed = false;
            }

            // En Passant field to be set?
            if (boardSquares[move.getFrom().getBoardIdx()].getPiece().getPieceType() == PieceType.PAWN && Math.abs(move.getFrom().getBoardIdx() - move.getTo().getBoardIdx()) == 20) {
                this.enPassantField = Square.byBoardIdx(move.getTo().getBoardIdx() + (sideToMove.getCalculationModifier() * 10) * -1);
            } else {
                this.enPassantField = null;
            }

            // Move piece
            movePiece(move.getFrom().getBoardIdx(), move.getTo().getBoardIdx());

        }

    }

    /**
     * Returns a list of legal moves on the current board for given side
     */
    public List<Move> getLegalMoves(Side sideToMove) {
        List<Move> moves = new ArrayList<>();

        BoardSquare[] boardSquares = getBoardSquares();
        List<Move> pseudoLegalMoves = new ArrayList<>();

        for (int i = 0; i < boardSquares.length; i++) {
            if (boardSquares[i] != null && boardSquares[i].getPiece() != null && boardSquares[i].getPiece().getSide() == sideToMove) {
                pseudoLegalMoves.addAll(boardSquares[i].getPiece().getPseudoLegalMoves(this, i));
            }
        }

        for (Move move : pseudoLegalMoves) {
            Board boardClone = clone();
            boardClone.applyMove(move, sideToMove);
            if (!boardClone.isInCheck(sideToMove)) {
                moves.add(move);
            }
        }

        return moves;
    }


    /**
     * Returns true if given side is stalemated
     */
    public boolean isStaleMate(Side sideToMove) {
        boolean isInCheck = isInCheck(sideToMove);

        List<Move> legalMoves = getLegalMoves(sideToMove);

        return !isInCheck && legalMoves.isEmpty();
    }

    /**
     * Returns true if given side is checkmated
     */
    public boolean isMate(Side sideToMove) {
        List<Move> legalMoves = getLegalMoves(sideToMove);
        boolean isInCheck = isInCheck(sideToMove);
        return isInCheck && legalMoves.isEmpty();
    }

    /**
     * Returns true if given move of given side would leave to a check
     */
    public boolean leadsToCheck(Move move, Side sideToMove) {
        Board boardClone = clone();
        boardClone.applyMove(move, sideToMove);

        return boardClone.isInCheck(sideToMove.invert());
    }

    /**
     * Returns true if given move of given side would leave to a checkmate
     */
    public boolean leadsToMate(Move move, Side sideToMove) {
        Board boardClone = clone();
        boardClone.applyMove(move, sideToMove);

        return boardClone.isMate(sideToMove.invert());
    }

    /**
     * Returns true if given side is currently in check
     */
    public boolean isInCheck(Side sideToMove) {

        BoardSquare[] playedBoardSquares = getBoardSquares();
        boolean isCheck = false;
        for (int x = 0; x < playedBoardSquares.length; x++) {
            if (playedBoardSquares[x] != null && playedBoardSquares[x].getPiece() != null && playedBoardSquares[x].getPiece().getPieceType() == PieceType.KING && playedBoardSquares[x].getPiece().getSide() == sideToMove) {
                isCheck = (KingMoveGenerator.isKingAttacked(this, x, sideToMove));
            }
        }

        return isCheck;
    }


    // ****************************************************************************************************************************************
    // *** Support
    // ****************************************************************************************************************************************

    /**
     * Clone board
     */
    @Override
    public Board clone() {
        Board clone;
        try {
            clone = (Board) super.clone();
        } catch (CloneNotSupportedException e) {
            throw new ChessGameException("Unable to clone board");
        }

        // Clone squares
        clone.boardSquares = new BoardSquare[boardSquares.length];

        for (int i = 0; i < boardSquares.length; i++) {
            if (boardSquares[i] != null) {
                clone.boardSquares[i] = boardSquares[i].clone();
            }
        }

        return clone;
    }


    /**
     * Returns a simple console representation of the actual board
     */
    @Override
    public String toString() {
        String result = "";
        result += dumpRow(91, 98);
        result += dumpRow(81, 88);
        result += dumpRow(71, 78);
        result += dumpRow(61, 68);
        result += dumpRow(51, 58);
        result += dumpRow(41, 48);
        result += dumpRow(31, 38);
        result += dumpRow(21, 28);
        result += "  A  B  C  D  E  F  G  H  ";

        return result;
    }

    private String dumpRow(int beg, int end) {
        String result = ((beg - 11) / 10) + " ";
        for (int i = beg; i <= end; i++) {

            BoardSquare boardSquare = getBoardSquares()[i];

            if (boardSquare.getPiece() == null) {
                result += "[] ";
            } else {
                if (boardSquare.getPiece().getSide() == Side.WHITE) {
                    result += "W";
                } else {
                    result += "B";
                }
                result += boardSquare.getPiece().getCharCode().toUpperCase();
                result += " ";
            }

        }
        return result + "\n";
    }

}
