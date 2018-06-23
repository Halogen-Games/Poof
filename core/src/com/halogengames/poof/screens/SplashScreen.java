package com.halogengames.poof.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.halogengames.poof.dataLoaders.PoofAssetManager;
import com.halogengames.poof.dataLoaders.GameData;
import com.halogengames.poof.Poof;
import com.halogengames.poof.dataLoaders.SoundManager;

/**
 * Created by Rohit on 22-10-2017.
 *
 * Defines the splash screen with company logo
 */

public class SplashScreen implements Screen {

    private Poof game;
    private Texture logo;

    private float splashDuration;
    private float elapsed;

    private float margin;
    private float targetMargin;
    private float logoWidth;
    private float logoHeight;

    public SplashScreen(Poof game){
        this.game = game;

        //load assets
        logo = new Texture("common/com_logo.png");
        logo.setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);

        splashDuration = 4;
        elapsed = 0;
    }

    @Override
    public void show() {

    }

    private void update(float dt){
        margin += (targetMargin-margin)*dt*1/splashDuration;
        logoWidth = Poof.VIEW_PORT.getWorldWidth() - 2*margin;
        logoHeight = logoWidth*logo.getHeight()/logo.getWidth();

        if(this.game.assetManager.isLoaded() && this.game.soundManager.isLoaded()){
            game.showGDPRConsentPageIfNeeded();
        }
    }

    @Override
    public void render(float delta) {
        update(delta);

        Gdx.gl.glClearColor(GameData.clearColor.r, GameData.clearColor.g, GameData.clearColor.b, GameData.clearColor.a);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        game.batch.setProjectionMatrix(Poof.CAM.combined);

        game.batch.begin();
        game.batch.draw(logo,margin,Poof.VIEW_PORT.getWorldHeight()/2 - logoHeight/2,logoWidth,logoHeight);
        game.batch.end();

        elapsed += delta;
    }

    @Override
    public void resize(int width, int height) {
        Poof.VIEW_PORT.update(width, height);

        margin = 0;
        targetMargin = Poof.VIEW_PORT.getWorldWidth()/6;
        logoWidth = Poof.VIEW_PORT.getWorldWidth() - 2*margin;
        logoHeight = logoWidth*logo.getHeight()/logo.getWidth();
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
