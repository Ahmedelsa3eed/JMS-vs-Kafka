package Median_latency;

import org.apache.activemq.ActiveMQConnection;
import org.apache.activemq.ActiveMQConnectionFactory;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
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
        List<Long> latency = new ArrayList<>();
        for (int i = 0; i < 100000; i++) {
            Message message = consumer.receive();
            // System.out.println(i);
            long timestamp = message.getLongProperty("timestamp");
            latency.add((System.nanoTime() - timestamp));
        }
        Collections.sort(latency);
        int n = latency.size();
        if(n % 2 == 0){
            double mid1 = latency.get(n/ 2);   
            double mid2 = latency.get(n / 2 - 1);
            double median_lat = (mid1 + mid2) / 2.0;
            System.out.println("Median latency = " + median_lat + "(ns)");
        }else{
            System.out.println("Median latency = " + latency.get(n/ 2) + "(ns)");
        }
        // Close the JMS objects
        consumer.close();
        session.close();
        connection.close();
    }
}
