package com.halogengames.poof.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.halogengames.poof.Data.AssetManager;
import com.halogengames.poof.Data.GameData;
import com.halogengames.poof.Poof;

import sun.rmi.runtime.Log;

/**
 * Created by Rohit on 22-10-2017.
 *
 * Defines the splash screen with company logo
 */

public class SplashScreen implements Screen {

    private Poof game;
    private Texture logo;
    private float logoWidth;
    private float logoHeight;
    private float margin;

    private boolean isRendered;
    private boolean isInit;
    private float splashDuration;
    private float elapsed;

    public SplashScreen(Poof game){
        this.game = game;

        //load assets
        logo = new Texture("common/splash.png");

        isRendered = false;
        isInit = false;

        splashDuration = 4 ;
        elapsed = 0;
    }

    @Override
    public void show() {

    }

    @Override
    public void render(float delta) {
        if(elapsed > splashDuration){
            game.setScreen(new MainMenuScreen(game));
        }

        Gdx.gl.glClearColor(GameData.clearColor.r, GameData.clearColor.g, GameData.clearColor.b, GameData.clearColor.a);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        game.batch.setProjectionMatrix(Poof.CAM.combined);

        margin = Poof.VIEW_PORT.getWorldWidth()/6;
        logoWidth = Poof.VIEW_PORT.getWorldWidth() - 2*margin;
        logoHeight = logoWidth*logo.getHeight()/logo.getWidth();

        game.batch.begin();
        game.batch.draw(logo,margin,Poof.VIEW_PORT.getWorldHeight()/2 - logoHeight/2,logoWidth,logoHeight);
        game.batch.end();

        elapsed += delta;
        isRendered = true;
    }

    @Override
    public void resize(int width, int height) {
        Poof.VIEW_PORT.update(width, height);
        System.out.println(width);
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
