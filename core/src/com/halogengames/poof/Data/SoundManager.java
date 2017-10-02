package com.halogengames.poof.Data;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;

/**
 * Created by Rohit on 29-09-2017.
 */

public class SoundManager {
    //Music
    public static Music mainMenuMusic;
    public static Music playMusic;
    public static float musicVolume;

    //Sound
    public static Sound blockTouched;
    public static Sound wrongMove;
    public static Sound blocksRemoved;
    public static Sound buttonTap;
    public static float soundVolume;

    public static void init(){
        soundVolume = GameData.prefs.getFloat("soundVolume", 1.0f);
        musicVolume = GameData.prefs.getFloat("musicVolume", 1.0f);

        mainMenuMusic = Gdx.audio.newMusic(Gdx.files.internal("music/cute.mp3"));
        mainMenuMusic.setVolume(musicVolume);
        mainMenuMusic.setLooping(true);

        playMusic = Gdx.audio.newMusic(Gdx.files.internal("music/enigmatic.mp3"));
        playMusic.setVolume(musicVolume);
        playMusic.setLooping(true);

        blockTouched = Gdx.audio.newSound(Gdx.files.internal("sound/blop.mp3"));
        wrongMove = Gdx.audio.newSound(Gdx.files.internal("sound/error.wav"));
        blocksRemoved = Gdx.audio.newSound(Gdx.files.internal("sound/confirm.wav"));
        buttonTap = Gdx.audio.newSound(Gdx.files.internal("sound/switchOn.wav"));
    }

    public static void playBlockTouched(){
        blockTouched.play(soundVolume);
    }

    public static void playWrongMove(){
        wrongMove.play(soundVolume);
    }

    public static void playBlocksRemoved(){
        blocksRemoved.play(soundVolume);
    }

    public static void playButtonTap(){
        buttonTap.play(soundVolume);
    }

    public static void dispose(){
        mainMenuMusic.dispose();
        playMusic.dispose();

        blockTouched.dispose();
        wrongMove.dispose();
        blocksRemoved.dispose();
        buttonTap.dispose();
    }
}
