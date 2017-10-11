package seedu.address.logic.commands;

import seedu.address.model.person.PersonContainsTagsPredicate;

public class FindPersonsWithTagsCommand extends Command{

    public static final String COMMAND_WORD = "findtags";

    public static final String MESSAGE_USAGE = COMMAND_WORD + ": Finds all persons whose has tags containing any of "
            + "the specified keywords (case-sensitive) and displays them as a list with index numbers.\n"
            + "Parameters: KEYWORD [MORE_KEYWORDS]...\n"
            + "Example: " + COMMAND_WORD + " classmates colleagues";

    public FindPersonsWithTagsCommand(PersonContainsTagsPredicate predicate) {
        this.predicate = predicate;
    }

    private final PersonContainsTagsPredicate predicate;

    @Override
    public CommandResult execute() {
        model.updateFilteredPersonList(predicate);
        return new CommandResult(getMessageForPersonListShownSummary(model.getFilteredPersonList().size()));
    }


}
