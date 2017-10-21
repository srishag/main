package seedu.address.logic.commands;

import com.google.api.services.people.v1.model.Person;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import seedu.address.commons.exceptions.IllegalValueException;
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

public class SyncCommandTest {
    private Model model;
    private List<Person> personList;
    TypicalGoogleContactsList GoogleContactList = new TypicalGoogleContactsList();

    @Rule
    public ExpectedException thrown = ExpectedException.none();

    @Before
    public void setUp() {
        model = new ModelManager(getTypicalAddressBook(), new UserPrefs());
        personList = new ArrayList<Person>();
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
     * Test for normal Syncing of a single google contact
     */
    @Test
    public void execute_assert_SyncSuccess() throws DuplicatePersonException,CommandException{
        model.addPerson(GoogleContactList.FreddyAddressBook);
        personList.add(GoogleContactList.FreddySyncGoogle);
        SyncCommand command = prepareCommand(personList, model);

        Model modelStub = new ModelManager(getTypicalAddressBook(), new UserPrefs());
        modelStub.addPerson(GoogleContactList.FreddySyncAddressBook);
        String expectedMessage = "1 contact/s Synced!     0 contact/s failed to Sync!";

        assert_Command_Success(command, expectedMessage, modelStub);
    }

  /**
   * Test for invalid Syncing of a google contact due to invalid attributes
   */
  @Test
  public void execute_assert_SyncFailure_ContactInvalidFormat() throws IllegalValueException,NullPointerException,CommandException{
      model.addPerson(GoogleContactList.MayAddressBook);
      personList.add(GoogleContactList.MayGoogle);
      SyncCommand command = prepareCommand(personList, model);

      String expectedMessage = "0 contact/s Synced!     1 contact/s failed to Sync!" + "\n" +
              "Please check the format of the following google contacts : May";

      assert_Command_Failure(command, expectedMessage, model);
  }

   /**
    * Test for syncing a contact that is of no difference than the one in the addressbook
    */
   @Test
   public void execute_assert_CommandFailure_ContactExists() throws DuplicatePersonException,CommandException{
       model.addPerson(GoogleContactList.FreddyAddressBook);
       personList.add(GoogleContactList.FreddyGoogle);
       String expectedMessage = "0 contact/s Synced!     0 contact/s failed to Sync!";

       SyncCommand command = prepareCommand(personList, model);
       assert_Command_Failure(command, expectedMessage, model);
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
    private void assert_Command_Success(Command command, String expectedMessage,Model modelstub) throws CommandException{
        CommandResult commandResult = command.execute();
        assertEquals(expectedMessage, commandResult.feedbackToUser);
        assertEquals(modelstub.getAddressBook(), model.getAddressBook());
    }

    /**
     * Asserts if a command unsuccessfully executed
     */
    private void assert_Command_Failure(Command command, String expectedMessage, Model modelstub) throws CommandException{
        CommandResult commandResult = command.execute();
        assertEquals(expectedMessage, commandResult.feedbackToUser);
        assertEquals(modelstub.getAddressBook(), model.getAddressBook());
    }
}
