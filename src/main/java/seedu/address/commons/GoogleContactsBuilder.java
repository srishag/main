package seedu.address.commons;

import com.google.api.client.googleapis.auth.oauth2.GoogleCredential;
import com.google.api.services.people.v1.PeopleService;
import com.google.api.services.people.v1.model.Person;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class GoogleContactsBuilder {

    private final GoogleAuthenticator googleAuthenticator = new GoogleAuthenticator();
    private List<Person> personlist = new ArrayList<>();


    //Returns a list of persons from the user's google contacts using the GoogleAuthenticator class
    public List<Person> getPersonlist() throws IOException {
        String token = googleAuthenticator.getToken();
        GoogleCredential credential = googleAuthenticator.getCredential(token);
        PeopleService peopleService = googleAuthenticator.BuildPeopleService(credential);
        personlist = googleAuthenticator.getConnections(peopleService);

        return personlist;
    }
}
