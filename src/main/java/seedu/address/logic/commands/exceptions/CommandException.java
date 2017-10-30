package seedu.address.logic.commands.exceptions;

import static seedu.address.commons.core.Messages.MESSAGE_MISSING_SOUND;

import seedu.address.Sound;

//@@author justintkj
/**
 * Represents an error which occurs during execution of a {@link Command}.
 */
public class CommandException extends Exception {
    public CommandException(String message) {
        super(Sound.FileExist() + message);
        if(!Sound.FileExist().equals(MESSAGE_MISSING_SOUND)) {
            Sound.music();
        }
    }
}
