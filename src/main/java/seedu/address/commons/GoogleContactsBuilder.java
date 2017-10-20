package seedu.address.commons;

import com.google.api.client.googleapis.auth.oauth2.GoogleCredential;
import com.google.api.services.people.v1.PeopleService;
import com.google.api.services.people.v1.model.Person;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class GoogleContactsBuilder{

    private final GoogleAuthenticator googleAuthenticator = new GoogleAuthenticator();
    private List<Person> personlist = new ArrayList<>();
    private PeopleService peopleService;


    //Returns a list of persons from the user's google contacts using the GoogleAuthenticator class
    public GoogleContactsBuilder() throws IOException {
        String token = googleAuthenticator.getToken();
        GoogleCredential credential = googleAuthenticator.getCredential(token);
        PeopleService peopleService = googleAuthenticator.BuildPeopleService(credential);
        this.peopleService = peopleService;
        this.personlist = googleAuthenticator.getConnections(peopleService);
    }

    //Returns PeopleService for export
    public PeopleService getPeopleService() {
        return peopleService;
    }

    //Returns list of contacts from Google contacts for import/sync
    public List<Person> getPersonlist(){return personlist;}
}
