package net.chikyu.chikyu.sdk.model.session.token;

import com.fasterxml.jackson.annotation.JsonProperty;
import net.chikyu.chikyu.sdk.model.ApiDataModel;

public class TokenRequestModel extends ApiDataModel {
    @JsonProperty(value="token_name")
    public String tokenName;

    @JsonProperty(value="email")
    public String email;

    @JsonProperty(value="password")
    public String password;
}

