package seedu.address.logic.commands;

import static seedu.address.logic.parser.CliSyntax.PREFIX_FACEBOOKADDRESS;

import seedu.address.logic.commands.exceptions.CommandException;

/**
 * Changes the FacebookAddress of a contact
 */
public class FacebookAddressCommand extends UndoableCommand{

    public static final String COMMAND_WORD = "facebook";

    public static final String MESSAGE_USAGE = COMMAND_WORD + "Edits the Facebook address of the person specified "
            + "by the index number used in the last person listing. "
            + "Existing remark will be overwritten by the input.\n"
            + "Parameters: INDEX (must be a positive integer) "
            + PREFIX_FACEBOOKADDRESS + "[Facebook URL]\n"
            + "Example: " + COMMAND_WORD + " 1 "
            + PREFIX_FACEBOOKADDRESS + "https://www.facebook.com/(Profile ID)/ .";

    public static final String MESSAGE_NOT_IMPLEMENTED_YET = "Facebook Command is not implemented yet";

    @Override
    protected CommandResult executeUndoableCommand() throws CommandException{
        throw new CommandException(MESSAGE_NOT_IMPLEMENTED_YET);
    }

}
