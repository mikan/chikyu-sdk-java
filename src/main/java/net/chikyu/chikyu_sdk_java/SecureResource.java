package net.chikyu.chikyu_sdk_java;

import net.chikyu.chikyu_sdk_java.auth.Session;
import net.chikyu.chikyu_sdk_java.exception.ApiCallException;
import net.chikyu.chikyu_sdk_java.helper.ApiRequestSigner;
import net.chikyu.chikyu_sdk_java.helper.RequestHelper;
import net.chikyu.chikyu_sdk_java.model.ApiModel;
import net.chikyu.chikyu_sdk_java.model.ApiRequest;
import net.chikyu.chikyu_sdk_java.model.ApiResponse;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;

import java.io.IOException;
import java.net.URI;
import java.util.logging.Level;
import java.util.logging.Logger;

public class SecureResource {
    private Session.SessionData sessionData;
    private HttpClient client;

    public SecureResource(Session.SessionData sessionData) {
        this.sessionData = sessionData;
        this.client = HttpClientBuilder.create().build();
    }

    public <T>T invoke(String path, ApiRequest req, Class<T> cls) throws ApiCallException, IOException {
        req.sessionId = this.sessionData.getSessionId();
        String requestJson = req.toJson();
        URI uri = RequestHelper.buildUri("secure", path);
        String apiPath = uri.toString().substring("https://".length());
        apiPath = apiPath.substring(apiPath.indexOf("/"));

        ApiRequestSigner signer =
                new ApiRequestSigner(this.sessionData.getCredentials(), this.sessionData.getApiKey());
        HttpPost post = signer.sign(apiPath, requestJson, null);

        HttpResponse res = this.client.execute(post);
        HttpEntity entity = res.getEntity();
        String responseJson = EntityUtils.toString(entity);

        this.logger().log(Level.FINE, responseJson);

        T model = ApiModel.fromJson(responseJson, cls);

        if (model instanceof ApiResponse && ((ApiResponse)model).hasError) {
            throw new ApiCallException(((ApiResponse)model).message);
        }
        req.sessionId = null;
        return model;
    }

    protected Logger logger() {
        return Logger.getLogger(this.getClass().getName());
    }
}
