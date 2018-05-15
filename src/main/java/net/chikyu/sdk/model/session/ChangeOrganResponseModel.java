package net.chikyu.sdk.model.session;

import com.fasterxml.jackson.annotation.JsonProperty;

public class ChangeOrganResponseModel {
    @JsonProperty(value = "api_key")
    public String apiKey;

    @JsonProperty(value = "user")
    public UserModel user;
}
