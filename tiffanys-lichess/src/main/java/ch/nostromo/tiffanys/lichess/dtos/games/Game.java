package ch.nostromo.tiffanys.lichess.dtos.games;

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
