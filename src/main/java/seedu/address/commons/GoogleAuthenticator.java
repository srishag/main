package seedu.address.commons;

import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeFlow;
import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeTokenRequest;
import com.google.api.client.googleapis.auth.oauth2.GoogleCredential;
import com.google.api.client.googleapis.auth.oauth2.GoogleTokenResponse;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.jackson2.JacksonFactory;

import java.io.IOException;
import java.util.Arrays;

public class GoogleAuthenticator {

    HttpTransport transport = new NetHttpTransport();
    JacksonFactory jsonFactory = new JacksonFactory();
    String clientId = "357781332625-n95uh41ub5ovd3qlvca75r28kjepiuhb.apps.googleusercontent.com";
    String clientSecret = "B-Ogvf73gshNLn7hV1nkGnae";
    String redirectUrl = "https://contacts.google.com";

    String scope_1 = "https://www.googleapis.com/auth/contacts.readonly";
    String scope_2 = "https://www.googleapis.com/auth/plus.login";
    String scope_3 = "https://www.googleapis.com/auth/user.phonenumbers.read";

    String authorizationUrl;


    //Constructor
    public GoogleAuthenticator(){
        GoogleAuthorizationCodeFlow GoogleAuth = new GoogleAuthorizationCodeFlow(transport, jsonFactory, clientId,
                clientSecret, Arrays.asList(scope_1, scope_2, scope_3));
        this.authorizationUrl =
                GoogleAuth.newAuthorizationUrl().setRedirectUri(redirectUrl).setScopes(Arrays.asList(scope_1, scope_2, scope_3)).build();
    }

    public String getAuthorizationUrl(){
        return authorizationUrl;
    }


    //Obtain authorization code from browser URL. Returns null if no authorization code is received
    public String getAuthorizationCode(String URL){
        String code = "";
        if(URL.contains("code=")){
            int index = URL.indexOf("code=");
            code = URL.substring(index + 5,code.length()-1);
        }

        return code;
    }


    //Obtain credentials from authorization code
    public GoogleCredential getAuthorizationToken(String code) throws IOException{

        GoogleTokenResponse Token = new GoogleAuthorizationCodeTokenRequest(transport, jsonFactory, clientId,
                clientSecret, code, redirectUrl).execute();

        GoogleCredential credential = new GoogleCredential.Builder()
                .setTransport(transport)
                .setJsonFactory(jsonFactory)
                .setClientSecrets(clientId, clientSecret)
                .build()
                .setFromTokenResponse(Token);
        return credential;
    }
}
