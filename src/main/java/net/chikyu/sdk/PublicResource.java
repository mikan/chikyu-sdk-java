package net.chikyu.sdk;

import net.chikyu.sdk.exception.ApiCallException;
import net.chikyu.sdk.helper.RequestHelper;
import net.chikyu.sdk.model.ApiRequest;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;

import java.io.IOException;

public class PublicResource extends ApiResource {
    private String apiKey;
    private String authKey;

    public PublicResource(String apiKey, String authKey) {
        this.apiKey = apiKey;
        this.authKey = authKey;
    }

    @Override
    public <T> T invoke(String path, ApiRequest req, Class<T> cls) throws ApiCallException, IOException {
        String payload = req.toJson();

        HttpPost post = new HttpPost();
        post.setHeader("content-type", "application/json");
        post.setHeader("x-api-key", this.apiKey);
        post.setHeader("x-auth-key", this.authKey);

        post.setEntity(new StringEntity(payload, "utf8"));
        post.setURI(RequestHelper.buildUri("public", path));

        return handleResponse(client.execute(post), cls);
    }
}
