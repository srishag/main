package seedu.address.logic.commands;

import static java.util.Objects.requireNonNull;
import static seedu.address.logic.parser.CliSyntax.PREFIX_FACEBOOKADDRESS;

import java.util.List;

import seedu.address.commons.core.Messages;
import seedu.address.commons.core.index.Index;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.person.FacebookAddress;
import seedu.address.model.person.Person;
import seedu.address.model.person.ReadOnlyPerson;
import seedu.address.model.person.exceptions.DuplicatePersonException;
import seedu.address.model.person.exceptions.PersonNotFoundException;

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

    public static final String MESSAGE_DUPLICATE_PERSON = "This person already exists in the address book.";

    private final Index index;
    private final FacebookAddress facebookAddress;

    public FacebookAddressCommand(Index index, FacebookAddress facebookAddress) {
        requireNonNull(index);
        requireNonNull(facebookAddress);

        this.index = index;
        this.facebookAddress = facebookAddress;
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
                && facebookAddress.equals(e.facebookAddress);
    }

    @Override
    protected CommandResult executeUndoableCommand() throws CommandException {
        List<ReadOnlyPerson> lastShownList = model.getFilteredPersonList();

        if (index.getZeroBased() >= lastShownList.size()) {
            throw new CommandException(Messages.MESSAGE_INVALID_PERSON_DISPLAYED_INDEX);
        }

        ReadOnlyPerson personToEdit = lastShownList.get(index.getZeroBased());
        Person editedPerson = new Person(personToEdit.getName(), personToEdit.getPhone(), personToEdit.getEmail(),
                personToEdit.getAddress(), facebookAddress, personToEdit.getTags());

        try {
            model.updatePerson(personToEdit, editedPerson);
        } catch (DuplicatePersonException dpe) {
            throw new CommandException(MESSAGE_DUPLICATE_PERSON);
        } catch (PersonNotFoundException pnfe) {
            throw new AssertionError("The target person cannot be missing");
        }
        //model.updateFilteredListToShowAll();

        return new CommandResult(generateSuccessMessage(editedPerson));
    }

    /**
     * Generates the success message for completion of a Facebook address update command
     */
    private String generateSuccessMessage(Person editedPerson) {
        String successMessage;
        if (facebookAddress.value.isEmpty()) {
            successMessage = "Facebook address of " + editedPerson.getName().fullName + " removed.";
        } else {
            successMessage = "Facebook address of " + editedPerson.getName().fullName + " updated to "
                    + facebookAddress.value;
        }
        return successMessage;
    }

}
