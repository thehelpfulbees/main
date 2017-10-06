package seedu.address.logic.commands;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class NextSongCommandTest {

    @Test
    public void execute() {
        NextSongCommand test = new NextSongCommand();
        CommandResult result = test.execute();
        assertEquals("Next Song Playing..FurElise.mp3", result.feedbackToUser);
    }
}
