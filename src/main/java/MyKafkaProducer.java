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
    private static final long numOfMessages = 100000;
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
        long start, responseTimeInNano = 0;
        for (long i = 0; i < numOfMessages; i++) {
            start = System.nanoTime();
            producer.send(record);
            responseTimeInNano += System.nanoTime() - start;
            if(i % 10000 == 0)
                LOGGER.info("Sent 10000 messages");
//            LOGGER.info("record " + i + "sent");
        }
        LOGGER.info("Producer response Time = " + ((double)responseTimeInNano)/numOfMessages/1e6 + " ms");

        // Flush and close the producer
        producer.flush();
        producer.close();
    }
}
