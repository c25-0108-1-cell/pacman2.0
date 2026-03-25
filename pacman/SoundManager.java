import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;

public class SoundManager {
    private static Clip backgroundMusicClip = null;

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

    public static void playLoopingSound(String fileName) {
        try {
            // Stop any existing background music
            stopBackgroundMusic();
            
            AudioInputStream audioInputStream =
                    AudioSystem.getAudioInputStream(SoundManager.class.getResource("/sounds/" + fileName));
            backgroundMusicClip = AudioSystem.getClip();
            backgroundMusicClip.open(audioInputStream);
            backgroundMusicClip.loop(Clip.LOOP_CONTINUOUSLY);
            backgroundMusicClip.start();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void stopBackgroundMusic() {
        if (backgroundMusicClip != null && backgroundMusicClip.isRunning()) {
            backgroundMusicClip.stop();
            backgroundMusicClip.close();
            backgroundMusicClip = null;
        }
    }
}