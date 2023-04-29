package Median_latency;

import Median_latency.kafka.serializer.MessageSerializer;
import Median_latency.proto.Message.MyMessage;
import com.google.protobuf.ByteString;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.serialization.StringSerializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Properties;

public class KafkaProducerLatency {
    private static final Logger LOGGER = LoggerFactory.getLogger(KafkaProducerLatency.class);
    private static final long numOfMessages = 10000000;
    private static final byte[] array = new byte[1000];
    private static final ByteString byteString = ByteString.copyFrom(array);
    private static final String topic = "latency-topic";
    private static final String key = "my-key";

    public static void main(String[] args) {

        // Set up the producer properties
        Properties properties = new Properties();
        properties.setProperty(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092");
        properties.setProperty(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());
        properties.setProperty(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, MessageSerializer.class.getName());

        // Create the Kafka producer
        org.apache.kafka.clients.producer.KafkaProducer<String, MyMessage> producer = new org.apache.kafka.clients.producer.KafkaProducer<>(properties);

        for (long i = 0; i < numOfMessages; i++) {
            producer.send(createRecord());
            if(i % 10000 == 0)
                LOGGER.info("Sent 10000 messages");
//            LOGGER.info("record " + i + "sent");
        }
        LOGGER.info("Done Sending");

        // Flush and close the producer
        producer.flush();
        producer.close();
    }

    public static ProducerRecord<String, MyMessage> createRecord(){
        return new ProducerRecord<>(topic, key,
                MyMessage
                        .newBuilder()
                        .setTimestamp(System.nanoTime())
                        .setValue(byteString)
                        .build());
    }

}
