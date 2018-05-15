package net.chikyu.sdk.model;

import com.fasterxml.jackson.annotation.JsonProperty;

public class ApiResponse extends ApiModel {
    @JsonProperty(value="has_error")
    public Boolean hasError;

    @JsonProperty(value="message")
    public String message;
}
