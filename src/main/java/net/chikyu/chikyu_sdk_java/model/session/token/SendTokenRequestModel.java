package net.chikyu.chikyu_sdk_java.model.session.token;

import com.fasterxml.jackson.annotation.JsonProperty;
import net.chikyu.chikyu_sdk_java.model.ApiDataModel;

public class SendTokenRequestModel extends ApiDataModel {
    @JsonProperty(value="token_name")
    public String tokenName;

    @JsonProperty(value="login_token")
    public String loginToken;

    @JsonProperty(value="login_secret_token")
    public String loginSecretToken;
}
