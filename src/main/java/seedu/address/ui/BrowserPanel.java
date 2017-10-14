package seedu.address.ui;

import java.net.URL;
import java.util.logging.Logger;

import com.google.api.client.auth.oauth2.AuthorizationCodeFlow;
import com.google.api.client.auth.oauth2.AuthorizationCodeResponseUrl;
import com.google.api.client.auth.oauth2.AuthorizationCodeTokenRequest;
import com.google.api.client.auth.oauth2.AuthorizationRequestUrl;
import com.google.api.client.googleapis.auth.clientlogin.ClientLogin;
import com.google.api.client.googleapis.auth.oauth2.*;
import com.google.api.services.people.v1.model.Person;
import com.google.common.eventbus.Subscribe;

import javafx.application.Platform;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.scene.layout.Region;
import javafx.scene.web.WebView;
import seedu.address.MainApp;
import seedu.address.commons.core.LogsCenter;
import seedu.address.commons.events.ui.NewResultAvailableEvent;
import seedu.address.commons.events.ui.PersonPanelSelectionChangedEvent;
import seedu.address.model.person.ReadOnlyPerson;

import com.google.api.client.http.HttpTransport;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.services.people.v1.PeopleService;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Arrays;

/**
 * The Browser Panel of the App.
 */
public class BrowserPanel extends UiPart<Region> {

    public static final String DEFAULT_PAGE = "default.html";
    public static final String GOOGLE_SEARCH_URL_PREFIX = "https://www.google.com.sg/search?safe=off&q=";
    public static final String GOOGLE_SEARCH_URL_SUFFIX = "&cad=h";

    private static final String FXML = "BrowserPanel.fxml";
    private static  String code =";";

    private final Logger logger = LogsCenter.getLogger(this.getClass());

    @FXML
    public void setUP() throws IOException {
        HttpTransport transport = new NetHttpTransport();
        JacksonFactory JsonFactory = new JacksonFactory();
        String clientId = "357781332625-n95uh41ub5ovd3qlvca75r28kjepiuhb.apps.googleusercontent.com";
        String clientSecret = "B-Ogvf73gshNLn7hV1nkGnae";
        String redirectUrl = "https://contacts.google.com";
        String scope1 = "https://www.googleapis.com/auth/contacts.readonly";
        String scope2 = "https://www.googleapis.com/auth/plus.login";


        GoogleAuthorizationCodeFlow Googlecode = new GoogleAuthorizationCodeFlow(transport,JsonFactory,clientId,
                clientSecret,Arrays.asList(scope1, scope2));
        String authorizationUrl = Googlecode.newAuthorizationUrl().setRedirectUri(redirectUrl).setScopes(Arrays.asList(scope1, scope2)).build();
        loadPage(authorizationUrl);
        String encodedURL = Googlecode.getAuthorizationServerEncodedUrl();

        if((browser.getEngine().getLocation().contains("code="))){
            int index = browser.getEngine().getLocation().indexOf("code=");
            code = browser.getEngine().getLocation().substring(index + 5);
            code = code.substring(0,code.length()-1);
        }

        GoogleTokenResponse Token = new GoogleAuthorizationCodeTokenRequest(transport, JsonFactory, clientId,
                clientSecret, code, redirectUrl).execute();

        if(code != null){
        GoogleCredential credential = new GoogleCredential.Builder()
                .setTransport(transport)
                .setJsonFactory(JsonFactory)
                .setClientSecrets(clientId, clientSecret)
                .build()
                .setFromTokenResponse(Token);
        PeopleService peopleService =
                new PeopleService.Builder(transport, JsonFactory, credential).build();

        Person profile = peopleService.people().get("people/me")
                .setPersonFields("names,emailAddresses")
                .execute();

        raise(new NewResultAvailableEvent(profile.getNames().toString()));
        }


    }


    @FXML
    private WebView browser;


    public BrowserPanel() {
        super(FXML);

        // To prevent triggering events for typing inside the loaded Web page.
        getRoot().setOnKeyPressed(Event::consume);

        loadDefaultPage();
        registerAsAnEventHandler(this);
    }


    private void loadPersonPage(ReadOnlyPerson person) {
        loadPage(GOOGLE_SEARCH_URL_PREFIX + person.getName().fullName.replaceAll(" ", "+")
                + GOOGLE_SEARCH_URL_SUFFIX);
    }

    public void loadPage(String url) {
        Platform.runLater(() -> browser.getEngine().load(url));
    }

    /**
     * Loads a default HTML file with a background that matches the general theme.
     */
    private void loadDefaultPage() {
        URL defaultPage = MainApp.class.getResource(FXML_FILE_FOLDER + DEFAULT_PAGE);
        loadPage(defaultPage.toExternalForm());
    }

    /**
     * Frees resources allocated to the browser.
     */
    public void freeResources() {
        browser = null;
    }

    @Subscribe
    private void handlePersonPanelSelectionChangedEvent(PersonPanelSelectionChangedEvent event) {
        logger.info(LogsCenter.getEventHandlingLogMessage(event));
        loadPersonPage(event.getNewSelection().person);
    }
}
