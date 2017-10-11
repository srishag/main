package seedu.address.logic.parser;

import static seedu.address.commons.core.Messages.MESSAGE_INVALID_COMMAND_FORMAT;

import seedu.address.logic.commands.FindPersonsWithTagsCommand;
import seedu.address.logic.parser.exceptions.ParseException;

public class FindPersonsWithTagsCommandParser {

    public FindPersonsWithTagsCommand parse(String args) throws ParseException {
        String trimmedArgs = args.trim();

        if(trimmedArgs.isEmpty()){
            throw new ParseException(String.format(MESSAGE_INVALID_COMMAND_FORMAT,
                    FindPersonsWithTagsCommand.MESSAGE_USAGE));
        }

        String[] tagKeywords = trimmedArgs.split("\\s+");

        return new FindPersonsWithTagsCommand();
    }
}
