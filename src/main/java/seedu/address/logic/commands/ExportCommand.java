package seedu.address.logic.commands;


import seedu.address.commons.GoogleContactsBuilder;
import seedu.address.commons.exceptions.IllegalValueException;
import seedu.address.model.person.Email;
import seedu.address.model.person.GoogleID;
import seedu.address.model.person.Phone;
import seedu.address.model.person.ReadOnlyPerson;
import seedu.address.model.person.exceptions.PersonNotFoundException;
import seedu.address.model.tag.Tag;
import com.google.api.services.people.v1.model.*;

import java.io.IOException;
import java.util.*;


/**
 * Finds and lists all persons in address book whose name contains any of the argument keywords.
 * Keyword matching is case sensitive.
 */
public class ExportCommand extends Command {

    public static final String COMMAND_WORD = "export";
    public static final String COMMAND_ALIAS = "export";

    public static final String MESSAGE_USAGE = COMMAND_WORD + ": Export contacts to google"
            + "process\n"
            + "Parameters: KEYWORD\n"
            + "Example: " + COMMAND_WORD;
    private String CommandMessage = "";
    private String ErrorMessage;

    private int contactsNotImported = 0;

    @Override
    public CommandResult execute() {
        List<ReadOnlyPerson> personList = model.getAddressBook().getPersonList();
        GoogleContactsBuilder builder = new GoogleContactsBuilder();
        List<Person> connections = null;

        try {
            connections = builder.getPersonlist();
        } catch (IOException E) {

            ErrorMessage = "Authentication Failed. Please login again.";
        }

        Person createdContact;
        for(ReadOnlyPerson contact : personList) {
            if (contact.getGoogleID().value.equals("not GoogleContact")) {
                try {
                    createdContact = createGoogleContact(contact);
                    createdContact = builder.getPeopleService().people().createContact(createdContact).execute();
                    model.updatePerson(contact, getNewAddressBookContact(contact, createdContact));
                } catch (IOException | IllegalValueException | PersonNotFoundException e) {
                }
            }
        }
        return new CommandResult("haha");
}

    public Person createGoogleContact(ReadOnlyPerson person)throws IOException{
        Person contactToCreate = new Person();
        List names = new ArrayList();
        List email = new ArrayList();
        List phone = new ArrayList();
        List address = new ArrayList();

        names.add(new Name().setGivenName(person.getName().fullName));
        email.add(new EmailAddress().setValue(person.getEmail().value));
        phone.add(new PhoneNumber().setValue(person.getPhone().value));
        address.add(new Address().setStreetAddress(person.getAddress().value));

        return contactToCreate.setNames(names).setEmailAddresses(email).setPhoneNumbers(phone).setAddresses(address);
    }

    public ReadOnlyPerson getNewAddressBookContact(ReadOnlyPerson contact, Person createdContact) throws IllegalValueException{

        GoogleID ID = new GoogleID(createdContact.getResourceName().substring(8));
        Tag tag = new Tag("GoogleContact");
        Set<Tag> Tags = new HashSet<>();
        Tags.add(tag);

        return new seedu.address.model.person.Person(contact.getName(), contact.getPhone(),
                contact.getEmail(), contact.getAddress(), Tags, ID);
    }
}