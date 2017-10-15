package seedu.address.logic.parser;

import static seedu.address.commons.core.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static seedu.address.logic.parser.CliSyntax.PREFIX_FACEBOOKADDRESS;
import static seedu.address.logic.parser.CommandParserTestUtil.assertParseFailure;
import static seedu.address.logic.parser.CommandParserTestUtil.assertParseSuccess;
import static seedu.address.testutil.TypicalIndexes.INDEX_FIRST_PERSON;

import org.junit.Test;

import seedu.address.commons.core.index.Index;
import seedu.address.logic.commands.FacebookAddressCommand;

public class FacebookAddressCommandParserTest {
    private FacebookAddressCommandParser parser = new FacebookAddressCommandParser();

    @Test
    public void parse_indexSpecified_failure() throws Exception {
        final String facebookAddress = "https://www.facebook.com/someaddress/";

        // has facebook address
        Index targetIndex = INDEX_FIRST_PERSON;
        String userInput = targetIndex.getOneBased() + " " + PREFIX_FACEBOOKADDRESS.toString() + " " + facebookAddress;
        FacebookAddressCommand expectedCommand = new FacebookAddressCommand(INDEX_FIRST_PERSON, facebookAddress);
        assertParseSuccess(parser, userInput, expectedCommand);

        // no facebook address
        userInput = targetIndex.getOneBased() + " " + PREFIX_FACEBOOKADDRESS.toString();
        expectedCommand = new FacebookAddressCommand(INDEX_FIRST_PERSON, "");
        assertParseSuccess(parser, userInput, expectedCommand);
    }

    @Test
    public void parse_noFieldSpecified_failure() throws Exception {
        String expectedMessage = String.format(MESSAGE_INVALID_COMMAND_FORMAT, FacebookAddressCommand.MESSAGE_USAGE);

        // nothing at all
        assertParseFailure(parser, FacebookAddressCommand.COMMAND_WORD, expectedMessage);
    }
}
