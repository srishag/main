package seedu.address.logic.commands;

import com.google.api.services.people.v1.model.Person;
import javafx.collections.ObservableList;
import seedu.address.commons.GoogleContactsBuilder;
import seedu.address.commons.core.Messages;
import seedu.address.commons.exceptions.IllegalValueException;
import seedu.address.logic.LogicManager;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.logic.parser.exceptions.ParseException;
import seedu.address.logic.Logic;
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
    private String ErrorMessage;
    private String NamesNotSynced = "";

    private int contactsSyncedCount = 0;
    private int errorSyncedCount =0;

    @Override
    public CommandResult execute() {
        GoogleContactsBuilder builder = new GoogleContactsBuilder();
        List<Person> connections = null;
        ObservableList<ReadOnlyPerson> personlist = model.getAddressBook().getPersonList();

        try {
            connections = builder.getPersonlist();
        } catch (IOException E) {
            ErrorMessage = "Authentication Failed. Please login again.";

        }

        if ((connections != null) && (connections.size() > 0)) {
            for (Person person : connections) {
                for (ReadOnlyPerson contact : personlist) {
                    if (person.getResourceName().substring(8).equals(contact.getGoogleID().value))
                        try {
                            if (!contact.isSameStateAs(PersonToCheck(person))) {
                                model.updatePerson(contact, PersonToCheck(person));
                                contactsSyncedCount++;
                            }
                        }catch (IllegalValueException | NullPointerException | PersonNotFoundException e) {
                            errorSyncedCount++;
                            NamesNotSynced += person.getNames().get(0).getDisplayName() + ", ";
                        }
                }
            }
        }
        CommandMessage = setCommandMessage(ErrorMessage, NamesNotSynced.substring(0, NamesNotSynced.length()-1),
                contactsSyncedCount, errorSyncedCount);

        return new CommandResult(CommandMessage);
    }

    /**
     * Creates a person in addressBook based on the contact in google contact
     */
    public seedu.address.model.person.Person PersonToCheck(Person person) throws IllegalValueException, NullPointerException {

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
     * Creates a detailed message on the status of the sync
     */
    public String setCommandMessage(String errorMessage, String NamesNotSynced, int contactsSyncedCount, int errorSyncedCount) {
        String CommandMessage;
        // If google contacts is unable to authenticate user, authentication failure message will be returned.
        if(errorMessage != null){
            return errorMessage;
        }

        CommandMessage = String.format(Messages.MESSAGE_SYNC_CONTACT, contactsSyncedCount, errorSyncedCount);
        if(errorSyncedCount > 0){
            CommandMessage += "\n" + "Please check the format of the following google contacts : " + NamesNotSynced;
        }
        return CommandMessage;
    }
}