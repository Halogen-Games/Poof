package com.halogengames.poof.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
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

//Todo:Incomplete Class
class GameModeSelectScreen implements Screen{
    //the game for sprite batch
    private Poof game;
    private Screen prevScreen;

    //To add the buttons on the screen
    private Stage stage;
    private TextButton timedButton;
    private TextButton relaxedButton;
    private TextButton backButton;

    //Constructor
    GameModeSelectScreen(Poof game, Screen prevScr){
        this.game = game;
        this.prevScreen = prevScr;

        //stage
        stage = new Stage(Poof.VIEW_PORT, game.batch);

        //create a table to position objects on stage
        Table table = new Table();
        table.bottom();
        table.setPosition(0, Poof.BANNER_AD_SIZE);
        table.setFillParent(true);

        //Title Label
        Label title = new Label("Select\nGame Mode", this.game.assetManager.levelSelectTitleStyle);
        title.setPosition(Poof.VIEW_PORT.getWorldWidth()/2, Poof.VIEW_PORT.getWorldHeight()*0.9f - title.getHeight(), Align.center);
        title.setAlignment(Align.center);

        //Adding buttons
        timedButton = new TextButton("Timed", this.game.assetManager.levelSelectButtonStyle);
        relaxedButton = new TextButton("Relaxed", this.game.assetManager.levelSelectButtonStyle);
        TextButton comingSoonButton = new TextButton("Coming Soon...", this.game.assetManager.levelSelectGreyedButtonStyle);
        backButton = new TextButton("Back", this.game.assetManager.levelSelectButtonStyle);
        addUIListeners();

        //adding buttons to table and table to stage
        table.add(timedButton);
        table.row();
        table.add(relaxedButton);
        table.row();
        table.add(comingSoonButton);
        table.row();
        table.add(backButton).padTop(Poof.V_HEIGHT/10);

        stage.addActor(title);
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
                Gdx.input.setInputProcessor(null);
                game.soundManager.playButtonTap();
                dispose();
                game.setScreen(prevScreen);
            }
        });
    }

    private void startGame(){
        Gdx.input.setInputProcessor(null);
        game.soundManager.mainMenuMusic.stop();
        dispose();
        game.setScreen(new PlayScreen(game));
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
        System.out.println("level Select Screen Disposed");
        stage.dispose();
    }
}
