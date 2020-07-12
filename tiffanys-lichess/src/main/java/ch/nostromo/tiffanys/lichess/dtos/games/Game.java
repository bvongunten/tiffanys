package ch.nostromo.tiffanys.lichess.dtos.games;

import ch.nostromo.tiffanys.lichess.dtos.commons.Clock;
import ch.nostromo.tiffanys.lichess.dtos.commons.GameState;
import ch.nostromo.tiffanys.lichess.dtos.commons.Opening;
import ch.nostromo.tiffanys.lichess.dtos.games.user.UserGamePlayers;
import lombok.Data;

@Data
public class Game {

    String id;
    Boolean rated;
    String variant;
    String speed;
    String perf;
    Long createdAt;
    Long lastMoveAt;
    String status;


    Integer turns;
    String color;

    GamePlayers players;

}
