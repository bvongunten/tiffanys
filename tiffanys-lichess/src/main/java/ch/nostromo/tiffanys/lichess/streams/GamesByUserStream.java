package ch.nostromo.tiffanys.lichess.streams;
import ch.nostromo.tiffanys.lichess.dtos.games.user.UserGame;
import com.google.gson.Gson;
import lombok.Getter;
import lombok.Setter;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;

@Getter
@Setter
public class GamesByUserStream {

    HttpRequest request;
    HttpClient httpClient;

    List<UserGame> gamesList = new ArrayList<>();

    List<GamesByUserStreamListener> listeners = new ArrayList<>();

    public GamesByUserStream(HttpClient httpClient, HttpRequest request) {
        this.httpClient = httpClient;
        this.request = request;
    }

    public CompletableFuture<List<UserGame>> startStream() {
        LichessSubscriber<UserGame> pgnSubscriber = new LichessSubscriber<>() {
            @Override
            List<UserGame> createElements(String buffer) {
                ArrayList<UserGame> result = new ArrayList<>();
                Gson gson = new Gson();

                String lines[] = buffer.split("\\r?\\n");

                for (String line : lines) {
                    result.add(gson.fromJson(line, UserGame.class));
                }

                gamesList.addAll(result);

                return result;
            }

            @Override
            void onElementRead(UserGame element) {
                listeners.forEach(e -> e.onGameByUser(element));
            }
        };

        return httpClient.sendAsync(request, responseInfo -> pgnSubscriber)
                .whenComplete((r, t) -> listeners.forEach(e -> e.onAllGamesByUser(gamesList)))
                .thenApply(HttpResponse::body);
    }





    public static void main(String[] args) throws IOException, InterruptedException {

        URI uri = URI.create("https://lichess.org/api/games/user/bvongunten");

        HttpRequest request = HttpRequest.newBuilder()
                .GET()
                .uri(uri)
                .setHeader("User-Agent", "Java 11 HttpClient Bot") // add request header
                .setHeader("Authorization", "Bearer icqNfImCqo8vFO3j")
                .setHeader("Accept", "application/x-ndjson")
                .setHeader("Content-Type", "application/json")
                .build();

        HttpClient httpClient = HttpClient.newBuilder()
                .version(HttpClient.Version.HTTP_1_1)
                .connectTimeout(Duration.ofSeconds(10))
                .build();



        GamesByUserStream cs = new GamesByUserStream(httpClient, request);
        List<UserGame> formats = cs.startStream().join();
        System.out.println(formats.size() + " pgns read");



    }

}
