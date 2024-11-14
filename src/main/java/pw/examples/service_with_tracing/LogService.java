package pw.examples.service_with_tracing;

import io.micrometer.tracing.Tracer;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Objects;

@Slf4j
public class LogService {

    /**
     * Prints message together with traceId and spanId
     * @param msg message to be printed
     */
    public static void logInfo(String msg, Tracer tracer) {
        var traceId = Objects.requireNonNull(tracer.currentSpan()).context().traceId();
        var spanId = Objects.requireNonNull(tracer.currentSpan()).context().spanId();
        log.info("traceId: {}, spanId: {}, msg: {}", traceId, spanId, msg);
    }
}
