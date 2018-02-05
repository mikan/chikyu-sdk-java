package net.chikyu.chikyu_sdk_java.model;

import com.fasterxml.jackson.annotation.JsonProperty;

public class ApiDataResponse<T> extends ApiResponse {
    @JsonProperty(value="data")
    public T data;
}
