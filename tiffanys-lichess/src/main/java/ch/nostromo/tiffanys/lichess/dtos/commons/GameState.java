package ch.nostromo.tiffanys.lichess.dtos.commons;

import lombok.Data;

@Data
public class GameState {
    String type;
    String moves;
    Long wtime;
    Long btime;
    Integer winc;
    Integer binc;
    String status;
}
