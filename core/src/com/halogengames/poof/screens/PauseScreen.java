package com.halogengames.poof.screens;

import com.badlogic.gdx.Screen;
import com.halogengames.poof.Poof;

/**
 * Defines the pause screen
 */

class PauseScreen implements Screen {

    //for renderer handles
    private final Poof game;

    //to return back to the game
    private final PlayScreen playScr;

    PauseScreen(PlayScreen scr, Poof game){
        this.playScr = scr;
        this.game = game;
    }

    @Override
    public void show() {

    }

    @Override
    public void render(float delta) {

    }

    @Override
    public void resize(int width, int height) {

    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void hide() {

    }

    @Override
    public void dispose() {

    }
}
