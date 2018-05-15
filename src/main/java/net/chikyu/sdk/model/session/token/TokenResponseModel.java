package net.chikyu.sdk.model.session.token;

import com.fasterxml.jackson.annotation.JsonProperty;
import net.chikyu.sdk.model.ApiDataModel;


public class TokenResponseModel extends ApiDataModel {
    @JsonProperty(value="login_token")
    public String loginToken;

    @JsonProperty(value="login_secret_token")
    public String loginSecretToken;
}
