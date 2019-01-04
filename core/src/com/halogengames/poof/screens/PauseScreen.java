package com.halogengames.poof.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.ImageTextButton;
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

    //Title Tex
    private Texture titleTex;
    private float titleWidth;
    private float titleHeight;

    private Stage stage;

    //buttons
    private ImageButton resumeButton;
    private ImageButton restartButton;
    private ImageButton optionsButton;
    private ImageButton mainMenuButton;

    PauseScreen(Poof game, PlayScreen scr){
        this.playScr = scr;
        this.game = game;

        stage = new Stage(Poof.VIEW_PORT, game.batch);

        Table table = new Table();
        table.bottom();
        table.setPosition(0, Poof.BANNER_AD_SIZE);
        table.setFillParent(true);

        //Title Label
        titleTex = game.assetManager.pauseTitleTex;
        titleWidth = Poof.V_WIDTH*2/3;
        titleHeight = titleTex.getHeight()*titleWidth/titleTex.getWidth();

        //Adding buttons
        resumeButton = new ImageButton(this.game.assetManager.UISkin.get("resumeButton", ImageButton.ImageButtonStyle.class));
        restartButton = new ImageButton(this.game.assetManager.UISkin.get("restartButton", ImageButton.ImageButtonStyle.class));
        optionsButton = new ImageButton(this.game.assetManager.UISkin.get("optionsButton", ImageButton.ImageButtonStyle.class));
        mainMenuButton = new ImageButton(this.game.assetManager.UISkin.get("mainMenuButton", ImageButton.ImageButtonStyle.class));
        addUIListeners();

        //assuming all buttons are same size
        float buttWidth = game.assetManager.buttWidth;
        float buttHeight = resumeButton.getHeight()*buttWidth/resumeButton.getWidth();

        //adding buttons to table and table to stage
        table.add(resumeButton).size(buttWidth,buttHeight).padTop(game.assetManager.buttPadding);
        table.row();
        table.add(restartButton).size(buttWidth,buttHeight).padTop(game.assetManager.buttPadding);
        table.row();
        table.add(optionsButton).size(buttWidth,buttHeight).padTop(game.assetManager.buttPadding);
        table.row();
        table.add(mainMenuButton).size(buttWidth,buttHeight).padTop(game.assetManager.buttPadding);
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
    }

    @Override
    public void render(float delta) {
        stage.act();

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
