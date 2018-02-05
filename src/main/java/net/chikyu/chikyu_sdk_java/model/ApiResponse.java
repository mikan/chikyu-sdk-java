package net.chikyu.chikyu_sdk_java.model;

import com.fasterxml.jackson.annotation.JsonProperty;

public class ApiResponse extends ApiModel {
    @JsonProperty(value="has_error")
    public Boolean hasError;

    @JsonProperty(value="message")
    public String message;
}
