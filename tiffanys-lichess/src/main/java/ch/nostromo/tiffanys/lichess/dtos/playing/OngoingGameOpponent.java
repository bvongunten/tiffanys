package ch.nostromo.tiffanys.lichess.dtos.playing;

import lombok.Data;

@Data
public class OngoingGameOpponent {

    String id;
    String username;
    Integer rating;

}
