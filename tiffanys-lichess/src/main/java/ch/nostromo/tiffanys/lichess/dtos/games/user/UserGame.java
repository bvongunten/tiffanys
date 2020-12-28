package ch.nostromo.tiffanys.lichess.dtos.games.user;

import ch.nostromo.tiffanys.lichess.dtos.commons.Clock;
import ch.nostromo.tiffanys.lichess.dtos.commons.GameState;
import ch.nostromo.tiffanys.lichess.dtos.commons.Opening;
import lombok.Data;

@Data
public class UserGame {

    String id;
    Boolean rated;
    String variant;
    String speed;
    String perf;
    Long createdAt;
    Long lastMoveAt;
    String status;

    String moves;

    UserGamePlayers players;

    Opening opening;

    Clock clock;

    GameState state;

}
