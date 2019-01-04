package com.halogengames.poof.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.ImageTextButton;
import com.badlogic.gdx.scenes.scene2d.ui.ImageTextButton.ImageTextButtonStyle;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.utils.Align;
import com.halogengames.poof.dataLoaders.PoofAssetManager;
import com.halogengames.poof.dataLoaders.GameData;
import com.halogengames.poof.dataLoaders.SoundManager;
import com.halogengames.poof.Poof;

/**
 * Created by Rohit on 20-05-2018.
 *
 * Screen to select the difficulty
 */

class GameModeSelectScreen implements Screen{
    //the game for sprite batch
    private Poof game;
    private Skin skin;
    private Screen prevScreen;

    private Texture titleTex;
    private float titleWidth;
    private float titleHeight;

    //To add the buttons on the screen
    private Stage stage;
    private ImageButton timedButton;
    private ImageButton relaxedButton;
    private ImageButton backButton;

    //Constructor
    GameModeSelectScreen(Poof game, Screen prevScr){
        this.game = game;
        this.skin = game.getAssetManager().UISkin;
        this.prevScreen = prevScr;

        //stage
        stage = new Stage(Poof.VIEW_PORT, game.batch);

        //create a table to position objects on stage
        Table table = new Table();
        table.bottom();
        table.setPosition(0, Poof.BANNER_AD_SIZE);
        table.setFillParent(true);

        //Title Label
        titleTex = game.assetManager.modeSelectTitleTex;
        titleWidth = Poof.V_WIDTH*2/3;
        titleHeight = titleTex.getHeight()*titleWidth/titleTex.getWidth();

        //Adding buttons
        timedButton = new ImageButton(skin.get("timedButton", ImageButton.ImageButtonStyle.class));
        float timedButtonScale = game.assetManager.buttWidth/timedButton.getWidth();

        relaxedButton = new ImageButton(skin.get("relaxedButton", ImageButton.ImageButtonStyle.class));
        float relaxedButtonScale = game.assetManager.buttWidth/relaxedButton.getWidth();

        backButton = new ImageButton(skin.get("backButton", ImageButton.ImageButtonStyle.class));
        float backButtonScale = game.assetManager.buttWidth/backButton.getWidth();
        addUIListeners();

        //adding buttons to table and table to stage
        table.add(timedButton).size(game.assetManager.buttWidth,timedButtonScale*backButton.getHeight()).padTop(game.assetManager.buttPadding);
        table.row();
        table.add(relaxedButton).size(game.assetManager.buttWidth,relaxedButtonScale*backButton.getHeight()).padTop(game.assetManager.buttPadding);
        table.row();
        table.add(backButton).size(game.assetManager.buttWidth,backButtonScale*backButton.getHeight()).padTop(5*game.assetManager.buttPadding);

        stage.addActor(table);

        //to allow stage to identify events
        Gdx.input.setInputProcessor(stage);
    }

    private void addUIListeners(){
        timedButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                game.soundManager.playButtonTap();
                GameData.setGameMode(GameData.GameMode.Timed);
                startGame();
            }
        });

        relaxedButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                game.soundManager.playButtonTap();
                GameData.setGameMode(GameData.GameMode.Relaxed);
                startGame();
            }
        });

        backButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                goBack();
            }
        });

        stage.addListener(new InputListener(){
            @Override
            public boolean keyDown(InputEvent event, int keycode) {
                if(keycode == Input.Keys.BACK){
                    goBack();
                    return true;
                }
                return false;
            }
        });
    }

    private void startGame(){
        Gdx.input.setInputProcessor(null);
        game.soundManager.mainMenuMusic.stop();
        dispose();
        //fixme: main menu not disposed. Possible memory leak?
        game.setScreen(new PlayScreen(game));
    }

    private void goBack(){
        Gdx.input.setInputProcessor(null);
        game.soundManager.playButtonTap();
        dispose();
        game.setScreen(prevScreen);
    }

    @Override
    public void show() {
        Gdx.input.setInputProcessor(stage);
    }

    @Override
    public void render(float delta){
        stage.act(delta);

        Gdx.gl.glClearColor(GameData.clearColor.r, GameData.clearColor.g, GameData.clearColor.b, GameData.clearColor.a);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        game.bg.render(delta);

        stage.draw();

        game.batch.setProjectionMatrix(Poof.CAM.combined);

        game.batch.begin();
        game.batch.draw(titleTex,Poof.V_WIDTH/2 - titleWidth/2, Poof.V_HEIGHT*0.9f - titleHeight, titleWidth, titleHeight);
        game.batch.end();
    }

    @Override
    public void resize(int width, int height) {
        game.resize(width,height);
    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {}

    @Override
    public void hide() {

    }

    @Override
    public void dispose() {
        System.out.println("level Select Screen Disposed");
        stage.dispose();
    }
}
