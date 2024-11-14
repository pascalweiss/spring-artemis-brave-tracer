package pw.examples.service_with_tracing;

import io.micrometer.tracing.Tracer;
import jakarta.jms.JMSException;
import jakarta.jms.Message;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.stereotype.Component;

import java.util.Objects;

@Component
@AllArgsConstructor
@Slf4j
public class MessagingListener {

    JmsTemplate template;
    PingPongService service;
    Tracer tracer;



    /**
     * Receives a message from the pong queue, which was sent by the endpoint {@link PingPongController}.
     * Then sends a message to the replyTo queue.
     * @param msg message received from the ping endpoint
     */
    @JmsListener(destination = "${pw.homelab.role}", containerFactory = "jmsListenerContainerFactory")
    public void onMessagePong(Message msg) throws JMSException {
        logInfo(msg);
        var replyTo = msg.getJMSReplyTo(); // set to ping queue in the {@link PingPongController}
        if(replyTo != null) {
            template.convertAndSend(replyTo, service.getPong());
        }
    }


    private void logInfo(Message msg) throws JMSException {
        var traceId = Objects.requireNonNull(tracer.currentSpan()).context().traceId();
        var spanId = Objects.requireNonNull(tracer.currentSpan()).context().spanId();
        log.info("traceId: {}, spanId: {}, msg: {}, queue: {}, replyTo: {}", traceId, spanId,  msg.getBody(String.class), msg.getJMSDestination(), msg.getJMSReplyTo());
    }

}
