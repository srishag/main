package seedu.address.commons;

import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.googleapis.auth.oauth2.GoogleBrowserClientRequestUrl;
import com.google.api.client.googleapis.auth.oauth2.GoogleCredential;
import com.google.api.client.googleapis.auth.oauth2.GoogleTokenResponse;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.services.people.v1.PeopleService;
import com.google.api.services.people.v1.model.ListConnectionsResponse;
import com.google.api.services.people.v1.model.Person;
import seedu.address.commons.core.EventsCenter;
import seedu.address.commons.events.ui.GetRedirectURLEvent;
import seedu.address.commons.events.ui.NewResultAvailableEvent;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;


public class GoogleAuthenticator {

    HttpTransport transport = new NetHttpTransport();
    JacksonFactory jsonFactory = new JacksonFactory();

    String clientId = "650819214900-b3m4dv6igjlf9q3nq9eqsbmspask57kp.apps.googleusercontent.com";
    String clientSecret = "ttunyBEmZMrK_a9MH_qc1kus";
    String redirectUrl = "https://contacts.google.com";

    String scope_1 = "https://www.googleapis.com/auth/contacts.readonly";
    String scope_2 = "https://www.googleapis.com/auth/plus.login";
    String scope_3 = "https://www.googleapis.com/auth/user.phonenumbers.read";
    String scope_4 = "https://www.googleapis.com/auth/contacts";
    String scope_5 = "https://mail.google.com/";


    String authorizationUrl;


    //Constructor
    public GoogleAuthenticator(){
        this.authorizationUrl = new GoogleBrowserClientRequestUrl(clientId,redirectUrl,
                Arrays.asList(scope_1,scope_2,scope_3,scope_4,scope_5)).build();
    }


    //Getter for Authorization URL for user login
    public String getAuthorizationUrl(){
        return authorizationUrl;
    }



    //This method obtains the token from the redirect URL after successful login
    public String getToken(){
        String token = "";
        try {
            GetRedirectURLEvent event = new GetRedirectURLEvent();
            EventsCenter.getInstance().post(event);
            String URL = event.getReDirectURL();
            token = URL.substring(URL.indexOf("token=") + 6, URL.indexOf("&"));
        } catch (StringIndexOutOfBoundsException e){
            EventsCenter.getInstance().post(new NewResultAvailableEvent("Authentication Failed. Please login again."));
        }
        return token;
    }


    //Obtain google credentials from token
    public GoogleCredential getCredential(String token) throws IOException{

        GoogleTokenResponse Token = new GoogleTokenResponse();
         Token.setAccessToken(token);

        GoogleCredential credential = new GoogleCredential.Builder()
                .setTransport(transport)
                .setJsonFactory(jsonFactory)
                .setClientSecrets(clientId, clientSecret)
                .build()
                .setFromTokenResponse(Token);
        return credential;
    }


    //Build PeopleService using google credentials
    public PeopleService BuildPeopleService(GoogleCredential credential){
        PeopleService peopleService =
                new PeopleService.Builder(transport, jsonFactory, credential).build();
        return peopleService;
    }


    //Obtain the list of Contacts from google
    public List<Person> getConnections(PeopleService peopleService)  throws IOException{
        ListConnectionsResponse response = new ListConnectionsResponse();
        response = peopleService.people().connections().list("people/me")
                .setPersonFields("names,emailAddresses,phoneNumbers,addresses")
                .execute();
        List<Person> connections = response.getConnections();

        return connections;
    }
}
