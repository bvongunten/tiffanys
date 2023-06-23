package ch.nostromo.tiffanys.lichess.streams;


import java.net.http.HttpResponse;
import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;
import java.util.concurrent.Flow;

public abstract class LichessSubscriber<T> implements HttpResponse.BodySubscriber<List<T>> {

    final CompletableFuture<List<T>> completableFuture = new CompletableFuture<>();

    Flow.Subscription subscription;

    List<T> responseData = new ArrayList<>();

    @Override
    public CompletionStage<List<T>> getBody() {
        return completableFuture;
    }

    @Override
    public void onSubscribe(Flow.Subscription subscription) {
        this.subscription = subscription;
        subscription.request(1);
    }

    abstract List<T> createElements(String element);

    abstract void onElementRead(T element);

    @Override
    public void onNext(List<ByteBuffer> buffers) {
        String bufferString = asString(buffers);
        List<T> elements = createElements(bufferString);
        responseData.addAll(elements);

        elements.forEach(this::onElementRead);

        subscription.request(1);
    }

    @Override
    public void onError(Throwable throwable) {
        completableFuture.completeExceptionally(throwable);
    }

    @Override
    public void onComplete() {
        completableFuture.complete(responseData);
    }

    private String asString(List<ByteBuffer> buffers) {
        return new String(toBytes(buffers), StandardCharsets.UTF_8);
    }

    private byte[] toBytes(List<ByteBuffer> buffers) {
        int size = buffers.stream()
                .mapToInt(ByteBuffer::remaining)
                .sum();
        byte[] bs = new byte[size];
        int offset = 0;
        for (ByteBuffer buffer : buffers) {
            int remaining = buffer.remaining();
            buffer.get(bs, offset, remaining);
            offset += remaining;
        }
        return bs;
    }

}
