package ch.nostromo.tiffanys.lichess.dtos.playing;

import lombok.Data;

@Data
public class OngoingGame {

    String fullId;
    String fen;
    String color;
    String lastMove;
    OngoingGameVariant variant;

    String spped;
    String perf;
    Boolean rated;
    Boolean hasMoved;
    Boolean isMyTurn;

    OngoingGameOpponent opponent;

    @Override
    public String toString() {
        String result =  color + " vs. " +  getOpponent().getUsername();

        if (isMyTurn) {
            result += " [My Turn]";
        }

        return result;
    }

}
