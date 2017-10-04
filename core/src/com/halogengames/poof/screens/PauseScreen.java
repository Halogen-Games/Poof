package com.halogengames.poof.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.utils.Align;
import com.halogengames.poof.Data.AssetManager;
import com.halogengames.poof.Data.SoundManager;
import com.halogengames.poof.Poof;
import com.halogengames.poof.libs.MotionEngine;

/**
 * Defines the pause screen
 */

class PauseScreen implements Screen {

    //for renderer handles
    private final Poof game;

    //to return back to the game
    private final Screen playScr;

    //Font
    private final Stage stage;

    //buttons
    private final TextButton resumeButton;
    private final TextButton optionsButton;
    private final TextButton mainMenuButton;

    PauseScreen(Poof game, PlayScreen scr){
        this.playScr = scr;
        this.game = game;

        stage = new Stage(Poof.VIEW_PORT, game.batch);

        Table table = new Table();
        table.bottom();
        table.setPosition(0, Poof.V_HEIGHT/10);
        table.setFillParent(true);

        //Title Label
        Label title = new Label("Paused!!!", AssetManager.pauseTitleStyle);
        title.setPosition(Poof.VIEW_PORT.getWorldWidth()/2, Poof.VIEW_PORT.getWorldHeight()*0.9f - title.getHeight(), Align.center);

        //Adding buttons
        resumeButton = new TextButton("Resume", AssetManager.pauseButtonStyle);
        optionsButton = new TextButton("Options", AssetManager.pauseButtonStyle);
        mainMenuButton = new TextButton("Main Menu", AssetManager.pauseButtonStyle);
        addUIListeners();

        //adding buttons to table and table to stage
        table.add(resumeButton);
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
                resumeGame();
            }
        });

        optionsButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                openOptions();
            }
        });

        mainMenuButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                openMainMenu();
            }
        });
    }

    private void resumeGame(){
        Gdx.input.setInputProcessor(null);
        SoundManager.playButtonTap();
        dispose();
        playScr.resume();
    }

    private void openOptions(){
        Gdx.input.setInputProcessor(null);
        SoundManager.playButtonTap();
        MotionEngine.fadeOut(stage.getRoot(),this,new OptionsScreen(game,this),false);
    }

    private void openMainMenu(){
        Gdx.input.setInputProcessor(null);
        SoundManager.playButtonTap();
        playScr.dispose();
        MotionEngine.fadeOut(stage.getRoot(),this,new MainMenuScreen(game),true);
    }

    @Override
    public void show() {
        Gdx.input.setInputProcessor(stage);
        MotionEngine.fadeIn(stage.getRoot());
    }

    @Override
    public void render(float delta) {
        stage.act();

        Gdx.gl.glClearColor(1,1,1,1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        stage.draw();
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
        System.out.println("Pause Screen Disposed");
        stage.dispose();
    }
}
