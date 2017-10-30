package seedu.address;

import java.io.File;
import java.util.logging.Logger;

import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import seedu.address.commons.core.LogsCenter;

//author @@justintkj
/**
 * The Playlist for the background music.
 */
public class Sound {
    private static final Logger logger = LogsCenter.getLogger("Error Sound");
    public static final String SOUND_ERROR = "Error with playing sound.";
    public static final String ERRORSOUND_PATH = "src/main/resources/ErrorSound.mp3";
    public static final String ERRORSOUND_MISSING_MESSAGE = "ErrorSound.mp3 missing,\n";
    public static final String EMPTY_MESSAGE = "";

    private static Boolean ValidPath;
    private static Media hit;
    private static MediaPlayer mediaPlayer;

    /**
     * start playing the first error music on the playlist.
     */
    public static void music() {
        try {
            hit = new Media(new File(ERRORSOUND_PATH).toURI().toString());
            mediaPlayer = new MediaPlayer(hit);
            mediaPlayer.play();
        } catch (Exception ex) {
            logger.info(SOUND_ERROR);
            ex.printStackTrace();
        }
    }

    /**
     * Checks for validity of Autocomplee.xml
     * @return message for user if file is mipossing
     */
    public static String exist() {
        File file = new File(ERRORSOUND_PATH);
        ValidPath = file.exists();
        if (!ValidPath) {
            return ERRORSOUND_MISSING_MESSAGE;
        } else {
            return EMPTY_MESSAGE;
        }
    }
}
