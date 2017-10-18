package seedu.address.logic.commands;

import com.google.api.services.people.v1.model.Person;
import javafx.collections.ObservableList;
import seedu.address.commons.GoogleAuthenticator;
import seedu.address.commons.GoogleContactsBuilder;
import seedu.address.commons.core.EventsCenter;
import seedu.address.commons.core.Messages;
import seedu.address.commons.events.ui.ExitAppRequestEvent;
import seedu.address.commons.events.ui.JumpToListRequestEvent;
import seedu.address.commons.events.ui.LoadLoginEvent;
import seedu.address.commons.events.ui.NewResultAvailableEvent;
import seedu.address.logic.LogicManager;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.logic.parser.exceptions.ParseException;
import seedu.address.logic.Logic;
import seedu.address.model.Model;
import seedu.address.model.person.ReadOnlyPerson;

import java.io.IOException;
import java.util.*;


/**
 * Finds and lists all persons in address book whose name contains any of the argument keywords.
 * Keyword matching is case sensitive.
 */
public class SyncCommand extends Command {

    public static final String COMMAND_WORD = "sync";
    public static final String COMMAND_ALIAS = "sy";

    public static final String MESSAGE_USAGE = COMMAND_WORD + ": Sync contacts from google"
            + "process\n"
            + "Parameters: KEYWORD\n"
            + "Example: " + COMMAND_WORD;
    private String editCommand = "edit +";
    private String CommandMessage;

    @Override
    public CommandResult execute() {
        int count = 0;
        int countEdited =0;
        boolean needEdit;

        GoogleContactsBuilder builder = new GoogleContactsBuilder();
        Logic logic = new LogicManager(model);
        List<Person> connections = null;

        try {
            connections = builder.getPersonlist();
        } catch (IOException E){
         //   ErrorMessage = "Authentication Failed. Please login again.";

        }
        ObservableList <ReadOnlyPerson> personlist = model.getAddressBook().getPersonList();

    //   if ((connections != null) && (connections.size() > 0)) {
    //       for (Person person : connections) {
    //           editCommand = "edit +";
    //           needEdit = false;
    //           for (ReadOnlyPerson contact : personlist) {
    //               count++;
    //               editCommand += String.valueOf(count);
    //               String a = contact.getGoogleID();
    //               if (person.getResourceName().substring(7).equals(contact.getGoogleID())) {
    //                   if (!person.getNames().get(0).getDisplayName().equals(contact.getName().fullName)) {
    //                       editCommand += " n/" + person.getNames().get(0).getDisplayName();
    //                       needEdit = true;
    //                   }
    //                   if (!person.getPhoneNumbers().get(0).getValue().replace(" ", "")
    //                           .equals(contact.getPhone().value)) {
    //                       editCommand += " p/" + person.getPhoneNumbers().get(0).getValue().replace(" ", "");
    //                       needEdit = true;
    //                   }
    //                   if (!person.getEmailAddresses().get(0).getValue().equals(contact.getEmail().value)) {
    //                       editCommand += " e/" + person.getEmailAddresses().get(0).getValue();
    //                       needEdit = true;
    //                   }
    //                   if (!person.getAddresses().get(0).getStreetAddress().equals(contact.getAddress().value)) {
    //                       editCommand += " a/" +person.getAddresses().get(0).getStreetAddress();
    //                       needEdit = true;
    //                   }
    //                   if (needEdit) {
    //                       try {
    //                           logic.execute(editCommand);
    //                           countEdited++;
    //                       } catch (CommandException | ParseException | NullPointerException e) {
    //                       }
    //                   }
    //               }
    //           }
    //       }
    //   }
    //   else{
    //       this.CommandMessage = "No Contacts Found!";
    //   }

    //   if(countEdited!=0){
    //       CommandMessage = "Contacts edited : " + String.valueOf(countEdited);
    //   }
    //   else{
    //       CommandMessage = "no contacts edited";
    //   }

        return new CommandResult(CommandMessage);
    }
}