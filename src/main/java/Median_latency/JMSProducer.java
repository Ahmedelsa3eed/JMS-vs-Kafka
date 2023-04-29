
package Median_latency;
import org.apache.activemq.ActiveMQConnection;
import org.apache.activemq.ActiveMQConnectionFactory;

import javax.jms.*;

public class JMSProducer {
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

        // Create a 1KB message
        // Message message = JMSProducer.createMessage(session);

        // Send the JMS message via the producer
        MessageProducer producer = session.createProducer(destination);
        long start, responseTimeInMillis = 0;
        for (int i = 0; i < 100000; i++) {
            Message message = JMSProducer.createMessage(session);
            start = System.currentTimeMillis();
            producer.send(message);
            responseTimeInMillis += (System.currentTimeMillis() - start);
            // System.out.println("msg " + i + " sent");
        }
        System.out.println("Producer response Time = " + responseTimeInMillis/20 + "(ms)");

        // Close the JMS objects
        producer.close();
        session.close();
        connection.close();
    }

    public static Message createMessage(Session session) throws JMSException {
        // Create a BytesMessage
        BytesMessage message = session.createBytesMessage();
        // Create a byte array with 1024 elements
        byte[] bytes = new byte[1024];
        // Set the byte array as the payload of the message
        message.writeBytes(bytes);
        message.setLongProperty("timestamp", System.nanoTime());
        return message;
    }
}