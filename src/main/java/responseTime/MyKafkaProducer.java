package responseTime;

import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.serialization.ByteArraySerializer;
import org.apache.kafka.common.serialization.StringSerializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Properties;

public class MyKafkaProducer {
    private static final Logger LOGGER = LoggerFactory.getLogger(MyKafkaProducer.class);
    public static void main(String[] args) {

        // Set up the producer properties
        Properties properties = new Properties();
        properties.setProperty(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092");
        properties.setProperty(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());
        properties.setProperty(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, ByteArraySerializer.class.getName());

        // Create the Kafka producer
        KafkaProducer<String, byte[]> producer = new KafkaProducer<>(properties);

        // Create a producer record
        String topic = "my-topic";
        String key = "my-key";
        byte[] value = new byte[1024];
        ProducerRecord<String, byte[]> record = new ProducerRecord<>(topic, key, value);

        // Send 20 1KB messages
        long start, responseTimeInMillis = 0;
        for (int i = 0; i < 20; i++) {
            start = System.currentTimeMillis();
            producer.send(record);
            responseTimeInMillis += System.currentTimeMillis() - start;
            LOGGER.info("record " + i + "sent");
        }
        LOGGER.info("Producer response Time = " + responseTimeInMillis/20 + "(ms)");

        // Flush and close the producer
        producer.flush();
        producer.close();
    }
}
