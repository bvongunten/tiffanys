package ch.nostromo.tiffanys.dragonborn.engine.board.impl.fast;

import ch.nostromo.tiffanys.dragonborn.engine.DragonbornEngineConstants;
import ch.nostromo.tiffanys.dragonborn.engine.board.RobustBoard;
import ch.nostromo.tiffanys.dragonborn.engine.board.RobustBoardPiecePositions;
import ch.nostromo.tiffanys.dragonborn.engine.move.EngineMove;
import ch.nostromo.tiffanys.dragonborn.engine.move.impl.fast.FastMoveGenPatterns;

public class TiffanysBoardInterfaceWhite implements DragonbornEngineConstants {

    public final boolean isWhiteKingInCheck(RobustBoard board) {
        return isWhitePieceAttacked(board, board.whiteKingPos);
    }

    public final boolean isWhitePieceAttacked(RobustBoard board, int kingPos) {
        int to = -1;
        int step;

        // Check attacks from black knights
        int dirCount = FastMoveGenPatterns.KNIGHT_MOVES[kingPos].length;
        for (step = 0; step < dirCount; step++) {
            if (board.board[FastMoveGenPatterns.KNIGHT_MOVES[kingPos][step]] == BLACK_KNIGHT) {
                return true;
            }
        }

        int nextPiece;
        int distance;
        int count;

        nextPiece = 0;
        distance = FastMoveGenPatterns.QUEEN_MOVES[kingPos][SOUTH];
        for (count = 1; count <= distance; count++) {
            nextPiece = board.board[kingPos + (count * -8)];
            if (nextPiece != 0) {
                break;
            }
        }
        if (nextPiece == BLACK_QUEEN || nextPiece == BLACK_ROOK || (count == 1 && nextPiece == BLACK_KING)) {
            return true;
        }

        nextPiece = 0;
        distance = FastMoveGenPatterns.BISHOP_MOVES[kingPos][SOUTHWEST];
        for (count = 1; count <= distance; count++) {
            nextPiece = board.board[kingPos + (count * -9)];
            if (nextPiece != 0) {
                break;
            }
        }
        if (nextPiece == BLACK_QUEEN || nextPiece == BLACK_BISHOP || (count == 1 && nextPiece == BLACK_KING)) {
            return true;
        }

        nextPiece = 0;
        distance = FastMoveGenPatterns.BISHOP_MOVES[kingPos][SOUTHEAST];
        for (count = 1; count <= distance; count++) {
            nextPiece = board.board[kingPos + (count * -7)];
            if (nextPiece != 0) {
                break;
            }
        }
        if (nextPiece == BLACK_QUEEN || nextPiece == BLACK_BISHOP || (count == 1 && nextPiece == BLACK_KING)) {
            return true;
        }

        nextPiece = 0;
        distance = FastMoveGenPatterns.QUEEN_MOVES[kingPos][EAST];
        for (count = 1; count <= distance; count++) {
            nextPiece = board.board[kingPos + (count * 1)];
            if (nextPiece != 0) {
                break;
            }
        }
        if (nextPiece == BLACK_QUEEN || nextPiece == BLACK_ROOK || (count == 1 && nextPiece == BLACK_KING)) {
            return true;
        }

        nextPiece = 0;
        distance = FastMoveGenPatterns.QUEEN_MOVES[kingPos][WEST];
        for (count = 1; count <= distance; count++) {
            nextPiece = board.board[kingPos + (count * -1)];
            if (nextPiece != 0) {
                break;
            }
        }
        if (nextPiece == BLACK_QUEEN || nextPiece == BLACK_ROOK || (count == 1 && nextPiece == BLACK_KING)) {
            return true;
        }

        nextPiece = 0;
        distance = FastMoveGenPatterns.QUEEN_MOVES[kingPos][NORTH];
        for (count = 1; count <= distance; count++) {
            nextPiece = board.board[kingPos + (count * 8)];
            if (nextPiece != 0) {
                break;
            }
        }
        if (nextPiece == BLACK_QUEEN || nextPiece == BLACK_ROOK || (count == 1 && nextPiece == BLACK_KING)) {
            return true;
        }

        nextPiece = 0;
        distance = FastMoveGenPatterns.BISHOP_MOVES[kingPos][NORTHWEST];
        for (count = 1; count <= distance; count++) {
            nextPiece = board.board[kingPos + (count * 7)];
            if (nextPiece != 0) {
                break;
            }
        }
        if (nextPiece == BLACK_QUEEN || nextPiece == BLACK_BISHOP || (count == 1 && nextPiece == BLACK_KING)) {
            return true;
        }

        nextPiece = 0;
        distance = FastMoveGenPatterns.BISHOP_MOVES[kingPos][NORTHEAST];
        for (count = 1; count <= distance; count++) {
            nextPiece = board.board[kingPos + (count * 9)];
            if (nextPiece != 0) {
                break;
            }
        }
        if (nextPiece == BLACK_QUEEN || nextPiece == BLACK_BISHOP || (count == 1 && nextPiece == BLACK_KING)) {
            return true;
        }

        // Check attacks as a pawn on pawn (makes king in check)
        if (FastMoveGenPatterns.WHITE_PAWN_HIT_MOVES[kingPos] != null) {
            to = FastMoveGenPatterns.WHITE_PAWN_HIT_MOVES[kingPos][0];
            if (to > 0 && board.board[to] == BLACK_PAWN) {
                return true;
            }

            to = FastMoveGenPatterns.WHITE_PAWN_HIT_MOVES[kingPos][1];
            if (to > 0 && board.board[to] == BLACK_PAWN) {
                return true;
            }
        }

        return false;
    }

    public final void makeMoveWhite(RobustBoard board, EngineMove move) {

        move.stackEnPassantField = board.enPassantField;
      //  move.stackZobristKey = board.zobristKey;

        board.enPassantField = -1;

        // board.colorToMove = BLACK;

        switch (move.moveType) {
        case EngineMove.HIT_MOVE_PROMOTION: {

            // Stack values
            move.stackFromBoardPieceIndex = board.boardPieceIndexes[move.from];
            move.stackFromBoardPiece = board.board[move.from];
            move.stackToBoardPieceIndex = board.boardPieceIndexes[move.to];
            move.stackToBoardPiece = board.board[move.to];

            // Remove pawn
            board.whitePawnsPositions.removePiece(move.stackFromBoardPieceIndex);

            // Remove target piece
            switch (move.stackToBoardPiece) {
            case DragonbornEngineConstants.BLACK_PAWN:
                board.blackPawnsPositions.removePiece(move.stackToBoardPieceIndex);
                break;
            case DragonbornEngineConstants.BLACK_KNIGHT:
                board.blackKnightsPositions.removePiece(move.stackToBoardPieceIndex);
                break;
            case DragonbornEngineConstants.BLACK_BISHOP:
                board.blackBishopsPositions.removePiece(move.stackToBoardPieceIndex);
                break;
            case DragonbornEngineConstants.BLACK_ROOK:
                board.blackRooksPositions.removePiece(move.stackToBoardPieceIndex);
                break;
            default:
                board.blackQueensPositions.removePiece(move.stackToBoardPieceIndex);
            }

            // Add promoted piece
            RobustBoardPiecePositions newPiecePosition;
            switch (move.promotionPiece) {
            case DragonbornEngineConstants.PIECE_KNIGHT:
                newPiecePosition = board.whiteKnightsPositions;
                break;
            case DragonbornEngineConstants.PIECE_BISHOP:
                newPiecePosition = board.whiteBishopsPositions;
                break;
            case DragonbornEngineConstants.PIECE_ROOK:
                newPiecePosition = board.whiteRooksPositions;
                break;
            default:
                newPiecePosition = board.whiteQueensPositions;
            }

            newPiecePosition.addPiece(move.to);

            board.board[move.to] = move.promotionPiece;
            board.board[move.from] = 0;

            board.boardPieceIndexes[move.to] = newPiecePosition.pieceCount - 1;
            board.boardPieceIndexes[move.from] = -1;

            // Now stack the new piece
            move.stackPromotedPieceIndex = newPiecePosition.pieceCount - 1;

            break;
        }
        case EngineMove.MOVE_PROMOTION: {

            // Stack values
            move.stackFromBoardPiece = board.board[move.from];
            move.stackFromBoardPieceIndex = board.boardPieceIndexes[move.from];

            // Remove pawn
            board.whitePawnsPositions.removePiece(move.stackFromBoardPieceIndex);

            // Add promoted piece
            RobustBoardPiecePositions newPiecePosition;
            switch (move.promotionPiece) {
            case DragonbornEngineConstants.PIECE_KNIGHT:
                newPiecePosition = board.whiteKnightsPositions;
                break;
            case DragonbornEngineConstants.PIECE_BISHOP:
                newPiecePosition = board.whiteBishopsPositions;
                break;
            case DragonbornEngineConstants.PIECE_ROOK:
                newPiecePosition = board.whiteRooksPositions;
                break;
            default:
                newPiecePosition = board.whiteQueensPositions;
            }

            newPiecePosition.addPiece(move.to);

            board.board[move.to] = move.promotionPiece;
            board.board[move.from] = 0;

            board.boardPieceIndexes[move.to] = newPiecePosition.pieceCount - 1;
            board.boardPieceIndexes[move.from] = -1;

            // Now stack the new piece
            move.stackPromotedPieceIndex = newPiecePosition.pieceCount - 1;

            break;
        }
        case EngineMove.HIT_MOVE_ENPASSANT: {

            // Stack values
            move.stackFromBoardPieceIndex = board.boardPieceIndexes[move.from];
            move.stackFromBoardPiece = board.board[move.from];
            move.stackToBoardPieceIndex = board.boardPieceIndexes[move.epHitPawn];
            move.stackToBoardPiece = board.board[move.epHitPawn];

            // Move pawn
            board.whitePawnsPositions.updatePosition(move.stackFromBoardPieceIndex, move.to);

            // Remove target pawn
            board.blackPawnsPositions.removePiece(move.stackToBoardPieceIndex);

            board.board[move.to] = move.stackFromBoardPiece;
            board.board[move.from] = 0;
            board.board[move.epHitPawn] = 0;

            board.boardPieceIndexes[move.to] = move.stackFromBoardPieceIndex;
            board.boardPieceIndexes[move.from] = -1;
            board.boardPieceIndexes[move.epHitPawn] = -1;

            break;

        }
        case EngineMove.HIT_MOVE_CASTLING_AFFECTED_ROOK: {

            // Stack values
            move.stackWhiteLongCastling = board.whiteLongCastling;
            move.stackWhiteShortCastling = board.whiteShortCastling;

            // Stack values
            move.stackFromBoardPieceIndex = board.boardPieceIndexes[move.from];
            move.stackFromBoardPiece = board.board[move.from];
            move.stackToBoardPieceIndex = board.boardPieceIndexes[move.to];
            move.stackToBoardPiece = board.board[move.to];

            // Move rook
            board.whiteRooksPositions.updatePosition(move.stackFromBoardPieceIndex, move.to);

            // Remove target piece
            switch (move.stackToBoardPiece) {
            case DragonbornEngineConstants.BLACK_PAWN:
                board.blackPawnsPositions.removePiece(move.stackToBoardPieceIndex);
                break;
            case DragonbornEngineConstants.BLACK_KNIGHT:
                board.blackKnightsPositions.removePiece(move.stackToBoardPieceIndex);
                break;
            case DragonbornEngineConstants.BLACK_BISHOP:
                board.blackBishopsPositions.removePiece(move.stackToBoardPieceIndex);
                break;
            case DragonbornEngineConstants.BLACK_ROOK:
                board.blackRooksPositions.removePiece(move.stackToBoardPieceIndex);
                break;
            default:
                board.blackQueensPositions.removePiece(move.stackToBoardPieceIndex);
            }

            board.board[move.to] = move.stackFromBoardPiece;
            board.board[move.from] = 0;

            board.boardPieceIndexes[move.to] = move.stackFromBoardPieceIndex;
            board.boardPieceIndexes[move.from] = -1;

            // Check which castling we loose
            if (move.from == POS_WHITE_ROOK_SHORT_CASTLING_START) {
                board.whiteShortCastling = false;
            } else if (move.from == POS_WHITE_ROOK_LONG_CASTLING_START) {
                board.whiteLongCastling = false;
            }

            break;
        }
        case EngineMove.HIT_MOVE_CASTLING_AFFECTED_KING: {

            // Stack values
            move.stackWhiteLongCastling = board.whiteLongCastling;
            move.stackWhiteShortCastling = board.whiteShortCastling;

            // Stack values
            move.stackFromBoardPieceIndex = board.boardPieceIndexes[move.from];
            move.stackFromBoardPiece = board.board[move.from];
            move.stackToBoardPieceIndex = board.boardPieceIndexes[move.to];
            move.stackToBoardPiece = board.board[move.to];

            // Move king
            board.setWhiteKingPos(move.from, move.to);
            // board.whiteKingPos = move.to;

            // Remove target piece
            switch (move.stackToBoardPiece) {
            case DragonbornEngineConstants.BLACK_PAWN:
                board.blackPawnsPositions.removePiece(move.stackToBoardPieceIndex);
                break;
            case DragonbornEngineConstants.BLACK_KNIGHT:
                board.blackKnightsPositions.removePiece(move.stackToBoardPieceIndex);
                break;
            case DragonbornEngineConstants.BLACK_BISHOP:
                board.blackBishopsPositions.removePiece(move.stackToBoardPieceIndex);
                break;
            case DragonbornEngineConstants.BLACK_ROOK:
                board.blackRooksPositions.removePiece(move.stackToBoardPieceIndex);
                break;
            default:
                board.blackQueensPositions.removePiece(move.stackToBoardPieceIndex);
            }

            board.board[move.to] = move.stackFromBoardPiece;
            board.board[move.from] = 0;

            board.boardPieceIndexes[move.to] = move.stackFromBoardPieceIndex;
            board.boardPieceIndexes[move.from] = -1;

            // Check which castling we loose
            board.whiteLongCastling = false;
            board.whiteShortCastling = false;
            break;
        }
        case EngineMove.HIT_MOVE_PAWN:
        case EngineMove.HIT_MOVE_QUEEN:
        case EngineMove.HIT_MOVE_BISHOP:
        case EngineMove.HIT_MOVE_KNIGHT: {

            // Stack values
            move.stackFromBoardPieceIndex = board.boardPieceIndexes[move.from];
            move.stackFromBoardPiece = board.board[move.from];
            move.stackToBoardPieceIndex = board.boardPieceIndexes[move.to];
            move.stackToBoardPiece = board.board[move.to];

            // Move piece
            switch (move.stackFromBoardPiece) {
            case DragonbornEngineConstants.WHITE_PAWN:
                board.whitePawnsPositions.updatePosition(move.stackFromBoardPieceIndex, move.to);
                break;
            case DragonbornEngineConstants.WHITE_KNIGHT:
                board.whiteKnightsPositions.updatePosition(move.stackFromBoardPieceIndex, move.to);
                break;
            case DragonbornEngineConstants.WHITE_BISHOP:
                board.whiteBishopsPositions.updatePosition(move.stackFromBoardPieceIndex, move.to);
                break;
            default:
                board.whiteQueensPositions.updatePosition(move.stackFromBoardPieceIndex, move.to);
            }

            // Remove target piece
            switch (move.stackToBoardPiece) {
            case DragonbornEngineConstants.BLACK_PAWN:
                board.blackPawnsPositions.removePiece(move.stackToBoardPieceIndex);
                break;
            case DragonbornEngineConstants.BLACK_KNIGHT:
                board.blackKnightsPositions.removePiece(move.stackToBoardPieceIndex);
                break;
            case DragonbornEngineConstants.BLACK_BISHOP:
                board.blackBishopsPositions.removePiece(move.stackToBoardPieceIndex);
                break;
            case DragonbornEngineConstants.BLACK_ROOK:
                board.blackRooksPositions.removePiece(move.stackToBoardPieceIndex);
                break;
            default:
                board.blackQueensPositions.removePiece(move.stackToBoardPieceIndex);
            }

            board.board[move.to] = move.stackFromBoardPiece;
            board.board[move.from] = 0;

            board.boardPieceIndexes[move.to] = move.stackFromBoardPieceIndex;
            board.boardPieceIndexes[move.from] = -1;

            break;
        }

        case EngineMove.MOVE_EP_FIELD: {

            // Stack values
            move.stackFromBoardPieceIndex = board.boardPieceIndexes[move.from];
            move.stackFromBoardPiece = board.board[move.from];

            // Move pawn
            board.whitePawnsPositions.updatePosition(move.stackFromBoardPieceIndex, move.to);

            board.board[move.to] = move.stackFromBoardPiece;
            board.board[move.from] = 0;

            board.boardPieceIndexes[move.to] = move.stackFromBoardPieceIndex;
            board.boardPieceIndexes[move.from] = -1;

            board.enPassantField = move.epField;

            break;
        }
        case EngineMove.MOVE_CASTLING_AFFECTED_ROOK: {

            // Stack values
            move.stackWhiteLongCastling = board.whiteLongCastling;
            move.stackWhiteShortCastling = board.whiteShortCastling;

            move.stackFromBoardPieceIndex = board.boardPieceIndexes[move.from];
            move.stackFromBoardPiece = board.board[move.from];

            // Move rook
            board.whiteRooksPositions.updatePosition(move.stackFromBoardPieceIndex, move.to);

            board.board[move.to] = move.stackFromBoardPiece;
            board.board[move.from] = 0;

            board.boardPieceIndexes[move.to] = move.stackFromBoardPieceIndex;
            board.boardPieceIndexes[move.from] = -1;

            // Check which castling we loose
            if (move.from == POS_WHITE_ROOK_SHORT_CASTLING_START) {
                board.whiteShortCastling = false;
            } else if (move.from == POS_WHITE_ROOK_LONG_CASTLING_START) {
                board.whiteLongCastling = false;
            }

            break;
        }
        case EngineMove.MOVE_CASTLING_AFFECTED_KING: {

            // Stack values
            move.stackWhiteLongCastling = board.whiteLongCastling;
            move.stackWhiteShortCastling = board.whiteShortCastling;

            move.stackFromBoardPieceIndex = board.boardPieceIndexes[move.from];
            move.stackFromBoardPiece = board.board[move.from];

            // Move king
            board.setWhiteKingPos(move.from, move.to);
            // board.whiteKingPos = move.to;

            board.board[move.to] = move.stackFromBoardPiece;
            board.board[move.from] = 0;

            board.boardPieceIndexes[move.to] = move.stackFromBoardPieceIndex;
            board.boardPieceIndexes[move.from] = -1;

            // Check which castling we loose
            board.whiteLongCastling = false;
            board.whiteShortCastling = false;

            break;
        }
        case EngineMove.MOVE: {

            // Stack values
            move.stackFromBoardPieceIndex = board.boardPieceIndexes[move.from];
            move.stackFromBoardPiece = board.board[move.from];

            // Move piece
            switch (move.stackFromBoardPiece) {
            case DragonbornEngineConstants.WHITE_PAWN:
                board.whitePawnsPositions.updatePosition(move.stackFromBoardPieceIndex, move.to);
                break;
            case DragonbornEngineConstants.WHITE_KNIGHT:
                board.whiteKnightsPositions.updatePosition(move.stackFromBoardPieceIndex, move.to);
                break;
            case DragonbornEngineConstants.WHITE_BISHOP:
                board.whiteBishopsPositions.updatePosition(move.stackFromBoardPieceIndex, move.to);
                break;
            default:
                board.whiteQueensPositions.updatePosition(move.stackFromBoardPieceIndex, move.to);
            }

            board.board[move.to] = move.stackFromBoardPiece;
            board.board[move.from] = 0;

            board.boardPieceIndexes[move.to] = move.stackFromBoardPieceIndex;
            board.boardPieceIndexes[move.from] = -1;

            break;
        }
        case EngineMove.SHORT_CASTLING: {

            // Stack Values
            move.stackWhiteLongCastling = board.whiteLongCastling;
            move.stackCastlingRookIdx = board.boardPieceIndexes[POS_WHITE_ROOK_SHORT_CASTLING_START];

            // Move King
            board.setWhiteKingPos(POS_WHITE_KING_CASTLING_START, POS_WHITE_KING_SHORT_CASTLING_END);
           // board.whiteKingPos = POS_WHITE_KING_SHORT_CASTLING_END;

            board.board[POS_WHITE_KING_SHORT_CASTLING_END] = DragonbornEngineConstants.WHITE_KING;
            board.board[POS_WHITE_KING_CASTLING_START] = 0;

            // Move Rook
            board.whiteRooksPositions.updatePosition(board.boardPieceIndexes[POS_WHITE_ROOK_SHORT_CASTLING_START], POS_WHITE_ROOK_SHORT_CASTLING_END);
            board.board[POS_WHITE_ROOK_SHORT_CASTLING_END] = DragonbornEngineConstants.WHITE_ROOK;
            board.board[POS_WHITE_ROOK_SHORT_CASTLING_START] = 0;
            board.boardPieceIndexes[POS_WHITE_ROOK_SHORT_CASTLING_END] = board.boardPieceIndexes[POS_WHITE_ROOK_SHORT_CASTLING_START];
            board.boardPieceIndexes[POS_WHITE_ROOK_SHORT_CASTLING_START] = -1;

            // No more castling on this board
            board.whiteLongCastling = false;
            board.whiteShortCastling = false;
            board.whiteCastled = true;

            break;
        }
        case EngineMove.LONG_CASTLING: {

            // Stack Values
            move.stackWhiteShortCastling = board.whiteShortCastling;
            move.stackCastlingRookIdx = board.boardPieceIndexes[POS_WHITE_ROOK_LONG_CASTLING_START];

            // Move King
            board.setWhiteKingPos(POS_WHITE_KING_CASTLING_START, POS_WHITE_KING_LONG_CASTLING_END);
            // board.whiteKingPos = POS_WHITE_KING_LONG_CASTLING_END;
            board.board[POS_WHITE_KING_LONG_CASTLING_END] = DragonbornEngineConstants.WHITE_KING;
            board.board[POS_WHITE_KING_CASTLING_START] = 0;

            // Move Rook
            board.whiteRooksPositions.updatePosition(board.boardPieceIndexes[POS_WHITE_ROOK_LONG_CASTLING_START], POS_WHITE_ROOK_LONG_CASTLING_END);
            board.board[POS_WHITE_ROOK_LONG_CASTLING_END] = DragonbornEngineConstants.WHITE_ROOK;
            board.board[POS_WHITE_ROOK_LONG_CASTLING_START] = 0;
            board.boardPieceIndexes[POS_WHITE_ROOK_LONG_CASTLING_END] = board.boardPieceIndexes[POS_WHITE_ROOK_LONG_CASTLING_START];
            board.boardPieceIndexes[POS_WHITE_ROOK_LONG_CASTLING_START] = -1;

            // No more castling on this board ;-)
            board.whiteLongCastling = false;
            board.whiteShortCastling = false;
            board.whiteCastled = true;

            break;
        }

        }

    }

    public final void undoMoveWhite(RobustBoard board, EngineMove move) {

        // board.colorToMove = WHITE;

        switch (move.moveType) {
        case EngineMove.HIT_MOVE_PROMOTION: {
            // Unstack values
            board.board[move.from] = move.stackFromBoardPiece;
            board.board[move.to] = move.stackToBoardPiece;

            board.boardPieceIndexes[move.from] = move.stackFromBoardPieceIndex;

            // Add pawn back
            board.whitePawnsPositions.insertPiece(move.stackFromBoardPieceIndex, move.from);

            // Remove promoted piece
            RobustBoardPiecePositions newPiecePosition;
            switch (move.promotionPiece) {
            case DragonbornEngineConstants.PIECE_KNIGHT:
                newPiecePosition = board.whiteKnightsPositions;
                break;
            case DragonbornEngineConstants.PIECE_BISHOP:
                newPiecePosition = board.whiteBishopsPositions;
                break;
            case DragonbornEngineConstants.PIECE_ROOK:
                newPiecePosition = board.whiteRooksPositions;
                break;
            default:
                newPiecePosition = board.whiteQueensPositions;
            }
            newPiecePosition.removePiece(move.stackPromotedPieceIndex);

            board.boardPieceIndexes[move.to] = move.stackToBoardPieceIndex;

            // Re Add target piece
            switch (move.stackToBoardPiece) {
            case DragonbornEngineConstants.BLACK_PAWN:
                board.blackPawnsPositions.insertPiece(move.stackToBoardPieceIndex, move.to);
                break;
            case DragonbornEngineConstants.BLACK_KNIGHT:
                board.blackKnightsPositions.insertPiece(move.stackToBoardPieceIndex, move.to);
                break;
            case DragonbornEngineConstants.BLACK_BISHOP:
                board.blackBishopsPositions.insertPiece(move.stackToBoardPieceIndex, move.to);
                break;
            case DragonbornEngineConstants.BLACK_ROOK:
                board.blackRooksPositions.insertPiece(move.stackToBoardPieceIndex, move.to);
                break;
            default:
                board.blackQueensPositions.insertPiece(move.stackToBoardPieceIndex, move.to);
            }

            break;

        }
        case EngineMove.MOVE_PROMOTION: {

            // Unstack values
            board.board[move.from] = move.stackFromBoardPiece;
            board.board[move.to] = 0;

            board.boardPieceIndexes[move.from] = move.stackFromBoardPieceIndex;
            board.boardPieceIndexes[move.to] = -1;

            // Add pawn back
            board.whitePawnsPositions.insertPiece(move.stackFromBoardPieceIndex, move.from);

            // Remove promoted piece
            RobustBoardPiecePositions newPiecePosition;
            switch (move.promotionPiece) {
            case DragonbornEngineConstants.PIECE_KNIGHT:
                newPiecePosition = board.whiteKnightsPositions;
                break;
            case DragonbornEngineConstants.PIECE_BISHOP:
                newPiecePosition = board.whiteBishopsPositions;
                break;
            case DragonbornEngineConstants.PIECE_ROOK:
                newPiecePosition = board.whiteRooksPositions;
                break;
            default:
                newPiecePosition = board.whiteQueensPositions;
            }
            newPiecePosition.removePiece(move.stackPromotedPieceIndex);

            break;
        }
        case EngineMove.HIT_MOVE_ENPASSANT: {
            // Unstack values

            board.board[move.from] = move.stackFromBoardPiece;
            board.board[move.to] = 0;
            board.board[move.epHitPawn] = move.stackToBoardPiece;

            board.boardPieceIndexes[move.from] = move.stackFromBoardPieceIndex;
            board.boardPieceIndexes[move.to] = -1;
            board.boardPieceIndexes[move.epHitPawn] = move.stackToBoardPieceIndex;

            // Move pawn back
            board.whitePawnsPositions.updatePosition(move.stackFromBoardPieceIndex, move.from);

            // Re add target pawn
            board.blackPawnsPositions.insertPiece(move.stackToBoardPieceIndex, move.epHitPawn);

            break;

        }
        case EngineMove.HIT_MOVE_CASTLING_AFFECTED_ROOK: {
            board.whiteLongCastling = move.stackWhiteLongCastling;
            board.whiteShortCastling = move.stackWhiteShortCastling;

            // Unstack values
            board.board[move.from] = move.stackFromBoardPiece;
            board.board[move.to] = move.stackToBoardPiece;

            board.boardPieceIndexes[move.from] = move.stackFromBoardPieceIndex;
            board.boardPieceIndexes[move.to] = move.stackToBoardPieceIndex;

            // Move rook back
            board.whiteRooksPositions.updatePosition(move.stackFromBoardPieceIndex, move.from);

            // Re Add target piece
            switch (move.stackToBoardPiece) {
            case DragonbornEngineConstants.BLACK_PAWN:
                board.blackPawnsPositions.insertPiece(move.stackToBoardPieceIndex, move.to);
                break;
            case DragonbornEngineConstants.BLACK_KNIGHT:
                board.blackKnightsPositions.insertPiece(move.stackToBoardPieceIndex, move.to);
                break;
            case DragonbornEngineConstants.BLACK_BISHOP:
                board.blackBishopsPositions.insertPiece(move.stackToBoardPieceIndex, move.to);
                break;
            case DragonbornEngineConstants.BLACK_ROOK:
                board.blackRooksPositions.insertPiece(move.stackToBoardPieceIndex, move.to);
                break;
            default:
                board.blackQueensPositions.insertPiece(move.stackToBoardPieceIndex, move.to);
            }

            break;
        }
        case EngineMove.HIT_MOVE_CASTLING_AFFECTED_KING: {
            board.whiteLongCastling = move.stackWhiteLongCastling;
            board.whiteShortCastling = move.stackWhiteShortCastling;

            // Unstack values
            board.board[move.from] = move.stackFromBoardPiece;
            board.board[move.to] = move.stackToBoardPiece;

            board.boardPieceIndexes[move.from] = move.stackFromBoardPieceIndex;
            board.boardPieceIndexes[move.to] = move.stackToBoardPieceIndex;

            // Move king back
            board.setWhiteKingPos(move.to, move.from);
            //board.whiteKingPos = move.from;

            // Re Add target piece
            switch (move.stackToBoardPiece) {
            case DragonbornEngineConstants.BLACK_PAWN:
                board.blackPawnsPositions.insertPiece(move.stackToBoardPieceIndex, move.to);
                break;
            case DragonbornEngineConstants.BLACK_KNIGHT:
                board.blackKnightsPositions.insertPiece(move.stackToBoardPieceIndex, move.to);
                break;
            case DragonbornEngineConstants.BLACK_BISHOP:
                board.blackBishopsPositions.insertPiece(move.stackToBoardPieceIndex, move.to);
                break;
            case DragonbornEngineConstants.BLACK_ROOK:
                board.blackRooksPositions.insertPiece(move.stackToBoardPieceIndex, move.to);
                break;
            default:
                board.blackQueensPositions.insertPiece(move.stackToBoardPieceIndex, move.to);
            }

            break;
        }
        case EngineMove.HIT_MOVE_PAWN:
        case EngineMove.HIT_MOVE_QUEEN:
        case EngineMove.HIT_MOVE_BISHOP:
        case EngineMove.HIT_MOVE_KNIGHT: {

            // Unstack values
            board.board[move.from] = move.stackFromBoardPiece;
            board.board[move.to] = move.stackToBoardPiece;

            board.boardPieceIndexes[move.from] = move.stackFromBoardPieceIndex;
            board.boardPieceIndexes[move.to] = move.stackToBoardPieceIndex;

            // Move piece back
            switch (move.stackFromBoardPiece) {
            case DragonbornEngineConstants.WHITE_PAWN:
                board.whitePawnsPositions.updatePosition(move.stackFromBoardPieceIndex, move.from);
                break;
            case DragonbornEngineConstants.WHITE_KNIGHT:
                board.whiteKnightsPositions.updatePosition(move.stackFromBoardPieceIndex, move.from);
                break;
            case DragonbornEngineConstants.WHITE_BISHOP:
                board.whiteBishopsPositions.updatePosition(move.stackFromBoardPieceIndex, move.from);
                break;
            default:
                board.whiteQueensPositions.updatePosition(move.stackFromBoardPieceIndex, move.from);
            }

            // Re Add target piece
            switch (move.stackToBoardPiece) {
            case DragonbornEngineConstants.BLACK_PAWN:
                board.blackPawnsPositions.insertPiece(move.stackToBoardPieceIndex, move.to);
                break;
            case DragonbornEngineConstants.BLACK_KNIGHT:
                board.blackKnightsPositions.insertPiece(move.stackToBoardPieceIndex, move.to);
                break;
            case DragonbornEngineConstants.BLACK_BISHOP:
                board.blackBishopsPositions.insertPiece(move.stackToBoardPieceIndex, move.to);
                break;
            case DragonbornEngineConstants.BLACK_ROOK:
                board.blackRooksPositions.insertPiece(move.stackToBoardPieceIndex, move.to);
                break;
            default:
                board.blackQueensPositions.insertPiece(move.stackToBoardPieceIndex, move.to);
            }

            break;
        }
        case EngineMove.MOVE_CASTLING_AFFECTED_ROOK: {
            board.whiteLongCastling = move.stackWhiteLongCastling;
            board.whiteShortCastling = move.stackWhiteShortCastling;

            // Unstack values
            board.board[move.from] = move.stackFromBoardPiece;
            board.board[move.to] = 0;

            board.boardPieceIndexes[move.from] = move.stackFromBoardPieceIndex;
            board.boardPieceIndexes[move.to] = -1;

            // Move rook back
            board.whiteRooksPositions.updatePosition(move.stackFromBoardPieceIndex, move.from);

            break;
        }
        case EngineMove.MOVE_CASTLING_AFFECTED_KING: {
            board.whiteLongCastling = move.stackWhiteLongCastling;
            board.whiteShortCastling = move.stackWhiteShortCastling;

            // Unstack values
            board.board[move.from] = move.stackFromBoardPiece;
            board.board[move.to] = 0;

            board.boardPieceIndexes[move.from] = move.stackFromBoardPieceIndex;
            board.boardPieceIndexes[move.to] = -1;

            // Move king back
            board.setWhiteKingPos(move.to, move.from);

            break;
        }
        case EngineMove.MOVE: {
            // Unstack values
            board.board[move.from] = move.stackFromBoardPiece;
            board.board[move.to] = 0;

            board.boardPieceIndexes[move.from] = move.stackFromBoardPieceIndex;
            board.boardPieceIndexes[move.to] = -1;

            // Move piece back
            switch (move.stackFromBoardPiece) {
            case DragonbornEngineConstants.WHITE_PAWN:
                board.whitePawnsPositions.updatePosition(move.stackFromBoardPieceIndex, move.from);
                break;
            case DragonbornEngineConstants.WHITE_KNIGHT:
                board.whiteKnightsPositions.updatePosition(move.stackFromBoardPieceIndex, move.from);
                break;
            case DragonbornEngineConstants.WHITE_BISHOP:
                board.whiteBishopsPositions.updatePosition(move.stackFromBoardPieceIndex, move.from);
                break;
            default:
                board.whiteQueensPositions.updatePosition(move.stackFromBoardPieceIndex, move.from);
            }

            break;
        }
        case EngineMove.MOVE_EP_FIELD: {
            // Unstack values
            board.board[move.from] = move.stackFromBoardPiece;
            board.board[move.to] = 0;

            board.boardPieceIndexes[move.from] = move.stackFromBoardPieceIndex;
            board.boardPieceIndexes[move.to] = -1;

            // Move pawn back
            board.whitePawnsPositions.updatePosition(move.stackFromBoardPieceIndex, move.from);

            break;
        }
        case EngineMove.SHORT_CASTLING: {
            // Unstack Values
            board.whiteLongCastling = move.stackWhiteLongCastling;
            board.whiteShortCastling = true;
            board.whiteCastled = false;

            // Move King
            board.setWhiteKingPos(move.POS_WHITE_KING_SHORT_CASTLING_END, move.POS_WHITE_KING_CASTLING_START);
            // board.whiteKingPos = POS_WHITE_KING_CASTLING_START;

            board.board[POS_WHITE_KING_CASTLING_START] = DragonbornEngineConstants.WHITE_KING;
            board.board[POS_WHITE_KING_SHORT_CASTLING_END] = 0;
            // board.boardPieceIndexes[POS_WHITE_KING_CASTLING_START] =
            // IDX_KING;
            // board.boardPieceIndexes[POS_WHITE_KING_SHORT_CASTLING_END] = -1;

            // Move Rook
            board.whiteRooksPositions.updatePosition(move.stackCastlingRookIdx, POS_WHITE_ROOK_SHORT_CASTLING_START);
            board.board[POS_WHITE_ROOK_SHORT_CASTLING_START] = DragonbornEngineConstants.WHITE_ROOK;
            board.board[POS_WHITE_ROOK_SHORT_CASTLING_END] = 0;
            board.boardPieceIndexes[POS_WHITE_ROOK_SHORT_CASTLING_START] = move.stackCastlingRookIdx;
            board.boardPieceIndexes[POS_WHITE_ROOK_SHORT_CASTLING_END] = -1;

            break;
        }
        case EngineMove.LONG_CASTLING: {
            // Unstack Values
            board.whiteShortCastling = move.stackWhiteShortCastling;
            board.whiteLongCastling = true;
            board.whiteCastled = false;

            // Move King
            board.setWhiteKingPos(move.POS_WHITE_KING_LONG_CASTLING_END, move.POS_WHITE_KING_CASTLING_START);
            //  board.whiteKingPos = POS_WHITE_KING_CASTLING_START;
            board.board[POS_WHITE_KING_CASTLING_START] = DragonbornEngineConstants.WHITE_KING;
            board.board[POS_WHITE_KING_LONG_CASTLING_END] = 0;
            // board.boardPieceIndexes[POS_WHITE_KING_CASTLING_START] =
            // IDX_KING;
            // board.boardPieceIndexes[POS_WHITE_KING_LONG_CASTLING_END] = -1;

            // Move Rook
            board.whiteRooksPositions.updatePosition(move.stackCastlingRookIdx, POS_WHITE_ROOK_LONG_CASTLING_START);
            board.board[POS_WHITE_ROOK_LONG_CASTLING_START] = DragonbornEngineConstants.WHITE_ROOK;
            board.board[POS_WHITE_ROOK_LONG_CASTLING_END] = 0;
            board.boardPieceIndexes[POS_WHITE_ROOK_LONG_CASTLING_START] = move.stackCastlingRookIdx;
            board.boardPieceIndexes[POS_WHITE_ROOK_LONG_CASTLING_END] = -1;

            break;
        }

        }

        board.enPassantField = move.stackEnPassantField;
        // board.zobristKey = move.stackZobristKey;

    }

}
