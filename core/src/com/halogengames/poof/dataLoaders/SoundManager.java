package com.halogengames.poof.dataLoaders;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;

/**
 * Creates one instance of each sound,music and these are shared across all the app elements
 */

public class SoundManager {
    //Asset Manager
    private AssetManager manager;

    //Music
    public Music mainMenuMusic;
    public Music playMusic;

    //Sound
    private Sound blockTouched;
    private Sound wrongMove;
    private Sound blocksRemoved;
    private Sound buttonTap;
    private Sound bombSound;
    public float soundVolume;

    //used for sounds played once every move
    private boolean soundsIdle;

    public SoundManager(AssetManager manager){
        this.manager = manager;

        soundVolume = GameData.prefs.getFloat("soundVolume", 1.0f);

        manager.load("music/memories.mp3",Music.class);
        manager.load("music/summer.mp3",Music.class);

        manager.load("sound/blop.mp3",Sound.class);
        manager.load("sound/error.wav",Sound.class);
        manager.load("sound/confirm.wav",Sound.class);
        manager.load("sound/switchOn.wav",Sound.class);
        manager.load("sound/cannon.wav",Sound.class);
    }

    public boolean isLoaded(){
        if(manager.update()){
            getAssets();
            return true;
        }
        return false;
    }

    private void getAssets(){
        float musicVolume = GameData.prefs.getFloat("musicVolume", 1.0f);

        mainMenuMusic = manager.get("music/memories.mp3",Music.class);
        mainMenuMusic.setVolume(musicVolume);
        mainMenuMusic.setLooping(true);

        playMusic = manager.get("music/summer.mp3",Music.class);
        playMusic.setVolume(musicVolume);
        playMusic.setLooping(true);

        blockTouched = manager.get("sound/blop.mp3",Sound.class);
        wrongMove = manager.get("sound/error.wav",Sound.class);
        blocksRemoved = manager.get("sound/confirm.wav",Sound.class);
        buttonTap = manager.get("sound/switchOn.wav",Sound.class);

        bombSound = manager.get("sound/cannon.wav",Sound.class);

        soundsIdle = true;
    }

    public void playBlockTouched(){
        blockTouched.play(soundVolume);
    }

    public void playWrongMove(){
        wrongMove.play(soundVolume);
    }

    public void playBlocksRemoved(){
        blocksRemoved.play(soundVolume);
    }

    public void playButtonTap(){
        buttonTap.play(soundVolume);
    }

    public void playShuffleSound(){
        //todo:add shuffle sound here
    }

    public void playExplosionSound(boolean playOncePerMove){
        if(!playOncePerMove || soundsIdle) {
            bombSound.play(soundVolume);
            soundsIdle = false;
        }
    }

    public void setSoundsToIdle(){
        soundsIdle = true;
    }

    public void dispose(){
        mainMenuMusic.dispose();
        playMusic.dispose();

        blockTouched.dispose();
        wrongMove.dispose();
        blocksRemoved.dispose();
        buttonTap.dispose();
    }
}
