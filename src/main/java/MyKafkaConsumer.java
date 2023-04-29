import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.common.serialization.ByteArrayDeserializer;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.Duration;
import java.util.Arrays;
import java.util.Properties;

public class MyKafkaConsumer {
    private static final Logger LOGGER = LoggerFactory.getLogger(MyKafkaConsumer.class);
    private static final long numOfMessages = 100000;
    public static void main(String[] args) {
        // Set up the consumer properties
        Properties properties = new Properties();
        properties.setProperty(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092");
        properties.setProperty(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName());
        properties.setProperty(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, ByteArrayDeserializer.class.getName());
        properties.setProperty(ConsumerConfig.GROUP_ID_CONFIG, "test");
        properties.setProperty(ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG, "true");
        properties.setProperty(ConsumerConfig.AUTO_COMMIT_INTERVAL_MS_CONFIG, "1000");
        properties.setProperty(ConsumerConfig.SESSION_TIMEOUT_MS_CONFIG, "30000");

        // Create the Kafka consumer
        KafkaConsumer<String, byte[]> consumer = new KafkaConsumer<>(properties);
        consumer.subscribe(Arrays.asList("my-topic"));

        // polling
        long start, responseTimeInNano = 0;
        for (long i = 0; i < numOfMessages-1000; i++) {
            start = System.nanoTime();
            ConsumerRecords<String, byte[]> records = consumer.poll(Duration.ofNanos(100));
//            for (ConsumerRecord<String, byte[]> record : records) {
//                LOGGER.info("key: " + record.key() + ", partition: " + record.partition() + ", offset: " + record.offset());
//            }
            responseTimeInNano += System.nanoTime() - start;
            if(i % 10000 == 0)
                LOGGER.info("Received 10000 messages");
//            LOGGER.info("record " + i + "pulled");
        }
        System.out.println("Consumer response Time = " + ((double)responseTimeInNano)/numOfMessages/1e6 + " ms");
    }
}
