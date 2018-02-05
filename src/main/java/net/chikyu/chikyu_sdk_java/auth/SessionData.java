package net.chikyu.chikyu_sdk_java.auth;

import com.amazonaws.auth.BasicSessionCredentials;

public class SessionData {
    private BasicSessionCredentials credentials;
    private String sessionId;
    private String apiKey;

    protected SessionData(BasicSessionCredentials credentials, String sessionId, String apiKey) {
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
