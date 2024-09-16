package com.wowo.wowo.util;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

public class ObjectUtil {

    static ObjectMapper objectMapper = new ObjectMapper();

    static {
        objectMapper.registerModule(new JavaTimeModule());
    }

    public static ObjectNode mergeObjects(Object... object) {


        ObjectNode result = objectMapper.createObjectNode();
        for (Object o : object) {
            result.setAll((ObjectNode) objectMapper.valueToTree(o));
        }

        return result;
    }

    public static ObjectNode wrapObject(String key, Object value) {
        ObjectNode wrapper = objectMapper.createObjectNode();
        wrapper.set(key, objectMapper.valueToTree(value));
        return wrapper;
    }

    public static String parseJson(Object object) {
        try {
            return objectMapper.writeValueAsString(object);
        } catch (Exception e) {
            return null;
        }
    }

    public static <T> T parseObject(String json, Class<T> clazz) {
        try {
            return objectMapper.readValue(json, clazz);
        } catch (Exception e) {
            return null;
        }
    }
}
