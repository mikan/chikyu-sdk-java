package net.chikyu.sdk.model;

import com.fasterxml.jackson.annotation.JsonProperty;

public class ApiDataResponse<T> extends ApiResponse {
    @JsonProperty(value="data")
    public T data;
}
