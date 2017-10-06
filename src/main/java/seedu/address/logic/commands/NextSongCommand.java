package seedu.address.logic.commands;

import seedu.address.Sound;

/**
 * Change the current playing song to the next song
 */

public class NextSongCommand extends Command {

    public static final String COMMAND_WORD = "nextsong";
    public static final String MESSAGE_SUCCESS = "Next Song Playing.." + Sound.currSong();

    public CommandResult execute() {
        Sound.next();
        return new CommandResult(MESSAGE_SUCCESS);
    }

}
