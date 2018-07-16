package com.halogengames.poof.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.halogengames.poof.dataLoaders.GameData;
import com.halogengames.poof.Poof;
import com.halogengames.poof.sprites.Explosion;

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

    private Label title;

    private TextButton playButton;
    private TextButton helpButton;
    private TextButton exitButton;
    private TextButton optionsButton;

    //Constructor
    public MainMenuScreen(Poof game){
        this.game = game;

        //start music
        game.soundManager.mainMenuMusic.play();

        //stage
        stage = new Stage(Poof.VIEW_PORT, game.batch);

        //Title Label
        title = new Label("Poof", this.game.assetManager.mainMenuTitleStyle);
        title.setPosition(Poof.VIEW_PORT.getWorldWidth()/2 - title.getWidth()/2, Poof.VIEW_PORT.getWorldHeight()*0.9f - title.getHeight());

        //Adding buttons
        playButton = new TextButton("Play", this.game.assetManager.mainMenuButtonStyle);
        helpButton = new TextButton("Help", this.game.assetManager.mainMenuButtonStyle);
        optionsButton = new TextButton("Options", this.game.assetManager.mainMenuButtonStyle);
        exitButton = new TextButton("Quit", this.game.assetManager.mainMenuButtonStyle);
        addUIListeners();

        //set positions
        exitButton.setPosition(Poof.VIEW_PORT.getWorldWidth()/2 - exitButton.getWidth()/2, Poof.BANNER_AD_SIZE);
        optionsButton.setPosition(Poof.VIEW_PORT.getWorldWidth()/2 - optionsButton.getWidth()/2, exitButton.getY()+exitButton.getHeight());
        helpButton.setPosition(Poof.VIEW_PORT.getWorldWidth()/2 - helpButton.getWidth()/2, optionsButton.getY()+optionsButton.getHeight());
        playButton.setPosition(Poof.VIEW_PORT.getWorldWidth()/2 - playButton.getWidth()/2, helpButton.getY()+helpButton.getHeight());


        stage.addActor(title);
        stage.addActor(exitButton);
        stage.addActor(optionsButton);
        stage.addActor(helpButton);
        stage.addActor(playButton);

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

        exitButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                game.soundManager.playButtonTap();
                dispose();
                Gdx.app.exit();
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
        //game.setScreen(new GameModeSelectScreen(game,this));
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

    private void openUpgrades(){
        Gdx.input.setInputProcessor(null);
        game.setScreen(new HelpScreen(game,this));
    }

    @Override
    public void show() {
        Gdx.input.setInputProcessor(stage);
        stage.getRoot().setColor(1,1,1,0);
        stage.getRoot().addAction(Actions.fadeIn(0.2f));
    }

    @Override
    public void render(float delta){
        stage.act(delta);

        Gdx.gl.glClearColor(GameData.clearColor.r, GameData.clearColor.g, GameData.clearColor.b, GameData.clearColor.a);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        game.bg.render(delta);

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
    public void resume() {}

    @Override
    public void hide() {

    }

    @Override
    public void dispose() {
        System.out.println("Main Menu Screen Disposed");
        stage.dispose();
    }
}
