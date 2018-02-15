package net.chikyu.chikyu.sdk.auth;

import com.amazonaws.auth.BasicSessionCredentials;

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
}
