package ch.nostromo.tiffanys.lichess.dtos.events;

import lombok.Data;

@Data
public class GameStartEvent {

    String type;
    GameStartEventGame game;

}
