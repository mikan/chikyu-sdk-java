package net.chikyu.sdk.helper;

import net.chikyu.sdk.config.ApiConfig;

import java.net.URI;
import java.net.URISyntaxException;

public class RequestHelper {

    public static String getBaseUrl() {
        return ApiConfig.getProtocol() + "://" + ApiConfig.getHost() + "/" + ApiConfig.getEnvName() + "/" + ApiConfig.getPathPrefix();
    }

    public static String buildApiPathWithEnvName(String apiClass, String path) {
        String envName = ApiConfig.getEnvName();
        if (envName.equals("")) {
            return "/" + ApiConfig.getPathPrefix() + buildApiPath(apiClass, path);
        } else {
            return "/" + ApiConfig.getEnvName() + "/" + ApiConfig.getPathPrefix() + buildApiPath(apiClass, path);
        }
    }

    public static String buildApiPath(String apiClass, String path) {
        StringBuilder res = new StringBuilder();
        if (apiClass == null || apiClass.equals("/")) {
            res.append("/");
        } else {
            if (!apiClass.startsWith("/")) {
                res.append("/");
            }
            res.append(apiClass);
        }

        if (!path.startsWith("/")) {
            res.append("/");
        }
        res.append(path);
        return res.toString();
    }

    public static URI buildUri(String apiClass, String path) {
        String p = buildApiPath(apiClass, path);
        try {
            return new URI(getBaseUrl() + p);
        } catch (URISyntaxException e) {
            throw new IllegalArgumentException(e);
        }
    }
}
