package uni.csw.medibug.telemetry_context.telemetry_management.infrastructure.persistence.timeseries.buffer;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.function.Consumer;

public class TelemetryBuffer<T> {

    private final int batchSize;
    private final Consumer<List<T>> flushFn;
    private final ConcurrentLinkedQueue<T> queue = new ConcurrentLinkedQueue<>();

    public TelemetryBuffer(int batchSize, Consumer<List<T>> flushFn) {
        this.batchSize = batchSize;
        this.flushFn = flushFn;
    }

    public void add(T t) {
        this.queue.add(t);
    }

    public void flush() {
        List<T> batch = new ArrayList<>();
        T item;
        // poll sirve para
        while ((item = queue.poll()) != null) {
            batch.add(item);
            if (batch.size() >= batchSize) {
                flushFn.accept(batch);
                batch.clear();
            }
        }
        if (!batch.isEmpty()) {
            flushFn.accept(batch);
        }
    }
}
