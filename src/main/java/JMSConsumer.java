import org.apache.activemq.ActiveMQConnection;
import org.apache.activemq.ActiveMQConnectionFactory;

import javax.jms.*;

public class JMSConsumer {
    private static final String url = ActiveMQConnection.DEFAULT_BROKER_URL;
    private static final String queueName = "MESSAGE_QUEUE";
    private static final long numOfMessages = 100000;

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
        long start, responseTimeInNano = 0;
        for (long i = 0; i < numOfMessages; i++) {
            start = System.nanoTime();
            consumer.receive();
            responseTimeInNano += (System.nanoTime() - start);
            if(i % 10000 == 0)
                System.out.println("Received 10000 messages");
//            System.out.println("msg " + i + " received");
        }
        System.out.println("Consumer response Time = " + ((double)responseTimeInNano)/numOfMessages/1e6 + " ms");

        // Close the JMS objects
        consumer.close();
        session.close();
        connection.close();
    }
}
