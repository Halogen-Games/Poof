package com.halogengames.poof.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.utils.Align;
import com.halogengames.poof.dataLoaders.PoofAssetManager;
import com.halogengames.poof.dataLoaders.GameData;
import com.halogengames.poof.dataLoaders.SoundManager;
import com.halogengames.poof.Poof;

/**
 * Created by Rohit on 22-10-2017.
 *
 * Screen to show help pages
 */

class HelpScreen implements Screen {

    //the game for sprite batch
    private Poof game;

    //previous screen
    private Screen prevScreen;

    //To add the buttons on the screen
    private Stage stage;
    private TextButton backButton;

    //for animation
    private TextureRegion animTex;
    private float animWidth;
    private float animHeight;

    private float elapsed;

    HelpScreen(Poof game, Screen prevScr){
        this.game = game;
        prevScreen = prevScr;

        elapsed = 0;

        stage = new Stage(Poof.VIEW_PORT, game.batch);

        Table table = new Table();
        table.bottom();
        table.setPosition(0, Poof.BANNER_AD_SIZE);
        table.setFillParent(true);

        //Adding buttons
        backButton = new TextButton("Back", this.game.assetManager.pauseButtonStyle);
        addUIListeners();

        //adding buttons to table and table to stage
        table.add(backButton);
        stage.addActor(table);

        //to allow stage to identify events
        Gdx.input.setInputProcessor(stage);
    }

    private void addUIListeners() {
        backButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                returnToPrevScreen();
            }
        });
    }

    private void returnToPrevScreen(){
        Gdx.input.setInputProcessor(null);
        game.soundManager.playButtonTap();
        dispose();
        game.setScreen(prevScreen);
    }

    @Override
    public void show() {
        stage.getRoot().setColor(1,1,1,0);
        stage.getRoot().addAction(Actions.fadeIn(0.2f));
    }

    private void update(float dt){
        elapsed += dt;
        animTex = this.game.assetManager.helpAnim.getKeyFrame(elapsed);

        animWidth = Poof.VIEW_PORT.getWorldWidth()*0.8f;
        animHeight = animTex.getRegionHeight() * animWidth/animTex.getRegionWidth();
    }

    @Override
    public void render(float delta) {
        update(delta);
        stage.act();

        Gdx.gl.glClearColor(GameData.clearColor.r, GameData.clearColor.g, GameData.clearColor.b, GameData.clearColor.a);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        game.batch.setProjectionMatrix(Poof.CAM.combined);

        game.bg.render(delta);

        game.batch.begin();
        //todo: (Low Priority) remove below hardcoded animWidth/15 and make animations such that their center is centered at screen as well
        game.batch.draw(
                animTex,
                Poof.VIEW_PORT.getWorldWidth()/2 - animWidth/2 + animWidth/15,
                Poof.VIEW_PORT.getWorldHeight()*0.8f - animHeight,
                animWidth,
                animHeight
        );

        this.game.assetManager.helpTextFont.draw(
                game.batch,
                "Drag to select a chain of similar colored blocks to remove them from the board",
                Poof.VIEW_PORT.getWorldWidth()*0.1f,
                Poof.VIEW_PORT.getWorldHeight()*0.7f - animHeight,
                Poof.VIEW_PORT.getWorldWidth()*0.8f,
                Align.center,
                true
        );
        game.batch.end();

        stage.draw();
    }

    @Override
    public void resize(int width, int height) {
        game.resize(width,height);
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
        System.out.println("Help Screen Disposed");
        stage.dispose();
    }
}
