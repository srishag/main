package seedu.address.logic.commands;

import com.google.api.services.people.v1.model.*;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import seedu.address.logic.CommandHistory;
import seedu.address.logic.UndoRedoStack;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.Model;
import seedu.address.model.ModelManager;
import seedu.address.model.UserPrefs;
import seedu.address.model.person.exceptions.DuplicatePersonException;
import seedu.address.testutil.TypicalGoogleContactsList;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static seedu.address.testutil.TypicalPersons.getTypicalAddressBook;


public class ImportCommandTest {

    private Model model;
    private List<Person> listperson;
    TypicalGoogleContactsList GoogleContactList = new TypicalGoogleContactsList();


    @Rule
    public ExpectedException thrown = ExpectedException.none();

    @Before
    public void setUp() {
        model = new ModelManager(getTypicalAddressBook(), new UserPrefs());
        listperson = new ArrayList<>();
    }


    /**
     * Checks if Login is authenticated. In this case it is not.
     */
    @Test
    public void execute_require_login() throws Exception {
        thrown.expect(NullPointerException.class);
        new ImportCommand();
    }

    /**
     * Test for normal importing of a google contact
     */
    @Test
    public void execute_assert_CommandSuccess() throws DuplicatePersonException,CommandException{
        listperson.add(GoogleContactList.FreddyGoogle);
        ImportCommand command = prepareCommand(listperson, this.model);

        Model modelstub = new ModelManager(getTypicalAddressBook(), new UserPrefs());
        modelstub.addPerson(GoogleContactList.FreddyAddressBook);
        String expectedMessage = "1 contact/s imported!     0 contact/s failed to import!" + "\n";

        assert_Command_Success(command, expectedMessage, modelstub);
    }

    /**
     * Test for invalid importing of a google contact due to invalid attributes
     */
    @Test
    public void execute_assert_CommandFailure_ContactInvalidFormat() throws DuplicatePersonException,CommandException{
        listperson.add(GoogleContactList.MayGoogle);
        ImportCommand command = prepareCommand(listperson, this.model);

        String expectedMessage = "0 contact/s imported!     1 contact/s failed to import!" + "\n" +
                "Contacts already existed : 0     Contacts not in the correct format : 1" + "\n" +
                "Please check the format of the following google contacts :  May";

        assert_Command_Failure(command, expectedMessage, this.model);
    }

    /**
     * Test for importing a contact that already exists in the addressbook
     */
    @Test
    public void execute_assert_CommandFailure_ContactExists() throws DuplicatePersonException,CommandException{
        model.addPerson(GoogleContactList.FreddyAddressBook);
        listperson.add(GoogleContactList.FreddyGoogle);
        ImportCommand command = prepareCommand(listperson, model);

        String expectedMessage = "0 contact/s imported!     1 contact/s failed to import!" + "\n" +
                "Contacts already existed : 1     Contacts not in the correct format : 0" + "\n";

        assert_Command_Failure(command, expectedMessage, model);
    }

    /**
     * Preparing import command by using a test-only constructor (Using a stub google person list)
     */
    private ImportCommand prepareCommand(List<Person> personList, Model model) throws CommandException{
        ImportCommand command = new ImportCommand(personList);
        command.setData(model, new CommandHistory(), new UndoRedoStack());
        return command;
    }

    /**
     * Asserts if a command successfully executed
     */
    private void assert_Command_Success(Command command, String expectedMessage,Model modelstub) throws CommandException{
        CommandResult commandResult = command.execute();
        assertEquals(expectedMessage, commandResult.feedbackToUser);
        assertEquals(modelstub.getAddressBook(), model.getAddressBook());
    }

    /**
     * Asserts if a command unsuccessfully executed
     */
    private void assert_Command_Failure(Command command, String expectedMessage,Model modelstub) throws CommandException{
        CommandResult commandResult = command.execute();
        assertEquals(expectedMessage, commandResult.feedbackToUser);
        assertEquals(modelstub.getAddressBook(), model.getAddressBook());
    }
}