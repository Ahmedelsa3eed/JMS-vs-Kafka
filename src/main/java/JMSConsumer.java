import org.apache.activemq.ActiveMQConnection;
import org.apache.activemq.ActiveMQConnectionFactory;

import javax.jms.*;

public class JMSConsumer {
    private static final String url = ActiveMQConnection.DEFAULT_BROKER_URL;
    private static final String queueName = "MESSAGE_QUEUE";
    public static void main(String[] args) throws JMSException {
        // Obtain a JMS connection to activeMQ provider
        ConnectionFactory connectionFactory = new ActiveMQConnectionFactory(url);
        Connection connection = connectionFactory.createConnection();
        connection.start();

        // Create a JMS session
        Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
        Destination destination = session.createQueue(queueName);

        // Receive the JMS message via the consumer
        MessageConsumer consumer = session.createConsumer(destination);
        long start, responseTimeInMillis = 0;
        for (int i = 0; i < 20; i++) {
            start = System.currentTimeMillis();
            consumer.receive();
            responseTimeInMillis += (System.currentTimeMillis() - start);
            System.out.println("msg " + i + " received");
        }
        System.out.println("Consumer response Time = " + responseTimeInMillis/20 + "(ms)");

        // Close the JMS objects
        consumer.close();
        session.close();
        connection.close();
    }
}
