package net.chikyu.chikyu.sdk.config;

public class Config {
    private static final String AWS_REGION_NAME = "ap-northeast-1";
    private static final String AWS_SERVICE_NAME = "execute-api";
    private static final String AWS_IAM_ROLE_ID = "arn:aws:iam::171608821407:role/Cognito_Chikyu_Normal_Id_PoolAuth_Role";
    private static final String PATH_PREFIX = "api/v2";

    public static String getAwsRegionName() {
        return AWS_REGION_NAME;
    }

    public static String getAwsServiceName() {
        return AWS_SERVICE_NAME;
    }

    public static String getAwsIamRoleId() {
        return AWS_IAM_ROLE_ID;
    }

    public static String getHost() {
        if (getMode().equals("local")) {
            return "localhost:9090";
        } else if (getMode().equals("dev")) {
            return "gateway.chikyu.mobi";
        }
        return null;
    }

    public static String getEnvName() {
        if (getMode().equals("local")) {
            return "local";
        } else if (getMode().equals("dev")) {
            return "dev";
        }
        return null;
    }

    public static String getProtocol() {
        if (getMode().equals("local")) {
            return "http";
        } else if (getMode().equals("dev")) {
            return "https";
        }
        return null;
    }

    public static String getPathPrefix() {
        return PATH_PREFIX;
    }

    public static String getMode() {
        return "dev";
    }
}
