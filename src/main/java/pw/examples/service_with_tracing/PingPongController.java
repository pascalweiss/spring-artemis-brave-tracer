package pw.examples.service_with_tracing;

import io.micrometer.tracing.Tracer;
import jakarta.jms.Message;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.activemq.artemis.jms.client.ActiveMQQueue;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import java.util.Objects;
import java.util.Queue;

@Slf4j
@RestController
@AllArgsConstructor
public class PingPongController {

    private final JmsTemplate jmsTemplate;
    private final PingPongService service;
    private final ActiveMQQueue pingQueue;
    private final ActiveMQQueue pongQueue;
    private final Tracer tracer;

    /**
     * Receives a ping from the user. Then request-reply with messaging: Sends a message to the pong queue with a replyTo the ping queue.
     * Message is received in {@link MessagingListener#onMessagePing(Message)}
     */
    @GetMapping("/ping")
    public String receivePing() {
        logInfo("REST endpoint: ping");
        jmsTemplate.convertAndSend(pongQueue, service.getPing(), message -> {
            message.setJMSReplyTo(pingQueue);
            return message;
        });
        return service.getPing();
    }

    public void logInfo(String msg) {
        var traceId = Objects.requireNonNull(tracer.currentSpan()).context().traceId();
        var spanId = Objects.requireNonNull(tracer.currentSpan()).context().spanId();
        log.info("traceId: {}, spanId: {}, msg: {}", traceId, spanId, msg);
    }

}
