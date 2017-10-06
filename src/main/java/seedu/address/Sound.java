package seedu.address;
import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.logging.Logger;

import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import seedu.address.commons.core.LogsCenter;

public class Sound {
    public static final int FIRSTSONG = 0;
    public static final int ONE_LESS = 1;
    public static String name = "Sound";
    private static final Logger logger = LogsCenter.getLogger(name);
    public static ArrayList<String> musicList = new ArrayList<String>(Arrays.asList("FurElise.mp3",
            "KissTheRain.mp3"));
    public static int curr = 0;
    public static final int NEXTSONG = curr + 1;
    public static String bip;
    public static Media hit;
    public static MediaPlayer mediaPlayer;

    public static String currSong() {
        return musicList.get(curr);
    }

    public static void next() {
        if(curr < musicList.size() - ONE_LESS) {
            curr = NEXTSONG;
        }
        else {
            curr = FIRSTSONG;
        }
        if(mediaPlayer!=null) {
            mediaPlayer.stop();
            bip = musicList.get(curr);
            hit = new Media(new File(bip).toURI().toString());
            mediaPlayer = new MediaPlayer(hit);
            mediaPlayer.play();
        }
    }

    public static void music() {
        try {
            bip = musicList.get(curr);
            hit = new Media(new File(bip).toURI().toString());
            mediaPlayer = new MediaPlayer(hit);
            mediaPlayer.play();
        } catch(Exception ex) {
            logger.info("Error with playing sound.");
            ex.printStackTrace();
        }
    }
}
