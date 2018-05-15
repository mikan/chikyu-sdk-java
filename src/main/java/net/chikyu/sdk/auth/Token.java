package net.chikyu.sdk.auth;

import net.chikyu.sdk.OpenResource;
import net.chikyu.sdk.exception.ApiCallException;
import net.chikyu.sdk.model.ApiDataResponse;
import net.chikyu.sdk.model.ApiRequest;
import net.chikyu.sdk.model.ApiResponse;
import net.chikyu.sdk.model.session.token.SendTokenRequestModel;
import net.chikyu.sdk.model.session.token.TokenRequestModel;
import net.chikyu.sdk.model.session.token.TokenResponseModel;

import java.io.IOException;

public class Token {
    public static TokenResponseModel create(TokenRequestModel model) throws IOException, ApiCallException {
        ApiRequest<TokenRequestModel> req = new ApiRequest<TokenRequestModel>().withData(model);
        TokenResponse res = (new OpenResource()).invoke("session/token/create", req, TokenResponse.class);
        return res.data;
    }

    public static TokenResponseModel renew(SendTokenRequestModel model) throws IOException, ApiCallException {
        ApiRequest<SendTokenRequestModel> req = new ApiRequest<SendTokenRequestModel>().withData(model);
        TokenResponse res = (new OpenResource()).invoke("session/token/renew", req, TokenResponse.class);
        return res.data;
    }

    public static boolean revoke(SendTokenRequestModel model) throws IOException, ApiCallException {
        ApiRequest<SendTokenRequestModel> req = new ApiRequest<SendTokenRequestModel>().withData(model);
        ApiResponse res = (new OpenResource()).invoke("session/token/renew", req, ApiResponse.class);
        return !res.hasError;
    }
}


class TokenResponse extends ApiDataResponse<TokenResponseModel> {

}

