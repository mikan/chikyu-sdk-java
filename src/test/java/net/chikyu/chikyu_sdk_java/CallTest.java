package net.chikyu.chikyu_sdk_java;

import net.chikyu.chikyu_sdk_java.auth.Session;
import net.chikyu.chikyu_sdk_java.auth.Token;
import net.chikyu.chikyu_sdk_java.model.ApiModel;
import net.chikyu.chikyu_sdk_java.model.generic.GenericApiRequest;
import net.chikyu.chikyu_sdk_java.model.generic.GenericApiResponse;
import net.chikyu.chikyu_sdk_java.model.session.token.SendTokenRequestModel;
import net.chikyu.chikyu_sdk_java.model.session.token.TokenRequestModel;
import net.chikyu.chikyu_sdk_java.model.session.token.TokenResponseModel;
import org.junit.Before;
import org.junit.Test;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

public class CallTest {
    private Properties properties;

    @Before
    public void prepare() throws IOException {
        String path = new File(".").getAbsoluteFile().getParent() + "/src/test/resources/config.properties";
        properties = new Properties();
        properties.load(new FileReader(path));
    }


    @Test
    public void testCall02() {
        GenericApiRequest req = new GenericApiRequest();
        req.sessionId = "hogehoge";
        req.data = new HashMap<>();
        req.data.put("hoge", "hogheoge");

        Map<String, Object> p = new HashMap<>();
        req.data.put("piyo", p);

        String s = req.toJson();

        GenericApiRequest r2 = ApiModel.fromJson(s, GenericApiRequest.class);
        System.out.println(r2.toJson());

    }

    @Test
    public void testCall03() throws Exception {
        Token token = new Token();

        TokenRequestModel req = new TokenRequestModel();
        req.email = properties.getProperty("login.create.email");
        req.password = properties.getProperty("login.create.password");
        req.tokenName = properties.getProperty("login.create.token_name");

        TokenResponseModel m = token.create(req);

        SendTokenRequestModel r = new SendTokenRequestModel();
        r.tokenName = req.tokenName;
        r.loginToken = m.loginToken;
        r.loginSecretToken = m.loginSecretToken;
        m = token.renew(r);

        r = new SendTokenRequestModel();
        r.tokenName = req.tokenName;
        r.loginToken = m.loginToken;
        r.loginSecretToken = m.loginSecretToken;
        System.out.println(token.revoke(r));
    }

    @Test
    public void testCall04() throws Exception {
        SendTokenRequestModel r = new SendTokenRequestModel();
        r.tokenName = properties.getProperty("login.execute.token_name");
        r.loginToken = properties.getProperty("login.execute.login_token");
        r.loginSecretToken = properties.getProperty("login.execute.login_secret_token");

        Session.SessionData session = new Session().login(r);
        System.out.println(session);

        SecureResource resource = new SecureResource(session);

        GenericApiRequest req = new GenericApiRequest();
        Map<String, Object> data = new HashMap<>();
        data.put("items_per_page", 10);
        data.put("page_index", 1);
        req.data = data;

        GenericApiResponse res = resource.invoke("entity/prospects/list", req, GenericApiResponse.class);

        System.out.println(((List<Map<String, Object>>)res.data.get("list")).size());
    }
}
