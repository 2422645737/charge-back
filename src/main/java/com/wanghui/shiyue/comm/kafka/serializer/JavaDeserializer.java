package com.wanghui.shiyue.comm.kafka.serializer;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.kafka.common.serialization.Deserializer;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.Map;

public class JavaDeserializer implements Deserializer<Object> {

    @Override
    public Object deserialize(String topic, byte[] data) {
        if (data == null || data.length == 0) {
            return null;
        }
        try {
            ByteArrayInputStream byteStream = new ByteArrayInputStream(data);
            ObjectInputStream objectStream = new ObjectInputStream(byteStream);
            Object obj = objectStream.readObject();
            objectStream.close();
            return obj;
        } catch (IOException | ClassNotFoundException e) {
            throw new IllegalStateException("Can't deserialize data from topic: " + topic, e);
        }
    }

    @Override
    public void configure(Map<String, ?> configs, boolean isKey) {

    }

    @Override
    public void close() {

    }
}