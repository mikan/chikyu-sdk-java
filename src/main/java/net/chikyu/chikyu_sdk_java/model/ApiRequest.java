package net.chikyu.chikyu_sdk_java.model;

import com.fasterxml.jackson.annotation.JsonProperty;

public class ApiRequest<T> extends ApiModel {
    @JsonProperty(value="session_id")
    public String sessionId;

    @JsonProperty(value="data")
    public T data;

    public ApiRequest<T> withSessionId(String sessionId) {
        this.sessionId = sessionId;
        return this;
    }

    public ApiRequest<T> withData(T data) {
        this.data = data;
        return this;
    }
}
