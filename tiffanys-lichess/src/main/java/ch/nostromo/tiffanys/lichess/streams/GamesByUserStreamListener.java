package ch.nostromo.tiffanys.lichess.streams;

import ch.nostromo.tiffanys.lichess.dtos.games.user.UserGame;

import java.util.List;

public interface GamesByUserStreamListener {

   void onGameByUser(UserGame game);

   void onAllGamesByUser(List<UserGame> games);

}
