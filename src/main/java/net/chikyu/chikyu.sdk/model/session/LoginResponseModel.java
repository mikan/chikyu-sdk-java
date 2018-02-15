package net.chikyu.chikyu.sdk.model.session;

import com.fasterxml.jackson.annotation.JsonProperty;
import net.chikyu.chikyu.sdk.model.ApiDataModel;

public class LoginResponseModel extends ApiDataModel {
    @JsonProperty(value = "session_id")
    public String sessionId;

    @JsonProperty(value = "cognito_token")
    public String cognitoToken;

    @JsonProperty(value = "api_key")
    public String apiKey;

    @JsonProperty(value = "cognito_identity_id")
    public String identityId;

    @JsonProperty(value = "user")
    public UserModel user;
}
