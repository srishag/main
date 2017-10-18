package seedu.address.logic.commands;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static seedu.address.logic.commands.CommandTestUtil.VALID_FACEBOOKADDRESS_AMY;
import static seedu.address.logic.commands.CommandTestUtil.VALID_FACEBOOKADDRESS_BOB;
import static seedu.address.logic.commands.CommandTestUtil.assertCommandFailure;
import static seedu.address.logic.commands.FacebookAddressCommand.MESSAGE_ARGUMENTS;
import static seedu.address.testutil.TypicalIndexes.INDEX_FIRST_PERSON;
import static seedu.address.testutil.TypicalIndexes.INDEX_SECOND_PERSON;
import static seedu.address.testutil.TypicalPersons.getTypicalAddressBook;

import org.junit.Test;

import seedu.address.commons.core.index.Index;
import seedu.address.logic.CommandHistory;
import seedu.address.logic.UndoRedoStack;
import seedu.address.model.Model;
import seedu.address.model.ModelManager;
import seedu.address.model.UserPrefs;
import seedu.address.model.person.FacebookAddress;

/**
 * Contains integration tests (interaction with the Model) and unit tests for FacebookAddressCommand.
 */
public class FacebookAddressCommandTest {

    private Model model = new ModelManager(getTypicalAddressBook(), new UserPrefs());

    @Test
    public void execute() throws Exception {
        final String facebookAddress = "Some facebook URL";

        assertCommandFailure(prepareCommand(INDEX_FIRST_PERSON, facebookAddress), model,
                String.format(MESSAGE_ARGUMENTS, INDEX_FIRST_PERSON.getOneBased(), facebookAddress));
    }

    @Test
    public void equals() {
        final FacebookAddressCommand standardCommand =
                new FacebookAddressCommand(INDEX_FIRST_PERSON, new FacebookAddress(VALID_FACEBOOKADDRESS_AMY));

        // same values -> returns true
        FacebookAddressCommand commandWithSameValues =
                new FacebookAddressCommand(INDEX_FIRST_PERSON, new FacebookAddress(VALID_FACEBOOKADDRESS_AMY));
        assertTrue(standardCommand.equals(commandWithSameValues));

        // same object -> returns true
        assertTrue(standardCommand.equals(standardCommand));

        // null -> returns false
        assertFalse(standardCommand.equals(null));

        // different types -> returns false
        assertFalse(standardCommand.equals(new ClearCommand()));

        // different index -> returns false
        assertFalse(standardCommand.equals(new FacebookAddressCommand(
                INDEX_SECOND_PERSON, new FacebookAddress(VALID_FACEBOOKADDRESS_AMY))));

        // different facebook address -> returns false
        assertFalse(standardCommand.equals(new FacebookAddressCommand(
                INDEX_FIRST_PERSON, new FacebookAddress(VALID_FACEBOOKADDRESS_BOB))));
    }

    /**
     * Returns a {@code FacebookAddressCommand}.
     */
    private FacebookAddressCommand prepareCommand(Index index, String facebookAddress) {
        FacebookAddressCommand facebookAddressCommand = new FacebookAddressCommand(
                index, new FacebookAddress(facebookAddress));
        facebookAddressCommand.setData(model, new CommandHistory(), new UndoRedoStack());
        return facebookAddressCommand;
    }
}
