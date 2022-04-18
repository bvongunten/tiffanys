package ch.nostromo.tiffanys.dragonborn.engine.ai.eval;

import ch.nostromo.tiffanys.commons.enums.GameColor;
import ch.nostromo.tiffanys.dragonborn.engine.ai.CalculationTimeoutException;
import ch.nostromo.tiffanys.dragonborn.engine.board.RobustBoard;
import ch.nostromo.tiffanys.dragonborn.engine.move.EngineMove;
import ch.nostromo.tiffanys.dragonborn.engine.DragonbornEngine;
import ch.nostromo.tiffanys.dragonborn.engine.DragonbornEngineConstants;

public class TiffanysEvaluation implements DragonbornEngineConstants {
    public static int MAT_RANGE = 10000;

    private EngineMove[] movesArray = new EngineMove[100];

    public TiffanysEvaluation() {
        for (int i = 0; i < 100; i++) {
            movesArray[i] = new EngineMove();
        }
    }

    public int evaluate(RobustBoard board, int currentRelativeDepth, boolean isInQuiescence) {

        // Abort by timeout
        if (!DragonbornEngine.running) {
            throw new CalculationTimeoutException("Timeout");
        }

        // Check for Check
        int movesCount = board.generateMovesList(movesArray);
        boolean legalMoveFound = false;
        for (int i = 0; i < movesCount; i++) {
            EngineMove currentMove = movesArray[i];
            if (!board.makeAndCheckMove(currentMove)) {
                legalMoveFound = true;
                board.unmakeMove(currentMove);
                break;
            }
            board.unmakeMove(currentMove);
        }

        if (!legalMoveFound) {
            if (board.isCheckNow()) {
                return -MAT_RANGE + currentRelativeDepth;
            } else {
                return 0;
            }
        }

        int whitePos = 0;
        int blackPos = 0;

        // positional values
        for (int i = 0; i < board.whitePawnsPositions.pieceCount; i++) {
            whitePos += TiffanysAiTools.PAWN_WHITE_VALUES[board.whitePawnsPositions.positions[i]];
        }

        for (int i = 0; i < board.blackPawnsPositions.pieceCount; i++) {
            blackPos += TiffanysAiTools.PAWN_BLACK_VALUES[board.blackPawnsPositions.positions[i]];
        }

        for (int i = 0; i < board.whiteKnightsPositions.pieceCount; i++) {
            whitePos += TiffanysAiTools.KNIGHT_WHITE_VALUES[board.whiteKnightsPositions.positions[i]];
        }

        for (int i = 0; i < board.blackKnightsPositions.pieceCount; i++) {
            blackPos += TiffanysAiTools.KNIGHT_BLACK_VALUES[board.blackKnightsPositions.positions[i]];
        }

        for (int i = 0; i < board.whiteBishopsPositions.pieceCount; i++) {
            whitePos += TiffanysAiTools.BISHOP_WHITE_VALUES[board.whiteBishopsPositions.positions[i]];
        }

        for (int i = 0; i < board.blackBishopsPositions.pieceCount; i++) {
            blackPos += TiffanysAiTools.BISHOP_BLACK_VALUES[board.blackBishopsPositions.positions[i]];
        }

        for (int i = 0; i < board.whiteRooksPositions.pieceCount; i++) {
            whitePos += TiffanysAiTools.ROOK_WHITE_VALUES[board.whiteRooksPositions.positions[i]];
        }

        for (int i = 0; i < board.blackRooksPositions.pieceCount; i++) {
            blackPos += TiffanysAiTools.ROOK_BLACK_VALUES[board.blackRooksPositions.positions[i]];
        }

        whitePos += TiffanysAiTools.KING_WHITE_VALUES[board.whiteKingPos];
        blackPos += TiffanysAiTools.KING_BLACK_VALUES[board.blackKingPos];

        // Net piece values
        whitePos += board.whitePawnsPositions.pieceCount * PAWN_SCORE;
        whitePos += board.whiteKnightsPositions.pieceCount * KNIGHT_SCORE;
        whitePos += board.whiteBishopsPositions.pieceCount * BISHOP_SCORE;
        whitePos += board.whiteRooksPositions.pieceCount * ROOK_SCORE;
        whitePos += board.whiteQueensPositions.pieceCount * QUEEN_SCORE;

        blackPos += board.blackPawnsPositions.pieceCount * PAWN_SCORE;
        blackPos += board.blackKnightsPositions.pieceCount * KNIGHT_SCORE;
        blackPos += board.blackBishopsPositions.pieceCount * BISHOP_SCORE;
        blackPos += board.blackRooksPositions.pieceCount * ROOK_SCORE;
        blackPos += board.blackQueensPositions.pieceCount * QUEEN_SCORE;

        // Castling values
        if (board.whiteCastled) {
            whitePos += 90;
        }
        if (board.whiteLongCastling || board.whiteShortCastling) {
            whitePos += 40;
        }

        // Castling values
        if (board.blackCastled) {
            blackPos += 90;
        }
        if (board.blackLongCastling || board.blackShortCastling) {
            blackPos += 40;
        }

        int result = 0;
        if (board.colorToMove == GameColor.WHITE) {
            result = whitePos - blackPos;
        } else {
            result = blackPos - whitePos;
        }

        return result;

    }

}
