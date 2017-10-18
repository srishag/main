package seedu.address.logic.commands;

import com.google.api.services.people.v1.model.*;
import seedu.address.commons.GoogleContactsBuilder;

import seedu.address.logic.LogicManager;
import seedu.address.logic.Logic;

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

        GoogleContactsBuilder builder = new GoogleContactsBuilder();
        Logic logic = new LogicManager(model);
        List<Person> connections = null;

        try {
            connections = builder.getPersonlist();
        } catch (IOException E){
            ErrorMessage = "Authentication Failed. Please login again.";

        }
        Person createdContact = new Person();
        Person contactToCreate = new Person();
        List names = new ArrayList();
        List email = new ArrayList();
        List phone = new ArrayList();
        List address = new ArrayList();

        names.add(new Name().setGivenName("John").setFamilyName("Doe"));
        email.add(new EmailAddress().setValue("haha@hotmail.com"));
        phone.add(new PhoneNumber().setValue("97848348"));
        address.add(new Address().setStreetAddress("Blk 480 #15-463"));
        contactToCreate.setNames(names).setEmailAddresses(email).setPhoneNumbers(phone).setAddresses(address);
        try{
             createdContact = builder.getPeopleService().people().createContact(contactToCreate).execute();
        }catch (IOException e){}


        return new CommandResult(createdContact.getNames().get(0).getDisplayName());
    }
}