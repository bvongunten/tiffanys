package ch.nostromo.tiffanys.engine.dragonborn.ai;

import ch.nostromo.tiffanys.engine.dragonborn.move.EngineMove;

public class PrincipalVariation {

    public EngineMove[] moves;;
    public int moveCount = 0;

    public PrincipalVariation() {
        moves = EngineMove.generateMoveArray();
    }

    public String toString() {
        String result = "";
        for (int i = 0; i < moveCount; i++) {
            result += i + " " + moves[i].toString() + " - ";
        }
        return result;
    }

}
