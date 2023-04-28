package Throughput;

import org.apache.activemq.ActiveMQConnection;
import org.apache.activemq.ActiveMQConnectionFactory;

import javax.jms.*;

public class JMSProducer {
    private static final String url = ActiveMQConnection.DEFAULT_BROKER_URL;
    private static final String queueName = "THROUGHPUT_QUEUE";
    private static final int numberOfMessages = 500000;
    private static long sleepIntervalMilli = (1000/numberOfMessages);
    private static int sleepIntervalNano = (int) (1e+9/numberOfMessages % 1000000);


    public static void main(String[] args) throws JMSException, InterruptedException {
        System.out.println("sleepIntervalMilli = " + sleepIntervalMilli + " ms\nsleepIntervalNano = " + sleepIntervalNano + " ns");
        sleepIntervalMilli *= 0.8;
        sleepIntervalNano *= 0.8;
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
        long startTime = System.nanoTime();
        for (int i = 0; i < numberOfMessages; i++) {
            producer.send(message);
//            Thread.sleep(sleepIntervalMilli, sleepIntervalNano);
        }

        long endTime = System.nanoTime();
        long elapsedTime = endTime - startTime;

        // Calculate the throughput
        double throughput = ((double) numberOfMessages / elapsedTime) * 1000000000;
        System.out.println("Sent " + numberOfMessages + " messages in " + elapsedTime/1000000000.0 + " sec");
        System.out.println("Throughput: " + numberOfMessages/(elapsedTime/1000000000.0) + " request/second");
        System.out.println("Throughput: " + throughput/1024 + " MB/second");

        // Close the JMS objects
        producer.close();
        session.close();
        connection.close();
    }

    public static Message createMessage(Session session) throws JMSException {

        // Create a BytesMessage
        BytesMessage message = session.createBytesMessage();

        // Create a byte array with 1024 elements
        byte[] bytes = new byte[1000];

        // Set the byte array as the payload of the message
        message.writeBytes(bytes);

        return message;
    }
}
