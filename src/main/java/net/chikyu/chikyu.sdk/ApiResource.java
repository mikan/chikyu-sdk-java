package net.chikyu.chikyu.sdk;

import net.chikyu.chikyu.sdk.exception.ApiCallException;
import net.chikyu.chikyu.sdk.model.ApiModel;
import net.chikyu.chikyu.sdk.model.ApiRequest;
import net.chikyu.chikyu.sdk.model.ApiResponse;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

public abstract class ApiResource {
    public abstract  <T>T invoke(String path, ApiRequest req, Class<T> cls) throws ApiCallException, IOException;
    private static Logger logger = null;
    protected HttpClient client;

    public ApiResource() {
        this.client = HttpClientBuilder.create().build();
    }

    protected<T> T handleResponse(HttpResponse res, Class<T> cls) throws IOException, ApiCallException {
        String respJson = EntityUtils.toString(res.getEntity());

        logger().log(Level.FINE, respJson);

        T model = ApiModel.fromJson(respJson, cls);
        if (model instanceof ApiResponse && ((ApiResponse)model).hasError) {
            throw new ApiCallException(((ApiResponse)model).message);
        }
        return model;
    }

    protected Logger logger() {
        if (logger == null) {
            logger = Logger.getLogger(this.getClass().getName());
        }
        return logger;
    }
}
