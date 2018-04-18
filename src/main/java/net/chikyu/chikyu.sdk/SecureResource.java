package net.chikyu.chikyu.sdk;

import net.chikyu.chikyu.sdk.auth.Session;
import net.chikyu.chikyu.sdk.config.ApiConfig;
import net.chikyu.chikyu.sdk.exception.ApiCallException;
import net.chikyu.chikyu.sdk.helper.ApiRequestSigner;
import net.chikyu.chikyu.sdk.helper.RequestHelper;
import net.chikyu.chikyu.sdk.model.ApiRequest;
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
        req.withSessionId(this.session.data().getSessionId());
        if (ApiConfig.getMode().equals("local") || ApiConfig.getMode().equals("docker")) {
            req.withIdentityId(this.session.data().getIdentityId());
        }

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
