package net.chikyu.chikyu_sdk_java.helper;

import net.chikyu.chikyu_sdk_java.config.Config;

import java.net.URI;
import java.net.URISyntaxException;

public class RequestHelper {

    public static String getBaseUrl() {
        return "https://" + Config.getHost() + "/" + Config.getEnvName() + "/" + Config.getPathPrefix();
    }

    public static String buildApiPathWithEnvName(String apiClass, String path) {
        return "/" + Config.getEnvName() + "/" + Config.getPathPrefix() + buildApiPath(apiClass, path);
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
