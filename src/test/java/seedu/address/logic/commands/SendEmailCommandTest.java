package seedu.address.logic.commands;

import static junit.framework.TestCase.assertTrue;
import static seedu.address.logic.commands.CommandTestUtil.EMAIL_SENDER;
import static seedu.address.logic.commands.CommandTestUtil.VALID_EMAIL_BODY;
import static seedu.address.logic.commands.CommandTestUtil.VALID_EMAIL_SUBJECT;
import static seedu.address.logic.commands.CommandTestUtil.assertCommandFailure;
import static seedu.address.logic.commands.CommandTestUtil.assertCommandSuccess;
import static seedu.address.logic.commands.CommandTestUtil.showFirstPersonOnly;
import static seedu.address.testutil.TypicalIndexes.INDEX_FIRST_PERSON;
import static seedu.address.testutil.TypicalIndexes.INDEX_SECOND_PERSON;
import static seedu.address.testutil.TypicalPersons.getTypicalAddressBook;

import java.io.IOException;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

import com.google.api.services.gmail.Gmail;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import seedu.address.commons.GetGmailService;
import seedu.address.commons.core.Messages;
import seedu.address.commons.core.index.Index;
import seedu.address.logic.CommandHistory;
import seedu.address.logic.UndoRedoStack;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.logic.commands.exceptions.GoogleAuthException;
import seedu.address.model.Model;
import seedu.address.model.ModelManager;
import seedu.address.model.UserPrefs;
import seedu.address.model.person.ReadOnlyPerson;

/**
 * Contains integration tests (interaction with the Model) and unit tests for SendEmailCommand.
 */
public class SendEmailCommandTest {

    @Rule
    public ExpectedException thrown = ExpectedException.none();

    /**
     * Checks if Login is authenticated. In this case it is not and GoogleAuthException is thrown.
     */
    @Before
    public void execute_require_login() throws Exception {
        thrown.expect(GoogleAuthException.class);
    }

    private Model model = new ModelManager(getTypicalAddressBook(), new UserPrefs());

    public Gmail getGmailService() throws IOException, GoogleAuthException {
        return new GetGmailService().getGmailService();
    }

    public MimeMessage getValidEmail(String personToSendEmail, String emailSubject,
                                     String emailBody) throws MessagingException {
        return ModelManager.createEmail(personToSendEmail, EMAIL_SENDER, emailSubject, emailBody);
    }

    @Test
    public void execute_validIndexUnfilteredList_success() throws Exception {
        ReadOnlyPerson recipientOfEmail = model.getFilteredPersonList().get(INDEX_FIRST_PERSON.getZeroBased());
        SendEmailCommand sendEmailCommand = prepareCommand(INDEX_FIRST_PERSON, VALID_EMAIL_SUBJECT,
                VALID_EMAIL_BODY);

        String expectedMessage = String.format(SendEmailCommand.MESSAGE_SUCCESS);

        ModelManager expectedModel = new ModelManager(model.getAddressBook(), new UserPrefs());
        expectedModel.sendMessage(getGmailService(), EMAIL_SENDER,
                getValidEmail(recipientOfEmail.getEmail().toString(), VALID_EMAIL_SUBJECT, VALID_EMAIL_BODY));

        assertCommandSuccess(sendEmailCommand, model, expectedMessage, expectedModel);
    }

    @Test
    public void execute_validIndexFilteredList_success() throws Exception {
        showFirstPersonOnly(model);

        ReadOnlyPerson recipientOfEmail = model.getFilteredPersonList().get(INDEX_FIRST_PERSON.getZeroBased());
        SendEmailCommand sendEmailCommand = prepareCommand(INDEX_FIRST_PERSON, VALID_EMAIL_SUBJECT,
                VALID_EMAIL_BODY);

        String expectedMessage = String.format(SendEmailCommand.MESSAGE_SUCCESS);

        ModelManager expectedModel = new ModelManager(model.getAddressBook(), new UserPrefs());
        expectedModel.sendMessage(getGmailService(), EMAIL_SENDER,
                getValidEmail(recipientOfEmail.getEmail().toString(), VALID_EMAIL_SUBJECT, VALID_EMAIL_BODY));

        assertCommandSuccess(sendEmailCommand, model, expectedMessage, expectedModel);

    }
/*
    @Test
    public void execute_invalidIndexUnfilteredList_throwsCommandException() throws Exception {
        Index outOfBoundIndex = Index.fromOneBased(model.getFilteredPersonList().size() + 1);
        SendEmailCommand sendEmailCommand = prepareCommand(outOfBoundIndex, VALID_EMAIL_SUBJECT, VALID_EMAIL_BODY);

        assertCommandFailure(sendEmailCommand, model, Messages.MESSAGE_INVALID_PERSON_DISPLAYED_INDEX);
    }

    @Test
    public void execute_invalidIndexFilteredList_throwsCommandException() {
        showFirstPersonOnly(model);

        Index outOfBoundIndex = INDEX_SECOND_PERSON;
        // ensures that outOfBoundIndex is still in bounds of address book list
        assertTrue(outOfBoundIndex.getZeroBased() < model.getAddressBook().getPersonList().size());

        SendEmailCommand sendEmailCommand = prepareCommand(outOfBoundIndex, VALID_EMAIL_SUBJECT, VALID_EMAIL_BODY);

        assertCommandFailure(sendEmailCommand, model, Messages.MESSAGE_INVALID_PERSON_DISPLAYED_INDEX);
    }
*/
    //Email subject and email body specified
    @Test
    public void execute_allEmailFieldsSpecifiedUnfilteredList_success() throws Exception {
        ReadOnlyPerson recipientOfEmail = model.getFilteredPersonList().get(INDEX_FIRST_PERSON.getZeroBased());
        SendEmailCommand sendEmailCommand = prepareCommand(INDEX_FIRST_PERSON, VALID_EMAIL_SUBJECT,
                VALID_EMAIL_BODY);

        String expectedMessage = String.format(SendEmailCommand.MESSAGE_SUCCESS);

        ModelManager expectedModel = new ModelManager(model.getAddressBook(), new UserPrefs());
        expectedModel.sendMessage(getGmailService(), EMAIL_SENDER,
                getValidEmail(recipientOfEmail.getEmail().toString(), VALID_EMAIL_SUBJECT, VALID_EMAIL_BODY));

        assertCommandSuccess(sendEmailCommand, model, expectedMessage, expectedModel);
    }

    //Only index and email subject specified
    @Test
    public void execute_EmailSubjectSpecifiedUnfilteredList_success() throws Exception {
        ReadOnlyPerson recipientOfEmail = model.getFilteredPersonList().get(INDEX_FIRST_PERSON.getZeroBased());
        SendEmailCommand sendEmailCommand = prepareCommand(INDEX_FIRST_PERSON, VALID_EMAIL_SUBJECT,
                "");

        String expectedMessage = String.format(SendEmailCommand.MESSAGE_SUCCESS);

        ModelManager expectedModel = new ModelManager(model.getAddressBook(), new UserPrefs());
        expectedModel.sendMessage(getGmailService(), EMAIL_SENDER,
                getValidEmail(recipientOfEmail.getEmail().toString(), VALID_EMAIL_SUBJECT, ""));

        assertCommandSuccess(sendEmailCommand, model, expectedMessage, expectedModel);
    }

    //Only index and email body specified
    @Test
    public void execute_EmailBodySpecifiedUnfilteredList_success() throws Exception {
        ReadOnlyPerson recipientOfEmail = model.getFilteredPersonList().get(INDEX_FIRST_PERSON.getZeroBased());
        SendEmailCommand sendEmailCommand = prepareCommand(INDEX_FIRST_PERSON, "", VALID_EMAIL_BODY);

        String expectedMessage = String.format(SendEmailCommand.MESSAGE_SUCCESS);

        ModelManager expectedModel = new ModelManager(model.getAddressBook(), new UserPrefs());
        expectedModel.sendMessage(getGmailService(), EMAIL_SENDER,
                getValidEmail(recipientOfEmail.getEmail().toString(), "", VALID_EMAIL_BODY));

        assertCommandSuccess(sendEmailCommand, model, expectedMessage, expectedModel);
    }

    //Both email body and email subject not specified
    @Test
    public void execute_noFieldSpecifiedUnfilteredList_success() throws Exception {
        ReadOnlyPerson recipientOfEmail = model.getFilteredPersonList().get(INDEX_FIRST_PERSON.getZeroBased());
        SendEmailCommand sendEmailCommand = prepareCommand(INDEX_FIRST_PERSON, "", "");

        String expectedMessage = String.format(SendEmailCommand.MESSAGE_SUCCESS);

        ModelManager expectedModel = new ModelManager(model.getAddressBook(), new UserPrefs());
        expectedModel.sendMessage(getGmailService(), EMAIL_SENDER,
                getValidEmail(recipientOfEmail.getEmail().toString(), "", ""));

        assertCommandSuccess(sendEmailCommand, model, expectedMessage, expectedModel);
    }

    /**
     * Returns a {@code SendEmailCommand} with the parameter {@code index, emailSubject, emailBody}.
     */
    private SendEmailCommand prepareCommand(Index index, String emailSubject, String emailBody) {
        SendEmailCommand sendEmailCommand = new SendEmailCommand(index, emailSubject, emailBody);
        sendEmailCommand.setData(model, new CommandHistory(), new UndoRedoStack());
        return sendEmailCommand;
    }
}
