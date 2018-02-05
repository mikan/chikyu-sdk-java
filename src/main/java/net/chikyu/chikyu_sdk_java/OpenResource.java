package net.chikyu.chikyu_sdk_java;


import net.chikyu.chikyu_sdk_java.exception.ApiCallException;
import net.chikyu.chikyu_sdk_java.helper.RequestHelper;
import net.chikyu.chikyu_sdk_java.model.ApiModel;
import net.chikyu.chikyu_sdk_java.model.ApiRequest;
import net.chikyu.chikyu_sdk_java.model.ApiResponse;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;

import java.io.IOException;

public class OpenResource {
    private HttpClient client;

    public OpenResource() {
        this.client = HttpClientBuilder.create().build();
    }

    public<T> T invoke(String path, ApiRequest req, Class<T> cls) throws IOException, ApiCallException {
        String payload = req.toJson();

        HttpPost post = new HttpPost();
        post.setHeader("content-type", "application/json");
        post.setEntity(new StringEntity(payload, "utf8"));
        post.setURI(RequestHelper.buildUri("open", path));

        HttpResponse res = client.execute(post);
        String respJson = EntityUtils.toString(res.getEntity());

        T model = ApiModel.fromJson(respJson, cls);

        if (model instanceof ApiResponse && ((ApiResponse)model).hasError) {
            throw new ApiCallException(((ApiResponse)model).message);
        }

        return model;
    }

}
