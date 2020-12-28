package ch.nostromo.tiffanys.lichess.streams;

import ch.nostromo.tiffanys.lichess.dtos.TypeHelper;
import ch.nostromo.tiffanys.lichess.dtos.board.BoardGameFull;
import ch.nostromo.tiffanys.lichess.dtos.commons.ChatLine;
import ch.nostromo.tiffanys.lichess.dtos.commons.GameState;
import ch.nostromo.tiffanys.lichess.exception.LichessException;
import com.google.gson.Gson;
import lombok.Getter;
import lombok.Setter;

import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.logging.Logger;

@Getter
@Setter
public class BoardGameStateStream {
    protected Logger LOG = Logger.getLogger(getClass().getName());

    HttpRequest request;
    HttpClient httpClient;

    List<BoardGameStateListener> listeners = new ArrayList<>();

    public BoardGameStateStream(HttpClient httpClient, HttpRequest request) {
        this.httpClient = httpClient;
        this.request = request;
    }

    public CompletableFuture<List<Object>> startStream() {
        LichessSubscriber<Object> pgnSubscriber = new LichessSubscriber<>() {
            @Override
            List<Object> createElements(String buffer) {
                ArrayList<Object> result = new ArrayList<>();
                Gson gson = new Gson();

                String lines[] = buffer.split("\\r?\\n");

                // Determine type
                for (String line : lines) {
                    LOG.fine("Received stte line: " + line);

                    TypeHelper th = gson.fromJson(line, TypeHelper.class);
                    switch(th.getType()) {
                        case "gameFull" : {
                            result.add(gson.fromJson(line, BoardGameFull.class));
                            break;
                        }
                        case "gameState" : {
                            result.add(gson.fromJson(line, GameState.class));
                            break;
                        }
                        case "chatLine" : {
                            result.add(gson.fromJson(line, ChatLine.class));
                            break;
                        }
                        default: {
                            throw new LichessException("Unexpected game stream type: " + th.getType());
                        }
                    }

                }

                return result;
            }

            @Override
            void onElementRead(Object element) {
                if (element instanceof BoardGameFull) {
                    listeners.forEach(e -> e.onBoardGameStateFull((BoardGameFull) element));
                } else if (element instanceof GameState) {
                    listeners.forEach(e -> e.onBoardGameState((GameState) element));
                }
            }
        };

        return httpClient.sendAsync(request, responseInfo -> pgnSubscriber)
                .thenApply(HttpResponse::body);
    }

}
