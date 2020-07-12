package ch.nostromo.tiffanys.lichess.dtos.games.user;

import lombok.Data;

@Data
public class UserGamePlayersPlayer {
    UserGamePlayersPlayerUser user;
    Long rating;
    Long ratingDiff;
}
