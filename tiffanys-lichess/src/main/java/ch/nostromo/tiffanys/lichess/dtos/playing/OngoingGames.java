package ch.nostromo.tiffanys.lichess.dtos.playing;

import lombok.Data;

import java.util.List;

@Data
public class OngoingGames {

    List<OngoingGame> nowPlaying;
}
