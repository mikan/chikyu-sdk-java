package net.chikyu.chikyu_sdk_java.auth;

import net.chikyu.chikyu_sdk_java.OpenResource;
import net.chikyu.chikyu_sdk_java.exception.ApiCallException;
import net.chikyu.chikyu_sdk_java.model.ApiDataResponse;
import net.chikyu.chikyu_sdk_java.model.ApiRequest;
import net.chikyu.chikyu_sdk_java.model.ApiResponse;
import net.chikyu.chikyu_sdk_java.model.session.token.SendTokenRequestModel;
import net.chikyu.chikyu_sdk_java.model.session.token.TokenRequestModel;
import net.chikyu.chikyu_sdk_java.model.session.token.TokenResponseModel;

import java.io.IOException;

public class Token {
    private OpenResource resource;

    public Token() {
        this.resource = new OpenResource();
    }

    public TokenResponseModel create(TokenRequestModel model) throws IOException, ApiCallException {
        ApiRequest<TokenRequestModel> req = new ApiRequest<TokenRequestModel>().withData(model);
        TokenResponse res = this.resource.invoke("session/token/create", req, TokenResponse.class);
        return res.data;
    }

    public TokenResponseModel renew(SendTokenRequestModel model) throws IOException, ApiCallException {
        ApiRequest<SendTokenRequestModel> req = new ApiRequest<SendTokenRequestModel>().withData(model);
        TokenResponse res = this.resource.invoke("session/token/renew", req, TokenResponse.class);
        return res.data;
    }

    public boolean revoke(SendTokenRequestModel model) throws IOException, ApiCallException {
        ApiRequest<SendTokenRequestModel> req = new ApiRequest<SendTokenRequestModel>().withData(model);
        ApiResponse res = this.resource.invoke("session/token/renew", req, ApiResponse.class);
        return !res.hasError;
    }
}


class TokenResponse extends ApiDataResponse<TokenResponseModel> {

}

