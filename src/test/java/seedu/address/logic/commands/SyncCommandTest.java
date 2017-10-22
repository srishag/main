package seedu.address.logic.commands;

import static org.junit.Assert.assertEquals;
import static seedu.address.testutil.TypicalPersons.getTypicalAddressBook;

import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import com.google.api.services.people.v1.model.Person;

import seedu.address.commons.exceptions.IllegalValueException;
import seedu.address.logic.CommandHistory;
import seedu.address.logic.UndoRedoStack;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.Model;
import seedu.address.model.ModelManager;
import seedu.address.model.UserPrefs;
import seedu.address.model.person.exceptions.DuplicatePersonException;
import seedu.address.testutil.TypicalGoogleContactsList;

public class SyncCommandTest {
    private Model model;
    private List<Person> personList;
    private TypicalGoogleContactsList googleContactList = new TypicalGoogleContactsList();

    @Before
    public void setUp() {
        model = new ModelManager(getTypicalAddressBook(), new UserPrefs());
        personList = new ArrayList<Person>();
    }

    @Rule
    public ExpectedException thrown = ExpectedException.none();

    /**
     * Checks if Login is authenticated. In this case it is not.
     */
    @Test
    public void execute_require_login() throws Exception {
        thrown.expect(NullPointerException.class);
        new ImportCommand();
    }
    /**
     * Test for normal Syncing of a single google contact
     */
    @Test
    public void execute_assert_syncSuccess() throws DuplicatePersonException, CommandException {
        model.addPerson(googleContactList.freddyAddressBook);
        personList.add(googleContactList.freddySyncGoogle);
        SyncCommand command = prepareCommand(personList, model);

        Model modelStub = new ModelManager(getTypicalAddressBook(), new UserPrefs());
        modelStub.addPerson(googleContactList.freddySyncAddressBook);
        String expectedMessage = "1 contact/s Synced!     0 contact/s failed to Sync!";

        assertCommandSuccess(command, expectedMessage, modelStub);
    }

    /**
     * * Test for invalid Syncing of a google contact due to invalid attributes
     * */
    @Test
     public void execute_assert_syncFailure_contactInvalidFormat()
            throws IllegalValueException, NullPointerException, CommandException {

        model.addPerson(googleContactList.mayAddressBook);
        personList.add(googleContactList.mayGoogle);
        SyncCommand command = prepareCommand(personList, model);

        String expectedMessage = "0 contact/s Synced!     1 contact/s failed to Sync!" + "\n"
                + "Please check the format of the following google contacts : May";

        assertCommandFailure(command, expectedMessage, model);
     }

     /**
      * Test for syncing a contact that is of no difference than the one in the addressbook
      */
     @Test
     public void execute_assert_commandFailure_contactExists() throws DuplicatePersonException, CommandException {
        model.addPerson(googleContactList.freddyAddressBook);
        personList.add(googleContactList.freddyGoogle);
        String expectedMessage = "0 contact/s Synced!     0 contact/s failed to Sync!";

        SyncCommand command = prepareCommand(personList, model);
        assertCommandFailure(command, expectedMessage, model);
   }

    /**
     * Preparing import command by using a test-only constructor (Using a stub google person list)
     */
    private SyncCommand prepareCommand(List<Person> personList, Model model) throws CommandException {
        SyncCommand command = new SyncCommand(personList);
        command.setData(model, new CommandHistory(), new UndoRedoStack());
        return command;
    }

    /**
     * Asserts if a command successfully executed
     */
    private void assertCommandSuccess(Command command, String expectedMessage, Model modelstub)
            throws CommandException {
        CommandResult commandResult = command.execute();
        assertEquals(expectedMessage, commandResult.feedbackToUser);
        assertEquals(modelstub.getAddressBook(), model.getAddressBook());
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
