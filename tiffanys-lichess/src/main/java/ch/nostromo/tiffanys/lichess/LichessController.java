package ch.nostromo.tiffanys.lichess;


import ch.nostromo.tiffanys.lichess.dtos.commons.Challenge;
import ch.nostromo.tiffanys.lichess.dtos.commons.MoveResponse;
import ch.nostromo.tiffanys.lichess.dtos.games.Game;
import ch.nostromo.tiffanys.lichess.dtos.playing.OngoingGames;
import ch.nostromo.tiffanys.lichess.exception.LichessException;
import ch.nostromo.tiffanys.lichess.streams.BoardGameStateStream;
import ch.nostromo.tiffanys.lichess.streams.EventStream;
import ch.nostromo.tiffanys.lichess.streams.GamesByUserStream;
import com.google.gson.Gson;
import lombok.Getter;
import lombok.Setter;

import java.io.IOException;
import java.net.URI;
import java.net.URLEncoder;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.util.HashMap;
import java.util.Map;
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

    private HttpRequest createPostRequestForm(String url, Map<String, String> data) {
        URI uri = URI.create(url);

        HttpRequest request = HttpRequest.newBuilder()
                .uri(uri)
                .POST(ofFormData(data))
                .timeout(Duration.ofMinutes(1))
                .setHeader("User-Agent", "Tiffanys Lichess Client") // add request header
                .setHeader("Authorization", "Bearer " + apiKey)
                .setHeader("Content-Type", "application/x-www-form-urlencoded")
                .setHeader("Accept", "application/x-ndjson")
                .build();

        return request;
    }

    public static HttpRequest.BodyPublisher ofFormData(Map<String, String> data) {
        var builder = new StringBuilder();
        for (Map.Entry<String, String> entry : data.entrySet()) {
            if (builder.length() > 0) {
                builder.append("&");
            }
            builder.append(URLEncoder.encode(entry.getKey().toString(), StandardCharsets.UTF_8));
            builder.append("=");
            builder.append(URLEncoder.encode(entry.getValue().toString(), StandardCharsets.UTF_8));
        }
        return HttpRequest.BodyPublishers.ofString(builder.toString());
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
                throw new LichessException("POST command returned HTTP Code != 200. (" + response.statusCode() + ") Response body: " + response.body());
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

    public Challenge challengePlayer(String player, String rated, String clockLimit, String increment, String color) {
        LOG.fine("Challenging player " + player);

        HashMap<String, String> parameters = new HashMap<>();
        parameters.put("rated", rated);
        parameters.put("clock.limit", clockLimit);
        parameters.put("clock.increment", increment);
        parameters.put("color", color);


        HttpRequest request = createPostRequestForm("https://lichess.org/api/challenge/" + player, parameters);
        HttpResponse<String> response = runPostRequest(request);

        LOG.fine(response.body());

        return new Gson().fromJson(response.body(), Challenge.class);
    }

    public Challenge createSeek(String rated, String clockLimit, String increment, String color) {
        LOG.fine("Create seek");

        HashMap<String, String> parameters = new HashMap<>();
        parameters.put("rated", rated);
        parameters.put("time", clockLimit);
        parameters.put("increment", increment);
        parameters.put("color", color);


        HttpRequest request = createPostRequestForm("https://lichess.org/api/board/seek", parameters);
        HttpResponse<String> response = runPostRequest(request);

        LOG.fine(response.body());

        return new Gson().fromJson(response.body(), Challenge.class);
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

    public EventStream getEventStream() {
        LOG.info("Createing event stream");

        HttpRequest request = createGetRequest("https://lichess.org/api/stream/event");
        return new EventStream(getHttpClient(), request);
    }


    public GamesByUserStream getGamesByUserStream(String userId) {
        LOG.info("Createing game list stream for user " + userId);

        HttpRequest request = createGetRequest("https://lichess.org/api/games/user/" + userId);
        return new GamesByUserStream(getHttpClient(), request);
    }



}
