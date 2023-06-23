package ch.nostromo.tiffanys.dragonborn.engine.board.impl.fast;

import ch.nostromo.tiffanys.dragonborn.engine.board.RobustBoard;
import ch.nostromo.tiffanys.dragonborn.engine.board.RobustBoardPiecePositions;
import ch.nostromo.tiffanys.dragonborn.engine.move.EngineMove;
import ch.nostromo.tiffanys.dragonborn.engine.DragonbornEngineConstants;
import ch.nostromo.tiffanys.dragonborn.engine.move.impl.fast.FastMoveGenPatterns;

public class TiffanysBoardInterfaceBlack implements DragonbornEngineConstants {

    public final boolean isBlackKingInCheck(RobustBoard board) {
        return isBlackPieceAttacked(board, board.blackKingPos);
    }

    public final boolean isBlackPieceAttacked(RobustBoard board, int kingPos) {
        int to = -1;

        // Check attacks from black knights
        int dirCount = FastMoveGenPatterns.KNIGHT_MOVES[kingPos].length;
        for (int step = 0; step < dirCount; step++) {
            if (board.board[FastMoveGenPatterns.KNIGHT_MOVES[kingPos][step]] == WHITE_KNIGHT) {
                return true;
            }
        }

        int nextPiece;
        int distance;
        int count;

        nextPiece = 0;
        distance = FastMoveGenPatterns.QUEEN_MOVES[kingPos][NORTH];
        for (count = 1; count <= distance; count++) {
            nextPiece = board.board[kingPos + (count * 8)];
            if (nextPiece != 0) {
                break;
            }
        }
        if (nextPiece == WHITE_QUEEN || nextPiece == WHITE_ROOK || (count == 1 && nextPiece == WHITE_KING)) {
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
        if (nextPiece == WHITE_QUEEN || nextPiece == WHITE_BISHOP || (count == 1 && nextPiece == WHITE_KING)) {
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
        if (nextPiece == WHITE_QUEEN || nextPiece == WHITE_BISHOP || (count == 1 && nextPiece == WHITE_KING)) {
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
        if (nextPiece == WHITE_QUEEN || nextPiece == WHITE_ROOK || (count == 1 && nextPiece == WHITE_KING)) {
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
        if (nextPiece == WHITE_QUEEN || nextPiece == WHITE_ROOK || (count == 1 && nextPiece == WHITE_KING)) {
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
        if (nextPiece == WHITE_QUEEN || nextPiece == WHITE_BISHOP || (count == 1 && nextPiece == WHITE_KING)) {
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
        if (nextPiece == WHITE_QUEEN || nextPiece == WHITE_BISHOP || (count == 1 && nextPiece == WHITE_KING)) {
            return true;
        }

        nextPiece = 0;
        distance = FastMoveGenPatterns.QUEEN_MOVES[kingPos][SOUTH];
        for (count = 1; count <= distance; count++) {
            nextPiece = board.board[kingPos + (count * -8)];
            if (nextPiece != 0) {
                break;
            }
        }
        if (nextPiece == WHITE_QUEEN || nextPiece == WHITE_ROOK || (count == 1 && nextPiece == WHITE_KING)) {
            return true;
        }

        // Check attacks as a pawn on pawn (makes king in check)
        if (FastMoveGenPatterns.BLACK_PAWN_HIT_MOVES[kingPos] != null) {
            to = FastMoveGenPatterns.BLACK_PAWN_HIT_MOVES[kingPos][0];
            if (to > 0 && board.board[to] == WHITE_PAWN) {
                return true;
            }

            to = FastMoveGenPatterns.BLACK_PAWN_HIT_MOVES[kingPos][1];
            if (to > 0 && board.board[to] == WHITE_PAWN) {
                return true;
            }
        }

        return false;
    }



    public final void makeMoveBlack(RobustBoard board, EngineMove move) {
        // Stack the En Passant Field always
        move.stackEnPassantField = board.enPassantField;
      //  move.stackZobristKey = board.zobristKey;
        board.enPassantField = -1;

        // board.colorToMove = WHITE;
        switch (move.moveType) {
        case EngineMove.HIT_MOVE_PROMOTION: {

            // Stack values
            move.stackFromBoardPieceIndex = board.boardPieceIndexes[move.from];
            move.stackFromBoardPiece = board.board[move.from];
            move.stackToBoardPieceIndex = board.boardPieceIndexes[move.to];
            move.stackToBoardPiece = board.board[move.to];

            // Remove pawn
            board.blackPawnsPositions.removePiece(move.stackFromBoardPieceIndex);

            // Remove the target piece
            switch (move.stackToBoardPiece) {
            case DragonbornEngineConstants.WHITE_PAWN:
                board.whitePawnsPositions.removePiece(move.stackToBoardPieceIndex);
                break;
            case DragonbornEngineConstants.WHITE_KNIGHT:
                board.whiteKnightsPositions.removePiece(move.stackToBoardPieceIndex);
                break;
            case DragonbornEngineConstants.WHITE_BISHOP:
                board.whiteBishopsPositions.removePiece(move.stackToBoardPieceIndex);
                break;
            case DragonbornEngineConstants.WHITE_ROOK:
                board.whiteRooksPositions.removePiece(move.stackToBoardPieceIndex);
                break;
            default:
                board.whiteQueensPositions.removePiece(move.stackToBoardPieceIndex);
            }

            // Add promoted piece
            RobustBoardPiecePositions newPiecePosition;
            switch (move.promotionPiece) {
            case DragonbornEngineConstants.PIECE_KNIGHT:
                newPiecePosition = board.blackKnightsPositions;
                break;
            case DragonbornEngineConstants.PIECE_BISHOP:
                newPiecePosition = board.blackBishopsPositions;
                break;
            case DragonbornEngineConstants.PIECE_ROOK:
                newPiecePosition = board.blackRooksPositions;
                break;
            default:
                newPiecePosition = board.blackQueensPositions;
            }
            newPiecePosition.addPiece(move.to);

            // Make the move on the board
            board.board[move.to] = -move.promotionPiece;
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
            board.blackPawnsPositions.removePiece(move.stackFromBoardPieceIndex);

            // Add promoted piece
            RobustBoardPiecePositions newPiecePosition;
            switch (move.promotionPiece) {
            case DragonbornEngineConstants.PIECE_KNIGHT:
                newPiecePosition = board.blackKnightsPositions;
                break;
            case DragonbornEngineConstants.PIECE_BISHOP:
                newPiecePosition = board.blackBishopsPositions;
                break;
            case DragonbornEngineConstants.PIECE_ROOK:
                newPiecePosition = board.blackRooksPositions;
                break;
            default:
                newPiecePosition = board.blackQueensPositions;
            }
            newPiecePosition.addPiece(move.to);

            // Make the move on the board
            board.board[move.to] = -move.promotionPiece;
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
            board.blackPawnsPositions.updatePosition(move.stackFromBoardPieceIndex, move.to);

            // Remove target pawn
            board.whitePawnsPositions.removePiece(move.stackToBoardPieceIndex);

            // Make the move on the board
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
            move.stackWhiteLongCastling = board.blackLongCastling;
            move.stackWhiteShortCastling = board.blackShortCastling;

            // Stack values
            move.stackFromBoardPieceIndex = board.boardPieceIndexes[move.from];
            move.stackFromBoardPiece = board.board[move.from];
            move.stackToBoardPieceIndex = board.boardPieceIndexes[move.to];
            move.stackToBoardPiece = board.board[move.to];

            // Move rook
            board.blackRooksPositions.updatePosition(move.stackFromBoardPieceIndex, move.to);

            // Remove the target piece
            switch (move.stackToBoardPiece) {
            case DragonbornEngineConstants.WHITE_PAWN:
                board.whitePawnsPositions.removePiece(move.stackToBoardPieceIndex);
                break;
            case DragonbornEngineConstants.WHITE_KNIGHT:
                board.whiteKnightsPositions.removePiece(move.stackToBoardPieceIndex);
                break;
            case DragonbornEngineConstants.WHITE_BISHOP:
                board.whiteBishopsPositions.removePiece(move.stackToBoardPieceIndex);
                break;
            case DragonbornEngineConstants.WHITE_ROOK:
                board.whiteRooksPositions.removePiece(move.stackToBoardPieceIndex);
                break;
            default:
                board.whiteQueensPositions.removePiece(move.stackToBoardPieceIndex);
            }

            // Make move on the board
            board.board[move.to] = move.stackFromBoardPiece;
            board.board[move.from] = 0;

            board.boardPieceIndexes[move.to] = move.stackFromBoardPieceIndex;
            board.boardPieceIndexes[move.from] = -1;

            // Check which castling we loose
            if (move.from == POS_BLACK_ROOK_SHORT_CASTLING_START) {
                board.blackShortCastling = false;
            } else if (move.from == POS_BLACK_ROOK_LONG_CASTLING_START) {
                board.blackLongCastling = false;
            }

            break;
        }
        case EngineMove.HIT_MOVE_CASTLING_AFFECTED_KING: {

            // Stack values
            move.stackWhiteLongCastling = board.blackLongCastling;
            move.stackWhiteShortCastling = board.blackShortCastling;

            // Stack values
            move.stackFromBoardPieceIndex = board.boardPieceIndexes[move.from];
            move.stackFromBoardPiece = board.board[move.from];
            move.stackToBoardPieceIndex = board.boardPieceIndexes[move.to];
            move.stackToBoardPiece = board.board[move.to];

            // Move king
            board.setBlackKingPos(move.from, move.to);
            // board.blackKingPos = move.to;

            // Remove the target piece
            switch (move.stackToBoardPiece) {
            case DragonbornEngineConstants.WHITE_PAWN:
                board.whitePawnsPositions.removePiece(move.stackToBoardPieceIndex);
                break;
            case DragonbornEngineConstants.WHITE_KNIGHT:
                board.whiteKnightsPositions.removePiece(move.stackToBoardPieceIndex);
                break;
            case DragonbornEngineConstants.WHITE_BISHOP:
                board.whiteBishopsPositions.removePiece(move.stackToBoardPieceIndex);
                break;
            case DragonbornEngineConstants.WHITE_ROOK:
                board.whiteRooksPositions.removePiece(move.stackToBoardPieceIndex);
                break;
            default:
                board.whiteQueensPositions.removePiece(move.stackToBoardPieceIndex);
            }

            // Make the move on the board
            board.board[move.to] = move.stackFromBoardPiece;
            board.board[move.from] = 0;

            board.boardPieceIndexes[move.to] = move.stackFromBoardPieceIndex;
            board.boardPieceIndexes[move.from] = -1;

            // Check which castling we loose
            board.blackLongCastling = false;
            board.blackShortCastling = false;

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

            // Move piece (movables are only pawn, knight, bishop and queens
            switch (move.stackFromBoardPiece) {
            case DragonbornEngineConstants.BLACK_PAWN:
                board.blackPawnsPositions.updatePosition(move.stackFromBoardPieceIndex, move.to);
                break;
            case DragonbornEngineConstants.BLACK_KNIGHT:
                board.blackKnightsPositions.updatePosition(move.stackFromBoardPieceIndex, move.to);
                break;
            case DragonbornEngineConstants.BLACK_BISHOP:
                board.blackBishopsPositions.updatePosition(move.stackFromBoardPieceIndex, move.to);
                break;
            default:
                board.blackQueensPositions.updatePosition(move.stackFromBoardPieceIndex, move.to);
            }

            // Remove the target piece
            switch (move.stackToBoardPiece) {
            case DragonbornEngineConstants.WHITE_PAWN:
                board.whitePawnsPositions.removePiece(move.stackToBoardPieceIndex);
                break;
            case DragonbornEngineConstants.WHITE_KNIGHT:
                board.whiteKnightsPositions.removePiece(move.stackToBoardPieceIndex);
                break;
            case DragonbornEngineConstants.WHITE_BISHOP:
                board.whiteBishopsPositions.removePiece(move.stackToBoardPieceIndex);
                break;
            case DragonbornEngineConstants.WHITE_ROOK:
                board.whiteRooksPositions.removePiece(move.stackToBoardPieceIndex);
                break;
            default:
                board.whiteQueensPositions.removePiece(move.stackToBoardPieceIndex);
            }

            // Make move on the board
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
            board.blackPawnsPositions.updatePosition(move.stackFromBoardPieceIndex, move.to);

            // Make the move on the board
            board.board[move.to] = move.stackFromBoardPiece;
            board.board[move.from] = 0;

            board.boardPieceIndexes[move.to] = move.stackFromBoardPieceIndex;
            board.boardPieceIndexes[move.from] = -1;

            board.enPassantField = move.epField;

            break;
        }
        case EngineMove.MOVE_CASTLING_AFFECTED_ROOK: {

            // Stack values
            move.stackWhiteLongCastling = board.blackLongCastling;
            move.stackWhiteShortCastling = board.blackShortCastling;

            move.stackFromBoardPieceIndex = board.boardPieceIndexes[move.from];
            move.stackFromBoardPiece = board.board[move.from];

            // Move rook
            board.blackRooksPositions.updatePosition(move.stackFromBoardPieceIndex, move.to);

            board.board[move.to] = move.stackFromBoardPiece;
            board.board[move.from] = 0;

            board.boardPieceIndexes[move.to] = move.stackFromBoardPieceIndex;
            board.boardPieceIndexes[move.from] = -1;

            // Check which castling we loose
            if (move.from == POS_BLACK_ROOK_SHORT_CASTLING_START) {
                board.blackShortCastling = false;
            } else if (move.from == POS_BLACK_ROOK_LONG_CASTLING_START) {
                board.blackLongCastling = false;
            }

            break;
        }
        case EngineMove.MOVE_CASTLING_AFFECTED_KING: {

            // Stack values
            move.stackWhiteLongCastling = board.blackLongCastling;
            move.stackWhiteShortCastling = board.blackShortCastling;

            move.stackFromBoardPieceIndex = board.boardPieceIndexes[move.from];
            move.stackFromBoardPiece = board.board[move.from];

            // Move king
            board.setBlackKingPos(move.from, move.to);
            // board.blackKingPos = move.to;

            board.board[move.to] = move.stackFromBoardPiece;
            board.board[move.from] = 0;

            board.boardPieceIndexes[move.to] = move.stackFromBoardPieceIndex;
            board.boardPieceIndexes[move.from] = -1;

            // Check which castling we loose
            board.blackLongCastling = false;
            board.blackShortCastling = false;

            break;
        }
        case EngineMove.MOVE: {

            // Stack values
            move.stackFromBoardPieceIndex = board.boardPieceIndexes[move.from];
            move.stackFromBoardPiece = board.board[move.from];

            // Move piece
            switch (move.stackFromBoardPiece) {
            case DragonbornEngineConstants.BLACK_PAWN:
                board.blackPawnsPositions.updatePosition(move.stackFromBoardPieceIndex, move.to);
                break;
            case DragonbornEngineConstants.BLACK_KNIGHT:
                board.blackKnightsPositions.updatePosition(move.stackFromBoardPieceIndex, move.to);
                break;
            case DragonbornEngineConstants.BLACK_BISHOP:
                board.blackBishopsPositions.updatePosition(move.stackFromBoardPieceIndex, move.to);
                break;
            default:
                board.blackQueensPositions.updatePosition(move.stackFromBoardPieceIndex, move.to);
            }

            board.board[move.to] = move.stackFromBoardPiece;
            board.board[move.from] = 0;

            board.boardPieceIndexes[move.to] = move.stackFromBoardPieceIndex;
            board.boardPieceIndexes[move.from] = -1;

            break;
        }
        case EngineMove.SHORT_CASTLING: {

            // Stack Values
            move.stackBlackLongCastling = board.blackLongCastling;
            move.stackCastlingRookIdx = board.boardPieceIndexes[POS_BLACK_ROOK_SHORT_CASTLING_START];

            // Move King
            board.setBlackKingPos(move.POS_BLACK_KING_CASTLING_START, move.POS_BLACK_KING_SHORT_CASTLING_END);
            //board.blackKingPos = POS_BLACK_KING_SHORT_CASTLING_END;

            board.board[POS_BLACK_KING_SHORT_CASTLING_END] = DragonbornEngineConstants.BLACK_KING;
            board.board[POS_BLACK_KING_CASTLING_START] = 0;
            // board.boardPieceIndexes[POS_BLACK_KING_SHORT_CASTLING_END] =
            // IDX_KING;
            // board.boardPieceIndexes[POS_BLACK_KING_CASTLING_START] = -1;

            // Move Rook
            board.blackRooksPositions.updatePosition(board.boardPieceIndexes[POS_BLACK_ROOK_SHORT_CASTLING_START], POS_BLACK_ROOK_SHORT_CASTLING_END);
            board.board[POS_BLACK_ROOK_SHORT_CASTLING_END] = DragonbornEngineConstants.BLACK_ROOK;
            board.board[POS_BLACK_ROOK_SHORT_CASTLING_START] = 0;
            board.boardPieceIndexes[POS_BLACK_ROOK_SHORT_CASTLING_END] = board.boardPieceIndexes[POS_BLACK_ROOK_SHORT_CASTLING_START];
            board.boardPieceIndexes[POS_BLACK_ROOK_SHORT_CASTLING_START] = -1;

            // No more castling on this board
            board.blackLongCastling = false;
            board.blackShortCastling = false;
            board.blackCastled = true;

            break;
        }
        case EngineMove.LONG_CASTLING: {

            // Stack Values
            move.stackBlackShortCastling = board.blackShortCastling;
            move.stackCastlingRookIdx = board.boardPieceIndexes[POS_BLACK_ROOK_LONG_CASTLING_START];

            // Move King
            board.setBlackKingPos(move.POS_BLACK_KING_CASTLING_START, move.POS_BLACK_KING_LONG_CASTLING_END);
            // board.blackKingPos = POS_BLACK_KING_LONG_CASTLING_END;

            board.board[POS_BLACK_KING_LONG_CASTLING_END] = DragonbornEngineConstants.BLACK_KING;
            board.board[POS_BLACK_KING_CASTLING_START] = 0;
            // board.boardPieceIndexes[POS_BLACK_KING_LONG_CASTLING_END] =
            // IDX_KING;
            // board.boardPieceIndexes[POS_BLACK_KING_CASTLING_START] = -1;

            // Move Rook
            board.blackRooksPositions.updatePosition(board.boardPieceIndexes[POS_BLACK_ROOK_LONG_CASTLING_START], POS_BLACK_ROOK_LONG_CASTLING_END);
            board.board[POS_BLACK_ROOK_LONG_CASTLING_END] = DragonbornEngineConstants.BLACK_ROOK;
            board.board[POS_BLACK_ROOK_LONG_CASTLING_START] = 0;
            board.boardPieceIndexes[POS_BLACK_ROOK_LONG_CASTLING_END] = board.boardPieceIndexes[POS_BLACK_ROOK_LONG_CASTLING_START];
            board.boardPieceIndexes[POS_BLACK_ROOK_LONG_CASTLING_START] = -1;

            // No more castling on this board ;-)
            board.blackLongCastling = false;
            board.blackShortCastling = false;
            board.blackCastled = true;

            break;
        }

        }

    }

    public final void undoMoveBlack(RobustBoard board, EngineMove move) {
        // board.colorToMove = BLACK;

        switch (move.moveType) {
        case EngineMove.HIT_MOVE_PROMOTION: {
            // Unstack values
            board.board[move.from] = move.stackFromBoardPiece;
            board.board[move.to] = move.stackToBoardPiece;

            board.boardPieceIndexes[move.from] = move.stackFromBoardPieceIndex;

            // Add pawn back
            board.blackPawnsPositions.insertPiece(move.stackFromBoardPieceIndex, move.from);

            // Remove promoted piece again
            RobustBoardPiecePositions newPiecePosition;
            switch (move.promotionPiece) {
            case DragonbornEngineConstants.PIECE_KNIGHT:
                newPiecePosition = board.blackKnightsPositions;
                break;
            case DragonbornEngineConstants.PIECE_BISHOP:
                newPiecePosition = board.blackBishopsPositions;
                break;
            case DragonbornEngineConstants.PIECE_ROOK:
                newPiecePosition = board.blackRooksPositions;
                break;
            default:
                newPiecePosition = board.blackQueensPositions;
            }
            newPiecePosition.removePiece(move.stackPromotedPieceIndex);

            board.boardPieceIndexes[move.to] = move.stackToBoardPieceIndex;

            // Re Add target piece
            switch (move.stackToBoardPiece) {
            case DragonbornEngineConstants.WHITE_PAWN:
                board.whitePawnsPositions.insertPiece(move.stackToBoardPieceIndex, move.to);
                break;
            case DragonbornEngineConstants.WHITE_KNIGHT:
                board.whiteKnightsPositions.insertPiece(move.stackToBoardPieceIndex, move.to);
                break;
            case DragonbornEngineConstants.WHITE_BISHOP:
                board.whiteBishopsPositions.insertPiece(move.stackToBoardPieceIndex, move.to);
                break;
            case DragonbornEngineConstants.WHITE_ROOK:
                board.whiteRooksPositions.insertPiece(move.stackToBoardPieceIndex, move.to);
                break;
            default:
                board.whiteQueensPositions.insertPiece(move.stackToBoardPieceIndex, move.to);
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
            board.blackPawnsPositions.insertPiece(move.stackFromBoardPieceIndex, move.from);

            // Remove promoted piece
            RobustBoardPiecePositions newPiecePosition;
            switch (move.promotionPiece) {
            case DragonbornEngineConstants.PIECE_KNIGHT:
                newPiecePosition = board.blackKnightsPositions;
                break;
            case DragonbornEngineConstants.PIECE_BISHOP:
                newPiecePosition = board.blackBishopsPositions;
                break;
            case DragonbornEngineConstants.PIECE_ROOK:
                newPiecePosition = board.blackRooksPositions;
                break;
            default:
                newPiecePosition = board.blackQueensPositions;
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
            board.blackPawnsPositions.updatePosition(move.stackFromBoardPieceIndex, move.from);

            // Re Add target pawn
            board.whitePawnsPositions.insertPiece(move.stackToBoardPieceIndex, move.epHitPawn);

            break;

        }
        case EngineMove.HIT_MOVE_CASTLING_AFFECTED_ROOK: {
            board.blackLongCastling = move.stackWhiteLongCastling;
            board.blackShortCastling = move.stackWhiteShortCastling;

            // Unstack values
            board.board[move.from] = move.stackFromBoardPiece;
            board.board[move.to] = move.stackToBoardPiece;

            board.boardPieceIndexes[move.from] = move.stackFromBoardPieceIndex;
            board.boardPieceIndexes[move.to] = move.stackToBoardPieceIndex;

            // Move rook back
            board.blackRooksPositions.updatePosition(move.stackFromBoardPieceIndex, move.from);

            // Re Add target piece
            switch (move.stackToBoardPiece) {
            case DragonbornEngineConstants.WHITE_PAWN:
                board.whitePawnsPositions.insertPiece(move.stackToBoardPieceIndex, move.to);
                break;
            case DragonbornEngineConstants.WHITE_KNIGHT:
                board.whiteKnightsPositions.insertPiece(move.stackToBoardPieceIndex, move.to);
                break;
            case DragonbornEngineConstants.WHITE_BISHOP:
                board.whiteBishopsPositions.insertPiece(move.stackToBoardPieceIndex, move.to);
                break;
            case DragonbornEngineConstants.WHITE_ROOK:
                board.whiteRooksPositions.insertPiece(move.stackToBoardPieceIndex, move.to);
                break;
            default:
                board.whiteQueensPositions.insertPiece(move.stackToBoardPieceIndex, move.to);
            }

            break;
        }
        case EngineMove.HIT_MOVE_CASTLING_AFFECTED_KING: {
            board.blackLongCastling = move.stackWhiteLongCastling;
            board.blackShortCastling = move.stackWhiteShortCastling;

            // Unstack values
            board.board[move.from] = move.stackFromBoardPiece;
            board.board[move.to] = move.stackToBoardPiece;

            board.boardPieceIndexes[move.from] = move.stackFromBoardPieceIndex;
            board.boardPieceIndexes[move.to] = move.stackToBoardPieceIndex;

            // Move king back
            board.setBlackKingPos(move.to, move.from);
            // board.blackKingPos = move.from;

            // Re Add target piece
            switch (move.stackToBoardPiece) {
            case DragonbornEngineConstants.WHITE_PAWN:
                board.whitePawnsPositions.insertPiece(move.stackToBoardPieceIndex, move.to);
                break;
            case DragonbornEngineConstants.WHITE_KNIGHT:
                board.whiteKnightsPositions.insertPiece(move.stackToBoardPieceIndex, move.to);
                break;
            case DragonbornEngineConstants.WHITE_BISHOP:
                board.whiteBishopsPositions.insertPiece(move.stackToBoardPieceIndex, move.to);
                break;
            case DragonbornEngineConstants.WHITE_ROOK:
                board.whiteRooksPositions.insertPiece(move.stackToBoardPieceIndex, move.to);
                break;
            default:
                board.whiteQueensPositions.insertPiece(move.stackToBoardPieceIndex, move.to);
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
            case DragonbornEngineConstants.BLACK_PAWN:
                board.blackPawnsPositions.updatePosition(move.stackFromBoardPieceIndex, move.from);
                break;
            case DragonbornEngineConstants.BLACK_KNIGHT:
                board.blackKnightsPositions.updatePosition(move.stackFromBoardPieceIndex, move.from);
                break;
            case DragonbornEngineConstants.BLACK_BISHOP:
                board.blackBishopsPositions.updatePosition(move.stackFromBoardPieceIndex, move.from);
                break;
            default:
                board.blackQueensPositions.updatePosition(move.stackFromBoardPieceIndex, move.from);
            }

            // Re Add target piece
            switch (move.stackToBoardPiece) {
            case DragonbornEngineConstants.WHITE_PAWN:
                board.whitePawnsPositions.insertPiece(move.stackToBoardPieceIndex, move.to);
                break;
            case DragonbornEngineConstants.WHITE_KNIGHT:
                board.whiteKnightsPositions.insertPiece(move.stackToBoardPieceIndex, move.to);
                break;
            case DragonbornEngineConstants.WHITE_BISHOP:
                board.whiteBishopsPositions.insertPiece(move.stackToBoardPieceIndex, move.to);
                break;
            case DragonbornEngineConstants.WHITE_ROOK:
                board.whiteRooksPositions.insertPiece(move.stackToBoardPieceIndex, move.to);
                break;
            default:
                board.whiteQueensPositions.insertPiece(move.stackToBoardPieceIndex, move.to);
            }

            break;
        }
        case EngineMove.MOVE_CASTLING_AFFECTED_ROOK: {
            board.blackLongCastling = move.stackWhiteLongCastling;
            board.blackShortCastling = move.stackWhiteShortCastling;

            // Unstack values
            board.board[move.from] = move.stackFromBoardPiece;
            board.board[move.to] = 0;

            board.boardPieceIndexes[move.from] = move.stackFromBoardPieceIndex;
            board.boardPieceIndexes[move.to] = -1;

            // Move rook back
            board.blackRooksPositions.updatePosition(move.stackFromBoardPieceIndex, move.from);

            break;
        }
        case EngineMove.MOVE_CASTLING_AFFECTED_KING: {
            board.blackLongCastling = move.stackWhiteLongCastling;
            board.blackShortCastling = move.stackWhiteShortCastling;

            // Unstack values
            board.board[move.from] = move.stackFromBoardPiece;
            board.board[move.to] = 0;

            board.boardPieceIndexes[move.from] = move.stackFromBoardPieceIndex;
            board.boardPieceIndexes[move.to] = -1;

            // Move king back
            board.setBlackKingPos(move.to, move.from);
            // board.blackKingPos = move.from;

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
            case DragonbornEngineConstants.BLACK_PAWN:
                board.blackPawnsPositions.updatePosition(move.stackFromBoardPieceIndex, move.from);
                break;
            case DragonbornEngineConstants.BLACK_KNIGHT:
                board.blackKnightsPositions.updatePosition(move.stackFromBoardPieceIndex, move.from);
                break;
            case DragonbornEngineConstants.BLACK_BISHOP:
                board.blackBishopsPositions.updatePosition(move.stackFromBoardPieceIndex, move.from);
                break;
            default:
                board.blackQueensPositions.updatePosition(move.stackFromBoardPieceIndex, move.from);
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
            board.blackPawnsPositions.updatePosition(move.stackFromBoardPieceIndex, move.from);

            break;
        }
        case EngineMove.SHORT_CASTLING: {
            // Unstack Values
            board.blackLongCastling = move.stackBlackLongCastling;
            board.blackShortCastling = true;
            board.blackCastled = false;

            // Move King
            board.setBlackKingPos(POS_BLACK_KING_SHORT_CASTLING_END, POS_BLACK_KING_CASTLING_START);
            // board.blackKingPos = POS_BLACK_KING_CASTLING_START;


            board.board[POS_BLACK_KING_CASTLING_START] = DragonbornEngineConstants.BLACK_KING;
            board.board[POS_BLACK_KING_SHORT_CASTLING_END] = 0;

            // Move Rook
            board.blackRooksPositions.updatePosition(move.stackCastlingRookIdx, POS_BLACK_ROOK_SHORT_CASTLING_START);
            board.board[POS_BLACK_ROOK_SHORT_CASTLING_START] = DragonbornEngineConstants.BLACK_ROOK;
            board.board[POS_BLACK_ROOK_SHORT_CASTLING_END] = 0;
            board.boardPieceIndexes[POS_BLACK_ROOK_SHORT_CASTLING_START] = move.stackCastlingRookIdx;
            board.boardPieceIndexes[POS_BLACK_ROOK_SHORT_CASTLING_END] = -1;

            break;
        }
        case EngineMove.LONG_CASTLING: {
            // Unstack Values
            board.blackShortCastling = move.stackBlackShortCastling;
            board.blackLongCastling = true;
            board.blackCastled = false;

            // Move King
            board.setBlackKingPos(POS_BLACK_KING_LONG_CASTLING_END, POS_BLACK_KING_CASTLING_START);

            // board.blackKingPos = POS_BLACK_KING_CASTLING_START;

            board.board[POS_BLACK_KING_CASTLING_START] = DragonbornEngineConstants.BLACK_KING;
            board.board[POS_BLACK_KING_LONG_CASTLING_END] = 0;

            // Move Rook
            board.blackRooksPositions.updatePosition(move.stackCastlingRookIdx, POS_BLACK_ROOK_LONG_CASTLING_START);
            board.board[POS_BLACK_ROOK_LONG_CASTLING_START] = DragonbornEngineConstants.BLACK_ROOK;
            board.board[POS_BLACK_ROOK_LONG_CASTLING_END] = 0;
            board.boardPieceIndexes[POS_BLACK_ROOK_LONG_CASTLING_START] = move.stackCastlingRookIdx;
            board.boardPieceIndexes[POS_BLACK_ROOK_LONG_CASTLING_END] = -1;

            break;
        }

        }

        board.enPassantField = move.stackEnPassantField;
//        board.zobristKey = move.stackZobristKey;

    }

}
