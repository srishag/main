package seedu.address.logic.parser;

import static seedu.address.commons.core.Messages.MESSAGE_INVALID_COMMAND_FORMAT;

import java.util.ArrayList;

import seedu.address.logic.commands.FindPersonsWithTagsCommand;
import seedu.address.logic.parser.exceptions.ParseException;
import seedu.address.model.person.PersonContainsTagsPredicate;

public class FindPersonsWithTagsCommandParser {

    public FindPersonsWithTagsCommand parse(String args) throws ParseException {
        String trimmedArgs = args.trim();

        if(trimmedArgs.isEmpty()){
            throw new ParseException(String.format(MESSAGE_INVALID_COMMAND_FORMAT,
                    FindPersonsWithTagsCommand.MESSAGE_USAGE));
        }

        String[] tagKeywords = trimmedArgs.split("\\s+");
        ArrayList<String> tagKeywordsImproved = getImprovedList(tagKeywords);

        return new FindPersonsWithTagsCommand(new PersonContainsTagsPredicate(tagKeywordsImproved));
    }

    private ArrayList<String> getImprovedList(String[] originalKeywords){
        ArrayList<String> improvedListOfKeywords = new ArrayList<String>();
        for(String originalKeyword : originalKeywords){
            improvedListOfKeywords.add(originalKeyword);
            if(originalKeyword.endsWith("s")){
                improvedListOfKeywords.add(originalKeyword.substring(0, originalKeyword.length()-1));
            }
            else{
                improvedListOfKeywords.add(originalKeyword.concat("s"));
            }
        }
        return improvedListOfKeywords;
    }
}
