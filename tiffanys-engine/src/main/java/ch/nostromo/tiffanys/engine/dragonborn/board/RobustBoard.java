package ch.nostromo.tiffanys.engine.dragonborn.board;

import ch.nostromo.tiffanys.commons.Side;
import ch.nostromo.tiffanys.commons.board.Board;
import ch.nostromo.tiffanys.commons.board.PieceType;
import ch.nostromo.tiffanys.commons.board.Square;
import ch.nostromo.tiffanys.commons.board.BoardSquare;
import ch.nostromo.tiffanys.commons.move.Castling;
import ch.nostromo.tiffanys.engine.dragonborn.DragonbornConstants;
import ch.nostromo.tiffanys.engine.dragonborn.board.api.BoardInteraction;
import ch.nostromo.tiffanys.engine.dragonborn.board.impl.BoardInteractionImpl;
import ch.nostromo.tiffanys.engine.dragonborn.move.EngineMove;
import ch.nostromo.tiffanys.engine.dragonborn.move.api.MoveGenerator;
import ch.nostromo.tiffanys.engine.dragonborn.move.impl.MoveGeneratorImpl;

import java.util.ArrayList;

import static ch.nostromo.tiffanys.commons.board.Square.A1;
import static ch.nostromo.tiffanys.commons.board.Square.A2;
import static ch.nostromo.tiffanys.commons.board.Square.A3;
import static ch.nostromo.tiffanys.commons.board.Square.A4;
import static ch.nostromo.tiffanys.commons.board.Square.A5;
import static ch.nostromo.tiffanys.commons.board.Square.A6;
import static ch.nostromo.tiffanys.commons.board.Square.A7;
import static ch.nostromo.tiffanys.commons.board.Square.A8;
import static ch.nostromo.tiffanys.commons.board.Square.B1;
import static ch.nostromo.tiffanys.commons.board.Square.B2;
import static ch.nostromo.tiffanys.commons.board.Square.B3;
import static ch.nostromo.tiffanys.commons.board.Square.B4;
import static ch.nostromo.tiffanys.commons.board.Square.B5;
import static ch.nostromo.tiffanys.commons.board.Square.B6;
import static ch.nostromo.tiffanys.commons.board.Square.B7;
import static ch.nostromo.tiffanys.commons.board.Square.B8;
import static ch.nostromo.tiffanys.commons.board.Square.C1;
import static ch.nostromo.tiffanys.commons.board.Square.C2;
import static ch.nostromo.tiffanys.commons.board.Square.C3;
import static ch.nostromo.tiffanys.commons.board.Square.C4;
import static ch.nostromo.tiffanys.commons.board.Square.C5;
import static ch.nostromo.tiffanys.commons.board.Square.C6;
import static ch.nostromo.tiffanys.commons.board.Square.C7;
import static ch.nostromo.tiffanys.commons.board.Square.C8;
import static ch.nostromo.tiffanys.commons.board.Square.D1;
import static ch.nostromo.tiffanys.commons.board.Square.D2;
import static ch.nostromo.tiffanys.commons.board.Square.D3;
import static ch.nostromo.tiffanys.commons.board.Square.D4;
import static ch.nostromo.tiffanys.commons.board.Square.D5;
import static ch.nostromo.tiffanys.commons.board.Square.D6;
import static ch.nostromo.tiffanys.commons.board.Square.D7;
import static ch.nostromo.tiffanys.commons.board.Square.D8;
import static ch.nostromo.tiffanys.commons.board.Square.E1;
import static ch.nostromo.tiffanys.commons.board.Square.E2;
import static ch.nostromo.tiffanys.commons.board.Square.E3;
import static ch.nostromo.tiffanys.commons.board.Square.E4;
import static ch.nostromo.tiffanys.commons.board.Square.E5;
import static ch.nostromo.tiffanys.commons.board.Square.E6;
import static ch.nostromo.tiffanys.commons.board.Square.E7;
import static ch.nostromo.tiffanys.commons.board.Square.E8;
import static ch.nostromo.tiffanys.commons.board.Square.F1;
import static ch.nostromo.tiffanys.commons.board.Square.F2;
import static ch.nostromo.tiffanys.commons.board.Square.F3;
import static ch.nostromo.tiffanys.commons.board.Square.F4;
import static ch.nostromo.tiffanys.commons.board.Square.F5;
import static ch.nostromo.tiffanys.commons.board.Square.F6;
import static ch.nostromo.tiffanys.commons.board.Square.F7;
import static ch.nostromo.tiffanys.commons.board.Square.F8;
import static ch.nostromo.tiffanys.commons.board.Square.G1;
import static ch.nostromo.tiffanys.commons.board.Square.G2;
import static ch.nostromo.tiffanys.commons.board.Square.G3;
import static ch.nostromo.tiffanys.commons.board.Square.G4;
import static ch.nostromo.tiffanys.commons.board.Square.G5;
import static ch.nostromo.tiffanys.commons.board.Square.G6;
import static ch.nostromo.tiffanys.commons.board.Square.G7;
import static ch.nostromo.tiffanys.commons.board.Square.G8;
import static ch.nostromo.tiffanys.commons.board.Square.H1;
import static ch.nostromo.tiffanys.commons.board.Square.H2;
import static ch.nostromo.tiffanys.commons.board.Square.H3;
import static ch.nostromo.tiffanys.commons.board.Square.H4;
import static ch.nostromo.tiffanys.commons.board.Square.H5;
import static ch.nostromo.tiffanys.commons.board.Square.H6;
import static ch.nostromo.tiffanys.commons.board.Square.H7;
import static ch.nostromo.tiffanys.commons.board.Square.H8;

public class RobustBoard implements DragonbornConstants, Cloneable {

    public int[] board;
    public int[] boardPieceIndexes;

    public RobustBoardPiecePositions whitePawnsPositions;
    public RobustBoardPiecePositions whiteRooksPositions;
    public RobustBoardPiecePositions whiteKnightsPositions;
    public RobustBoardPiecePositions whiteBishopsPositions;
    public RobustBoardPiecePositions whiteQueensPositions;
    public int whiteKingPos;

    public RobustBoardPiecePositions blackPawnsPositions;
    public RobustBoardPiecePositions blackRooksPositions;
    public RobustBoardPiecePositions blackKnightsPositions;
    public RobustBoardPiecePositions blackBishopsPositions;
    public RobustBoardPiecePositions blackQueensPositions;
    public int blackKingPos;

    public int enPassantField = -1;
    public boolean whiteLongCastling = true;
    public boolean whiteShortCastling = true;
    public boolean blackShortCastling = true;
    public boolean blackLongCastling = true;

    public boolean whiteCastled = false;
    public boolean blackCastled = false;

    // public long zobristKey = 0;

    public Side colorToMove;

    private static final int[] BOARD64 = {21, 22, 23, 24, 25, 26, 27, 28, 31, 32, 33, 34, 35, 36, 37, 38, 41, 42, 43, 44, 45, 46, 47, 48, 51, 52, 53, 54, 55, 56, 57, 58, 61, 62, 63, 64, 65, 66, 67, 68, 71, 72, 73, 74, 75, 76, 77, 78, 81, 82, 83,
            84, 85, 86, 87, 88, 91, 92, 93, 94, 95, 96, 97, 98};

    public static final Square[] BOARD64_COORDS = {A1, B1, C1, D1, E1, F1, G1, H1, A2, B2, C2, D2, E2, F2, G2, H2, A3, B3, C3, D3, E3, F3, G3, H3, A4, B4, C4, D4, E4, F4, G4, H4, A5,
            B5, C5, D5, E5, F5, G5, H5, A6, B6, C6, D6, E6, F6, G6, H6, A7, B7, C7, D7, E7, F7, G7, H7, A8, B8, C8, D8, E8, F8, G8, H8};

    MoveGenerator moveGenerator = new MoveGeneratorImpl();
    BoardInteraction boardInteraction = new BoardInteractionImpl();

    public RobustBoard(Board importBoard, Side colorToMove) {
        importBoard(importBoard);
        this.colorToMove = colorToMove;
    }

    private void importBoard(Board importBoard) {
        initBoard();

        BoardSquare[] boardArray = importBoard.getBoardSquares();

        if (importBoard.getEnPassantField() != null) {

            this.enPassantField = getShortField(importBoard.getEnPassantField().getBoardIdx());
        } else {
            this.enPassantField = Integer.MIN_VALUE;
        }


        this.whiteShortCastling = importBoard.isCastlingAllowed(Castling.WHITE_SHORT);
        this.whiteLongCastling = importBoard.isCastlingAllowed(Castling.WHITE_LONG);

        this.blackShortCastling = importBoard.isCastlingAllowed(Castling.BLACK_SHORT);
        this.blackLongCastling = importBoard.isCastlingAllowed(Castling.BLACK_LONG);

        for (int i = 0; i < 120; i++) {

            if (boardArray[i] != null && boardArray[i].getPiece() != null) {

                if (boardArray[i].getPiece().getPieceType() == PieceType.BISHOP) {
                    if (boardArray[i].getPiece().getSide() == Side.WHITE) {
                        this.board[getShortField(i)] = WHITE_BISHOP;
                        this.whiteBishopsPositions.addPiece(getShortField(i));
                        this.boardPieceIndexes[getShortField(i)] = this.whiteBishopsPositions.pieceCount - 1;
                    } else {
                        this.board[getShortField(i)] = BLACK_BISHOP;
                        this.blackBishopsPositions.addPiece(getShortField(i));
                        this.boardPieceIndexes[getShortField(i)] = this.blackBishopsPositions.pieceCount - 1;
                    }
                } else if (boardArray[i].getPiece().getPieceType() == PieceType.ROOK) {
                    if (boardArray[i].getPiece().getSide() == Side.WHITE) {
                        this.board[getShortField(i)] = WHITE_ROOK;
                        this.whiteRooksPositions.addPiece(getShortField(i));
                        this.boardPieceIndexes[getShortField(i)] = this.whiteRooksPositions.pieceCount - 1;
                    } else {
                        this.board[getShortField(i)] = BLACK_ROOK;
                        this.blackRooksPositions.addPiece(getShortField(i));
                        this.boardPieceIndexes[getShortField(i)] = this.blackRooksPositions.pieceCount - 1;
                    }
                } else if (boardArray[i].getPiece().getPieceType() == PieceType.KNIGHT) {
                    if (boardArray[i].getPiece().getSide() == Side.WHITE) {
                        this.board[getShortField(i)] = WHITE_KNIGHT;
                        this.whiteKnightsPositions.addPiece(getShortField(i));
                        this.boardPieceIndexes[getShortField(i)] = this.whiteKnightsPositions.pieceCount - 1;
                    } else {
                        this.board[getShortField(i)] = BLACK_KNIGHT;
                        this.blackKnightsPositions.addPiece(getShortField(i));
                        this.boardPieceIndexes[getShortField(i)] = this.blackKnightsPositions.pieceCount - 1;
                    }
                } else if (boardArray[i].getPiece().getPieceType() == PieceType.QUEEN) {
                    if (boardArray[i].getPiece().getSide() == Side.WHITE) {
                        this.board[getShortField(i)] = WHITE_QUEEN;
                        this.whiteQueensPositions.addPiece(getShortField(i));
                        this.boardPieceIndexes[getShortField(i)] = this.whiteQueensPositions.pieceCount - 1;
                    } else {
                        this.board[getShortField(i)] = BLACK_QUEEN;
                        this.blackQueensPositions.addPiece(getShortField(i));
                        this.boardPieceIndexes[getShortField(i)] = this.blackQueensPositions.pieceCount - 1;
                    }
                } else if (boardArray[i].getPiece().getPieceType() == PieceType.PAWN) {
                    if (boardArray[i].getPiece().getSide() == Side.WHITE) {
                        this.board[getShortField(i)] = WHITE_PAWN;
                        this.whitePawnsPositions.addPiece(getShortField(i));
                        this.boardPieceIndexes[getShortField(i)] = this.whitePawnsPositions.pieceCount - 1;
                    } else {
                        this.board[getShortField(i)] = BLACK_PAWN;
                        this.blackPawnsPositions.addPiece(getShortField(i));
                        this.boardPieceIndexes[getShortField(i)] = this.blackPawnsPositions.pieceCount - 1;
                    }
                } else if (boardArray[i].getPiece().getPieceType() == PieceType.KING) {
                    if (boardArray[i].getPiece().getSide() == Side.WHITE) {
                        this.board[getShortField(i)] = WHITE_KING;
                        this.whiteKingPos = getShortField(i);

                        //     this.zobristKey ^= Zobrist.ZOBRIST_FACTORS[Zobrist.ZOBRIST_WHITE][Zobrist.ZOBRIST_KING][whiteKingPos];
                    } else {
                        this.board[getShortField(i)] = BLACK_KING;
                        this.blackKingPos = getShortField(i);

                        //   this.zobristKey ^= Zobrist.ZOBRIST_FACTORS[Zobrist.ZOBRIST_BLACK][Zobrist.ZOBRIST_KING][blackKingPos];
                    }
                }

            }
        }
    }


    public void setWhiteKingPos(int from, int to) {
        whiteKingPos = to;
        //    this.zobristKey ^= Zobrist.ZOBRIST_FACTORS[Zobrist.ZOBRIST_WHITE][Zobrist.ZOBRIST_KING][from];
        //    this.zobristKey ^= Zobrist.ZOBRIST_FACTORS[Zobrist.ZOBRIST_WHITE][Zobrist.ZOBRIST_KING][to];
    }

    public void setBlackKingPos(int from, int to) {
        blackKingPos = to;
        //   this.zobristKey ^= Zobrist.ZOBRIST_FACTORS[Zobrist.ZOBRIST_BLACK][Zobrist.ZOBRIST_KING][from];
        //   this.zobristKey ^= Zobrist.ZOBRIST_FACTORS[Zobrist.ZOBRIST_BLACK][Zobrist.ZOBRIST_KING][to];
    }


    private void initBoard() {

        boardPieceIndexes = new int[64];
        for (int i = 0; i < boardPieceIndexes.length; i++) {
            boardPieceIndexes[i] = -1;
        }

        whitePawnsPositions = new RobustBoardPiecePositions(this, Zobrist.ZOBRIST_WHITE, Zobrist.ZOBRIST_PAWN);
        whiteRooksPositions = new RobustBoardPiecePositions(this, Zobrist.ZOBRIST_WHITE, Zobrist.ZOBRIST_ROOK);
        whiteKnightsPositions = new RobustBoardPiecePositions(this, Zobrist.ZOBRIST_WHITE, Zobrist.ZOBRIST_KNIGHT);
        whiteBishopsPositions = new RobustBoardPiecePositions(this, Zobrist.ZOBRIST_WHITE, Zobrist.ZOBRIST_BISHOP);
        whiteQueensPositions = new RobustBoardPiecePositions(this, Zobrist.ZOBRIST_WHITE, Zobrist.ZOBRIST_QUEEN);

        blackPawnsPositions = new RobustBoardPiecePositions(this, Zobrist.ZOBRIST_BLACK, Zobrist.ZOBRIST_PAWN);
        blackRooksPositions = new RobustBoardPiecePositions(this, Zobrist.ZOBRIST_BLACK, Zobrist.ZOBRIST_ROOK);
        blackKnightsPositions = new RobustBoardPiecePositions(this, Zobrist.ZOBRIST_BLACK, Zobrist.ZOBRIST_KNIGHT);
        blackBishopsPositions = new RobustBoardPiecePositions(this, Zobrist.ZOBRIST_BLACK, Zobrist.ZOBRIST_BISHOP);
        blackQueensPositions = new RobustBoardPiecePositions(this, Zobrist.ZOBRIST_BLACK, Zobrist.ZOBRIST_QUEEN);

        this.board = new int[64];

    }

    public long generateBoardZobristHash() {
        long result = 0;

        result = addHash(result, whitePawnsPositions, Zobrist.ZOBRIST_WHITE, Zobrist.ZOBRIST_PAWN);
        result = addHash(result, whiteRooksPositions, Zobrist.ZOBRIST_WHITE, Zobrist.ZOBRIST_ROOK);
        result = addHash(result, whiteKnightsPositions, Zobrist.ZOBRIST_WHITE, Zobrist.ZOBRIST_KNIGHT);
        result = addHash(result, whiteBishopsPositions, Zobrist.ZOBRIST_WHITE, Zobrist.ZOBRIST_BISHOP);
        result = addHash(result, whiteQueensPositions, Zobrist.ZOBRIST_WHITE, Zobrist.ZOBRIST_QUEEN);

        result ^= Zobrist.ZOBRIST_FACTORS[Zobrist.ZOBRIST_WHITE][Zobrist.ZOBRIST_KING][this.whiteKingPos];

        result = addHash(result, blackPawnsPositions, Zobrist.ZOBRIST_BLACK, Zobrist.ZOBRIST_PAWN);
        result = addHash(result, blackRooksPositions, Zobrist.ZOBRIST_BLACK, Zobrist.ZOBRIST_ROOK);
        result = addHash(result, blackKnightsPositions, Zobrist.ZOBRIST_BLACK, Zobrist.ZOBRIST_KNIGHT);
        result = addHash(result, blackBishopsPositions, Zobrist.ZOBRIST_BLACK, Zobrist.ZOBRIST_BISHOP);
        result = addHash(result, blackQueensPositions, Zobrist.ZOBRIST_BLACK, Zobrist.ZOBRIST_QUEEN);

        result ^= Zobrist.ZOBRIST_FACTORS[Zobrist.ZOBRIST_BLACK][Zobrist.ZOBRIST_KING][this.blackKingPos];

        if (colorToMove == Side.BLACK) {
            result ^= Zobrist.ZOBRIST_SIDE_TO_MOVE;
        }

        return result;

    }

    private long addHash(long board, RobustBoardPiecePositions positions, int zobristColor, int zobristFigure) {
        for (int i = 0; i < positions.pieceCount; i++) {
            board ^= Zobrist.ZOBRIST_FACTORS[zobristColor][zobristFigure][positions.positions[i]];
        }
        return board;
    }


    private int getShortField(int maxField) {
        for (int i = 0; i < BOARD64.length; i++) {
            if (BOARD64[i] == maxField) {
                return i;
            }
        }
        return -1;
    }

    public final int generateMovesList(EngineMove[] moves) {
        return moveGenerator.generateMovesList(this, moves, colorToMove);
    }

    public final int generateMovesHitList(EngineMove[] moves) {
        return moveGenerator.generateHitMovesList(this, moves, colorToMove);
    }

    public final boolean makeAndCheckMove(EngineMove move) {
        boardInteraction.applyMove(this, colorToMove, move);
        boolean result = boardInteraction.isKingInCheck(this, colorToMove);
        colorToMove = colorToMove.invert();

        return result;
    }

    public final boolean isCheckNow() {
        return boardInteraction.isKingInCheck(this, colorToMove);
    }

    public final void unmakeMove(EngineMove move) {
        colorToMove = colorToMove.invert();
        boardInteraction.unapplyMove(this, colorToMove, move);
    }

    public final EngineMove[] generateLegalMovesList() {
        EngineMove[] moves = EngineMove.generateMoveArray();
        int moveCount = moveGenerator.generateMovesList(this, moves, colorToMove);
        return generateLegalMovesList(moves, moveCount);
    }


    public final EngineMove[] generateLegalMovesList(EngineMove[] moves) {
        int moveCount = moveGenerator.generateMovesList(this, moves, colorToMove);
        return generateLegalMovesList(moves, moveCount);
    }

    public final EngineMove[] generateLegalHitMovesList() {
        EngineMove[] moves = EngineMove.generateMoveArray();
        int moveCount = moveGenerator.generateHitMovesList(this, moves, colorToMove);
        return generateLegalMovesList(moves, moveCount);
    }

    private final EngineMove[] generateLegalMovesList(EngineMove[] moves, int moveCount) {
        ArrayList<EngineMove> result = new ArrayList<EngineMove>();

        for (int i = 0; i < moveCount; i++) {
            boardInteraction.applyMove(this, colorToMove, moves[i]);
            if (!boardInteraction.isKingInCheck(this, colorToMove)) {
                result.add(moves[i]);
            }
            boardInteraction.unapplyMove(this, colorToMove, moves[i]);
        }

        return result.toArray(new EngineMove[result.size()]);
    }

    public RobustBoard safeClone() {
        try {

            RobustBoard result = (RobustBoard) super.clone();

            result.board = board.clone();
            result.boardPieceIndexes = boardPieceIndexes.clone();

            result.blackBishopsPositions = blackBishopsPositions.clone(result);
            result.blackKnightsPositions = blackKnightsPositions.clone(result);
            result.blackPawnsPositions = blackPawnsPositions.clone(result);
            result.blackQueensPositions = blackQueensPositions.clone(result);
            result.blackRooksPositions = blackRooksPositions.clone(result);

            result.whiteBishopsPositions = whiteBishopsPositions.clone(result);
            result.whiteKnightsPositions = whiteKnightsPositions.clone(result);
            result.whitePawnsPositions = whitePawnsPositions.clone(result);
            result.whiteQueensPositions = whiteQueensPositions.clone(result);
            result.whiteRooksPositions = whiteRooksPositions.clone(result);

            return result;
        } catch (CloneNotSupportedException e) {
            throw new IllegalStateException("Unable to clone board");
        }
    }

}
