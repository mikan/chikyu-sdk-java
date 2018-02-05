package net.chikyu.chikyu_sdk_java;

import net.chikyu.chikyu_sdk_java.auth.Session;
import net.chikyu.chikyu_sdk_java.auth.Token;
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

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.assertThat;

public class ApiCallTest {

    private Properties properties;

    @Before
    public void prepare() throws IOException {
        String path = new File(".").getAbsoluteFile().getParent() + "/src/test/resources/config.properties";
        properties = new Properties();
        properties.load(new FileReader(path));
    }

    @Test
    public void アクセストークンの作成と削除() throws Exception{
        TokenRequestModel req = new TokenRequestModel();
        req.email = properties.getProperty("login.create.email");
        req.password = properties.getProperty("login.create.password");
        req.tokenName = properties.getProperty("login.create.token_name");

        TokenResponseModel m = Token.create(req);
        assertThat(m.loginToken, is(notNullValue()));
        assertThat(m.loginSecretToken, is(notNullValue()));

        SendTokenRequestModel r = new SendTokenRequestModel();
        r.tokenName = req.tokenName;
        r.loginToken = m.loginToken;
        r.loginSecretToken = m.loginSecretToken;
        TokenResponseModel m2 = Token.renew(r);

        assertThat(m2.loginToken, is(notNullValue()));
        assertThat(m2.loginSecretToken, is(notNullValue()));
        assertThat(m2.loginToken, is(not(equalTo(m.loginToken))));
        assertThat(m2.loginSecretToken, is(not(equalTo(m.loginSecretToken))));

        r = new SendTokenRequestModel();
        r.tokenName = req.tokenName;
        r.loginToken = m.loginToken;
        r.loginSecretToken = m.loginSecretToken;
        assertThat(Token.revoke(r), is(equalTo(true)));
    }

    @Test
    public void ログインとログアウト() throws Exception{
        String tokenName = properties.getProperty("login.execute.token_name");
        String loginToken = properties.getProperty("login.execute.login_token");
        String loginSecretToken = properties.getProperty("login.execute.login_secret_token");

        Session session = Session.login(new SendTokenRequestModel()
                .withTokenName(tokenName)
                .withLoginToken(loginToken)
                .withLoginSecretToken(loginSecretToken));

        SecureResource resource = new SecureResource(session);

        GenericApiRequest req = new GenericApiRequest();
        Map<String, Object> data = new HashMap<>();
        data.put("items_per_page", 10);
        data.put("page_index", 1);
        req.data = data;

        GenericApiResponse res = resource.invoke("entity/prospects/list", req, GenericApiResponse.class);

        List<Map<String, Object>> items = (List<Map<String, Object>>)res.data.get("list");

        assertThat(items.size(), is(equalTo(10)));

        session.logout();
    }

    @Test
    public void APIキーを経由してデータ作成() throws Exception{
        String apiKey = properties.getProperty("api_key.api_key");
        String authKey = properties.getProperty("api_key.auth_key");

        PublicResource resource = new PublicResource(apiKey, authKey);
        Map<String, Object> item = new HashMap<>();

        Map<String, Object> fields = new HashMap<>();
        fields.put("first_name", "test");
        fields.put("last_name", "hogehoge");
        fields.put("company_name", "company01");
        item.put("fields", fields);

        GenericApiResponse resp =
                resource.invoke("/entity/prospects/create", new GenericApiRequest().withData(item), GenericApiResponse.class);

        assertThat(resp.data.get("_id"), is(not(nullValue())));
        assertThat(resp.data.get("transaction_id"), is(not(nullValue())));
        assertThat(resp.data.get("action"), is(equalTo("create")));
    }

}
