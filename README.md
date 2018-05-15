# Chikyu
## APIの基本仕様について
こちらのレポジトリをご覧ください

https://github.com/chikyuinc/chikyu-api-specification
 
## インストール
Mavenの公式レポジトリには登録していないため、レポジトリを追加してインストールを行ってください。

例えばGradleの場合、build.gradleは以下のようになります。

```build.gradle
group 'com.examle'
version '0.0.1-SNAPSHOT'

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
import net.chikyu.sdk.model.session.token.SendTokenRequestModel;

class Sample {

	public static void main(String[] args) {
		//2018/05/14現在、まだ本番環境が未構築であるため、こちらのテスト用の環境名を指定して下さい。
		ApiConfig.setMode('devdc');
		
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
       req.data = data;
       
       //Mapでレスポンスを受け取る汎用クラスを使い、レスポンスを受け取る
　　　　//＊Jacksonの機能を利用し、EntityからJSONへの変換を行うことも可能
       GenericApiResponse res = resource.invoke("entity/prospects/list", GenericApiRequest.withData(req), GenericApiResponse.class);

       List<Map<String, Object>> items = (List<Map<String, Object>>)res.data.get("list");

       for (Map<String, Object> item : items) {
            System.out.println(item.get("__display_name"));
       }

	}
	
}
```

## 詳細
### class1(APIキーのみで呼び出し可能)
#### APIトークンを生成する
```TokenSample.java
import net.chikyu.sdk.auth.Session;
import net.chikyu.sdk.SecureResource;

class TokenSample {

    public static void main(String[] args) {
        //下記のclass2 apiを利用し、予めトークンを生成しておく。
        Session session = Session.login(new SendTokenRequestModel()
			                  .withTokenName("tokenName")
			                  .withLoginToken("loginToken")
			                  .withLoginSecretToken("loginSecretToken")
			               );
			               
        Map<String, Object> data = new HashMap<>();
        data.put("api_key_name", "key_name");
        data.put("role_id", 1234);
        data.put("allowed_hosts", {});
        req.data = data;
        
        //MapからJSONに変換を行う汎用クラスを使い、リクエストを生成する
        //JSONの定義に書かれている「data」フィールド内の項目を設定する
        //＊Jacksonの機能を利用し、EntityからJSONへの変換を行うことも可能
        GenericApiResponse res = resource.invoke(
                                     "/system/api_auth_key/create", 
                                     GenericApiRequest.withData(data), 
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

class InvokePublicSample {
    
    public static void main(String[] args) {
        PublicResource invoker = new PublicResource("apiKey", "authKey");
        
        //MapからJSONに変換を行う汎用クラスを使い、リクエストを生成する
        //JSONの定義に書かれている「data」フィールド内の項目を設定する
        //＊Jacksonの機能を利用し、EntityからJSONへの変換を行うことも可能
        Map<String, Object> data = new HashMap<>();
        data.put("field1", "data");
        
        //API呼び出しを実行する
        //  第一引数: APIのパス
        //  第二引数: リクエスト(ここでは汎用のものを指定)
        //  第三引数: レスポンスオブジェクトのデータ型(ここでは汎用のものを指定)
        GenericApiResponse res = resource.invoke(
                                     "/some/api", 
                                     GenericApiRequest.withData(data), 
                                     GenericApiResponse.class);

        //（取得したデータは「data」プロパティに格納される
        System.out.println(res.data);        
    }
    
}
```

### class2(APIトークンからセッションを生成)
#### APIトークンを生成する
```CreateTokenSample.java
import net.chikyu.sdk.auth.Token;
import net.chikyu.sdk.model.session.token.TokenRequestModel;
import net.chikyu.sdk.model.session.token.TokenResponseModel;

class CreateTokenSample {

    public static void main(String[] args) {
        TokenRequestModel req = new TokenRequestModel();
        req.tokenName = "tokenName";
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
import net.chikyu.sdk.model.session.token.SendTokenRequestModel;


class CreateSessionSample {
    public static void main(String[] args) {
        Session session = Session.login(new SendTokenRequestModel()
			                    .withTokenName("tokenName")
			                    .withLoginToken("loginToken")
			                    .withLoginSecretToken("loginSecretToken")
			                );
        SecureResource invoker = new SecureResouce(session);
        
        //生成したセッションはローカル変数などに保存しておく。
        System.out.println(session);
        
        //セッション情報をテキストに変換する
        String text = session.toString();
        
        //セッション情報をテキストから復元する
        session = session.fromJson(text);
        
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
import net.chikyu.sdk.model.session.token.SendTokenRequestModel;

class InvokeSecureSample {

	public static void main(String[] args) {
       SecureResource invoker = new SecureResource(session);
       
        //MapからJSONに変換を行う汎用クラスを使い、リクエストを生成する
        //JSONの定義に書かれている「data」フィールド内の項目を設定する
        //＊Jacksonの機能を利用し、EntityからJSONへの変換を行うことも可能
        Map<String, Object> data = new HashMap<>();
        data.put("field1", "data");
        
        //API呼び出しを実行する
        //  第一引数: APIのパス
        //  第二引数: リクエスト(ここでは汎用のものを指定)
        //  第三引数: レスポンスオブジェクトのデータ型(ここでは汎用のものを指定)
        GenericApiResponse res = invoker.invoke(
                                     "/some/api", 
                                     GenericApiRequest.withData(data), 
                                     GenericApiResponse.class);

        //（取得したデータは「data」プロパティに格納される
        System.out.println(res.data);        

	}
	
}

```


## APIリスト
こちらをご覧ください。

http://dev-docs.chikyu.mobi/

