package net.chikyu.chikyu_sdk_java.auth;

import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.AWSCredentialsProvider;
import com.amazonaws.auth.AnonymousAWSCredentials;
import com.amazonaws.auth.BasicSessionCredentials;
import com.amazonaws.services.securitytoken.AWSSecurityTokenService;
import com.amazonaws.services.securitytoken.AWSSecurityTokenServiceClientBuilder;
import com.amazonaws.services.securitytoken.model.AssumeRoleWithWebIdentityRequest;
import com.amazonaws.services.securitytoken.model.AssumeRoleWithWebIdentityResult;
import net.chikyu.chikyu_sdk_java.OpenResource;
import net.chikyu.chikyu_sdk_java.config.Config;
import net.chikyu.chikyu_sdk_java.exception.ApiCallException;
import net.chikyu.chikyu_sdk_java.model.ApiDataResponse;
import net.chikyu.chikyu_sdk_java.model.ApiRequest;
import net.chikyu.chikyu_sdk_java.model.session.LoginResponseModel;
import net.chikyu.chikyu_sdk_java.model.session.token.SendTokenRequestModel;

import java.io.IOException;

public class Session {

    public SessionData login(SendTokenRequestModel model) throws IOException, ApiCallException {
        OpenResource resource = new OpenResource();
        ApiRequest<SendTokenRequestModel> req = new ApiRequest<SendTokenRequestModel>().withData(model);

        LoginResponse res = resource.invoke("/session/login", req, LoginResponse.class);
        BasicSessionCredentials credentials = this.createCredentials(res.data);

        SessionData data = new SessionData(credentials, res.data.sessionId, res.data.apiKey);

        return data;
    }

    private BasicSessionCredentials createCredentials(LoginResponseModel model) {
        class DummyProvider implements AWSCredentialsProvider {
            @Override
            public AWSCredentials getCredentials() {
                return new AnonymousAWSCredentials();
            }

            @Override
            public void refresh() {

            }
        }

        AWSSecurityTokenService stsService = AWSSecurityTokenServiceClientBuilder.standard()
                .withCredentials(new DummyProvider())
                .withRegion(Config.getAwsRegionName())
                .build();

        AssumeRoleWithWebIdentityRequest req = new AssumeRoleWithWebIdentityRequest()
                .withRoleArn(Config.getAwsIamRoleId())
                .withWebIdentityToken(model.cognitoToken)
                .withRoleSessionName(Config.getAwsServiceName());

        AssumeRoleWithWebIdentityResult stsResult = stsService.assumeRoleWithWebIdentity(req);

        return new BasicSessionCredentials(stsResult.getCredentials().getAccessKeyId(),
                stsResult.getCredentials().getSecretAccessKey(), stsResult.getCredentials().getSessionToken());
    }

    public class SessionData {
        private BasicSessionCredentials credentials;
        private String sessionId;
        private String apiKey;

        private SessionData(BasicSessionCredentials credentials, String sessionId, String apiKey) {
            this.credentials = credentials;
            this.sessionId = sessionId;
            this.apiKey = apiKey;
        }

        public BasicSessionCredentials getCredentials() {
            return credentials;
        }

        public String getSessionId() {
            return sessionId;
        }

        public String getApiKey() {
            return apiKey;
        }
    }
}


class LoginResponse extends ApiDataResponse<LoginResponseModel> {

}