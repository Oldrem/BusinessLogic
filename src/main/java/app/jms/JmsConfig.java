package app.jms;

import org.apache.activemq.command.ActiveMQQueue;
import org.apache.qpid.jms.JmsConnectionFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jms.annotation.EnableJms;
import org.springframework.jms.config.DefaultJmsListenerContainerFactory;

import javax.jms.Queue;

@Configuration
@EnableJms
public class JmsConfig {

    @Bean
    public JmsConnectionFactory jmsConnection() {
        JmsConnectionFactory jmsConnection = new JmsConnectionFactory();
        jmsConnection.setRemoteURI("amqp://localhost:5672");
        jmsConnection.setUsername("user");
        jmsConnection.setPassword("user");
        return jmsConnection;
    }

    @Bean
    public DefaultJmsListenerContainerFactory jmsListenerContainerFactory() {
        DefaultJmsListenerContainerFactory factory = new DefaultJmsListenerContainerFactory();
        factory.setConnectionFactory(jmsConnection());
        return factory;
    }
}
