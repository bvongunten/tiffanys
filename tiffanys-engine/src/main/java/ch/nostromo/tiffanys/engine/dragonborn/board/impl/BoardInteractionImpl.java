package ch.nostromo.tiffanys.engine.dragonborn.board.impl;

import ch.nostromo.tiffanys.commons.enums.GameColor;
import ch.nostromo.tiffanys.engine.dragonborn.board.RobustBoard;
import ch.nostromo.tiffanys.engine.dragonborn.board.impl.fast.TiffanysBoardInterfaceBlack;
import ch.nostromo.tiffanys.engine.dragonborn.board.impl.fast.TiffanysBoardInterfaceWhite;
import ch.nostromo.tiffanys.engine.dragonborn.move.EngineMove;
import ch.nostromo.tiffanys.engine.dragonborn.DragonbornConstants;
import ch.nostromo.tiffanys.engine.dragonborn.board.api.BoardInteraction;

public class BoardInteractionImpl implements BoardInteraction, DragonbornConstants {

    TiffanysBoardInterfaceWhite boardInterfaceWhite = new TiffanysBoardInterfaceWhite();
    TiffanysBoardInterfaceBlack boardInterfaceBlack = new TiffanysBoardInterfaceBlack();

    @Override
    public boolean isKingInCheck(RobustBoard board, GameColor colorToMove) {
        if (colorToMove == GameColor.WHITE) {
            return boardInterfaceWhite.isWhiteKingInCheck(board);
        } else {
            return boardInterfaceBlack.isBlackKingInCheck(board);
        }
    }

    @Override
    public void applyMove(RobustBoard board, GameColor colorToMove, EngineMove move) {
        if (colorToMove == GameColor.WHITE) {
            boardInterfaceWhite.makeMoveWhite(board, move);
        } else {
            boardInterfaceBlack.makeMoveBlack(board, move);
         //   board.zobristKey  ^= Zobrist.ZOBRIST_SIDE_TO_MOVE;
        }
    }

    @Override
    public void unapplyMove(RobustBoard board, GameColor colorToMove, EngineMove move) {
        if (colorToMove == GameColor.WHITE) {
            boardInterfaceWhite.undoMoveWhite(board, move);
        } else {
            //  board.zobristKey  ^= Zobrist.ZOBRIST_SIDE_TO_MOVE;
            boardInterfaceBlack.undoMoveBlack(board, move);
        }

    }

}
