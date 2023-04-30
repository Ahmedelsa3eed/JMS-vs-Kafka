package Median_latency;

import Median_latency.kafka.serializer.MessageDeserializer;
import Median_latency.proto.Message.MyMessage;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.Duration;
import java.util.*;

public class KafkaConsumerLatency {
    private static final Logger LOGGER = LoggerFactory.getLogger(KafkaConsumerLatency.class);
    private static final long numOfMessages = 10000000;
    public static void main(String[] args) {
        // Set up the consumer properties
        Properties properties = new Properties();
        properties.setProperty(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092");
        properties.setProperty(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName());
        properties.setProperty(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, MessageDeserializer.class.getName());
        properties.setProperty(ConsumerConfig.GROUP_ID_CONFIG, "test");
        properties.setProperty(ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG, "true");
        properties.setProperty(ConsumerConfig.AUTO_COMMIT_INTERVAL_MS_CONFIG, "1000");
        properties.setProperty(ConsumerConfig.SESSION_TIMEOUT_MS_CONFIG, "30000");

        // Create the Kafka consumer
        KafkaConsumer<String, MyMessage> consumer = new KafkaConsumer<>(properties);
        consumer.subscribe(Collections.singletonList("latency-topic"));

        List<Long> latency = new ArrayList<>((int) numOfMessages);
        // polling
        long i = 0;
        while(i < numOfMessages) {
            ConsumerRecords<String, MyMessage> records = consumer.poll(Duration.ofNanos(10));

            for (ConsumerRecord<String, MyMessage> record : records)
                latency.add(System.nanoTime() - record.value().getTimestamp());

            i += records.count();
            if(i % 10000 == 0 && i != 0)
                LOGGER.info("Received 10000 messages");
//            LOGGER.info("record " + i + "pulled");
        }
        Collections.sort(latency);
        int n = latency.size();
        long medianLatency = latency.get(n/2);
        if(n % 2 == 0)
            medianLatency = (medianLatency + latency.get(n/2 - 1))/2;

        System.out.println("Median latency = " + medianLatency/1000000.0 + " ms");
    }
}
