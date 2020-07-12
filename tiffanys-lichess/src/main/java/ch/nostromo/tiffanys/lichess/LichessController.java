package ch.nostromo.tiffanys.lichess;


import ch.nostromo.tiffanys.lichess.dtos.commons.MoveResponse;
import ch.nostromo.tiffanys.lichess.dtos.games.Game;
import ch.nostromo.tiffanys.lichess.dtos.playing.OngoingGames;
import ch.nostromo.tiffanys.lichess.exception.LichessException;
import ch.nostromo.tiffanys.lichess.streams.BoardGameStateStream;
import ch.nostromo.tiffanys.lichess.streams.GamesByUserStream;
import com.google.gson.Gson;
import lombok.Getter;
import lombok.Setter;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.util.List;
import java.util.logging.Logger;

@Getter
@Setter
public class LichessController {
    protected Logger LOG = Logger.getLogger(getClass().getName());

    private String apiKey;

    public LichessController(String apiKey) {
        this.apiKey = apiKey;
    }


    private HttpRequest createPostRequest(String url, String body) {
        URI uri = URI.create(url);

        HttpRequest request = HttpRequest.newBuilder()
                .uri(uri)
                .timeout(Duration.ofMinutes(1))
                .setHeader("User-Agent", "Tiffanys Lichess Client") // add request header
                .setHeader("Authorization", "Bearer " + apiKey)
                .setHeader("Accept", "application/x-ndjson")
                .POST(HttpRequest.BodyPublishers.ofString(body))
                .build();

        return request;
    }

    private HttpRequest createGetRequest(String url) {
        URI uri = URI.create(url);

        HttpRequest request = HttpRequest.newBuilder()
                .uri(uri)
                .timeout(Duration.ofMinutes(1))
                .setHeader("User-Agent", "Tiffanys Lichess Client") // add request header
                .setHeader("Authorization", "Bearer " + apiKey)
                .setHeader("Accept", "application/x-ndjson")
                .GET()
                .build();

        return request;
    }

    private HttpClient getHttpClient() {

        HttpClient httpClient = HttpClient.newBuilder()
                .version(HttpClient.Version.HTTP_1_1)
                .connectTimeout(Duration.ofSeconds(10))
                .build();

        return httpClient;
    }

    private HttpResponse runPostRequest(HttpRequest request) {
        try {
            HttpResponse<String> response = getHttpClient().send(request,
                    HttpResponse.BodyHandlers.ofString());
            if (response.statusCode() != 200) {
                throw new LichessException("POST command returned HTTP Code != 200. Response body: " + response.body());
            }

            return response;
        } catch (IOException e) {
            throw new LichessException("IO exception runing lichess POST request with message " + e.getMessage(), e);
        } catch (InterruptedException e) {
            throw new LichessException("Interrupted exception runing lichess POST request with message " + e.getMessage(), e);
        }

    }

    public void makeMove(String gameId, String uciMove) {
        LOG.info("Making move: " +uciMove + " to game " + gameId);
        HttpRequest request = createPostRequest("https://lichess.org/api/board/game/" + gameId + "/move/" + uciMove, "");
        HttpResponse<String> response = runPostRequest(request);

        MoveResponse lichessResponse = new Gson().fromJson(response.body(), MoveResponse.class);

        if (!lichessResponse.getOk()) {
            throw new LichessException("Move not accepted by lichess! GameId=" + gameId + ", Move=" + uciMove);
        }
    }

    public Game getGameByGameId(String gameId) {
        LOG.info("Searching for game " + gameId);

        HttpRequest request = createGetRequest("https://lichess.org/api/game/" + gameId);
        HttpResponse<String> response = runPostRequest(request);
        Game result = new Gson().fromJson(response.body(), Game.class);

        return result;

    }

    public OngoingGames getOpenGames() {
        LOG.info("Searching for games ...");

        HttpRequest request = createGetRequest("https://lichess.org/api/account/playing");
        HttpResponse<String> response = runPostRequest(request);
        OngoingGames result = new Gson().fromJson(response.body(), OngoingGames.class);

        return result;
    }


    public BoardGameStateStream getBoardGameStateStream(String gameId) {
        LOG.info("Createing state stream for game " + gameId);

        HttpRequest request = createGetRequest("https://lichess.org/api/board/game/stream/" + gameId);
        return new BoardGameStateStream(getHttpClient(), request);
    }

    public GamesByUserStream getGamesByUserStream(String userId) {
        LOG.info("Createing game list stream for user " + userId);

        HttpRequest request = createGetRequest("https://lichess.org/api/games/user/" + userId);
        return new GamesByUserStream(getHttpClient(), request);
    }



}
