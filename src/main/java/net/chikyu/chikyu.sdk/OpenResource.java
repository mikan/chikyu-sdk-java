package net.chikyu.chikyu.sdk;


import net.chikyu.chikyu.sdk.exception.ApiCallException;
import net.chikyu.chikyu.sdk.helper.RequestHelper;
import net.chikyu.chikyu.sdk.model.ApiRequest;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;

import java.io.IOException;

public class OpenResource extends ApiResource {
    public OpenResource() {
        super();
    }

    @Override
    public<T> T invoke(String path, ApiRequest req, Class<T> cls) throws IOException, ApiCallException {
        String payload = req.toJson();

        HttpPost post = new HttpPost();
        post.setHeader("content-type", "application/json");
        post.setEntity(new StringEntity(payload, "utf8"));
        post.setURI(RequestHelper.buildUri("open", path));

        return handleResponse(client.execute(post), cls);
    }
}
