package Median_latency.kafka.serializer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.protobuf.InvalidProtocolBufferException;
import org.apache.kafka.common.serialization.Deserializer;
import Median_latency.proto.Message.MyMessage;

public class MessageDeserializer extends Adapter implements Deserializer<MyMessage> {
    private static final Logger LOG = LoggerFactory.getLogger(MessageDeserializer.class);

    @Override
    public MyMessage deserialize(final String topic, byte[] data) {
        try {
            return MyMessage.parseFrom(data);
        } catch (final InvalidProtocolBufferException e) {
            LOG.error("Received unparseable message", e);
            throw new RuntimeException("Received unparseable message " + e.getMessage(), e);
        }
    }

}
