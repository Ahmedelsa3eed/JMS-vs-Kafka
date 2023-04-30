package Median_latency.kafka.serializer;

import Median_latency.proto.Message;
import org.apache.kafka.common.serialization.Serializer;

public class MessageSerializer extends Adapter implements Serializer<Message.MyMessage> {
    @Override
    public byte[] serialize(String s, Message.MyMessage myMessage) {
        return myMessage.toByteArray();
    }
}