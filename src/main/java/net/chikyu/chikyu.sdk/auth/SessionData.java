package net.chikyu.chikyu.sdk.auth;

import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.auth.BasicSessionCredentials;

import java.util.HashMap;
import java.util.Map;

public class SessionData {
    private BasicSessionCredentials credentials;
    private String sessionId;
    private String apiKey;
    private String identityId;

    protected SessionData(BasicSessionCredentials credentials, String sessionId, String identityId, String apiKey) {
        this.credentials = credentials;
        this.sessionId = sessionId;
        this.identityId = identityId;
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

    public String getIdentityId() {
        return identityId;
    }

    public static SessionData fromMap(Map<String, String> map) {
        BasicSessionCredentials credentials = new BasicSessionCredentials(
            map.get("credential__accessKey"), map.get("credential__secretKey"), map.get("credential__sessionToken")
        );
        return new SessionData(credentials, map.get("sessionId"), map.get("identityId"), map.get("apiKey"));
    }

    public Map<String, String> toMap() {
        Map<String, String> res = new HashMap<>();
        res.put("sessionId", this.sessionId);
        res.put("identityId", this.identityId);
        res.put("apiKey", this.apiKey);
        res.put("credential__accessKey", this.credentials.getAWSAccessKeyId());
        res.put("credential__secretKey", this.credentials.getAWSSecretKey());
        res.put("credential__sessionToken", this.credentials.getSessionToken());
        return res;
    }
}
