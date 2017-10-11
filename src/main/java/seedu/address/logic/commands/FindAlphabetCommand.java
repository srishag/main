package seedu.address.logic.commands;

import seedu.address.model.person.NameContainsKeywordsPredicate;

/**
 * Finds and lists all persons in address book whose name contains any of the characters.
 * Keyword matching is case insensitive.
 */
public class FindAlphabetCommand extends Command {

    public static final String COMMAND_WORD = "findABC";

    public static final String MESSAGE_USAGE = COMMAND_WORD + ": Finds all persons whose names contain any of "
            + "the sequential alphabets\n"
            + "Parameters: Sequence of characters [MORE_KEYWORDS]...\n";

    private final NameContainsKeywordsPredicate predicate;

    public FindAlphabetCommand(NameContainsKeywordsPredicate predicate) {
        this.predicate = predicate;
    }

    @Override
    public CommandResult execute() {
        model.updateFilteredPersonList(predicate);
        return new CommandResult(getMessageForPersonListShownSummary(model.getFilteredPersonList().size()));
    }

}