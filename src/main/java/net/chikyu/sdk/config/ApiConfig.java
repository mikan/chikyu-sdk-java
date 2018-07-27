package net.chikyu.sdk.config;

import java.util.HashMap;
import java.util.Map;

public class ApiConfig {
    private static final String AWS_REGION_NAME = "ap-northeast-1";
    private static final String AWS_SERVICE_NAME = "execute-api";
    private static final String AWS_IAM_ROLE_ID = "arn:aws:iam::171608821407:role/Cognito_Chikyu_Normal_Id_PoolAuth_Role";
    private static final String AWS_IAM_PROD_ROLE_ID = "arn:aws:iam::171608821407:role/Cognito_chikyu_PROD_idpoolAuth_Role";
    private static final String AWS_IAM_DEV_ROLE_ID = "arn:aws:iam::527083274078:role/Cognito_ChikyuDevLocalAuth_Role";
    private static final String PATH_PREFIX = "api/v2";

    private static Map<String, String> ENV_NAMES;
    private static Map<String, String> PROTOCOLS;
    private static Map<String, String> HOSTS;

    private static String mode = "prod";

    public static String getAwsRegionName() {
        return AWS_REGION_NAME;
    }

    public static String getAwsServiceName() {
        return AWS_SERVICE_NAME;
    }

    public static String getAwsIamRoleId() {
        if (mode.equals("prod")) {
            return AWS_IAM_PROD_ROLE_ID;
        } else if (mode.equals("local") || mode.equals("docker")) {
            return AWS_IAM_DEV_ROLE_ID;
        } else {
            return AWS_IAM_ROLE_ID;
        }
    }

    public static String getHost() {
        return HOSTS.get(getMode());
    }

    public static String getEnvName() {
        return ENV_NAMES.get(getMode());
    }

    public static String getProtocol() {
        return PROTOCOLS.get(getMode());
    }

    public static String getPathPrefix() {
        return PATH_PREFIX;
    }

    public static String getMode() {
        return mode;
    }

    public static void setMode(String _mode) {
        mode = _mode;
    }

    static {
        ENV_NAMES = new HashMap<>();
        ENV_NAMES.put("local",    "");
        ENV_NAMES.put("docker",   "");
        ENV_NAMES.put("devdc",    "dev");
        ENV_NAMES.put("dev01",    "dev01");
        ENV_NAMES.put("dev02",    "dev02");
        ENV_NAMES.put("hotfix01", "hotfix01");
        ENV_NAMES.put("prod",     "");

        PROTOCOLS = new HashMap<>();
        PROTOCOLS.put("local",    "http");
        PROTOCOLS.put("docker",   "http");
        PROTOCOLS.put("devdc",    "https");
        PROTOCOLS.put("dev01",    "https");
        PROTOCOLS.put("dev02",    "https");
        PROTOCOLS.put("hotfix01", "https");
        PROTOCOLS.put("prod",     "https");

        HOSTS = new HashMap<>();
        HOSTS.put("local",    "localhost:9090");
        HOSTS.put("docker",   "dev-python:9090");
        HOSTS.put("devdc",    "gateway.chikyu.mobi");
        HOSTS.put("dev01",    "gateway.chikyu.mobi");
        HOSTS.put("dev02",    "gateway.chikyu.mobi");
        HOSTS.put("hotfix01", "gateway.chikyu.mobi");
        HOSTS.put("prod",     "endpoint.chikyu.net");
    }
}
