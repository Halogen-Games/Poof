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
import com.halogengames.poof.dataLoaders.AssetManager;
import com.halogengames.poof.dataLoaders.GameData;
import com.halogengames.poof.dataLoaders.SoundManager;
import com.halogengames.poof.Poof;

/**
 * Created by Rohit on 20-05-2018.
 *
 * Screen to select the difficulty
 */

class LevelSelectScreen implements Screen{
    //the game for sprite batch
    private Poof game;
    private Screen prevScreen;

    //To add the buttons on the screen
    private Stage stage;
    private TextButton easyButton;
    private TextButton mediumButton;
    private TextButton hardButton;
    private TextButton backButton;

    //Constructor
    LevelSelectScreen(Poof game, Screen prevScr){
        this.game = game;
        this.prevScreen = prevScr;

        //stage
        stage = new Stage(Poof.VIEW_PORT, game.batch);

        //create a table to position objects on stage
        Table table = new Table();
        table.bottom();
        table.setPosition(0, Poof.V_HEIGHT/10);
        table.setFillParent(true);

        //Title Label
        Label title = new Label("Select\nDifficulty", AssetManager.levelSelectTitleStyle);
        title.setPosition(Poof.VIEW_PORT.getWorldWidth()/2, Poof.VIEW_PORT.getWorldHeight()*0.9f - title.getHeight(), Align.center);
        title.setAlignment(Align.center);

        //Adding buttons
        easyButton = new TextButton("Easy", AssetManager.levelSelectButtonStyle);
        mediumButton = new TextButton("Medium", AssetManager.levelSelectButtonStyle);
        hardButton = new TextButton("Hard", AssetManager.levelSelectButtonStyle);
        backButton = new TextButton("Back", AssetManager.levelSelectButtonStyle);
        addUIListeners();

        //adding buttons to table and table to stage
        table.add(easyButton);
        table.row();
        table.add(mediumButton);
        table.row();
        table.add(hardButton);
        table.row();
        table.add(backButton).padTop(Poof.V_HEIGHT/10);

        stage.addActor(title);
        stage.addActor(table);

        //to allow stage to identify events
        Gdx.input.setInputProcessor(stage);
    }

    private void addUIListeners(){
        easyButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                SoundManager.playButtonTap();
                GameData.setLevel("easy");
                startGame();
            }
        });

        mediumButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                SoundManager.playButtonTap();
                GameData.setLevel("medium");
                startGame();
            }
        });

        hardButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                SoundManager.playButtonTap();
                GameData.setLevel("hard");
                startGame();
            }
        });

        backButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                Gdx.input.setInputProcessor(null);
                SoundManager.playButtonTap();
                dispose();
                game.setScreen(prevScreen);
            }
        });
    }

    private void startGame(){
        Gdx.input.setInputProcessor(null);
        SoundManager.mainMenuMusic.stop();
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

        Poof.bg.render(delta);

        stage.draw();
    }

    @Override
    public void resize(int width, int height) {
        Poof.VIEW_PORT.update( width, height);
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
