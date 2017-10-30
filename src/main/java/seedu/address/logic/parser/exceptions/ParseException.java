package seedu.address.logic.parser.exceptions;

import static seedu.address.commons.core.Messages.MESSAGE_MISSING_SOUND;

import seedu.address.Sound;
import seedu.address.commons.exceptions.IllegalValueException;
import seedu.address.logic.commands.exceptions.CommandException;

//@@author justintkj
/**
 * Represents a parse error encountered by a parser.
 */
public class ParseException extends IllegalValueException {

    public ParseException(String message) {
        super(Sound.FileExist() + message);
        if(!Sound.FileExist().equals(MESSAGE_MISSING_SOUND)) {
            Sound.music();
        }
    }

    public ParseException(String message, Throwable cause) {
        super(Sound.FileExist() + message, cause);
        if(!Sound.FileExist().equals(MESSAGE_MISSING_SOUND)) {
            Sound.music();
        }
    }
}
