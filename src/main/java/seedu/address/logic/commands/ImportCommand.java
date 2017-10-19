package seedu.address.logic.commands;

import com.google.api.services.people.v1.model.Person;
import seedu.address.commons.GoogleContactsBuilder;
import seedu.address.commons.core.Messages;
import seedu.address.commons.exceptions.IllegalValueException;
import seedu.address.logic.LogicManager;
import seedu.address.logic.Logic;
import seedu.address.model.person.*;
import seedu.address.model.tag.Tag;


import java.io.IOException;
import java.util.*;


/**
 * Finds and lists all persons in address book whose name contains any of the argument keywords.
 * Keyword matching is case sensitive.
 */
public class ImportCommand extends Command {

    public static final String COMMAND_WORD = "import";
    public static final String COMMAND_ALIAS = "import";

    public static final String MESSAGE_USAGE = COMMAND_WORD + ": Import contacts from google"
            + "process\n"
            + "Parameters: KEYWORD\n"
            + "Example: " + COMMAND_WORD;
    private String CommandMessage = "";
    private String ErrorMessage;
    private String NamesNotImported = "";

    private int contactsImportedCount = 0;
    private int errorImportsCount =0;

    @Override
    public CommandResult execute() {

        GoogleContactsBuilder builder = new GoogleContactsBuilder();
        List<Person> connections = null;
        try {
            connections = builder.getPersonlist();
        } catch (IOException E) {
            ErrorMessage = "Authentication Failed. Please login again.";

        }

        List<ReadOnlyPerson> personList = model.getAddressBook().getPersonList();
        boolean contactAlreadyExists;


        if ((connections != null) && (connections.size() > 0)) {
            for (Person person : connections) {
                contactAlreadyExists = this.ifContactExists(personList, person);
                if (!contactAlreadyExists) {
                    try {
                        model.addPerson(this.newPerson(person));
                        contactsImportedCount++;
                    } catch (IllegalValueException | NullPointerException e) {
                        NamesNotImported += " " + person.getNames().get(0).getDisplayName() + ", ";
                        errorImportsCount ++;
                    }
                }
            }
            CommandMessage = setCommandMessage(ErrorMessage,NamesNotImported.substring(0,NamesNotImported.length()-2),
                    contactsImportedCount,errorImportsCount,connections.size());
        }



        if(ErrorMessage != null){
            CommandMessage = ErrorMessage;
        }
        return new CommandResult(CommandMessage);
    }


    /**
     * Check if a particular google contact already exists in the addressbook
     * Returns true if already exists. Otherwise, false.
     */
   public boolean ifContactExists(List<ReadOnlyPerson> list, Person person){
        for(ReadOnlyPerson readOnlyPerson : list){
            if(person.getResourceName().substring(8).equals(readOnlyPerson.getGoogleID().value)){
                return true;
            }
        }
        return false;
   }
    /**
     * Creates a person in addressBook based on the contact in google contact
     */
   public seedu.address.model.person.Person newPerson(Person person) throws IllegalValueException, NullPointerException{
         Name name = new Name(person.getNames().get(0).getDisplayName());
         Email email = new Email(person.getEmailAddresses().get(0).getValue());
         Phone phone = new Phone(person.getPhoneNumbers().get(0).getValue().replace(" ", ""));
         Address address = new Address(person.getAddresses().get(0).getStreetAddress());
         GoogleID ID = new GoogleID(person.getResourceName().substring(8));

         Tag tag = new Tag("GoogleContact");
         Set<Tag> Tags = new HashSet<>();
         Tags.add(tag);

         return new seedu.address.model.person.Person(name,phone,email,address,Tags,ID);

   }
    /**
     * Creates a detailed message on the status of the import
     */
   public String setCommandMessage(String errorMessage, String notimported, int contactsImported, int errorImports, int size) {
       int existedContacts = size - contactsImported - errorImports;
       String CommandMessage;
       // If google contacts is unable to authenticate user, authentication failure message will be returned.
       if (errorMessage != null) {
           return errorMessage;
       }
       CommandMessage = String.format(Messages.MESSAGE_IMPORT_CONTACT, contactsImported, size - contactsImported) + "\n";

       if (size > contactsImported) {
           CommandMessage += "Contacts already existed : " + String.valueOf(existedContacts)
                   + "     Contacts not in the correct format : " + String.valueOf(errorImports) + "\n";
       }
       if (errorImports > 0) {
           CommandMessage += "Please check the format of the following google contacts : " + notimported;
       }
       return CommandMessage;
   }
}
