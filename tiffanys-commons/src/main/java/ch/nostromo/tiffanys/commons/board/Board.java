package ch.nostromo.tiffanys.commons.board;

import ch.nostromo.tiffanys.commons.Side;
import ch.nostromo.tiffanys.commons.format.FenFormat;
import ch.nostromo.tiffanys.commons.move.Castling;
import ch.nostromo.tiffanys.commons.move.Move;
import ch.nostromo.tiffanys.commons.move.movegenerator.KingMoveGenerator;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.*;

/**
 * Board can be constructed by a fen. It handles squares, enPassant field and castling rights but is independent of
 * the current side to play. Provides helper methods.
 */
@Data
@AllArgsConstructor
public class Board {

    // Legal / remaining castle moves
    boolean castlingWhiteShortAllowed;
    boolean castlingBlackShortAllowed;
    boolean castlingWhiteLongAllowed;
    boolean castlingBlackLongAllowed;

    // Current en passant field
    Square enPassantField;

    // Board Squares (null are border fields for move genration
    BoardSquare[] boardSquares = new BoardSquare[120];

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
        initializeBoard(fenFormat);
    }


    public Board copy() {
        Board copy = new Board();

        copy.setCastlingBlackLongAllowed(castlingBlackLongAllowed);
        copy.setCastlingWhiteLongAllowed(castlingWhiteLongAllowed);
        copy.setCastlingBlackShortAllowed(castlingBlackShortAllowed);
        copy.setCastlingWhiteShortAllowed(castlingWhiteShortAllowed);

        copy.setEnPassantField(enPassantField);

        copy.boardSquares = new BoardSquare[boardSquares.length];

        for (int i = 0; i < boardSquares.length; i++) {
            if (boardSquares[i] != null) {
                copy.boardSquares[i] = boardSquares[i].copy();
            }
        }

        return copy;
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
        StringBuilder position = new StringBuilder();

        for (int i = 9; i >= 2; i--) {
            position.append(getRankFenString(i));
            if (i > 2) {
                position.append("/");
            }
        }

        return position.toString();
    }

    /**
     * Converts a single rank to its FEN representation
     */
    private String getRankFenString(int rank) {
        StringBuilder line = new StringBuilder();
        int emptySquares = 0;

        for (int i = rank * 10 + 1; i <= rank * 10 + 8; i++) {
            if (boardSquares[i].getPiece() == null) {
                emptySquares++;
            } else {
                if (emptySquares > 0) {
                    line.append(emptySquares);
                    emptySquares = 0;
                }
                line.append(getPieceCharCode(boardSquares[i].getPiece()));
            }
        }

        if (emptySquares > 0) {
            line.append(emptySquares);
        }

        return line.toString();
    }

    /**
     * Returns the FEN character code for a piece (uppercase for white, lowercase for black)
     */
    private String getPieceCharCode(Piece piece) {
        String charCode = piece.getCharCode();
        return piece.getSide() == Side.WHITE ? charCode.toUpperCase() : charCode.toLowerCase();
    }

    /**
     * Returns partial fen string - legal castling
     */
    public String getFenCastling() {
        String castling = "";
        if (!castlingWhiteLongAllowed && !castlingWhiteShortAllowed
                && !castlingBlackLongAllowed && !castlingBlackShortAllowed) {
            castling = "-";
        } else {
            if (castlingWhiteShortAllowed) castling += "K";  // K = Kingside = Short
            if (castlingWhiteLongAllowed)  castling += "Q";  // Q = Queenside = Long
            if (castlingBlackShortAllowed) castling += "k";  // k = Kingside = Short
            if (castlingBlackLongAllowed)  castling += "q";  // q = Queenside = Long
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
            throw new IllegalArgumentException("No piece at square: " + square);
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
     * Returns a list of legal moves on the current board for given side
     */
    public List<Move> getLegalMoves(Side sideToMove) {
        List<Move> moves = new ArrayList<>();

        BoardSquare[] currentBoardSquares = getBoardSquares();
        List<Move> pseudoLegalMoves = new ArrayList<>();

        for (int i = 0; i < currentBoardSquares.length; i++) {
            if (currentBoardSquares[i] != null && currentBoardSquares[i].getPiece() != null && currentBoardSquares[i].getPiece().getSide() == sideToMove) {
                pseudoLegalMoves.addAll(currentBoardSquares[i].getPiece().getPseudoLegalMoves(this, i));
            }
        }

        for (Move move : pseudoLegalMoves) {
            Board copy = copy();
            copy.applyMove(move, sideToMove);
            if (!copy.isInCheck(sideToMove)) {
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
        Board copy = copy();
        copy.applyMove(move, sideToMove);

        return copy.isInCheck(sideToMove.invert());
    }

    /**
     * Returns true if given move of given side would leave to a checkmate
     */
    public boolean leadsToMate(Move move, Side sideToMove) {
        Board copy = copy();
        copy.applyMove(move, sideToMove);

        return copy.isMate(sideToMove.invert());
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

    /**
     * Does apply a move on the board, updates enPassant fields and castling if affected
     */
    public void applyMove(Move move, Side sideToMove) {
        if (move.getPromotion() != null) {
            applyPromotion(move);
        } else if (move.getCastling() != null) {
            applyCastling(move);
        } else if (isEnPassantCapture(move)) {
            applyEnPassantCapture(move, sideToMove);
        } else {
            applyRegularMove(move, sideToMove);
        }
    }

    private void applyPromotion(Move move) {
        setPiece(move.getTo().getBoardIdx(), move.getPromotion());
        boardSquares[move.getFrom().getBoardIdx()].setPiece(null);
        this.enPassantField = null;
    }

    private void applyCastling(Move move) {
        updateCastlingRightsAfterCastling(move.getCastling());
        movePiece(move.getCastling().getFromRook().getBoardIdx(), move.getCastling().getToRook().getBoardIdx());
        movePiece(move.getCastling().getFromKing().getBoardIdx(), move.getCastling().getToKing().getBoardIdx());
        this.enPassantField = null;
    }

    private void updateCastlingRightsAfterCastling(Castling castling) {
        switch (castling) {
            case WHITE_SHORT, WHITE_LONG -> {
                castlingWhiteLongAllowed = false;
                castlingWhiteShortAllowed = false;
            }
            case BLACK_SHORT, BLACK_LONG -> {
                castlingBlackLongAllowed = false;
                castlingBlackShortAllowed = false;
            }
        }
    }

    private boolean isEnPassantCapture(Move move) {
        return boardSquares[move.getFrom().getBoardIdx()].getPiece().getPieceType() == PieceType.PAWN
                && enPassantField != null
                && move.getTo().getBoardIdx() == enPassantField.getBoardIdx();
    }

    private void applyEnPassantCapture(Move move, Side sideToMove) {
        movePiece(move.getFrom().getBoardIdx(), move.getTo().getBoardIdx());
        int capturedPawnIdx = move.getTo().getBoardIdx() + (sideToMove.getCalculationModifier() * 10) * -1;
        boardSquares[capturedPawnIdx].setPiece(null);
        this.enPassantField = null;
    }

    private void applyRegularMove(Move move, Side sideToMove) {
        updateCastlingRightsAfterRegularMove(move);
        updateEnPassantField(move, sideToMove);
        movePiece(move.getFrom().getBoardIdx(), move.getTo().getBoardIdx());
    }

    private void updateCastlingRightsAfterRegularMove(Move move) {
        int fromIdx = move.getFrom().getBoardIdx();
        int toIdx = move.getTo().getBoardIdx();

        if (affectsCastling(fromIdx, toIdx, Castling.WHITE_LONG)) {
            castlingWhiteLongAllowed = false;
        }
        if (affectsCastling(fromIdx, toIdx, Castling.WHITE_SHORT)) {
            castlingWhiteShortAllowed = false;
        }
        if (affectsCastling(fromIdx, toIdx, Castling.BLACK_LONG)) {
            castlingBlackLongAllowed = false;
        }
        if (affectsCastling(fromIdx, toIdx, Castling.BLACK_SHORT)) {
            castlingBlackShortAllowed = false;
        }
    }

    private boolean affectsCastling(int fromIdx, int toIdx, Castling castling) {
        int rookIdx = castling.getFromRook().getBoardIdx();
        int kingIdx = castling.getFromKing().getBoardIdx();

        return fromIdx == rookIdx || fromIdx == kingIdx || toIdx == rookIdx;
    }

    private void updateEnPassantField(Move move, Side sideToMove) {
        Piece piece = boardSquares[move.getFrom().getBoardIdx()].getPiece();
        boolean isPawnDoubleMove = piece.getPieceType() == PieceType.PAWN
                && Math.abs(move.getFrom().getBoardIdx() - move.getTo().getBoardIdx()) == 20;

        if (isPawnDoubleMove) {
            int enPassantIdx = move.getTo().getBoardIdx() + (sideToMove.getCalculationModifier() * 10) * -1;
            this.enPassantField = Square.byBoardIdx(enPassantIdx);
        } else {
            this.enPassantField = null;
        }
    }



    // ****************************************************************************************************************************************
    // *** Support
    // ****************************************************************************************************************************************


    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Board board = (Board) o;
        return castlingWhiteShortAllowed == board.castlingWhiteShortAllowed && castlingBlackShortAllowed == board.castlingBlackShortAllowed && castlingWhiteLongAllowed == board.castlingWhiteLongAllowed && castlingBlackLongAllowed == board.castlingBlackLongAllowed && enPassantField == board.enPassantField && Objects.deepEquals(boardSquares, board.boardSquares);
    }

    @Override
    public int hashCode() {
        return Objects.hash(castlingWhiteShortAllowed, castlingBlackShortAllowed, castlingWhiteLongAllowed, castlingBlackLongAllowed, enPassantField, Arrays.hashCode(boardSquares));
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
        StringBuilder result = new StringBuilder(((beg - 11) / 10) + " ");
        for (int i = beg; i <= end; i++) {

            BoardSquare boardSquare = getBoardSquares()[i];

            if (boardSquare.getPiece() == null) {
                result.append("[] ");
            } else {
                if (boardSquare.getPiece().getSide() == Side.WHITE) {
                   result.append("W");
                } else {
                    result.append("B");
                }
                result.append(boardSquare.getPiece().getCharCode().toUpperCase());
                result.append(" ");
            }

        }
        return result + "\n";
    }

}
