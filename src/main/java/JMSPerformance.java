import org.apache.activemq.ActiveMQConnection;
import org.apache.activemq.ActiveMQConnectionFactory;

import javax.jms.*;

public class JMSPerformance {
    private static final String url = ActiveMQConnection.DEFAULT_BROKER_URL;
    private static final String queueName = "MESSAGE_QUEUE";
    public static void main(String[] args) throws JMSException {
        ConnectionFactory connectionFactory = new ActiveMQConnectionFactory(url);
        Connection connection = connectionFactory.createConnection();
        connection.start();
        Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
        Destination destination = session.createQueue(queueName);

        MessageProducer producer = session.createProducer(destination);
        TextMessage message = session.createTextMessage("Hello, World!");
        producer.send(message);

        // Create a message consumer object
        MessageConsumer consumer = session.createConsumer(destination);
        Message receivedMessage = consumer.receive();

        System.out.println(((TextMessage) receivedMessage).getText());

        consumer.close();
        producer.close();
        session.close();
        connection.close();
    }
}