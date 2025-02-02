package ch.nostromo.tiffanys.engine.dragonborn.move.impl.fast;

import ch.nostromo.tiffanys.engine.dragonborn.board.RobustBoard;
import ch.nostromo.tiffanys.engine.dragonborn.board.impl.fast.TiffanysBoardInterfaceBlack;
import ch.nostromo.tiffanys.engine.dragonborn.board.impl.fast.TiffanysBoardInterfaceWhite;
import ch.nostromo.tiffanys.engine.dragonborn.move.EngineMove;
import ch.nostromo.tiffanys.engine.dragonborn.DragonbornConstants;

public class FastMoveGen implements DragonbornConstants {

    TiffanysBoardInterfaceWhite tiffanysBoardInterfaceWhite = new TiffanysBoardInterfaceWhite();
    TiffanysBoardInterfaceBlack tiffanysBoardInterfaceBlack = new TiffanysBoardInterfaceBlack();

    // N,S,W,E,NW,NE,SW,SE

    private final int getWhiteHitScore(int piece) {
        switch (piece) {
        case BLACK_PAWN: {
            return HIT_PAWN_SCORE;
        }
        case BLACK_KNIGHT: {
            return HIT_KNIGHT_SCORE;
        }
        case BLACK_BISHOP: {
            return HIT_BISHOP_SCORE;
        }
        case BLACK_ROOK: {
            return HIT_ROOK_SCORE;
        }
        case BLACK_QUEEN: {
            return HIT_QUEEN_SCORE;
        }
        default:
            return 0;
        }
    }

    private final int getBlackHitScore(int piece) {
        switch (piece) {
        case WHITE_PAWN: {
            return HIT_PAWN_SCORE;
        }
        case WHITE_KNIGHT: {
            return HIT_KNIGHT_SCORE;
        }
        case WHITE_BISHOP: {
            return HIT_BISHOP_SCORE;
        }
        case WHITE_ROOK: {
            return HIT_ROOK_SCORE;
        }
        case WHITE_QUEEN: {
            return HIT_QUEEN_SCORE;
        }
        default:
            return 0;
        }
    }

    public final int generateWhiteMovesList(RobustBoard tiffanysBoard, EngineMove[] moves) {
        int moveIdx = 0;
        int from = -1;
        int to = -1;
        int dirCount = 0;

        // PAWNS
        for (int i = 0; i < tiffanysBoard.whitePawnsPositions.pieceCount; i++) {
            from = tiffanysBoard.whitePawnsPositions.positions[i];

            // One step forward ?
            to = from + 8;
            if (tiffanysBoard.board[to] == 0) {
                if (to < 56) {
                    moves[moveIdx].setMove(from, to);
                    moveIdx++;
                } else {
                    moves[moveIdx].setPromotionMoveQueen(from, to);
                    moveIdx++;
                    moves[moveIdx].setPromotionMoveRook(from, to);
                    moveIdx++;
                    moves[moveIdx].setPromotionMoveBishop(from, to);
                    moveIdx++;
                    moves[moveIdx].setPromotionMoveKnight(from, to);
                    moveIdx++;
                }
            }

            to = from + 16;
            // Two step forward ?
            if (from <= 15 && tiffanysBoard.board[from + 8] == 0 && tiffanysBoard.board[to] == 0) {
                moves[moveIdx].setMove(from, to, from + 8);
                moveIdx++;
            }

            // Capture move ?
            // To left
            to = FastMoveGenPatterns.WHITE_PAWN_HIT_MOVES[from][0];
            if (to >= 0) {
                // to = from + 7;
                if (tiffanysBoard.board[to] < 0) {
                    if (to < 56) {
                        moves[moveIdx].setHitMovePawn(from, to, getWhiteHitScore(tiffanysBoard.board[to]));
                        moveIdx++;
                    } else {
                        moves[moveIdx].setPromotionHitMoveQueen(from, to, getWhiteHitScore(tiffanysBoard.board[to]));
                        moveIdx++;
                        moves[moveIdx].setPromotionHitMoveRook(from, to, getWhiteHitScore(tiffanysBoard.board[to]));
                        moveIdx++;
                        moves[moveIdx].setPromotionHitMoveBishop(from, to, getWhiteHitScore(tiffanysBoard.board[to]));
                        moveIdx++;
                        moves[moveIdx].setPromotionHitMoveKnight(from, to, getWhiteHitScore(tiffanysBoard.board[to]));
                        moveIdx++;
                    }
                } else if (tiffanysBoard.enPassantField == to && tiffanysBoard.board[to] == 0) {
                    moves[moveIdx].setEnpassantMove(from, to, to - 8);
                    moveIdx++;
                }
            }

            to = FastMoveGenPatterns.WHITE_PAWN_HIT_MOVES[from][1];
            if (to >= 0) {
                // to = from + 7;
                if (tiffanysBoard.board[to] < 0) {
                    if (to < 56) {
                        moves[moveIdx].setHitMovePawn(from, to, getWhiteHitScore(tiffanysBoard.board[to]));
                        moveIdx++;
                    } else {
                        moves[moveIdx].setPromotionHitMoveQueen(from, to, getWhiteHitScore(tiffanysBoard.board[to]));
                        moveIdx++;
                        moves[moveIdx].setPromotionHitMoveRook(from, to, getWhiteHitScore(tiffanysBoard.board[to]));
                        moveIdx++;
                        moves[moveIdx].setPromotionHitMoveBishop(from, to, getWhiteHitScore(tiffanysBoard.board[to]));
                        moveIdx++;
                        moves[moveIdx].setPromotionHitMoveKnight(from, to, getWhiteHitScore(tiffanysBoard.board[to]));
                        moveIdx++;
                    }
                } else if (tiffanysBoard.enPassantField == to && tiffanysBoard.board[to] == 0) {
                    moves[moveIdx].setEnpassantMove(from, to, to - 8);
                    moveIdx++;
                }
            }

        }

        // KING
        from = tiffanysBoard.whiteKingPos;
        dirCount = FastMoveGenPatterns.KING_MOVES[from].length;
        for (int i = 0; i < dirCount; i++) {
            to = FastMoveGenPatterns.KING_MOVES[from][i];

            if (tiffanysBoard.board[to] == 0) {
                moves[moveIdx].setMoveKing(from, to);
                moveIdx++;
            } else if (tiffanysBoard.board[to] < 0) {
                moves[moveIdx].setHitMoveKing(from, to, getWhiteHitScore(tiffanysBoard.board[to]));
                moveIdx++;
            }

        }

        // long castling
        if (tiffanysBoard.whiteShortCastling && tiffanysBoard.board[4] == WHITE_KING && tiffanysBoard.board[7] == WHITE_ROOK && tiffanysBoard.board[5] == 0 && tiffanysBoard.board[6] == 0
                && !tiffanysBoardInterfaceWhite.isWhitePieceAttacked(tiffanysBoard, 4) && !tiffanysBoardInterfaceWhite.isWhitePieceAttacked(tiffanysBoard, 5) && !tiffanysBoardInterfaceWhite.isWhitePieceAttacked(tiffanysBoard, 6)) {
            moves[moveIdx].setCastlingMoveShort(4, 6);
            moveIdx++;
        }

        // short castling
        if (tiffanysBoard.whiteLongCastling && tiffanysBoard.board[4] == WHITE_KING && tiffanysBoard.board[0] == WHITE_ROOK && tiffanysBoard.board[1] == 0 && tiffanysBoard.board[2] == 0 && tiffanysBoard.board[3] == 0
                && !tiffanysBoardInterfaceWhite.isWhitePieceAttacked(tiffanysBoard, 2) && !tiffanysBoardInterfaceWhite.isWhitePieceAttacked(tiffanysBoard, 3) && !tiffanysBoardInterfaceWhite.isWhitePieceAttacked(tiffanysBoard, 4)) {
            moves[moveIdx].setCastlingMoveLong(4, 2);
            moveIdx++;
        }

        // KNIGHTS
        for (int pieceCount = 0; pieceCount < tiffanysBoard.whiteKnightsPositions.pieceCount; pieceCount++) {

            from = tiffanysBoard.whiteKnightsPositions.positions[pieceCount];
            dirCount = FastMoveGenPatterns.KNIGHT_MOVES[from].length;
            for (int i = 0; i < dirCount; i++) {
                to = FastMoveGenPatterns.KNIGHT_MOVES[from][i];

                if (tiffanysBoard.board[to] == 0) {
                    moves[moveIdx].setMove(from, to);
                    moveIdx++;
                } else if (tiffanysBoard.board[to] < 0) {
                    moves[moveIdx].setHitMoveKnight(from, to, getWhiteHitScore(tiffanysBoard.board[to]));
                    moveIdx++;
                }

            }

        }

        // QUEEN
        for (int pieceCount = 0; pieceCount < tiffanysBoard.whiteQueensPositions.pieceCount; pieceCount++) {
            from = tiffanysBoard.whiteQueensPositions.positions[pieceCount];

            // NORTH
            for (int step = 1; step <= FastMoveGenPatterns.QUEEN_MOVES[from][NORTH]; step++) {
                to = from + (step * 8);
                if (tiffanysBoard.board[to] == 0) {
                    moves[moveIdx].setMove(from, to);
                    moveIdx++;
                } else if (tiffanysBoard.board[to] < 0) {
                    moves[moveIdx].setHitMoveQueen(from, to, getWhiteHitScore(tiffanysBoard.board[to]));
                    moveIdx++;
                    break;
                } else {
                    break;
                }
            }

            // SOUTH
            for (int step = 1; step <= FastMoveGenPatterns.QUEEN_MOVES[from][SOUTH]; step++) {
                to = from - (step * 8);
                if (tiffanysBoard.board[to] == 0) {
                    moves[moveIdx].setMove(from, to);
                    moveIdx++;
                } else if (tiffanysBoard.board[to] < 0) {
                    moves[moveIdx].setHitMoveQueen(from, to, getWhiteHitScore(tiffanysBoard.board[to]));
                    moveIdx++;
                    break;
                } else {
                    break;
                }
            }

            // WEST
            for (int step = 1; step <= FastMoveGenPatterns.QUEEN_MOVES[from][WEST]; step++) {
                to = from - step;
                if (tiffanysBoard.board[to] == 0) {
                    moves[moveIdx].setMove(from, to);
                    moveIdx++;
                } else if (tiffanysBoard.board[to] < 0) {
                    moves[moveIdx].setHitMoveQueen(from, to, getWhiteHitScore(tiffanysBoard.board[to]));
                    moveIdx++;
                    break;
                } else {
                    break;
                }
            }

            // EAST
            for (int step = 1; step <= FastMoveGenPatterns.QUEEN_MOVES[from][EAST]; step++) {
                to = from + step;
                if (tiffanysBoard.board[to] == 0) {
                    moves[moveIdx].setMove(from, to);
                    moveIdx++;
                } else if (tiffanysBoard.board[to] < 0) {
                    moves[moveIdx].setHitMoveQueen(from, to, getWhiteHitScore(tiffanysBoard.board[to]));
                    moveIdx++;
                    break;
                } else {
                    break;
                }
            }

            // NORTHWEST
            for (int step = 1; step <= FastMoveGenPatterns.QUEEN_MOVES[from][NORTHWEST]; step++) {
                to = from + (step * 7);
                if (tiffanysBoard.board[to] == 0) {
                    moves[moveIdx].setMove(from, to);
                    moveIdx++;
                } else if (tiffanysBoard.board[to] < 0) {
                    moves[moveIdx].setHitMoveQueen(from, to, getWhiteHitScore(tiffanysBoard.board[to]));
                    moveIdx++;
                    break;
                } else {
                    break;
                }
            }

            // NORTHEAST
            for (int step = 1; step <= FastMoveGenPatterns.QUEEN_MOVES[from][NORTHEAST]; step++) {
                to = from + (step * 9);
                if (tiffanysBoard.board[to] == 0) {
                    moves[moveIdx].setMove(from, to);
                    moveIdx++;
                } else if (tiffanysBoard.board[to] < 0) {
                    moves[moveIdx].setHitMoveQueen(from, to, getWhiteHitScore(tiffanysBoard.board[to]));
                    moveIdx++;
                    break;
                } else {
                    break;
                }
            }

            // SOUTHWEST
            for (int step = 1; step <= FastMoveGenPatterns.QUEEN_MOVES[from][SOUTHWEST]; step++) {
                to = from - (step * 9);
                if (tiffanysBoard.board[to] == 0) {
                    moves[moveIdx].setMove(from, to);
                    moveIdx++;
                } else if (tiffanysBoard.board[to] < 0) {
                    moves[moveIdx].setHitMoveQueen(from, to, getWhiteHitScore(tiffanysBoard.board[to]));
                    moveIdx++;
                    break;
                } else {
                    break;
                }
            }

            // SOUTHEAST
            for (int step = 1; step <= FastMoveGenPatterns.QUEEN_MOVES[from][SOUTHEAST]; step++) {
                to = from - (step * 7);
                if (tiffanysBoard.board[to] == 0) {
                    moves[moveIdx].setMove(from, to);
                    moveIdx++;
                } else if (tiffanysBoard.board[to] < 0) {
                    moves[moveIdx].setHitMoveQueen(from, to, getWhiteHitScore(tiffanysBoard.board[to]));
                    moveIdx++;
                    break;
                } else {
                    break;
                }
            }

        }

        // ROOK
        for (int pieceCount = 0; pieceCount < tiffanysBoard.whiteRooksPositions.pieceCount; pieceCount++) {
            from = tiffanysBoard.whiteRooksPositions.positions[pieceCount];

            // NORTH
            for (int step = 1; step <= FastMoveGenPatterns.ROOK_MOVES[from][NORTH]; step++) {
                to = from + (step * 8);
                if (tiffanysBoard.board[to] == 0) {
                    moves[moveIdx].setMoveRook(from, to);
                    moveIdx++;
                } else if (tiffanysBoard.board[to] < 0) {
                    moves[moveIdx].setHitMoveRook(from, to, getWhiteHitScore(tiffanysBoard.board[to]));
                    moveIdx++;
                    break;
                } else {
                    break;
                }
            }

            // SOUTH
            for (int step = 1; step <= FastMoveGenPatterns.ROOK_MOVES[from][SOUTH]; step++) {
                to = from - (step * 8);
                if (tiffanysBoard.board[to] == 0) {
                    moves[moveIdx].setMoveRook(from, to);
                    moveIdx++;
                } else if (tiffanysBoard.board[to] < 0) {
                    moves[moveIdx].setHitMoveRook(from, to, getWhiteHitScore(tiffanysBoard.board[to]));
                    moveIdx++;
                    break;
                } else {
                    break;
                }
            }

            // WEST
            for (int step = 1; step <= FastMoveGenPatterns.ROOK_MOVES[from][WEST]; step++) {
                to = from - step;
                if (tiffanysBoard.board[to] == 0) {
                    moves[moveIdx].setMoveRook(from, to);
                    moveIdx++;
                } else if (tiffanysBoard.board[to] < 0) {
                    moves[moveIdx].setHitMoveRook(from, to, getWhiteHitScore(tiffanysBoard.board[to]));
                    moveIdx++;
                    break;
                } else {
                    break;
                }
            }

            // EAST
            for (int step = 1; step <= FastMoveGenPatterns.ROOK_MOVES[from][EAST]; step++) {
                to = from + step;
                if (tiffanysBoard.board[to] == 0) {
                    moves[moveIdx].setMoveRook(from, to);
                    moveIdx++;
                } else if (tiffanysBoard.board[to] < 0) {
                    moves[moveIdx].setHitMoveRook(from, to, getWhiteHitScore(tiffanysBoard.board[to]));
                    moveIdx++;
                    break;
                } else {
                    break;
                }
            }

        }

        // BISHOP
        for (int pieceCount = 0; pieceCount < tiffanysBoard.whiteBishopsPositions.pieceCount; pieceCount++) {
            from = tiffanysBoard.whiteBishopsPositions.positions[pieceCount];

            // NORTHWEST
            for (int step = 1; step <= FastMoveGenPatterns.BISHOP_MOVES[from][NORTHWEST]; step++) {
                to = from + (step * 7);
                if (tiffanysBoard.board[to] == 0) {
                    moves[moveIdx].setMove(from, to);
                    moveIdx++;
                } else if (tiffanysBoard.board[to] < 0) {
                    moves[moveIdx].setHitMoveBishop(from, to, getWhiteHitScore(tiffanysBoard.board[to]));
                    moveIdx++;
                    break;
                } else {
                    break;
                }
            }

            // NORTHEAST
            for (int step = 1; step <= FastMoveGenPatterns.BISHOP_MOVES[from][NORTHEAST]; step++) {
                to = from + (step * 9);
                if (tiffanysBoard.board[to] == 0) {
                    moves[moveIdx].setMove(from, to);
                    moveIdx++;
                } else if (tiffanysBoard.board[to] < 0) {
                    moves[moveIdx].setHitMoveBishop(from, to, getWhiteHitScore(tiffanysBoard.board[to]));
                    moveIdx++;
                    break;
                } else {
                    break;
                }
            }

            // SOUTHWEST
            for (int step = 1; step <= FastMoveGenPatterns.BISHOP_MOVES[from][SOUTHWEST]; step++) {
                to = from - (step * 9);
                if (tiffanysBoard.board[to] == 0) {
                    moves[moveIdx].setMove(from, to);
                    moveIdx++;
                } else if (tiffanysBoard.board[to] < 0) {
                    moves[moveIdx].setHitMoveBishop(from, to, getWhiteHitScore(tiffanysBoard.board[to]));
                    moveIdx++;
                    break;
                } else {
                    break;
                }
            }

            // SOUTHEAST
            for (int step = 1; step <= FastMoveGenPatterns.BISHOP_MOVES[from][SOUTHEAST]; step++) {
                to = from - (step * 7);
                if (tiffanysBoard.board[to] == 0) {
                    moves[moveIdx].setMove(from, to);
                    moveIdx++;
                } else if (tiffanysBoard.board[to] < 0) {
                    moves[moveIdx].setHitMoveBishop(from, to, getWhiteHitScore(tiffanysBoard.board[to]));
                    moveIdx++;
                    break;
                } else {
                    break;
                }
            }

        }

        return moveIdx;

    }

    public final int generateBlackMovesList(RobustBoard tiffanysBoard, EngineMove[] moves) {
        int moveIdx = 0;
        int from = -1;
        int to = -1;
        int dirCount = 0;

        // PAWNS
        for (int i = 0; i < tiffanysBoard.blackPawnsPositions.pieceCount; i++) {
            from = tiffanysBoard.blackPawnsPositions.positions[i];

            // One step forward ?
            to = from - 8;
            if (tiffanysBoard.board[to] == 0) {
                if (to > 7) {
                    moves[moveIdx].setMove(from, to);
                    moveIdx++;
                } else {
                    moves[moveIdx].setPromotionMoveQueen(from, to);
                    moveIdx++;
                    moves[moveIdx].setPromotionMoveRook(from, to);
                    moveIdx++;
                    moves[moveIdx].setPromotionMoveBishop(from, to);
                    moveIdx++;
                    moves[moveIdx].setPromotionMoveKnight(from, to);
                    moveIdx++;
                }

            }

            to = from - 16;
            // Two step forward ?
            if (from >= 48 && tiffanysBoard.board[from - 8] == 0 && tiffanysBoard.board[to] == 0) {
                moves[moveIdx].setMove(from, to, from - 8);
                moveIdx++;
            }

            // Capture move ?
            // To left

            to = FastMoveGenPatterns.BLACK_PAWN_HIT_MOVES[from][0];
            if (to >= 0) {
                // to = from + 7;
                if (tiffanysBoard.board[to] > 0) {
                    if (to > 7) {
                        moves[moveIdx].setHitMovePawn(from, to, getBlackHitScore(tiffanysBoard.board[to]));
                        moveIdx++;
                    } else {
                        moves[moveIdx].setPromotionHitMoveQueen(from, to, getBlackHitScore(tiffanysBoard.board[to]));
                        moveIdx++;
                        moves[moveIdx].setPromotionHitMoveRook(from, to, getBlackHitScore(tiffanysBoard.board[to]));
                        moveIdx++;
                        moves[moveIdx].setPromotionHitMoveBishop(from, to, getBlackHitScore(tiffanysBoard.board[to]));
                        moveIdx++;
                        moves[moveIdx].setPromotionHitMoveKnight(from, to, getBlackHitScore(tiffanysBoard.board[to]));
                        moveIdx++;
                    }
                } else if (tiffanysBoard.enPassantField == to && tiffanysBoard.board[to] == 0) {
                    moves[moveIdx].setEnpassantMove(from, to, to + 8);
                    moveIdx++;
                }
            }

            // To right
            to = FastMoveGenPatterns.BLACK_PAWN_HIT_MOVES[from][1];
            if (to >= 0) {
                // to = from + 7;
                if (tiffanysBoard.board[to] > 0) {
                    if (to > 7) {
                        moves[moveIdx].setHitMovePawn(from, to, getBlackHitScore(tiffanysBoard.board[to]));
                        moveIdx++;
                    } else {
                        moves[moveIdx].setPromotionHitMoveQueen(from, to, getBlackHitScore(tiffanysBoard.board[to]));
                        moveIdx++;
                        moves[moveIdx].setPromotionHitMoveRook(from, to, getBlackHitScore(tiffanysBoard.board[to]));
                        moveIdx++;
                        moves[moveIdx].setPromotionHitMoveBishop(from, to, getBlackHitScore(tiffanysBoard.board[to]));
                        moveIdx++;
                        moves[moveIdx].setPromotionHitMoveKnight(from, to, getBlackHitScore(tiffanysBoard.board[to]));
                        moveIdx++;
                    }
                } else if (tiffanysBoard.enPassantField == to && tiffanysBoard.board[to] == 0) {
                    moves[moveIdx].setEnpassantMove(from, to, to + 8);
                    moveIdx++;

                }
            }

        }

        // KING
        from = tiffanysBoard.blackKingPos;
        dirCount = FastMoveGenPatterns.KING_MOVES[from].length;
        for (int i = 0; i < dirCount; i++) {
            to = FastMoveGenPatterns.KING_MOVES[from][i];

            if (tiffanysBoard.board[to] == 0) {
                moves[moveIdx].setMoveKing(from, to);
                moveIdx++;
            } else if (tiffanysBoard.board[to] > 0) {
                moves[moveIdx].setHitMoveKing(from, to, getBlackHitScore(tiffanysBoard.board[to]));
                moveIdx++;
            }

        }

        // long castling
        if (tiffanysBoard.blackShortCastling && tiffanysBoard.board[60] == BLACK_KING && tiffanysBoard.board[63] == BLACK_ROOK && tiffanysBoard.board[62] == 0 && tiffanysBoard.board[61] == 0
                && !tiffanysBoardInterfaceBlack.isBlackPieceAttacked(tiffanysBoard, 60) && !tiffanysBoardInterfaceBlack.isBlackPieceAttacked(tiffanysBoard, 61) && !tiffanysBoardInterfaceBlack.isBlackPieceAttacked(tiffanysBoard, 62)) {
            moves[moveIdx].setCastlingMoveShort(60, 62);
            moveIdx++;
        }

        // short castling
        if (tiffanysBoard.blackLongCastling && tiffanysBoard.board[60] == BLACK_KING && tiffanysBoard.board[56] == BLACK_ROOK && tiffanysBoard.board[57] == 0 && tiffanysBoard.board[58] == 0 && tiffanysBoard.board[59] == 0
                && !tiffanysBoardInterfaceBlack.isBlackPieceAttacked(tiffanysBoard, 58) && !tiffanysBoardInterfaceBlack.isBlackPieceAttacked(tiffanysBoard, 59) && !tiffanysBoardInterfaceBlack.isBlackPieceAttacked(tiffanysBoard, 60)) {
            moves[moveIdx].setCastlingMoveLong(60, 58);
            moveIdx++;
        }

        // KNIGHTS
        for (int pieceCount = 0; pieceCount < tiffanysBoard.blackKnightsPositions.pieceCount; pieceCount++) {

            from = tiffanysBoard.blackKnightsPositions.positions[pieceCount];
            dirCount = FastMoveGenPatterns.KNIGHT_MOVES[from].length;
            for (int i = 0; i < dirCount; i++) {
                to = FastMoveGenPatterns.KNIGHT_MOVES[from][i];

                if (tiffanysBoard.board[to] == 0) {
                    moves[moveIdx].setMove(from, to);
                    moveIdx++;
                } else if (tiffanysBoard.board[to] > 0) {
                    moves[moveIdx].setHitMoveKnight(from, to, getBlackHitScore(tiffanysBoard.board[to]));
                    moveIdx++;
                }

            }

        }

        // QUEEN
        for (int pieceCount = 0; pieceCount < tiffanysBoard.blackQueensPositions.pieceCount; pieceCount++) {
            from = tiffanysBoard.blackQueensPositions.positions[pieceCount];

            // NORTH
            for (int step = 1; step <= FastMoveGenPatterns.QUEEN_MOVES[from][NORTH]; step++) {
                to = from + (step * 8);
                if (tiffanysBoard.board[to] == 0) {
                    moves[moveIdx].setMove(from, to);
                    moveIdx++;
                } else if (tiffanysBoard.board[to] > 0) {
                    moves[moveIdx].setHitMoveQueen(from, to, getBlackHitScore(tiffanysBoard.board[to]));
                    moveIdx++;
                    break;
                } else {
                    break;
                }
            }

            // SOUTH
            for (int step = 1; step <= FastMoveGenPatterns.QUEEN_MOVES[from][SOUTH]; step++) {
                to = from - (step * 8);
                if (tiffanysBoard.board[to] == 0) {
                    moves[moveIdx].setMove(from, to);
                    moveIdx++;
                } else if (tiffanysBoard.board[to] > 0) {
                    moves[moveIdx].setHitMoveQueen(from, to, getBlackHitScore(tiffanysBoard.board[to]));
                    moveIdx++;
                    break;
                } else {
                    break;
                }
            }

            // WEST
            for (int step = 1; step <= FastMoveGenPatterns.QUEEN_MOVES[from][WEST]; step++) {
                to = from - step;
                if (tiffanysBoard.board[to] == 0) {
                    moves[moveIdx].setMove(from, to);
                    moveIdx++;
                } else if (tiffanysBoard.board[to] > 0) {
                    moves[moveIdx].setHitMoveQueen(from, to, getBlackHitScore(tiffanysBoard.board[to]));
                    moveIdx++;
                    break;
                } else {
                    break;
                }
            }

            // EAST
            for (int step = 1; step <= FastMoveGenPatterns.QUEEN_MOVES[from][EAST]; step++) {
                to = from + step;
                if (tiffanysBoard.board[to] == 0) {
                    moves[moveIdx].setMove(from, to);
                    moveIdx++;
                } else if (tiffanysBoard.board[to] > 0) {
                    moves[moveIdx].setHitMoveQueen(from, to, getBlackHitScore(tiffanysBoard.board[to]));
                    moveIdx++;
                    break;
                } else {
                    break;
                }
            }

            // NORTHWEST
            for (int step = 1; step <= FastMoveGenPatterns.QUEEN_MOVES[from][NORTHWEST]; step++) {
                to = from + (step * 7);
                if (tiffanysBoard.board[to] == 0) {
                    moves[moveIdx].setMove(from, to);
                    moveIdx++;
                } else if (tiffanysBoard.board[to] > 0) {
                    moves[moveIdx].setHitMoveQueen(from, to, getBlackHitScore(tiffanysBoard.board[to]));
                    moveIdx++;
                    break;
                } else {
                    break;
                }
            }

            // NORTHEAST
            for (int step = 1; step <= FastMoveGenPatterns.QUEEN_MOVES[from][NORTHEAST]; step++) {
                to = from + (step * 9);
                if (tiffanysBoard.board[to] == 0) {
                    moves[moveIdx].setMove(from, to);
                    moveIdx++;
                } else if (tiffanysBoard.board[to] > 0) {
                    moves[moveIdx].setHitMoveQueen(from, to, getBlackHitScore(tiffanysBoard.board[to]));
                    moveIdx++;
                    break;
                } else {
                    break;
                }
            }

            // SOUTHWEST
            for (int step = 1; step <= FastMoveGenPatterns.QUEEN_MOVES[from][SOUTHWEST]; step++) {
                to = from - (step * 9);
                if (tiffanysBoard.board[to] == 0) {
                    moves[moveIdx].setMove(from, to);
                    moveIdx++;
                } else if (tiffanysBoard.board[to] > 0) {
                    moves[moveIdx].setHitMoveQueen(from, to, getBlackHitScore(tiffanysBoard.board[to]));
                    moveIdx++;
                    break;
                } else {
                    break;
                }
            }

            // SOUTHEAST
            for (int step = 1; step <= FastMoveGenPatterns.QUEEN_MOVES[from][SOUTHEAST]; step++) {
                to = from - (step * 7);
                if (tiffanysBoard.board[to] == 0) {
                    moves[moveIdx].setMove(from, to);
                    moveIdx++;
                } else if (tiffanysBoard.board[to] > 0) {
                    moves[moveIdx].setHitMoveQueen(from, to, getBlackHitScore(tiffanysBoard.board[to]));
                    moveIdx++;
                    break;
                } else {
                    break;
                }
            }

        }

        // ROOK
        for (int pieceCount = 0; pieceCount < tiffanysBoard.blackRooksPositions.pieceCount; pieceCount++) {
            from = tiffanysBoard.blackRooksPositions.positions[pieceCount];

            // NORTH
            for (int step = 1; step <= FastMoveGenPatterns.ROOK_MOVES[from][NORTH]; step++) {
                to = from + (step * 8);
                if (tiffanysBoard.board[to] == 0) {
                    moves[moveIdx].setMoveRook(from, to);
                    moveIdx++;
                } else if (tiffanysBoard.board[to] > 0) {
                    moves[moveIdx].setHitMoveRook(from, to, getBlackHitScore(tiffanysBoard.board[to]));
                    moveIdx++;
                    break;
                } else {
                    break;
                }
            }

            // SOUTH
            for (int step = 1; step <= FastMoveGenPatterns.ROOK_MOVES[from][SOUTH]; step++) {
                to = from - (step * 8);
                if (tiffanysBoard.board[to] == 0) {
                    moves[moveIdx].setMoveRook(from, to);
                    moveIdx++;
                } else if (tiffanysBoard.board[to] > 0) {
                    moves[moveIdx].setHitMoveRook(from, to, getBlackHitScore(tiffanysBoard.board[to]));
                    moveIdx++;
                    break;
                } else {
                    break;
                }
            }

            // WEST
            for (int step = 1; step <= FastMoveGenPatterns.ROOK_MOVES[from][WEST]; step++) {
                to = from - step;
                if (tiffanysBoard.board[to] == 0) {
                    moves[moveIdx].setMoveRook(from, to);
                    moveIdx++;
                } else if (tiffanysBoard.board[to] > 0) {
                    moves[moveIdx].setHitMoveRook(from, to, getBlackHitScore(tiffanysBoard.board[to]));
                    moveIdx++;
                    break;
                } else {
                    break;
                }
            }

            // EAST
            for (int step = 1; step <= FastMoveGenPatterns.ROOK_MOVES[from][EAST]; step++) {
                to = from + step;
                if (tiffanysBoard.board[to] == 0) {
                    moves[moveIdx].setMoveRook(from, to);
                    moveIdx++;
                } else if (tiffanysBoard.board[to] > 0) {
                    moves[moveIdx].setHitMoveRook(from, to, getBlackHitScore(tiffanysBoard.board[to]));
                    moveIdx++;
                    break;
                } else {
                    break;
                }
            }

        }

        // BISHOP
        for (int pieceCount = 0; pieceCount < tiffanysBoard.blackBishopsPositions.pieceCount; pieceCount++) {
            from = tiffanysBoard.blackBishopsPositions.positions[pieceCount];

            // NORTHWEST
            for (int step = 1; step <= FastMoveGenPatterns.BISHOP_MOVES[from][NORTHWEST]; step++) {
                to = from + (step * 7);
                if (tiffanysBoard.board[to] == 0) {
                    moves[moveIdx].setMove(from, to);
                    moveIdx++;
                } else if (tiffanysBoard.board[to] > 0) {
                    moves[moveIdx].setHitMoveBishop(from, to, getBlackHitScore(tiffanysBoard.board[to]));
                    moveIdx++;
                    break;
                } else {
                    break;
                }
            }

            // NORTHEAST
            for (int step = 1; step <= FastMoveGenPatterns.BISHOP_MOVES[from][NORTHEAST]; step++) {
                to = from + (step * 9);
                if (tiffanysBoard.board[to] == 0) {
                    moves[moveIdx].setMove(from, to);
                    moveIdx++;
                } else if (tiffanysBoard.board[to] > 0) {
                    moves[moveIdx].setHitMoveBishop(from, to, getBlackHitScore(tiffanysBoard.board[to]));
                    moveIdx++;
                    break;
                } else {
                    break;
                }
            }

            // SOUTHWEST
            for (int step = 1; step <= FastMoveGenPatterns.BISHOP_MOVES[from][SOUTHWEST]; step++) {
                to = from - (step * 9);
                if (tiffanysBoard.board[to] == 0) {
                    moves[moveIdx].setMove(from, to);
                    moveIdx++;
                } else if (tiffanysBoard.board[to] > 0) {
                    moves[moveIdx].setHitMoveBishop(from, to, getBlackHitScore(tiffanysBoard.board[to]));
                    moveIdx++;
                    break;
                } else {
                    break;
                }
            }

            // SOUTHEAST
            for (int step = 1; step <= FastMoveGenPatterns.BISHOP_MOVES[from][SOUTHEAST]; step++) {
                to = from - (step * 7);
                if (tiffanysBoard.board[to] == 0) {
                    moves[moveIdx].setMove(from, to);
                    moveIdx++;
                } else if (tiffanysBoard.board[to] > 0) {
                    moves[moveIdx].setHitMoveBishop(from, to, getBlackHitScore(tiffanysBoard.board[to]));
                    moveIdx++;
                    break;
                } else {
                    break;
                }
            }

        }

        return moveIdx;

    }

    public final int generateWhiteMovesHitList(RobustBoard tiffanysBoard, EngineMove[] moves) {
        int moveIdx = 0;
        int from = -1;
        int to = -1;
        int dirCount = 0;

        // PAWNS
        for (int i = 0; i < tiffanysBoard.whitePawnsPositions.pieceCount; i++) {
            from = tiffanysBoard.whitePawnsPositions.positions[i];

            // Capture move ?
            // To left
            to = FastMoveGenPatterns.WHITE_PAWN_HIT_MOVES[from][0];
            if (to >= 0) {
                // to = from + 7;
                if (tiffanysBoard.board[to] < 0) {
                    if (to < 56) {
                        moves[moveIdx].setHitMovePawn(from, to, getWhiteHitScore(tiffanysBoard.board[to]));
                        moveIdx++;
                    } else {
                        moves[moveIdx].setPromotionHitMoveQueen(from, to, getWhiteHitScore(tiffanysBoard.board[to]));
                        moveIdx++;
                        moves[moveIdx].setPromotionHitMoveRook(from, to, getWhiteHitScore(tiffanysBoard.board[to]));
                        moveIdx++;
                        moves[moveIdx].setPromotionHitMoveBishop(from, to, getWhiteHitScore(tiffanysBoard.board[to]));
                        moveIdx++;
                        moves[moveIdx].setPromotionHitMoveKnight(from, to, getWhiteHitScore(tiffanysBoard.board[to]));
                        moveIdx++;
                    }
                } else if (tiffanysBoard.enPassantField == to && tiffanysBoard.board[to] == 0) {
                    moves[moveIdx].setEnpassantMove(from, to, to - 8);
                    moveIdx++;
                }
            }

            to = FastMoveGenPatterns.WHITE_PAWN_HIT_MOVES[from][1];
            if (to >= 0) {
                // to = from + 7;
                if (tiffanysBoard.board[to] < 0) {
                    if (to < 56) {
                        moves[moveIdx].setHitMovePawn(from, to, getWhiteHitScore(tiffanysBoard.board[to]));
                        moveIdx++;
                    } else {
                        moves[moveIdx].setPromotionHitMoveQueen(from, to, getWhiteHitScore(tiffanysBoard.board[to]));
                        moveIdx++;
                        moves[moveIdx].setPromotionHitMoveRook(from, to, getWhiteHitScore(tiffanysBoard.board[to]));
                        moveIdx++;
                        moves[moveIdx].setPromotionHitMoveBishop(from, to, getWhiteHitScore(tiffanysBoard.board[to]));
                        moveIdx++;
                        moves[moveIdx].setPromotionHitMoveKnight(from, to, getWhiteHitScore(tiffanysBoard.board[to]));
                        moveIdx++;
                    }
                } else if (tiffanysBoard.enPassantField == to && tiffanysBoard.board[to] == 0) {
                    moves[moveIdx].setEnpassantMove(from, to, to - 8);
                    moveIdx++;
                }
            }

        }

        // KING
        from = tiffanysBoard.whiteKingPos;
        dirCount = FastMoveGenPatterns.KING_MOVES[from].length;
        for (int i = 0; i < dirCount; i++) {
            to = FastMoveGenPatterns.KING_MOVES[from][i];

            if (tiffanysBoard.board[to] < 0) {
                moves[moveIdx].setHitMoveKing(from, to, getWhiteHitScore(tiffanysBoard.board[to]));
                moveIdx++;
            }

        }

        // KNIGHTS
        for (int pieceCount = 0; pieceCount < tiffanysBoard.whiteKnightsPositions.pieceCount; pieceCount++) {

            from = tiffanysBoard.whiteKnightsPositions.positions[pieceCount];
            dirCount = FastMoveGenPatterns.KNIGHT_MOVES[from].length;
            for (int i = 0; i < dirCount; i++) {
                to = FastMoveGenPatterns.KNIGHT_MOVES[from][i];

                if (tiffanysBoard.board[to] < 0) {
                    moves[moveIdx].setHitMoveKnight(from, to, getWhiteHitScore(tiffanysBoard.board[to]));
                    moveIdx++;
                }

            }

        }

        // QUEEN
        for (int pieceCount = 0; pieceCount < tiffanysBoard.whiteQueensPositions.pieceCount; pieceCount++) {
            from = tiffanysBoard.whiteQueensPositions.positions[pieceCount];

            // NORTH
            for (int step = 1; step <= FastMoveGenPatterns.QUEEN_MOVES[from][NORTH]; step++) {
                to = from + (step * 8);
                if (tiffanysBoard.board[to] == 0) {
                    // Do nothing
                } else if (tiffanysBoard.board[to] < 0) {
                    moves[moveIdx].setHitMoveQueen(from, to, getWhiteHitScore(tiffanysBoard.board[to]));
                    moveIdx++;
                    break;
                } else {
                    break;
                }
            }

            // SOUTH
            for (int step = 1; step <= FastMoveGenPatterns.QUEEN_MOVES[from][SOUTH]; step++) {
                to = from - (step * 8);
                if (tiffanysBoard.board[to] == 0) {
                    // Do nothing

                } else if (tiffanysBoard.board[to] < 0) {
                    moves[moveIdx].setHitMoveQueen(from, to, getWhiteHitScore(tiffanysBoard.board[to]));
                    moveIdx++;
                    break;
                } else {
                    break;
                }
            }

            // WEST
            for (int step = 1; step <= FastMoveGenPatterns.QUEEN_MOVES[from][WEST]; step++) {
                to = from - step;
                if (tiffanysBoard.board[to] == 0) {
                    // Do nothing

                } else if (tiffanysBoard.board[to] < 0) {
                    moves[moveIdx].setHitMoveQueen(from, to, getWhiteHitScore(tiffanysBoard.board[to]));
                    moveIdx++;
                    break;
                } else {
                    break;
                }
            }

            // EAST
            for (int step = 1; step <= FastMoveGenPatterns.QUEEN_MOVES[from][EAST]; step++) {
                to = from + step;
                if (tiffanysBoard.board[to] == 0) {
                    // Do nothing

                } else if (tiffanysBoard.board[to] < 0) {
                    moves[moveIdx].setHitMoveQueen(from, to, getWhiteHitScore(tiffanysBoard.board[to]));
                    moveIdx++;
                    break;
                } else {
                    break;
                }
            }

            // NORTHWEST
            for (int step = 1; step <= FastMoveGenPatterns.QUEEN_MOVES[from][NORTHWEST]; step++) {
                to = from + (step * 7);
                if (tiffanysBoard.board[to] == 0) {
                    // Do nothing

                } else if (tiffanysBoard.board[to] < 0) {
                    moves[moveIdx].setHitMoveQueen(from, to, getWhiteHitScore(tiffanysBoard.board[to]));
                    moveIdx++;
                    break;
                } else {
                    break;
                }
            }

            // NORTHEAST
            for (int step = 1; step <= FastMoveGenPatterns.QUEEN_MOVES[from][NORTHEAST]; step++) {
                to = from + (step * 9);
                if (tiffanysBoard.board[to] == 0) {
                    // Do nothing

                } else if (tiffanysBoard.board[to] < 0) {
                    moves[moveIdx].setHitMoveQueen(from, to, getWhiteHitScore(tiffanysBoard.board[to]));
                    moveIdx++;
                    break;
                } else {
                    break;
                }
            }

            // SOUTHWEST
            for (int step = 1; step <= FastMoveGenPatterns.QUEEN_MOVES[from][SOUTHWEST]; step++) {
                to = from - (step * 9);
                if (tiffanysBoard.board[to] == 0) {
                    // Do nothing

                } else if (tiffanysBoard.board[to] < 0) {
                    moves[moveIdx].setHitMoveQueen(from, to, getWhiteHitScore(tiffanysBoard.board[to]));
                    moveIdx++;
                    break;
                } else {
                    break;
                }
            }

            // SOUTHEAST
            for (int step = 1; step <= FastMoveGenPatterns.QUEEN_MOVES[from][SOUTHEAST]; step++) {
                to = from - (step * 7);
                if (tiffanysBoard.board[to] == 0) {
                    // Do nothing

                } else if (tiffanysBoard.board[to] < 0) {
                    moves[moveIdx].setHitMoveQueen(from, to, getWhiteHitScore(tiffanysBoard.board[to]));
                    moveIdx++;
                    break;
                } else {
                    break;
                }
            }

        }

        // ROOK
        for (int pieceCount = 0; pieceCount < tiffanysBoard.whiteRooksPositions.pieceCount; pieceCount++) {
            from = tiffanysBoard.whiteRooksPositions.positions[pieceCount];

            // NORTH
            for (int step = 1; step <= FastMoveGenPatterns.ROOK_MOVES[from][NORTH]; step++) {
                to = from + (step * 8);
                if (tiffanysBoard.board[to] == 0) {
                    // Do nothing

                } else if (tiffanysBoard.board[to] < 0) {
                    moves[moveIdx].setHitMoveRook(from, to, getWhiteHitScore(tiffanysBoard.board[to]));
                    moveIdx++;
                    break;
                } else {
                    break;
                }
            }

            // SOUTH
            for (int step = 1; step <= FastMoveGenPatterns.ROOK_MOVES[from][SOUTH]; step++) {
                to = from - (step * 8);
                if (tiffanysBoard.board[to] == 0) {
                    // Do nothing

                } else if (tiffanysBoard.board[to] < 0) {
                    moves[moveIdx].setHitMoveRook(from, to, getWhiteHitScore(tiffanysBoard.board[to]));
                    moveIdx++;
                    break;
                } else {
                    break;
                }
            }

            // WEST
            for (int step = 1; step <= FastMoveGenPatterns.ROOK_MOVES[from][WEST]; step++) {
                to = from - step;
                if (tiffanysBoard.board[to] == 0) {
                    // Do nothing

                } else if (tiffanysBoard.board[to] < 0) {
                    moves[moveIdx].setHitMoveRook(from, to, getWhiteHitScore(tiffanysBoard.board[to]));
                    moveIdx++;
                    break;
                } else {
                    break;
                }
            }

            // EAST
            for (int step = 1; step <= FastMoveGenPatterns.ROOK_MOVES[from][EAST]; step++) {
                to = from + step;
                if (tiffanysBoard.board[to] == 0) {
                    // Do nothing

                } else if (tiffanysBoard.board[to] < 0) {
                    moves[moveIdx].setHitMoveRook(from, to, getWhiteHitScore(tiffanysBoard.board[to]));
                    moveIdx++;
                    break;
                } else {
                    break;
                }
            }

        }

        // BISHOP
        for (int pieceCount = 0; pieceCount < tiffanysBoard.whiteBishopsPositions.pieceCount; pieceCount++) {
            from = tiffanysBoard.whiteBishopsPositions.positions[pieceCount];

            // NORTHWEST
            for (int step = 1; step <= FastMoveGenPatterns.BISHOP_MOVES[from][NORTHWEST]; step++) {
                to = from + (step * 7);
                if (tiffanysBoard.board[to] == 0) {
                    // Do nothing

                } else if (tiffanysBoard.board[to] < 0) {
                    moves[moveIdx].setHitMoveBishop(from, to, getWhiteHitScore(tiffanysBoard.board[to]));
                    moveIdx++;
                    break;
                } else {
                    break;
                }
            }

            // NORTHEAST
            for (int step = 1; step <= FastMoveGenPatterns.BISHOP_MOVES[from][NORTHEAST]; step++) {
                to = from + (step * 9);
                if (tiffanysBoard.board[to] == 0) {
                    // Do nothing

                } else if (tiffanysBoard.board[to] < 0) {
                    moves[moveIdx].setHitMoveBishop(from, to, getWhiteHitScore(tiffanysBoard.board[to]));
                    moveIdx++;
                    break;
                } else {
                    break;
                }
            }

            // SOUTHWEST
            for (int step = 1; step <= FastMoveGenPatterns.BISHOP_MOVES[from][SOUTHWEST]; step++) {
                to = from - (step * 9);
                if (tiffanysBoard.board[to] == 0) {
                    // Do nothing

                } else if (tiffanysBoard.board[to] < 0) {
                    moves[moveIdx].setHitMoveBishop(from, to, getWhiteHitScore(tiffanysBoard.board[to]));
                    moveIdx++;
                    break;
                } else {
                    break;
                }
            }

            // SOUTHEAST
            for (int step = 1; step <= FastMoveGenPatterns.BISHOP_MOVES[from][SOUTHEAST]; step++) {
                to = from - (step * 7);
                if (tiffanysBoard.board[to] == 0) {
                    // Do nothing

                } else if (tiffanysBoard.board[to] < 0) {
                    moves[moveIdx].setHitMoveBishop(from, to, getWhiteHitScore(tiffanysBoard.board[to]));
                    moveIdx++;
                    break;
                } else {
                    break;
                }
            }

        }

        return moveIdx;

    }

    public final int generateBlackMovesHitList(RobustBoard tiffanysBoard, EngineMove[] moves) {
        int moveIdx = 0;
        int from = -1;
        int to = -1;
        int dirCount = 0;

        // PAWNS
        for (int i = 0; i < tiffanysBoard.blackPawnsPositions.pieceCount; i++) {
            from = tiffanysBoard.blackPawnsPositions.positions[i];

            // Capture move ?
            // To left

            to = FastMoveGenPatterns.BLACK_PAWN_HIT_MOVES[from][0];
            if (to >= 0) {
                // to = from + 7;
                if (tiffanysBoard.board[to] > 0) {
                    if (to > 7) {
                        moves[moveIdx].setHitMovePawn(from, to, getBlackHitScore(tiffanysBoard.board[to]));
                        moveIdx++;
                    } else {
                        moves[moveIdx].setPromotionHitMoveQueen(from, to, getBlackHitScore(tiffanysBoard.board[to]));
                        moveIdx++;
                        moves[moveIdx].setPromotionHitMoveRook(from, to, getBlackHitScore(tiffanysBoard.board[to]));
                        moveIdx++;
                        moves[moveIdx].setPromotionHitMoveBishop(from, to, getBlackHitScore(tiffanysBoard.board[to]));
                        moveIdx++;
                        moves[moveIdx].setPromotionHitMoveKnight(from, to, getBlackHitScore(tiffanysBoard.board[to]));
                        moveIdx++;
                    }
                } else if (tiffanysBoard.enPassantField == to && tiffanysBoard.board[to] == 0) {

                    moves[moveIdx].setEnpassantMove(from, to, to + 8);

                    moveIdx++;

                }
            }

            // To right
            to = FastMoveGenPatterns.BLACK_PAWN_HIT_MOVES[from][1];
            if (to >= 0) {
                // to = from + 7;
                if (tiffanysBoard.board[to] > 0) {
                    if (to > 7) {
                        moves[moveIdx].setHitMovePawn(from, to, getBlackHitScore(tiffanysBoard.board[to]));
                        moveIdx++;
                    } else {
                        moves[moveIdx].setPromotionHitMoveQueen(from, to, getBlackHitScore(tiffanysBoard.board[to]));
                        moveIdx++;
                        moves[moveIdx].setPromotionHitMoveRook(from, to, getBlackHitScore(tiffanysBoard.board[to]));
                        moveIdx++;
                        moves[moveIdx].setPromotionHitMoveBishop(from, to, getBlackHitScore(tiffanysBoard.board[to]));
                        moveIdx++;
                        moves[moveIdx].setPromotionHitMoveKnight(from, to, getBlackHitScore(tiffanysBoard.board[to]));
                        moveIdx++;
                    }
                } else if (tiffanysBoard.enPassantField == to && tiffanysBoard.board[to] == 0) {
                    moves[moveIdx].setEnpassantMove(from, to, to + 8);
                    moveIdx++;

                }
            }

        }

        // KING
        from = tiffanysBoard.blackKingPos;
        dirCount = FastMoveGenPatterns.KING_MOVES[from].length;
        for (int i = 0; i < dirCount; i++) {
            to = FastMoveGenPatterns.KING_MOVES[from][i];

            if (tiffanysBoard.board[to] > 0) {
                moves[moveIdx].setHitMoveKing(from, to, getBlackHitScore(tiffanysBoard.board[to]));
                moveIdx++;
            }

        }

        // KNIGHTS
        for (int pieceCount = 0; pieceCount < tiffanysBoard.blackKnightsPositions.pieceCount; pieceCount++) {

            from = tiffanysBoard.blackKnightsPositions.positions[pieceCount];
            dirCount = FastMoveGenPatterns.KNIGHT_MOVES[from].length;
            for (int i = 0; i < dirCount; i++) {
                to = FastMoveGenPatterns.KNIGHT_MOVES[from][i];

                if (tiffanysBoard.board[to] > 0) {
                    moves[moveIdx].setHitMoveQueen(from, to, getBlackHitScore(tiffanysBoard.board[to]));
                    moveIdx++;
                }

            }

        }

        // QUEEN
        for (int pieceCount = 0; pieceCount < tiffanysBoard.blackQueensPositions.pieceCount; pieceCount++) {
            from = tiffanysBoard.blackQueensPositions.positions[pieceCount];

            // NORTH
            for (int step = 1; step <= FastMoveGenPatterns.QUEEN_MOVES[from][NORTH]; step++) {
                to = from + (step * 8);
                if (tiffanysBoard.board[to] == 0) {
                    // Do nothing

                } else if (tiffanysBoard.board[to] > 0) {
                    moves[moveIdx].setHitMoveQueen(from, to, getBlackHitScore(tiffanysBoard.board[to]));
                    moveIdx++;
                    break;
                } else {
                    break;
                }
            }

            // SOUTH
            for (int step = 1; step <= FastMoveGenPatterns.QUEEN_MOVES[from][SOUTH]; step++) {
                to = from - (step * 8);
                if (tiffanysBoard.board[to] == 0) {
                    // Do nothing

                } else if (tiffanysBoard.board[to] > 0) {
                    moves[moveIdx].setHitMoveQueen(from, to, getBlackHitScore(tiffanysBoard.board[to]));
                    moveIdx++;
                    break;
                } else {
                    break;
                }
            }

            // WEST
            for (int step = 1; step <= FastMoveGenPatterns.QUEEN_MOVES[from][WEST]; step++) {
                to = from - step;
                if (tiffanysBoard.board[to] == 0) {
                    // Do nothing

                } else if (tiffanysBoard.board[to] > 0) {
                    moves[moveIdx].setHitMoveQueen(from, to, getBlackHitScore(tiffanysBoard.board[to]));
                    moveIdx++;
                    break;
                } else {
                    break;
                }
            }

            // EAST
            for (int step = 1; step <= FastMoveGenPatterns.QUEEN_MOVES[from][EAST]; step++) {
                to = from + step;
                if (tiffanysBoard.board[to] == 0) {
                    // Do nothing

                } else if (tiffanysBoard.board[to] > 0) {
                    moves[moveIdx].setHitMoveQueen(from, to, getBlackHitScore(tiffanysBoard.board[to]));
                    moveIdx++;
                    break;
                } else {
                    break;
                }
            }

            // NORTHWEST
            for (int step = 1; step <= FastMoveGenPatterns.QUEEN_MOVES[from][NORTHWEST]; step++) {
                to = from + (step * 7);
                if (tiffanysBoard.board[to] == 0) {
                    // Do nothing

                } else if (tiffanysBoard.board[to] > 0) {
                    moves[moveIdx].setHitMoveQueen(from, to, getBlackHitScore(tiffanysBoard.board[to]));
                    moveIdx++;
                    break;
                } else {
                    break;
                }
            }

            // NORTHEAST
            for (int step = 1; step <= FastMoveGenPatterns.QUEEN_MOVES[from][NORTHEAST]; step++) {
                to = from + (step * 9);
                if (tiffanysBoard.board[to] == 0) {
                    // Do nothing

                } else if (tiffanysBoard.board[to] > 0) {
                    moves[moveIdx].setHitMoveQueen(from, to, getBlackHitScore(tiffanysBoard.board[to]));
                    moveIdx++;
                    break;
                } else {
                    break;
                }
            }

            // SOUTHWEST
            for (int step = 1; step <= FastMoveGenPatterns.QUEEN_MOVES[from][SOUTHWEST]; step++) {
                to = from - (step * 9);
                if (tiffanysBoard.board[to] == 0) {
                    // Do nothing

                } else if (tiffanysBoard.board[to] > 0) {
                    moves[moveIdx].setHitMoveQueen(from, to, getBlackHitScore(tiffanysBoard.board[to]));
                    moveIdx++;
                    break;
                } else {
                    break;
                }
            }

            // SOUTHEAST
            for (int step = 1; step <= FastMoveGenPatterns.QUEEN_MOVES[from][SOUTHEAST]; step++) {
                to = from - (step * 7);
                if (tiffanysBoard.board[to] == 0) {
                    // Do nothing

                } else if (tiffanysBoard.board[to] > 0) {
                    moves[moveIdx].setHitMoveQueen(from, to, getBlackHitScore(tiffanysBoard.board[to]));
                    moveIdx++;
                    break;
                } else {
                    break;
                }
            }

        }

        // ROOK
        for (int pieceCount = 0; pieceCount < tiffanysBoard.blackRooksPositions.pieceCount; pieceCount++) {
            from = tiffanysBoard.blackRooksPositions.positions[pieceCount];

            // NORTH
            for (int step = 1; step <= FastMoveGenPatterns.ROOK_MOVES[from][NORTH]; step++) {
                to = from + (step * 8);
                if (tiffanysBoard.board[to] == 0) {
                    // Do nothing

                } else if (tiffanysBoard.board[to] > 0) {
                    moves[moveIdx].setHitMoveRook(from, to, getBlackHitScore(tiffanysBoard.board[to]));
                    moveIdx++;
                    break;
                } else {
                    break;
                }
            }

            // SOUTH
            for (int step = 1; step <= FastMoveGenPatterns.ROOK_MOVES[from][SOUTH]; step++) {
                to = from - (step * 8);
                if (tiffanysBoard.board[to] == 0) {
                    // Do nothing

                } else if (tiffanysBoard.board[to] > 0) {
                    moves[moveIdx].setHitMoveRook(from, to, getBlackHitScore(tiffanysBoard.board[to]));
                    moveIdx++;
                    break;
                } else {
                    break;
                }
            }

            // WEST
            for (int step = 1; step <= FastMoveGenPatterns.ROOK_MOVES[from][WEST]; step++) {
                to = from - step;
                if (tiffanysBoard.board[to] == 0) {
                    // Do nothing

                } else if (tiffanysBoard.board[to] > 0) {
                    moves[moveIdx].setHitMoveRook(from, to, getBlackHitScore(tiffanysBoard.board[to]));
                    moveIdx++;
                    break;
                } else {
                    break;
                }
            }

            // EAST
            for (int step = 1; step <= FastMoveGenPatterns.ROOK_MOVES[from][EAST]; step++) {
                to = from + step;
                if (tiffanysBoard.board[to] == 0) {
                    // Do nothing

                } else if (tiffanysBoard.board[to] > 0) {
                    moves[moveIdx].setHitMoveRook(from, to, getBlackHitScore(tiffanysBoard.board[to]));
                    moveIdx++;
                    break;
                } else {
                    break;
                }
            }

        }

        // BISHOP
        for (int pieceCount = 0; pieceCount < tiffanysBoard.blackBishopsPositions.pieceCount; pieceCount++) {
            from = tiffanysBoard.blackBishopsPositions.positions[pieceCount];

            // NORTHWEST
            for (int step = 1; step <= FastMoveGenPatterns.BISHOP_MOVES[from][NORTHWEST]; step++) {
                to = from + (step * 7);
                if (tiffanysBoard.board[to] == 0) {
                    // Do nothing

                } else if (tiffanysBoard.board[to] > 0) {
                    moves[moveIdx].setHitMoveBishop(from, to, getBlackHitScore(tiffanysBoard.board[to]));
                    moveIdx++;
                    break;
                } else {
                    break;
                }
            }

            // NORTHEAST
            for (int step = 1; step <= FastMoveGenPatterns.BISHOP_MOVES[from][NORTHEAST]; step++) {
                to = from + (step * 9);
                if (tiffanysBoard.board[to] == 0) {
                    // Do nothing

                } else if (tiffanysBoard.board[to] > 0) {
                    moves[moveIdx].setHitMoveBishop(from, to, getBlackHitScore(tiffanysBoard.board[to]));
                    moveIdx++;
                    break;
                } else {
                    break;
                }
            }

            // SOUTHWEST
            for (int step = 1; step <= FastMoveGenPatterns.BISHOP_MOVES[from][SOUTHWEST]; step++) {
                to = from - (step * 9);
                if (tiffanysBoard.board[to] == 0) {
                    // Do nothing

                } else if (tiffanysBoard.board[to] > 0) {
                    moves[moveIdx].setHitMoveBishop(from, to, getBlackHitScore(tiffanysBoard.board[to]));
                    moveIdx++;
                    break;
                } else {
                    break;
                }
            }

            // SOUTHEAST
            for (int step = 1; step <= FastMoveGenPatterns.BISHOP_MOVES[from][SOUTHEAST]; step++) {
                to = from - (step * 7);
                if (tiffanysBoard.board[to] == 0) {
                    // Do nothing

                } else if (tiffanysBoard.board[to] > 0) {
                    moves[moveIdx].setHitMoveBishop(from, to, getBlackHitScore(tiffanysBoard.board[to]));
                    moveIdx++;
                    break;
                } else {
                    break;
                }
            }

        }

        return moveIdx;

    }

}
