package responseTime;

import org.apache.activemq.ActiveMQConnection;
import org.apache.activemq.ActiveMQConnectionFactory;

import javax.jms.*;

public class JMSProducer {
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

        // Create a 1KB message
        Message message = JMSProducer.createMessage(session);

        // Send the JMS message via the producer
        MessageProducer producer = session.createProducer(destination);
        long start, responseTimeInNano = 0;
        for (long i = 0; i < numOfMessages; i++) {
            start = System.nanoTime();
            producer.send(message);
            responseTimeInNano += (System.nanoTime() - start);
            if(i % 10000 == 0)
                System.out.println("Sent 10000 messages");
//            System.out.println("msg " + i + " sent");
        }
        System.out.println("Producer response Time = " + ((double)responseTimeInNano)/numOfMessages/1e6 + " ms");

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

        return message;
    }
}