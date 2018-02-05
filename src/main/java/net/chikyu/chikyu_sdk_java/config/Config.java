package net.chikyu.chikyu_sdk_java.config;

public class Config {
    private static final String AWS_REGION_NAME = "ap-northeast-1";
    private static final String AWS_SERVICE_NAME = "execute-api";
    private static final String AWS_IAM_ROLE_ID = "arn:aws:iam::171608821407:role/Cognito_Chikyu_Normal_Id_PoolAuth_Role";
    private static final String HOST = "cxpybqnsd0.execute-api.ap-northeast-1.amazonaws.com";
    private static final String ENV_NAME = "devdc";
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
        return HOST;
    }

    public static String getEnvName() {
        return ENV_NAME;
    }

    public static String getPathPrefix() {
        return PATH_PREFIX;
    }
}
