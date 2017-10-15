package seedu.address.logic.commands;

import static java.util.Objects.requireNonNull;
import static seedu.address.logic.parser.CliSyntax.PREFIX_FACEBOOKADDRESS;

import seedu.address.commons.core.index.Index;
import seedu.address.logic.commands.exceptions.CommandException;

/**
 * Changes the FacebookAddress of a contact
 */
public class FacebookAddressCommand extends UndoableCommand {

    public static final String COMMAND_WORD = "facebook";

    public static final String MESSAGE_USAGE = COMMAND_WORD + ": Edits the Facebook address of the person specified "
            + "by the index number used in the last person listing. "
            + "Existing Facebook address will be overwritten by the input.\n"
            + "Parameters: INDEX (must be a positive integer) "
            + PREFIX_FACEBOOKADDRESS + "[Facebook URL]\n"
            + "Example: " + COMMAND_WORD + " 1 "
            + PREFIX_FACEBOOKADDRESS + "https://www.facebook.com/(Profile ID)/ .";

    public static final String MESSAGE_ARGUMENTS = "Index: %1$d, Facebook Address: %2$s";

    private final Index index;
    private final String facebookAddressString;

    public FacebookAddressCommand(Index index, String facebookAddressString) {
        requireNonNull(index);
        requireNonNull(facebookAddressString);

        this.index = index;
        this.facebookAddressString = facebookAddressString;
    }

    @Override
    public boolean equals(Object other) {
        // short circuit if same object
        if (other == this) {
            return true;
        }

        // instanceof handles nulls
        if (!(other instanceof FacebookAddressCommand)) {
            return false;
        }

        // state check
        FacebookAddressCommand e = (FacebookAddressCommand) other;
        return index.equals(e.index)
                && facebookAddressString.equals(e.facebookAddressString);
    }

    @Override
    protected CommandResult executeUndoableCommand() throws CommandException {
        throw new CommandException(String.format(MESSAGE_ARGUMENTS, index.getOneBased(), facebookAddressString));
    }

}
