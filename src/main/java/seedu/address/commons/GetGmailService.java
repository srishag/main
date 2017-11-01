package seedu.address.commons;

import com.google.api.client.googleapis.auth.oauth2.GoogleCredential;
import com.google.api.services.gmail.Gmail;
import seedu.address.logic.commands.exceptions.GoogleAuthException;

import java.io.IOException;

public class GetGmailService {

    private final GoogleAuthenticator googleAuthenticator = new GoogleAuthenticator();

    //Returns Gmail service using the GoogleAuthenticator class
    public Gmail getGmailService() throws GoogleAuthException, IOException {
        String token = googleAuthenticator.getToken();
        GoogleCredential credential = googleAuthenticator.getCredential(token);
        return new Gmail.Builder(googleAuthenticator.getTransport(), googleAuthenticator.getJsonFactory(), credential)
                .build();
    }

}
