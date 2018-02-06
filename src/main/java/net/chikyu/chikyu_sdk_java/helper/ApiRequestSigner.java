package net.chikyu.chikyu_sdk_java.helper;

import com.amazonaws.auth.BasicSessionCredentials;
import net.chikyu.chikyu_sdk_java.config.Config;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.net.URISyntaxException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.TimeZone;
import java.util.logging.Logger;

/*
 * 下記URLのコードを参考に、AWS 署名バージョン4 署名プロセスを実装。
 * http://www.javaquery.com/2016/01/aws-version-4-signing-process-complete.html
 * https://docs.aws.amazon.com/ja_jp/general/latest/gr/sigv4_signing.html
 */
public class ApiRequestSigner {
    private String regionName = "ap-northeast-1";
    private String serviceName = "execute-api";
    private BasicSessionCredentials credentials;
    private String apiKey;

    private static Logger logger = Logger.getLogger(ApiRequestSigner.class.getName());

    public ApiRequestSigner(BasicSessionCredentials credentials, String apiKey) {
        this.credentials = credentials;
        this.apiKey = apiKey;
    }

    private static final String POST = "POST";
    private static final String AWS4_HMAC_SHA256 = "AWS4-HMAC-SHA256";
    private static final String AWS4_REQUEST = "aws4_request";

    public HttpPost sign(String path, String payload, HttpPost post) {
        String currentDate = getDate();
        String timeStamp = getTimeStamp();

        if (path == null || path.equals("")) {
            path = "/";
        } else {
            if (!path.startsWith("/")) {
                path = "/" + path;
            }
        }

        Map<String, String> headers = new LinkedHashMap<>();
        headers.put("content-type", "application/json");
        headers.put("host", Config.getHost());
        headers.put("x-amz-date", timeStamp);
        headers.put("x-amz-security-token", this.credentials.getSessionToken());
        headers.put("x-api-key", this.apiKey);

        String signedHeader = this.getSignedHeader(headers);
        String canonicalUrl = this.getCanonicalUrl(path, payload, signedHeader, headers);
        String signature = this.getSignature(canonicalUrl, timeStamp, currentDate);

        headers.put("Authorization", buildAuthorizationString(signedHeader, signature, currentDate));

        if (post == null) {
            post = new HttpPost();
        }

        for (Map.Entry<String, String> item : headers.entrySet()) {
            post.addHeader(item.getKey(), item.getValue());
        }

        try {
            String s2 = "https://" + Config.getHost() + path;
            post.setURI(new URI(s2));
        } catch (URISyntaxException e) {
            throw new IllegalArgumentException(e);
        }
        post.setEntity(new StringEntity(payload, "utf8"));

        return post;
    }

    private String getSignedHeader(Map<String, String> headers) {
        StringBuilder result = new StringBuilder("");
        int i = 0;

        for (String key : headers.keySet()) {
            if (i++ > 0) {
                result.append(";");
            }
            result.append(key);
        }

        return result.toString();
    }

    private String getCanonicalUrl(String path, String payload, String signedHeaders, Map<String, String> headers) {
        StringBuilder canonicalURL = new StringBuilder("");

        canonicalURL.append(POST).append("\n");
        canonicalURL.append(path).append("\n");
        canonicalURL.append("\n");

        for (Map.Entry<String, String> item : headers.entrySet()) {
            canonicalURL.append(item.getKey()).append(":").append(item.getValue()).append("\n");
        }
        canonicalURL.append("\n");

        canonicalURL.append(signedHeaders).append("\n");

        payload = payload == null ? "" : payload;
        canonicalURL.append(sha256Digest(payload));

        return canonicalURL.toString();
    }

    private String buildAuthorizationString(String signedHeader, String signature, String currentDate) {
        return AWS4_HMAC_SHA256 + " "
                + "Credential=" + this.credentials.getAWSAccessKeyId() + "/" + getServiceDescription(currentDate) + ","
                + "SignedHeaders=" + signedHeader + ","
                + "Signature=" + signature;
    }

    private String getServiceDescription(String currentDate) {
        return currentDate + "/" + this.regionName + "/" + this.serviceName + "/" + AWS4_REQUEST;
    }

    private String getSignature(String canonicalURL, String timeStamp,  String currentDate) {
        try {
            String stringToSign = AWS4_HMAC_SHA256 + "\n" +
                                  timeStamp + "\n" +
                                  getServiceDescription(currentDate) + "\n" +
                                  sha256Digest(canonicalURL);

            byte[] signatureKey = getSignatureKey(
                    this.credentials.getAWSSecretKey(), currentDate, this.regionName, this.serviceName);

            byte[] signature = HmacSHA256(signatureKey, stringToSign);
            return bytesToHex(signature);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return null;
    }

    private String getTimeStamp() {
        DateFormat dateFormat = new SimpleDateFormat("yyyyMMdd'T'HHmmss'Z'");
        dateFormat.setTimeZone(TimeZone.getTimeZone("UTC"));
        return dateFormat.format(new Date());
    }

    private String getDate() {
        DateFormat dateFormat = new SimpleDateFormat("yyyyMMdd");
        dateFormat.setTimeZone(TimeZone.getTimeZone("UTC"));
        return dateFormat.format(new Date());
    }

    private String sha256Digest(String data) {
        MessageDigest messageDigest;
        try {
            messageDigest = MessageDigest.getInstance("SHA-256");
            messageDigest.update(data.getBytes("UTF-8"));
            byte[] digest = messageDigest.digest();
            return bytesToHex(digest); //String.format("%064x", new java.math.BigInteger(1, digest));
        } catch (NoSuchAlgorithmException | UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return null;
    }

    private byte[] HmacSHA256(byte[] key, String data) throws Exception {
        String algorithm = "HmacSHA256";
        Mac mac = Mac.getInstance(algorithm);
        mac.init(new SecretKeySpec(key, algorithm));
        return mac.doFinal(data.getBytes("UTF8"));
    }

    private byte[] getSignatureKey(String key, String date, String regionName, String serviceName) throws Exception {
        byte[] kSecret = ("AWS4" + key).getBytes("UTF8");
        byte[] kDate = HmacSHA256(kSecret, date);
        byte[] kRegion = HmacSHA256(kDate, regionName);
        byte[] kService = HmacSHA256(kRegion, serviceName);
        return HmacSHA256(kService, AWS4_REQUEST);
    }

    private final static char[] hexArray = "0123456789ABCDEF".toCharArray();
    private String bytesToHex(byte[] bytes) {
        char[] hexChars = new char[bytes.length * 2];
        for (int j = 0; j < bytes.length; j++) {
            int v = bytes[j] & 0xFF;
            hexChars[j * 2] = hexArray[v >>> 4];
            hexChars[j * 2 + 1] = hexArray[v & 0x0F];
        }
        return new String(hexChars).toLowerCase();
    }
}
