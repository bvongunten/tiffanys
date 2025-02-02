package ch.nostromo.tiffanys.engine.dragonborn.move;

import ch.nostromo.tiffanys.commons.board.Square;
import ch.nostromo.tiffanys.commons.move.Castling;
import ch.nostromo.tiffanys.commons.Side;
import ch.nostromo.tiffanys.commons.board.Piece;
import ch.nostromo.tiffanys.commons.move.Move;
import ch.nostromo.tiffanys.commons.move.MoveAttributes;
import ch.nostromo.tiffanys.engine.dragonborn.board.RobustBoard;
import ch.nostromo.tiffanys.engine.dragonborn.DragonbornConstants;
import ch.nostromo.tiffanys.engine.dragonborn.ai.PrincipalVariation;

import java.util.ArrayList;
import java.util.List;

public class EngineMove implements Comparable<EngineMove>, DragonbornConstants, Cloneable {

    // Move stuff
    public static final int MOVE = 0;
    public static final int MOVE_EP_FIELD = 1;
    public static final int MOVE_CASTLING_AFFECTED_ROOK = 2;
    public static final int MOVE_CASTLING_AFFECTED_KING = 3;

    public static final int SHORT_CASTLING = 15;
    public static final int LONG_CASTLING = 16;

    public static final int HIT_MOVE_PAWN = 25;
    public static final int HIT_MOVE_QUEEN = 26;
    public static final int HIT_MOVE_KNIGHT = 27;
    public static final int HIT_MOVE_BISHOP = 28;
    public static final int HIT_MOVE_CASTLING_AFFECTED_ROOK = 29;
    public static final int HIT_MOVE_CASTLING_AFFECTED_KING = 30;

    public static final int HIT_MOVE_ENPASSANT = 35;
    public static final int HIT_MOVE_PROMOTION = 40;

    public static final int MOVE_PROMOTION = 50;

    public int score = 0;
    public int maxDepth = 0;
    public int cutOffs = 0;
    public int nodes = 0;

    public PrincipalVariation principalVariation = null;

    public int moveType = -1;

    public int from = -1;
    public int to = -1;
    public int epField = -1;

    public int promotionPiece = -1;
    public int epHitPawn = -1;

    public boolean stackWhiteLongCastling;
    public boolean stackWhiteShortCastling;
    public boolean stackBlackShortCastling;
    public boolean stackBlackLongCastling;
    public int stackEnPassantField;

    public int hitScore = 0;

    // The overwritten fields
    public int stackFromBoardPiece;
    public int stackFromBoardPieceIndex;

    public int stackToBoardPiece;
    public int stackToBoardPieceIndex;

    public int stackPromotedPieceIndex;

    public int stackEpBoardPieceIndex;

    public int stackCastlingRookIdx;

    public int plannedDepth;
    public long timeMs;

    public long stackZobristKey;

    public final void setMove(int from, int to) {
        this.from = from;
        this.to = to;
        this.moveType = MOVE;
        this.hitScore = 0;
    }

    public final void setMoveRook(int from, int to) {
        this.from = from;
        this.to = to;
        this.moveType = MOVE_CASTLING_AFFECTED_ROOK;
        this.hitScore = 0;
    }

    public final void setMoveKing(int from, int to) {
        this.from = from;
        this.to = to;
        this.moveType = MOVE_CASTLING_AFFECTED_KING;
        this.hitScore = 0;
    }

    public final void setMove(int from, int to, int epField) {
        this.from = from;
        this.to = to;
        this.epField = epField;
        this.moveType = MOVE_EP_FIELD;
        this.hitScore = 0;
    }

    public final void setHitMovePawn(int from, int to, int targetScore) {
        this.from = from;
        this.to = to;
        this.moveType = HIT_MOVE_PAWN;
        this.hitScore = targetScore - PAWN_SCORE;
    }

    public final void setHitMoveQueen(int from, int to, int targetScore) {
        this.from = from;
        this.to = to;
        this.moveType = HIT_MOVE_QUEEN;
        this.hitScore = targetScore - QUEEN_SCORE;

    }

    public final void setHitMoveBishop(int from, int to, int targetScore) {
        this.from = from;
        this.to = to;
        this.moveType = HIT_MOVE_BISHOP;
        this.hitScore = targetScore - BISHOP_SCORE;

    }

    public final void setHitMoveKnight(int from, int to, int targetScore) {
        this.from = from;
        this.to = to;
        this.moveType = HIT_MOVE_KNIGHT;
        this.hitScore = targetScore - KNIGHT_SCORE;

    }

    public final void setHitMoveRook(int from, int to, int targetScore) {
        this.from = from;
        this.to = to;
        this.moveType = HIT_MOVE_CASTLING_AFFECTED_ROOK;
        this.hitScore = targetScore - ROOK_SCORE;

    }

    public final void setHitMoveKing(int from, int to, int targetScore) {
        this.from = from;
        this.to = to;
        this.moveType = HIT_MOVE_CASTLING_AFFECTED_KING;
        this.hitScore = targetScore;
    }

    public final void setEnpassantMove(int from, int to, int epPawn) {
        this.from = from;
        this.to = to;
        this.epHitPawn = epPawn;
        this.moveType = HIT_MOVE_ENPASSANT;
        this.hitScore = 0;
    }

    public final void setPromotionMoveQueen(int from, int to) {
        this.from = from;
        this.to = to;
        this.promotionPiece = PIECE_QUEEN;
        this.moveType = MOVE_PROMOTION;
        this.hitScore = 0;

    }

    public final void setPromotionMoveRook(int from, int to) {
        this.from = from;
        this.to = to;
        this.promotionPiece = PIECE_ROOK;
        this.moveType = MOVE_PROMOTION;
        this.hitScore = 0;
    }

    public final void setPromotionMoveBishop(int from, int to) {
        this.from = from;
        this.to = to;
        this.promotionPiece = DragonbornConstants.PIECE_BISHOP;
        this.moveType = MOVE_PROMOTION;
        this.hitScore = 0;
    }

    public final void setPromotionMoveKnight(int from, int to) {
        this.from = from;
        this.to = to;
        this.promotionPiece = DragonbornConstants.PIECE_KNIGHT;
        this.moveType = MOVE_PROMOTION;
        this.hitScore = 0;
    }

    public final void setPromotionHitMoveQueen(int from, int to, int targetScore) {
        this.from = from;
        this.to = to;
        this.promotionPiece = DragonbornConstants.PIECE_QUEEN;
        this.moveType = HIT_MOVE_PROMOTION;
        this.hitScore = targetScore - QUEEN_SCORE;
    }

    public final void setPromotionHitMoveRook(int from, int to, int targetScore) {
        this.from = from;
        this.to = to;
        this.promotionPiece = DragonbornConstants.PIECE_ROOK;
        this.moveType = HIT_MOVE_PROMOTION;
        this.hitScore = targetScore - ROOK_SCORE;
    }

    public final void setPromotionHitMoveBishop(int from, int to, int targetScore) {
        this.from = from;
        this.to = to;
        this.promotionPiece = DragonbornConstants.PIECE_BISHOP;
        this.moveType = HIT_MOVE_PROMOTION;
        this.hitScore = targetScore - BISHOP_SCORE;
    }

    public final void setPromotionHitMoveKnight(int from, int to, int targetScore) {
        this.from = from;
        this.to = to;
        this.promotionPiece = DragonbornConstants.PIECE_KNIGHT;
        this.moveType = HIT_MOVE_PROMOTION;
        this.hitScore = targetScore - KNIGHT_SCORE;
    }

    public final void setCastlingMoveShort(int from, int to) {
        this.from = from;
        this.to = to;
        this.moveType = SHORT_CASTLING;
        this.hitScore = 0;
    }

    public final void setCastlingMoveLong(int from, int to) {
        this.from = from;
        this.to = to;
        this.moveType = LONG_CASTLING;
        this.hitScore = 0;
    }

    public Move convertToMove(Side colorToMove) {
        Square fromS = RobustBoard.BOARD64_COORDS[from];
        Square toS = RobustBoard.BOARD64_COORDS[to];

        Move result;

        if (moveType == HIT_MOVE_ENPASSANT) {
            result = new Move(fromS, toS);

        } else if (moveType == SHORT_CASTLING) {
            if (colorToMove == Side.WHITE) {
                result = new Move(Castling.WHITE_SHORT);
            } else {
                result = new Move(Castling.BLACK_SHORT);
            }

        } else if (moveType == LONG_CASTLING) {
            if (colorToMove == Side.WHITE) {
                result = new Move(Castling.WHITE_LONG);
            } else {
                result = new Move(Castling.BLACK_LONG);
            }

        } else if (moveType == HIT_MOVE_PROMOTION || moveType == MOVE_PROMOTION) {

            if (colorToMove == Side.WHITE) {

                switch (promotionPiece) {
                    case DragonbornConstants.PIECE_BISHOP: {
                        result = new Move(fromS, toS, Piece.WHITE_BISHOP);
                        break;
                    }
                    case DragonbornConstants.PIECE_KNIGHT: {
                        result = new Move(fromS, toS, Piece.WHITE_KNIGHT);
                        break;
                    }
                    case DragonbornConstants.PIECE_ROOK: {
                        result = new Move(fromS, toS, Piece.WHITE_ROOK);
                        break;
                    }
                    case DragonbornConstants.PIECE_QUEEN: {
                        result = new Move(fromS, toS, Piece.WHITE_QUEEN);
                        break;
                    }
                    default: {
                        throw new IllegalArgumentException("Unknown promotion piece: " + promotionPiece);
                    }
                }
            } else {

                switch (promotionPiece) {
                    case DragonbornConstants.PIECE_BISHOP: {
                        result = new Move(fromS, toS, Piece.BLACK_BISHOP);
                        break;
                    }
                    case DragonbornConstants.PIECE_KNIGHT: {
                        result = new Move(fromS, toS, Piece.BLACK_KNIGHT);
                        break;
                    }
                    case DragonbornConstants.PIECE_ROOK: {
                        result = new Move(fromS, toS, Piece.BLACK_ROOK);
                        break;
                    }
                    case DragonbornConstants.PIECE_QUEEN: {
                        result = new Move(fromS, toS, Piece.BLACK_QUEEN);
                        break;
                    }
                    default: {
                        throw new IllegalArgumentException("Unknown promotion piece: " + promotionPiece);
                    }
                }

            }

        } else {
            result = new Move(fromS, toS);

        }

        List<Move> pv = new ArrayList<Move>();

        if (principalVariation != null) {
            Side pvColor = colorToMove;
            for (int i = 0; i < principalVariation.moveCount; i++) {
                pv.add(principalVariation.moves[i].convertToMove(pvColor));
                pvColor.invert();
            }
        }


        // cpScoreWhite
        double cpScoreWhite = ((double) score) / 100;

        //        if (colorToMove == GameColor.BLACK) {
        //            cpScoreWhite = cpScoreWhite * -1;
        //        }

        cpScoreWhite = Math.round(cpScoreWhite * 100.0) / 100.0;

        // Potential mate in n
        int mateIn = 0;
        if (Math.abs(score) > 9000) {
            int plys = 9999 - Math.abs(score);
            mateIn = plys / 2;
            mateIn++;

            // Remove "-" for black mates
//                        if (cpScoreWhite < 0) {
//                            mateIn = mateIn * -1;
//                        }

        }


        MoveAttributes attributes = new MoveAttributes(colorToMove, cpScoreWhite, mateIn, nodes, cutOffs, plannedDepth, maxDepth, timeMs, pv);
        result.setMoveAttributes(attributes);

        return result;

    }

    public EngineMove copy() {
        try {
            return (EngineMove) super.clone();
        } catch (CloneNotSupportedException e) {
            throw new IllegalStateException("Unable to clone move");
        }
    }

    public void fillPvFrom(EngineMove original) {
        this.moveType = original.moveType;
        this.from = original.from;
        this.to = original.to;
        this.promotionPiece = original.promotionPiece;
    }

    @Override
    public int compareTo(EngineMove otherMove) {

        if (otherMove.score > score) {
            return 1;
        }
        if (otherMove.score < score) {
            return -1;
        }
        return 0;
    }

    public static EngineMove[] generateMoveArray() {
        EngineMove[] moves = new EngineMove[100];
        for (int i = 0; i < moves.length; i++) {
            moves[i] = new EngineMove();
        }

        return moves;
    }

    @Override
    public String toString() {
        return this.from + "-" + this.to;
    }

}
