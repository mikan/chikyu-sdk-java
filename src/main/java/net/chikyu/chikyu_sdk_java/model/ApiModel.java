package net.chikyu.chikyu_sdk_java.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.beans.Transient;
import java.io.IOException;

@JsonIgnoreProperties(ignoreUnknown=true)
public class ApiModel {

    @Transient
    public String toJson() {
        ObjectMapper mapper = new ObjectMapper();
        try {
            return mapper.writeValueAsString(this);
        } catch (JsonProcessingException e) {
            throw new IllegalArgumentException(e);
        }
    }

    @Transient
    public static<T> T fromJson(String json, Class<T> cls) {
        ObjectMapper mapper = new ObjectMapper();
        try {
            return mapper.readValue(json, cls);
        } catch (IOException e) {
            throw new IllegalStateException(e);
        }
    }
}
