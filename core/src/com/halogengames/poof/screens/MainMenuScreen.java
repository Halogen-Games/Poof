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
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.halogengames.poof.dataLoaders.GameData;
import com.halogengames.poof.Poof;

/**
 * Created by Rohit on 01-08-2017.
 *
 * Opening screen for the game
 */

public class MainMenuScreen implements Screen {

    //the game for sprite batch
    private Poof game;

    //To add the buttons on the screen
    private Stage stage;

    private Texture titleTex;
    private float titleWidth;
    private float titleHeight;

    private ImageButton playButton;
    private ImageButton helpButton;
    private ImageButton hallOfFameButton;
    private ImageButton optionsButton;
    private ImageButton shopButton;

    private float playButtSize;
    private float smallButtSize;


    //Constructor
    public MainMenuScreen(Poof game){
        this.game = game;

        //start music
        game.soundManager.mainMenuMusic.play();

        //stage
        stage = new Stage(Poof.VIEW_PORT, game.batch);

        //Title Label
        titleTex = game.assetManager.mainMenuTitleTex;
        titleWidth = Poof.V_WIDTH*2/3;
        titleHeight = titleTex.getHeight()*titleWidth/titleTex.getWidth();

        //Adding buttons
        playButton = new ImageButton(this.game.assetManager.mainMenuPlayButtonDrawable);
        helpButton = new ImageButton(this.game.assetManager.mainMenuHelpButtonDrawable);
        hallOfFameButton = new ImageButton(this.game.assetManager.mainMenuFameButtonDrawable);
        optionsButton = new ImageButton(this.game.assetManager.mainMenuOptionsButtonDrawable);
        shopButton = new ImageButton(this.game.assetManager.mainMenuShopButtonDrawable);
        addUIListeners();

        playButtSize = Poof.V_WIDTH/3;
        smallButtSize = Poof.V_WIDTH/5;

        //set size
        playButton.setSize(playButtSize,playButtSize);
        helpButton.setSize(smallButtSize,smallButtSize);
        hallOfFameButton.setSize(smallButtSize,smallButtSize);
        optionsButton.setSize(smallButtSize,smallButtSize);
        shopButton.setSize(smallButtSize,smallButtSize);

        //set positions
        playButton.setPosition(Poof.VIEW_PORT.getWorldWidth()/2 - playButton.getWidth()/2, Poof.VIEW_PORT.getWorldHeight()/2.5f - playButton.getHeight()/2);
        optionsButton.setPosition(Poof.VIEW_PORT.getWorldWidth()*1/5 - hallOfFameButton.getWidth()/2, playButton.getY() - optionsButton.getHeight()*1f);
        helpButton.setPosition(Poof.VIEW_PORT.getWorldWidth()*2/4 - hallOfFameButton.getWidth()/2, playButton.getY() - helpButton.getHeight()*1.5f);
        //shopButton.setPosition(Poof.VIEW_PORT.getWorldWidth()*4/6 - hallOfFameButton.getWidth()/2, playButton.getY() - helpButton.getHeight()*2f);
        hallOfFameButton.setPosition(Poof.VIEW_PORT.getWorldWidth()*4/5 - hallOfFameButton.getWidth()/2, playButton.getY() - hallOfFameButton.getHeight()*1f);

        stage.addActor(hallOfFameButton);
        stage.addActor(optionsButton);
        stage.addActor(helpButton);
        stage.addActor(playButton);
        //stage.addActor(shopButton);

        //to allow stage to identify events
        Gdx.input.setInputProcessor(stage);
    }

    private void addUIListeners(){
        playButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                game.soundManager.playButtonTap();
                openGameModeSelect();
            }
        });

        helpButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                game.soundManager.playButtonTap();
                openHelp();
            }
        });

        optionsButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                game.soundManager.playButtonTap();
                openOptions();
            }
        });

        hallOfFameButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                game.soundManager.playButtonTap();
                openHallOfFame();
            }
        });

        shopButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                game.soundManager.playButtonTap();
                openShop();
            }
        });

        stage.addListener(new InputListener(){
            @Override
            public boolean keyDown(InputEvent event, int keycode) {
                if(keycode == Input.Keys.BACK){
                    dispose();
                    Gdx.app.exit();
                    return true;
                }
                return false;
            }
        });
    }

    private void openGameModeSelect(){
        Gdx.input.setInputProcessor(null);
        game.setScreen(new GameModeSelectScreen(game,this));
    }

    private void openHelp(){
        Gdx.input.setInputProcessor(null);
        game.setScreen(new HelpScreen(game,this));
    }

    private void openOptions(){
        Gdx.input.setInputProcessor(null);
        game.setScreen(new OptionsScreen(game,this));
    }

    private void openShop(){
        Gdx.input.setInputProcessor(null);
        game.setScreen(new ShopScreen(game,this));
    }

    private void openHallOfFame(){
        //no need to remove input processor as playgames work correctly
        game.leaderboardManager.showLeaderBoard();
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
    public void resume() {
        Gdx.input.setInputProcessor(stage);
    }

    @Override
    public void hide() {

    }

    @Override
    public void dispose() {
        System.out.println("Main Menu Screen Disposed");
        stage.dispose();
    }
}
