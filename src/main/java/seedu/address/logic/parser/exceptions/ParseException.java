package seedu.address.logic.parser.exceptions;

import static seedu.address.commons.core.Messages.MESSAGE_MISSING_SOUND;

import seedu.address.Sound;
import seedu.address.commons.exceptions.IllegalValueException;

//@@author justintkj
/**
 * Represents a parse error encountered by a parser.
 */
public class ParseException extends IllegalValueException {

    public ParseException(String message) {
        super(Sound.exist() + message);
        if (!Sound.exist().equals(MESSAGE_MISSING_SOUND)) {
            Sound.music();
        }
    }

    public ParseException(String message, Throwable cause) {
        super(Sound.exist() + message, cause);
        if (!Sound.exist().equals(MESSAGE_MISSING_SOUND)) {
            Sound.music();
        }
    }
}
