/**
 *
 * @file        AudioManager
 * @author      Brian McCarthy, 20063914
 * @assignment  Warbirds
 * @brief       Audio manager
 * @notes       DESCRIPTION OF CODE, BUGS, FEATURES, ISSUES, ETC.
 *
 */
package wit.cgd.warbirds.game.util;

import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;

public class AudioManager {
    public static final AudioManager    instance    = new AudioManager();

    private Music                       playingMusic;

    // singleton: prevent instantiation from other classes
    private AudioManager() {}

    /**
     * Plays the sound
     * @param sound
     */
    public void play(Sound sound) {
        play(sound, 1);
    }

    /**
     * Plays the sound
     * @param sound
     * @param volume
     */
    public void play(Sound sound, float volume) {
        play(sound, volume, 1);
    }

    /**
     * Plays the sound
     * @param sound
     * @param volume
     * @param pitch
     */
    public void play(Sound sound, float volume, float pitch) {
        play(sound, volume, pitch, 0);
    }

    /**
     * Plays the sound
     * @param sound
     * @param volume
     * @param pitch
     * @param pan
     */
    public void play(Sound sound, float volume, float pitch, float pan) {
        if (!GamePreferences.instance.sound) return;
        sound.play(GamePreferences.instance.soundVolume * volume, pitch, pan);
    }
    
    /**
     * Play background music
     * @param music
     */
    public void play(Music music) {
        stopMusic();
        playingMusic = music;
        if (GamePreferences.instance.music) {
            music.setLooping(true);
            music.setVolume(GamePreferences.instance.musicVolume);
            music.play();
        }
    }

    /**
     * Stops background music
     */
    public void stopMusic() {
        if (playingMusic != null) playingMusic.stop();
    }

    /**
     * Update volume of background music
     */
    public void onSettingsUpdated() {
        if (playingMusic == null) return;
        playingMusic.setVolume(GamePreferences.instance.musicVolume);
        if (GamePreferences.instance.music) {
            if (!playingMusic.isPlaying()) playingMusic.play();
        } else {
            playingMusic.pause();
        }
    }


}