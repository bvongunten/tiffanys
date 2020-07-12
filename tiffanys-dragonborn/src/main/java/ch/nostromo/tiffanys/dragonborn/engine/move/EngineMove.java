package ch.nostromo.tiffanys.dragonborn.engine.move;

import java.util.ArrayList;
import java.util.List;

import ch.nostromo.tiffanys.commons.enums.Castling;
import ch.nostromo.tiffanys.commons.enums.GameColor;
import ch.nostromo.tiffanys.commons.enums.Piece;
import ch.nostromo.tiffanys.commons.move.Move;
import ch.nostromo.tiffanys.commons.move.MoveAttributes;
import ch.nostromo.tiffanys.dragonborn.engine.DragonbornEngineConstants;
import ch.nostromo.tiffanys.dragonborn.engine.board.RobustBoard;
import ch.nostromo.tiffanys.dragonborn.engine.ai.PrincipalVariation;

public class EngineMove implements Comparable<EngineMove>, DragonbornEngineConstants, Cloneable {

    // Move stuff
    public static final int MOVE = 0;
    public static final int MOVE_EP_FIELD = 1;
    public static final int MOVE_CASTLING_AFFECTED_ROOK = 2;
    public static final int MOVE_CASTLING_AFFECTED_KING = 3;

    public static final int SHORT_CASTLING = 15;
    public static final int LONG_CASTLING = 16;

    public static final int HIT_MOVES_TRESHHOLD = 20;

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
    public int rookFrom = -1;
    public int rookTo = -1;
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

    public EngineMove[] killersBuffer;
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
        this.promotionPiece = DragonbornEngineConstants.PIECE_BISHOP;
        this.moveType = MOVE_PROMOTION;
        this.hitScore = 0;
    }

    public final void setPromotionMoveKnight(int from, int to) {
        this.from = from;
        this.to = to;
        this.promotionPiece = DragonbornEngineConstants.PIECE_KNIGHT;
        this.moveType = MOVE_PROMOTION;
        this.hitScore = 0;
    }

    public final void setPromotionHitMoveQueen(int from, int to, int targetScore) {
        this.from = from;
        this.to = to;
        this.promotionPiece = DragonbornEngineConstants.PIECE_QUEEN;
        this.moveType = HIT_MOVE_PROMOTION;
        this.hitScore = targetScore - QUEEN_SCORE;
    }

    public final void setPromotionHitMoveRook(int from, int to, int targetScore) {
        this.from = from;
        this.to = to;
        this.promotionPiece = DragonbornEngineConstants.PIECE_ROOK;
        this.moveType = HIT_MOVE_PROMOTION;
        this.hitScore = targetScore - ROOK_SCORE;
    }

    public final void setPromotionHitMoveBishop(int from, int to, int targetScore) {
        this.from = from;
        this.to = to;
        this.promotionPiece = DragonbornEngineConstants.PIECE_BISHOP;
        this.moveType = HIT_MOVE_PROMOTION;
        this.hitScore = targetScore - BISHOP_SCORE;
    }

    public final void setPromotionHitMoveKnight(int from, int to, int targetScore) {
        this.from = from;
        this.to = to;
        this.promotionPiece = DragonbornEngineConstants.PIECE_KNIGHT;
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

    public Move convertToMove(GameColor colorToMove) {
        String fromS = RobustBoard.BOARD64_COORDS[from];
        String toS = RobustBoard.BOARD64_COORDS[to];

        Move result;

        if (moveType == HIT_MOVE_ENPASSANT) {
            result = new Move(fromS, toS);

        } else if (moveType == SHORT_CASTLING) {
            if (colorToMove == GameColor.WHITE) {
                result = new Move(Castling.WHITE_SHORT);
            } else {
                result = new Move(Castling.BLACK_SHORT);
            }

        } else if (moveType == LONG_CASTLING) {
            if (colorToMove == GameColor.WHITE) {
                result = new Move(Castling.WHITE_LONG);
            } else {
                result = new Move(Castling.BLACK_LONG);
            }

        } else if (moveType == HIT_MOVE_PROMOTION || moveType == MOVE_PROMOTION) {

            switch (promotionPiece) {
            case DragonbornEngineConstants.PIECE_BISHOP: {
                result = new Move(fromS, toS, Piece.BISHOP);
                break;
            }
            case DragonbornEngineConstants.PIECE_KNIGHT: {
                result = new Move(fromS, toS, Piece.KNIGHT);
                break;
            }
            case DragonbornEngineConstants.PIECE_ROOK: {
                result = new Move(fromS, toS, Piece.ROOK);
                break;
            }
            case DragonbornEngineConstants.PIECE_QUEEN: {
                result = new Move(fromS, toS, Piece.QUEEN);
                break;
            }
            default: {
                throw new IllegalArgumentException("Unknown promotion piece: " + promotionPiece);
            }
            }

        } else {
            result = new Move(fromS, toS);

        }

        List<Move> pv = new ArrayList<Move>();

        if (principalVariation != null) {
            GameColor pvColor = colorToMove;
            for (int i = 0; i < principalVariation.moveCount; i++) {
                pv.add(principalVariation.moves[i].convertToMove(pvColor));
                pvColor.invert();
            }
        }

        MoveAttributes attributes = new MoveAttributes(score, nodes, cutOffs, plannedDepth, maxDepth, timeMs, pv);
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
