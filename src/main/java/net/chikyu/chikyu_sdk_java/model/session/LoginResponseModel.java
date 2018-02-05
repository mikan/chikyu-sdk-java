package net.chikyu.chikyu_sdk_java.model.session;

import com.fasterxml.jackson.annotation.JsonProperty;
import net.chikyu.chikyu_sdk_java.model.ApiDataModel;

public class LoginResponseModel extends ApiDataModel {
    @JsonProperty(value = "session_id")
    public String sessionId;

    @JsonProperty(value = "cognito_token")
    public String cognitoToken;

    @JsonProperty(value = "api_key")
    public String apiKey;

    @JsonProperty(value = "user")
    public UserModel user;
}
