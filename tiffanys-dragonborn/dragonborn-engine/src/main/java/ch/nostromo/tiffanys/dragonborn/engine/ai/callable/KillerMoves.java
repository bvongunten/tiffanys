package ch.nostromo.tiffanys.dragonborn.engine.ai.callable;

import ch.nostromo.tiffanys.dragonborn.engine.move.EngineMove;

public class KillerMoves {

    EngineMove topKillerMove;
    EngineMove secondKillerMove;

    public final void addKillerMove(EngineMove move) {
        if (topKillerMove == null || move.score > topKillerMove.score) {
            secondKillerMove = topKillerMove;
            topKillerMove = move;
        } else if (secondKillerMove == null || move.score > secondKillerMove.score) {
            secondKillerMove = move;
        }
    }

    public void scoreMoves(EngineMove[] moves, int movesCount) {

        for (int i = 0; i < movesCount; i++) {

            if (topKillerMove != null && moves[i].from == topKillerMove.from && moves[i].to == topKillerMove.to) {
                moves[i].score += 9000;
                break;
            }

            if (secondKillerMove != null && moves[i].from == secondKillerMove.from
                    && moves[i].to == secondKillerMove.to) {
                moves[i].score += 8000;
                break;
            }
        }

    }
}
