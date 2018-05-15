package net.chikyu.sdk.model;

import com.fasterxml.jackson.annotation.JsonProperty;

public class ApiRequest<T> extends ApiModel {
    /**
     * SDK側で設定。
     */
    @JsonProperty(value="session_id")
    public String sessionId;

    /**
     * SDK側で設定。
     */
    @JsonProperty(value="identity_id")
    public String identityId;

    @JsonProperty(value="data")
    public T data;

    public ApiRequest<T> withSessionId(String sessionId) {
        this.sessionId = sessionId;
        return this;
    }

    public ApiRequest<T> withIdentityId(String identityId) {
        this.identityId = identityId;
        return this;
    }

    public ApiRequest<T> withData(T data) {
        this.data = data;
        return this;
    }
}
