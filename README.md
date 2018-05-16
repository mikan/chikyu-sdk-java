# chikyu-sdk-java
## 概要
**内容は全てリリース前のものであり、予告なく変更となる場合があります**

ちきゅうのWeb APIをJavaから利用するためのライブラリです。

SDKの開発にはJava 1.8を利用しています。

## APIの基本仕様について
こちらのレポジトリをご覧ください

https://github.com/chikyuinc/chikyu-api-specification
 
## インストール
Mavenの公式レポジトリには登録していないため、レポジトリを追加してインストールを行ってください。

例えばGradleの場合、build.gradleは以下のようになります。

```build.gradle
group 'com.examle'
version '0.0.2-SNAPSHOT'

apply plugin: 'java'

sourceCompatibility = 1.8

repositories {
    mavenCentral()
    maven {
        url 'https://s3-ap-northeast-1.amazonaws.com/chikyu-cors/maven'
    }
}

dependencies {
    compile group:'net.chikyu.sdk',  name: 'chikyu-sdk', version: '0.0.1-SNAPSHOT'
}
```

## SDKを利用する
### テスト段階でのサンプルコード
```Sample.java
import net.chikyu.sdk.config.ApiConfig;
import net.chikyu.sdk.auth.Session;
import net.chikyu.sdk.SecureResource;
import net.chikyu.sdk.model.generic.GenericApiRequest;
import net.chikyu.sdk.model.generic.GenericApiResponse;
import net.chikyu.sdk.model.session.token.SendTokenRequestModel;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

class Sample {

    public static void main(String[] args) throws Exception {
        //2018/05/14現在、まだ本番環境が未構築であるため、こちらのテスト用の環境名を指定して下さい。
        ApiConfig.setMode("devdc");

        //セッションを生成する
        Session session = Session.login(new SendTokenRequestModel()
                .withTokenName("tokenName")
                .withLoginToken("loginToken")
                .withLoginSecretToken("loginSecretToken")
        );

        //API呼び出し用のインスタンスを生成する
        SecureResource resource = new SecureResource(session);

        //MapからJSONに変換を行う汎用クラスを使い、リクエストを生成する
        //JSONの定義に書かれている「data」フィールド内の項目を設定する
        //＊Jacksonの機能を利用し、EntityからJSONへの変換を行うことも可能
        Map<String, Object> data = new HashMap<>();
        data.put("items_per_page", 10);
        data.put("page_index", 1);

        //Mapでレスポンスを受け取る汎用クラスを使い、レスポンスを受け取る
        //＊Jacksonの機能を利用し、EntityからJSONへの変換を行うことも可能
        GenericApiResponse res = resource.invoke("entity/prospects/list",
                new GenericApiRequest().withData(data), GenericApiResponse.class);

        List<Map<String, Object>> items = (List<Map<String, Object>>)res.data.get("list");
        for (Map<String, Object> item : items) {
            System.out.println(item.get("__display_name"));
        }
    }

}
```

## 詳細
### class1(APIキーのみで呼び出し可能)
#### APIキーを生成する
```TokenSample.java
import net.chikyu.sdk.SecureResource;
import net.chikyu.sdk.auth.Session;
import net.chikyu.sdk.config.ApiConfig;
import net.chikyu.sdk.model.generic.GenericApiRequest;
import net.chikyu.sdk.model.generic.GenericApiResponse;
import net.chikyu.sdk.model.session.token.SendTokenRequestModel;

import java.util.HashMap;
import java.util.Map;


class TokenSample {

    public static void main(String[] args) throws Exception {
        //2018/05/15現在、まだ本番環境が存在しないため、接続先の指定が必要。
        ApiConfig.setMode("devdc");

        //後述のclass2 apiを利用し、予めログイン用の「認証トークン」(＊ここで言う「APIキー」とは別)を生成しておく。
        Session session = Session.login(new SendTokenRequestModel()
                                                .withTokenName("token_name")
                                                .withLoginToken("login_token")
                                                .withLoginSecretToken("login_secret_token"));

        Map<String, Object> data = new HashMap<>();
        data.put("api_key_name", "key_name");
        data.put("role_id", 2);
        data.put("allowed_hosts", new String[0]);

        SecureResource invoker = new SecureResource(session);

        //MapからJSONに変換を行う汎用クラスを使い、リクエストを生成する
        //JSONの定義に書かれている「data」フィールド内の項目を設定する
        //＊Jacksonの機能を利用し、EntityからJSONへの変換を行うことも可能
        GenericApiResponse res = invoker.invoke(
                "/system/api_auth_key/create",
                new GenericApiRequest().withData(data),
                GenericApiResponse.class);

        //生成したキーをファイルなどに保存しておく
        //（取得したデータは「data」プロパティに格納される
        System.out.println(res.data);
    }

}
```

#### 呼び出しを実行する
```InvokePublicSample.java
import net.chikyu.sdk.PublicResource;
import net.chikyu.sdk.config.ApiConfig;
import net.chikyu.sdk.model.generic.GenericApiRequest;
import net.chikyu.sdk.model.generic.GenericApiResponse;

import java.util.HashMap;
import java.util.Map;

class InvokePublicSample {

    public static void main(String[] args) throws Exception{
        //2018/05/15現在、まだ本番環境が存在しないため、接続先の指定が必要。
        ApiConfig.setMode("devdc");

        PublicResource invoker = new PublicResource("api_key", "auth_key");

        //MapからJSONに変換を行う汎用クラスを使い、リクエストを生成する
        //JSONの定義に書かれている「data」フィールド内の項目を設定する
        //＊Jacksonの機能を利用し、EntityからJSONへの変換を行うことも可能
        Map<String, Object> data = new HashMap<>();
        data.put("items_per_page", 10);
        data.put("page_index", 0);

        //API呼び出しを実行する
        //  第一引数: APIのパス
        //  第二引数: リクエスト(ここでは汎用のものを指定)
        //  第三引数: レスポンスオブジェクトのデータ型(ここでは汎用のものを指定)
        GenericApiResponse res = invoker.invoke(
                "/entity/prospects/list",
                new GenericApiRequest().withData(data),
                GenericApiResponse.class);

        //（取得したデータは「data」プロパティに格納される
        System.out.println(res.data);
    }

}
```

### class2(認証トークンからセッションを生成)
#### 認証トークンを生成する
```CreateTokenSample.java
import net.chikyu.sdk.auth.Token;
import net.chikyu.sdk.config.ApiConfig;
import net.chikyu.sdk.model.session.token.TokenRequestModel;
import net.chikyu.sdk.model.session.token.TokenResponseModel;

class CreateTokenSample {

    public static void main(String[] args) throws Exception{
        //2018/05/15現在、まだ本番環境が存在しないため、接続先の指定が必要。
        ApiConfig.setMode("devdc");

        TokenRequestModel req = new TokenRequestModel();
        req.tokenName = "token_name";
        req.email = "email";
        req.password = "password";

        TokenResponseModel token = Token.create(req);

        // トークン情報をファイルなどに保存しておく。
        System.out.println(token);
    }

}
```

#### ログインしてセッションを生成する
```CreateSessionSample.java
import net.chikyu.sdk.auth.Session;
import net.chikyu.sdk.config.ApiConfig;
import net.chikyu.sdk.model.session.token.SendTokenRequestModel;


class CreateSessionSample {
    public static void main(String[] args) throws Exception {
        //2018/05/15現在、まだ本番環境が存在しないため、接続先の指定が必要。
        ApiConfig.setMode("devdc");

        Session session = Session.login(new SendTokenRequestModel()
                .withTokenName("token_name")
                .withLoginToken("login_token")
                .withLoginSecretToken("login_secret_token")
        );

        //生成したセッションはローカル変数などに保存しておく。
        System.out.println(session);

        //セッション情報をテキストに変換する
        String text = session.toString();

        //セッション情報をテキストから復元する
        session = Session.fromJson(text);

        //処理対象の組織を変更する
        session.changeOrgan(1234);

        //ログアウトする
        session.logout();
    }
}
```


#### 呼び出しを実行する
```InvokeSecureSample.java
import net.chikyu.sdk.config.ApiConfig;
import net.chikyu.sdk.auth.Session;
import net.chikyu.sdk.SecureResource;
import net.chikyu.sdk.model.generic.GenericApiRequest;
import net.chikyu.sdk.model.generic.GenericApiResponse;
import net.chikyu.sdk.model.session.token.SendTokenRequestModel;

import java.util.HashMap;
import java.util.Map;

class InvokeSecureSample {

    public static void main(String[] args) throws Exception {
        //2018/05/15現在、まだ本番環境が存在しないため、接続先の指定が必要。
        ApiConfig.setMode("devdc");

        Session session = Session.login(new SendTokenRequestModel()
                .withTokenName("20170206")
                .withLoginToken("6042e10801c44e89f9532d1a411349ca6402d0319c42a3029ab27fb05e93ae2468571507a03c503fe181d07f4b15ba54539a9fb71404ac6afd4bde49d42e01d4")
                .withLoginSecretToken("6417942b9c3440ed420b08ea0627676f55ff3c26a8d80e81c84c5b2bd1902629c71f56a5c92e18b6269c654643bd77b1c0cd149cd579bc57f4aa7069a9a9fcf4d9718cf94acaee19745665c9c9e69294fa66cbdc4a751b288a0b0a97b383e6380c7946ce8a9fd3a4cdb215d7a6cad0f5382858f19a0190721fa0d86cdc952ee9")
        );

        SecureResource invoker = new SecureResource(session);

        //MapからJSONに変換を行う汎用クラスを使い、リクエストを生成する
        //JSONの定義に書かれている「data」フィールド内の項目を設定する
        //＊Jacksonの機能を利用し、EntityからJSONへの変換を行うことも可能
        Map<String, Object> data = new HashMap<>();
        data.put("items_per_page", 10);
        data.put("page_index", 0);

        //API呼び出しを実行する
        //  第一引数: APIのパス
        //  第二引数: リクエスト(ここでは汎用のものを指定)
        //  第三引数: レスポンスオブジェクトのデータ型(ここでは汎用のものを指定)
        GenericApiResponse res = invoker.invoke(
                "/entity/prospects/list",
                new GenericApiRequest().withData(data),
                GenericApiResponse.class);

        //（取得したデータは「data」プロパティに格納される
        System.out.println(res.data);

    }

}
```


## APIリスト
こちらをご覧ください。

http://dev-docs.chikyu.mobi/

