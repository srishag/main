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

import seedu.address.logic.CommandHistory;
import seedu.address.logic.UndoRedoStack;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.Model;
import seedu.address.model.ModelManager;
import seedu.address.model.UserPrefs;
import seedu.address.model.person.exceptions.DuplicatePersonException;
import seedu.address.testutil.TypicalGoogleContactsList;

public class ImportCommandTest {

    private TypicalGoogleContactsList googleContactList = new TypicalGoogleContactsList();
    private Model model;
    private List<Person> listperson;

    @Before
    public void setUp() {
        model = new ModelManager(getTypicalAddressBook(), new UserPrefs());
        listperson = new ArrayList<>();
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
     * Test for normal importing of a google contact
     */
    @Test
    public void execute_assert_commandSuccess() throws DuplicatePersonException, CommandException {
        listperson.add(googleContactList.freddyGoogle);
        ImportCommand command = prepareCommand(listperson, this.model);

        Model modelstub = new ModelManager(getTypicalAddressBook(), new UserPrefs());
        modelstub.addPerson(googleContactList.freddyAddressBook);
        String expectedMessage = "1 contact/s imported!     0 contact/s failed to import!" + "\n";

        assertCommandSuccess(command, expectedMessage, modelstub);
    }

    /**
     * Test for invalid importing of a google contact due to invalid attributes
     */
    @Test
    public void execute_assert_commandFailure_contactInvalidFormat() throws DuplicatePersonException, CommandException {
        listperson.add(googleContactList.mayGoogle);
        ImportCommand command = prepareCommand(listperson, this.model);

        String expectedMessage = "0 contact/s imported!     1 contact/s failed to import!" + "\n"
                + "Contacts already existed : 0     Contacts not in the correct format : 1" + "\n"
                + "Please check the format of the following google contacts :  May";

        assertCommandFailure(command, expectedMessage, this.model);
    }

    /**
     * Test for importing a contact that already exists in the addressbook
     */
    @Test
    public void execute_assert_commandFailure_contactExists() throws DuplicatePersonException, CommandException {
        model.addPerson(googleContactList.freddyAddressBook);
        listperson.add(googleContactList.freddyGoogle);
        ImportCommand command = prepareCommand(listperson, model);

        String expectedMessage = "0 contact/s imported!     1 contact/s failed to import!" + "\n"
                + "Contacts already existed : 1     Contacts not in the correct format : 0" + "\n";

        assertCommandFailure(command, expectedMessage, model);
    }

    /**
     * Preparing import command by using a test-only constructor (Using a stub google person list)
     */
    private ImportCommand prepareCommand(List<Person> personList, Model model) throws CommandException {
        ImportCommand command = new ImportCommand(personList);
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
