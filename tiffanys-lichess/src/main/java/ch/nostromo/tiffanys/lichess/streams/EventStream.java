package ch.nostromo.tiffanys.lichess.streams;

import ch.nostromo.tiffanys.lichess.dtos.TypeHelper;
import ch.nostromo.tiffanys.lichess.dtos.board.BoardGameFull;
import ch.nostromo.tiffanys.lichess.dtos.commons.ChatLine;
import ch.nostromo.tiffanys.lichess.dtos.commons.GameState;
import ch.nostromo.tiffanys.lichess.dtos.events.GameStartEvent;
import ch.nostromo.tiffanys.lichess.exception.LichessException;
import com.google.gson.Gson;
import lombok.Getter;
import lombok.Setter;

import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CancellationException;
import java.util.concurrent.CompletableFuture;
import java.util.logging.Logger;

@Getter
@Setter
public class EventStream {
    protected Logger LOG = Logger.getLogger(getClass().getName());

    HttpRequest request;
    HttpClient httpClient;

    boolean isRunning = true;

    List<EventStreamListener> listeners = new ArrayList<>();

    public EventStream(HttpClient httpClient, HttpRequest request) {
        this.httpClient = httpClient;
        this.request = request;
    }

    public CompletableFuture<List<Object>> startStream() {
        LichessSubscriber<Object> subscriber = new LichessSubscriber<>() {
            @Override
            List<Object> createElements(String buffer) {
                ArrayList<Object> result = new ArrayList<>();

                try {
                    Gson gson = new Gson();

                    String lines[] = buffer.split("\\r?\\n");

                    // Determine type
                    for (String line : lines) {
                        LOG.fine("Received event line: " + line);

                        TypeHelper th = gson.fromJson(line, TypeHelper.class);
                        switch (th.getType()) {
                            case "gameStart": {
                                LOG.fine("gamestart");
                                result.add(gson.fromJson(line, GameStartEvent.class));
                                break;
                            }
                            default: {
                                LOG.warning("Ignoring stream event line: " + line);
                            }
                        }

                    }

                    LOG.fine("Result: " + result);

                } catch (Exception e) {
                    result.add(new LichessException("Unexpected exception during stream read", e));
                }


                if (!isRunning) {
                    throw new CancellationException("Stream cancelled");
                }

                return result;

            }

            @Override
            void onElementRead(Object element) {
                if (element instanceof GameStartEvent) {
                    listeners.forEach(e -> e.onGameStartEvent((GameStartEvent) element));
                } else if (element instanceof LichessException) {
                    listeners.forEach(e -> e.onStreamException((LichessException) element));
                }
            }
        };

        return httpClient.sendAsync(request, responseInfo -> subscriber)
                .exceptionally(ex -> {
                    throw new LichessException("Stream aborted: " + ex.getMessage(), ex);
                }).thenApply(HttpResponse::body);
    }

}
