package pw.examples.service_with_tracing;

import brave.Tracing;

import brave.jakarta.jms.JmsTracing;
import jakarta.jms.ConnectionFactory;
import lombok.RequiredArgsConstructor;
import org.apache.activemq.artemis.jms.client.ActiveMQConnectionFactory;
import org.apache.activemq.artemis.jms.client.ActiveMQQueue;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jms.annotation.EnableJms;
import org.springframework.jms.core.JmsTemplate;

@EnableJms
@Configuration
@RequiredArgsConstructor
public class MessagingConfig {

    @Bean("pingQueue")
    public ActiveMQQueue pingQueue() {
        return new ActiveMQQueue("ping");
    }



    @Bean("pongQueue")
    public ActiveMQQueue pongQueue() {
        return new ActiveMQQueue("pong");
    }


    /**
     * Creates a ConnectionFactory with tracing enabled
     * @return ConnectionFactory with tracing
     */
    @Bean
    public ConnectionFactory connectionFactory() {
        ActiveMQConnectionFactory factory = new ActiveMQConnectionFactory();
        factory.setUser("admin");
        factory.setPassword("admin");

        var jmsTracing = JmsTracing.newBuilder(Tracing.current())
                .remoteServiceName("artemis")
                .build();

        // wrap the connection factory with brave's tracing
        return jmsTracing.connectionFactory(factory);
    }

    /**
     * JmsTemplate with tracing
     * @param connectionFactory connection factory with tracing
     * @return JmsTemplate
     */
    @Bean
    public JmsTemplate jmsTemplate(ConnectionFactory connectionFactory) {
        return new JmsTemplate(connectionFactory);
    }

}
