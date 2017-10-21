package seedu.address.logic.commands;

import com.google.api.services.people.v1.model.Person;
import javafx.collections.ObservableList;
import seedu.address.commons.GoogleContactsBuilder;
import seedu.address.commons.core.Messages;
import seedu.address.commons.exceptions.IllegalValueException;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.person.*;
import seedu.address.model.person.exceptions.PersonNotFoundException;
import seedu.address.model.tag.Tag;

import java.io.IOException;
import java.util.*;


/**
 * Syncs all existing google contacts in the addressbook with the contacts in google contacts
 * Contacts in google contacts takes higher precedence.
 */
public class SyncCommand extends Command {

    public static final String COMMAND_WORD = "sync";
    public static final String COMMAND_ALIAS = "sync";

    public static final String MESSAGE_USAGE = COMMAND_WORD + ": Sync contacts from google"
            + "process\n"
            + "Parameters: KEYWORD\n"
            + "Example: " + COMMAND_WORD;
    private String CommandMessage = "";
    private String NamesNotSynced = "";

    private int contactsSyncedCount = 0;
    private int errorSyncedCount =0;

    @Override
    public CommandResult execute() throws CommandException {

        ObservableList<ReadOnlyPerson> personlist = model.getAddressBook().getPersonList();
        List<Person> connections = null;
        boolean checkifexists = false;

        try {
            GoogleContactsBuilder builder = new GoogleContactsBuilder();
            connections = builder.getPersonlist();
        } catch (IOException E) {
            throw new CommandException("Authentication Failed. Please login again.");
        }

        if(personlist.isEmpty()){
            throw new CommandException("No contacts in addressbook to sync");
        }

        for (ReadOnlyPerson contact : personlist) {
            checkifexists = false;
            if ((connections != null) && (connections.size() > 0)) {
                for (Person person : connections) {
                    if (person.getResourceName().substring(8).equals(contact.getGoogleID().value)) {
                        checkifexists = true;
                        try {
                            if (!contact.isSameStateAs(PersonToCheck(person))) {
                                model.updatePerson(contact, PersonToCheck(person));
                                contactsSyncedCount++;
                            }
                        } catch (IllegalValueException | NullPointerException | PersonNotFoundException e) {
                            errorSyncedCount++;
                            NamesNotSynced += person.getNames().get(0).getDisplayName() + ", ";
                        }
                    }
                }
            }

            if ((contact.getGoogleID().value != "not GoogleContact") && (checkifexists == false)) {
                try {
                    model.updatePerson(contact, RemoveGoogleContactStatus(contact));
                    contactsSyncedCount++;
                } catch (IllegalValueException | NullPointerException | PersonNotFoundException e) {
                }
            }
        }
        CommandMessage = setCommandMessage(NamesNotSynced, contactsSyncedCount, errorSyncedCount);

        return new CommandResult(CommandMessage);
    }

    /**
     * Creates a person in addressBook based on the contact in google contact
     */
    public ReadOnlyPerson PersonToCheck(Person person) throws IllegalValueException, NullPointerException {

        Name name = new Name(person.getNames().get(0).getDisplayName());
        Phone phone = new Phone(person.getPhoneNumbers().get(0).getValue().replace(" ", ""));
        Email email = new Email(person.getEmailAddresses().get(0).getValue());
        Address address = new Address(person.getAddresses().get(0).getStreetAddress());
        GoogleID ID = new GoogleID(person.getResourceName().substring(8));

        Tag tag = new Tag("GoogleContact");
        Set<Tag> Tags = new HashSet<>();
        Tags.add(tag);

        return new seedu.address.model.person.Person(name, phone, email, address, Tags, ID);
    }
    /**
     * Creates a new person without its previous google contact status
     */
    public ReadOnlyPerson RemoveGoogleContactStatus(ReadOnlyPerson contact)
            throws IllegalValueException, NullPointerException {

        Name name = new Name(contact.getName().fullName);
        Phone phone = new Phone(contact.getPhone().value);
        Email email = new Email(contact.getEmail().value);
        Address address = new Address(contact.getAddress().value);
        GoogleID ID = new GoogleID("not GoogleContact");
        Set<Tag> Tags = new HashSet<>();

        return new seedu.address.model.person.Person(name, phone, email, address, Tags, ID);
    }

    /**
     * Creates a detailed message on the status of the sync
     */
    public String setCommandMessage(String NamesNotSynced, int contactsSyncedCount, int errorSyncedCount) {
        String CommandMessage;

        CommandMessage = String.format(Messages.MESSAGE_SYNC_CONTACT, contactsSyncedCount, errorSyncedCount);
        if(errorSyncedCount > 0){
            CommandMessage += "\n" + "Please check the format of the following google contacts : " +
                    NamesNotSynced.substring(0, NamesNotSynced.length()-2);
        }
        return CommandMessage;
    }
}