package ch.nostromo.tiffanys.engine.impl.move.impl;

import ch.nostromo.tiffanys.commons.enums.GameColor;
import ch.nostromo.tiffanys.engine.impl.board.RobustBoard;
import ch.nostromo.tiffanys.engine.impl.move.EngineMove;
import ch.nostromo.tiffanys.engine.impl.move.impl.fast.FastMoveGen;
import ch.nostromo.tiffanys.engine.impl.DragonbornEngineConstants;
import ch.nostromo.tiffanys.engine.impl.move.api.MoveGenerator;

public class MoveGeneratorImpl implements DragonbornEngineConstants, MoveGenerator {

    FastMoveGen moveGen = new FastMoveGen();

    @Override
    public int generateMovesList(RobustBoard board, EngineMove[] moves, GameColor color) {
        if (color == GameColor.WHITE) {
            return moveGen.generateWhiteMovesList(board, moves);
        } else {
            return moveGen.generateBlackMovesList(board, moves);
        }
    }

    @Override
    public int generateHitMovesList(RobustBoard board, EngineMove[] moves, GameColor color) {
        if (color == GameColor.WHITE) {
            return moveGen.generateWhiteMovesHitList(board, moves);
        } else {
            return moveGen.generateBlackMovesHitList(board, moves);
        }
    }

}
