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
 * Defines the pause screen
 */

class PauseScreen implements Screen {

    //for renderer handles
    private Poof game;

    //to return back to the game
    private Screen playScr;

    //Font
    private Stage stage;

    //buttons
    private TextButton resumeButton;
    private TextButton restartButton;
    private TextButton optionsButton;
    private TextButton mainMenuButton;

    PauseScreen(Poof game, PlayScreen scr){
        this.playScr = scr;
        this.game = game;

        stage = new Stage(Poof.VIEW_PORT, game.batch);

        Table table = new Table();
        table.bottom();
        table.setPosition(0, Poof.BANNER_AD_SIZE);
        table.setFillParent(true);

        //Title Label
        Label title = new Label("Paused!!!", this.game.assetManager.pauseTitleStyle);
        title.setPosition(Poof.VIEW_PORT.getWorldWidth()/2, Poof.VIEW_PORT.getWorldHeight()*0.9f - title.getHeight(), Align.center);

        //Adding buttons
        resumeButton = new TextButton("Resume", this.game.assetManager.pauseButtonStyle);
        restartButton = new TextButton("Restart", this.game.assetManager.pauseButtonStyle);
        optionsButton = new TextButton("Options", this.game.assetManager.pauseButtonStyle);
        mainMenuButton = new TextButton("Main Menu", this.game.assetManager.pauseButtonStyle);
        addUIListeners();

        //adding buttons to table and table to stage
        table.add(resumeButton);
        table.row();
        table.add(restartButton);
        table.row();
        table.add(optionsButton);
        table.row();
        table.add(mainMenuButton);
        stage.addActor(title);
        stage.addActor(table);

        //to allow stage to identify events
        Gdx.input.setInputProcessor(stage);
    }

    private void addUIListeners(){
        resumeButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                game.soundManager.playButtonTap();
                resumeGame();
            }
        });

        restartButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                game.soundManager.playButtonTap();
                restartGame();
            }
        });

        optionsButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                game.soundManager.playButtonTap();
                openOptions();
            }
        });

        mainMenuButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                game.soundManager.playButtonTap();
                openMainMenu();
            }
        });
    }

    private void resumeGame(){
        Gdx.input.setInputProcessor(null);
        dispose();
        playScr.resume();
    }

    private void restartGame(){
        //todo: add confirmation dialogue (Possibly needs sprite queue)
        Gdx.input.setInputProcessor(null);
        game.soundManager.playMusic.stop();
        playScr.dispose();
        dispose();
        game.setScreen(new PlayScreen(game));
    }

    private void openOptions(){
        Gdx.input.setInputProcessor(null);
        game.setScreen(new OptionsScreen(game,this));
    }

    private void openMainMenu(){
        Gdx.input.setInputProcessor(null);
        game.soundManager.playMusic.stop();
        playScr.dispose();
        dispose();
        game.setScreen(new MainMenuScreen(game));
    }

    @Override
    public void show() {
        Gdx.input.setInputProcessor(stage);
        stage.getRoot().setColor(1,1,1,0);
        stage.getRoot().addAction(Actions.fadeIn(0.2f));
    }

    @Override
    public void render(float delta) {
        stage.act();

        Gdx.gl.glClearColor(GameData.clearColor.r, GameData.clearColor.g, GameData.clearColor.b, GameData.clearColor.a);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        Poof.bg.render(delta);

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
        System.out.println("Pause Screen Disposed");
        stage.dispose();
    }
}
