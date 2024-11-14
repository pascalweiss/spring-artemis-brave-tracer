package pw.examples.service_with_tracing;

import io.micrometer.tracing.annotation.NewSpan;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@AllArgsConstructor
@Service
public class PingPongService {

    /**
     * returns pong with a custom trace
     */
    @NewSpan
    public String getPong() {
        return "here is my pong";
    }


    /**
     * returns ping
     */
    @NewSpan
    public String getPing() {
        return "here is my ping";
    }
}
