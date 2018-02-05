package net.chikyu.chikyu_sdk_java;

import net.chikyu.chikyu_sdk_java.auth.Session;
import net.chikyu.chikyu_sdk_java.exception.ApiCallException;
import net.chikyu.chikyu_sdk_java.helper.ApiRequestSigner;
import net.chikyu.chikyu_sdk_java.helper.RequestHelper;
import net.chikyu.chikyu_sdk_java.model.ApiRequest;
import org.apache.http.client.methods.HttpPost;

import java.io.IOException;

public class SecureResource extends ApiResource {
    private Session session;

    public SecureResource(Session session) {
        super();
        this.session = session;
    }

    @Override
    public <T>T invoke(String path, ApiRequest req, Class<T> cls) throws ApiCallException, IOException {
        req.sessionId = this.session.data().getSessionId();
        String requestJson = req.toJson();
        String apiPath = RequestHelper.buildApiPathWithEnvName("secure", path);

        ApiRequestSigner signer =
                new ApiRequestSigner(this.session.data().getCredentials(), this.session.data().getApiKey());
        HttpPost post = signer.sign(apiPath, requestJson, null);

        return handleResponse(client.execute(post), cls);
    }

    public Session getSession() {
        return this.session;
    }
}
