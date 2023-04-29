package Throughput;

import org.apache.activemq.ActiveMQConnection;
import org.apache.activemq.ActiveMQConnectionFactory;

import javax.jms.*;

public class JMSConsumer implements MessageListener{
    private static final String url = ActiveMQConnection.DEFAULT_BROKER_URL;
    private static final String queueName = "THROUGHPUT_QUEUE";
    private static final int numberOfMessages = 100000;
    private static int messageCount = 0;
    private final long startTime;

    JMSConsumer(long startTime){
        this.startTime = startTime;
    }

    public static void main(String[] args) throws JMSException, InterruptedException {
        // Obtain a JMS connection to activeMQ provider
        ConnectionFactory connectionFactory = new ActiveMQConnectionFactory(url);
        Connection connection = connectionFactory.createConnection();
        connection.start();

        // Create a JMS session
        Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
        Destination destination = session.createQueue(queueName);

        // Receive the JMS message via the consumer
        MessageConsumer consumer = session.createConsumer(destination);

        // Set the message listener
        consumer.setMessageListener(new JMSConsumer(System.nanoTime()));

        // Wait for the messages to be consumed
        Thread.sleep(100000000);

        // Clean up
        consumer.close();
        session.close();
        connection.close();

        // Close the JMS objects
        consumer.close();
        session.close();
        connection.close();
    }

    @Override
    public void onMessage(Message message) {
        messageCount++;
        if (messageCount % 10000 == 0) {
            System.out.println("Received " + messageCount + " messages");
        }
        if (messageCount == numberOfMessages) {
            long endTime = System.nanoTime();
            long elapsedTime = endTime - startTime;

            // Calculate the throughput
            double throughput = ((double) messageCount / elapsedTime) * 1000000000;
            System.out.println("Received " + messageCount + " messages in " + elapsedTime/1000000000.0 + " sec");
            System.out.println("Throughput: " + messageCount/(elapsedTime/1000000000.0) + " request/second");
            System.out.println("Throughput: " + throughput/1024 + " MB/second");

            // Stop the consumer
            try {
                message.getJMSMessageID(); // simulate some processing
            } catch (JMSException e) {
                e.printStackTrace();
            }
            System.exit(0);
        }
    }
}
