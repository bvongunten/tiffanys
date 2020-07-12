package ch.nostromo.tiffanys.lichess.dtos.games;

import lombok.Data;

@Data
public class GamePlayersPlayer {

    String userId;
    Integer rating;
    Integer ratingDiff;
    Boolean provisional;

}
