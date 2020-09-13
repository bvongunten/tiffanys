package ch.nostromo.tiffanys.lichess.streams;

import ch.nostromo.tiffanys.lichess.dtos.events.GameStartEvent;
import ch.nostromo.tiffanys.lichess.exception.LichessException;

public interface EventStreamListener {

    void onGameStartEvent(GameStartEvent gameStartEvent);
    void onStreamException(LichessException element);

}
