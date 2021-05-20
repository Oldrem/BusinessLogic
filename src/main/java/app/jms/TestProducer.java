package app.jms;

import org.apache.activemq.ActiveMQConnectionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.config.JmsListenerContainerFactory;
import org.springframework.jms.core.JmsTemplate;
import org.apache.qpid.jms.JmsConnectionFactory;
import javax.jms.*;

public class TestProducer implements Runnable {

    public void run() {
        try {
            //ActiveMQConnectionFactory factory = new ActiveMQConnectionFactory("amqp://localhost:5672");

            ConnectionFactory factory = new JmsConnectionFactory("amqp://localhost:5672");
            Connection connection = factory.createConnection();
            connection.start();

            // non transactional
            Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
            Destination dest = session.createQueue("test-queue");
            MessageProducer producer = session.createProducer(dest);
            producer.setDeliveryMode(DeliveryMode.NON_PERSISTENT);

            // 10 test messages
            for (int i = 1; i <= 10; i++) {
                String msg = "Message #" + i;
                TextMessage message = session.createTextMessage(msg);
                System.out.println("Producer Sent: " + msg);
                producer.send(message);
            }

            session.close();
            connection.close();
        }
        catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}
