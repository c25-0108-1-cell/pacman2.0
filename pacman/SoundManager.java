import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;

public class SoundManager {
    public static void playSound(String fileName) {
        try {
            AudioInputStream audioInputStream =
                    AudioSystem.getAudioInputStream(SoundManager.class.getResource("/sounds/" + fileName));
            Clip clip = AudioSystem.getClip();
            clip.open(audioInputStream);
            clip.start();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}