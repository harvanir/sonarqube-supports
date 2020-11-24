package org.harvanir.sonarqube.share.util;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * @author Harvan Irsyadi
 */
public class JsonUtils {

    private JsonUtils() {
    }

    public static <T> T readValue(String source, Class<T> targetClass, ObjectMapper objectMapper) {
        try {
            return objectMapper.readValue(source, targetClass);
        } catch (JsonProcessingException e) {
            throw new MappingException(e);
        }
    }

    private static class MappingException extends RuntimeException {

        private static final long serialVersionUID = -8195943412838136928L;

        public MappingException(JsonProcessingException e) {
            super(e);
        }
    }
}