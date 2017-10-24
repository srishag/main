package seedu.address.logic.commands;

import static org.junit.Assert.assertEquals;
import static seedu.address.testutil.TypicalPersons.ALICE;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import com.google.api.services.people.v1.model.Person;

import seedu.address.commons.GoogleContactsBuilder;
import seedu.address.logic.CommandHistory;
import seedu.address.logic.UndoRedoStack;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.AddressBook;
import seedu.address.model.Model;
import seedu.address.model.ModelManager;
import seedu.address.model.UserPrefs;
import seedu.address.model.person.exceptions.DuplicatePersonException;
import seedu.address.testutil.TypicalGoogleContactsList;

public class ExportCommandTest {
    @Rule
    public ExpectedException thrown = ExpectedException.none();

    private Model model;
    private List<Person> listperson;

    @Before
    public void setUp() {
        model = new ModelManager(new AddressBook(), new UserPrefs());
        listperson = new ArrayList<>();
    }

    /**
     * Checks if Login is authenticated. In this case it is not and null exception is thrown.
     */
    @Test
    public void execute_require_login() throws Exception {
        thrown.expect(NullPointerException.class);
        new ExportCommand();
    }

    /**
     * Test for exporting an empty addressbook. Throws Command exception for empty addressbook
     */
    @Test
    public void execute_commandFailure_noContacts() throws Exception {
        thrown.expect(CommandException.class);
        ExportCommand command = prepareCommand(this.model);
        command.execute();
    }

    /**
     * Test for exporting an empty addressbook when authentication is lost.
     */
    @Test
    public void execute_commandFailure_noAuthenticationToExport() throws Exception {
        thrown.expect(CommandException.class);
        model.addPerson(ALICE);
        ExportCommand command = prepareCommand(this.model);
        command.execute();
    }


    /**
     * Test for no importing of contacts to addressbook as all contacts in addressbook are already in google contacts.
     */
    @Test
    public void execute_commandFailure_allGoogleContacts() throws DuplicatePersonException, CommandException {
        model.addPerson(TypicalGoogleContactsList.FREEDYADDRESSBOOK);

        ExportCommand command = prepareCommand(this.model);
        String expectedMessage = "0 contact/s exported!     All contacts can be now found in google contact";
        assertCommandFailure(command, expectedMessage, model);
    }

    /**
     * Test for no importing of contacts to addressbook as all contacts in addressbook are already in google contacts.
     */
    @Test
    public void execute_updateAddressBookContactToGoogle() throws IOException, CommandException,
            DuplicatePersonException {
        model.addPerson(ALICE);

        ExportCommand command = prepareCommand(this.model);
        Person aliceGoogleContact = command.createGoogleContact(ALICE);
        assertEquals(aliceGoogleContact, TypicalGoogleContactsList.ALICE);
    }

    /**
     * Preparing import command by using a test-only constructor (Using a stub google person list)
     */
    private ExportCommand prepareCommand(Model model) throws CommandException {
        GoogleContactsBuilder builder = null;
        ExportCommand command = new ExportCommand(builder);
        command.setData(model, new CommandHistory(), new UndoRedoStack());
        return command;
    }

    /**
     * Asserts if a command unsuccessfully executed
     */
    private void assertCommandFailure(Command command, String expectedMessage, Model modelstub)
            throws CommandException {
        CommandResult commandResult = command.execute();
        assertEquals(expectedMessage, commandResult.feedbackToUser);
        assertEquals(modelstub.getAddressBook(), model.getAddressBook());
    }
}
