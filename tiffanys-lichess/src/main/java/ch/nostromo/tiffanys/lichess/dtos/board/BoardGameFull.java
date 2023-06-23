package ch.nostromo.tiffanys.lichess.dtos.board;

import ch.nostromo.tiffanys.lichess.dtos.commons.Clock;
import ch.nostromo.tiffanys.lichess.dtos.commons.GameState;
import ch.nostromo.tiffanys.lichess.dtos.commons.Perf;
import lombok.Data;

@Data
public class BoardGameFull {

    String type;
    String id;
    String rated;
    String speed;

    String initialFen;

    BoardGameFullVariant variant;
    Clock clock;
    Perf perf;

    BoardGameFullPlayer white;
    BoardGameFullPlayer black;

    GameState state;

}
