package app.jms;

import app.model.DeliveryRequest;
import org.apache.activemq.ActiveMQConnectionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.config.JmsListenerContainerFactory;
import org.springframework.jms.core.JmsTemplate;
import org.apache.qpid.jms.JmsConnectionFactory;
import javax.jms.*;

public class DeliveryRequestProducer {

    public void sendMessage(DeliveryRequest request) {
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

            //TextMessage msg = session.createTextMessage(message);
            ObjectMessage msg = session.createObjectMessage(request);
            System.out.println("Producer sent: " + msg.toString());
            producer.send(msg);

            session.close();
            connection.close();
        }
        catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}
