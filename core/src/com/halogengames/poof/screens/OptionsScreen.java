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
import com.badlogic.gdx.scenes.scene2d.ui.Slider;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.utils.Align;
import com.halogengames.poof.dataLoaders.GameData;
import com.halogengames.poof.Poof;

/**
 * Defined the options screen
 */

class OptionsScreen implements Screen {

    private Poof game;

    private Screen prevScreen;

    //Title Tex
    private Texture titleTex;
    private float titleWidth;
    private float titleHeight;

    //Sliders
    private Slider musicSlider;
    private Slider soundSlider;

    //To add the buttons on the screen
    private Stage stage;
    private ImageButton backButton;
    private ImageButton privacyButton;

    OptionsScreen(Poof game, Screen prevScr){
        this.game = game;

        this.prevScreen = prevScr;

        //stage
        stage = new Stage(Poof.VIEW_PORT, game.batch);

        //create a table to position objects on stage
        Table table = new Table();
        table.bottom();
        table.setPosition(0, Poof.BANNER_AD_SIZE);
        table.setFillParent(true);

        //Add Label
        titleTex = game.assetManager.optionsTitleTex;
        titleWidth = Poof.V_WIDTH*2/3;
        titleHeight = titleTex.getHeight()*titleWidth/titleTex.getWidth();

        //adding back button
        backButton = new ImageButton(game.assetManager.UISkin.get("backButton", ImageButton.ImageButtonStyle.class));
        float buttonScale = game.assetManager.buttWidth/backButton.getWidth();

        privacyButton = new ImageButton(game.assetManager.UISkin.get("privacyButton", ImageButton.ImageButtonStyle.class));

        //adding sliders
        musicSlider = new Slider(0,1,0.01f,false, this.game.assetManager.optionsMusicSliderStyle);
        musicSlider.setValue(GameData.prefs.getFloat("musicVolume", 1.0f));

        soundSlider = new Slider(0,1,0.2f,false, this.game.assetManager.optionsSoundSliderStyle);
        soundSlider.setValue(GameData.prefs.getFloat("soundVolume", 1.0f));

        addUIListeners();

        //adding buttons to table and table to stage
        table.add(musicSlider).width(Poof.V_WIDTH*0.65f).padBottom(Poof.V_HEIGHT/15);
        table.row();
        table.add(soundSlider).width(Poof.V_WIDTH*0.65f).padBottom(Poof.V_HEIGHT/15);
        if(game.adManager.isEURegion()) {
            table.row();
            table.add(privacyButton).size(buttonScale*privacyButton.getWidth(),buttonScale*privacyButton.getHeight()).padTop(game.assetManager.buttPadding);
        }
        table.row();
        table.add(backButton).size(game.assetManager.buttWidth,buttonScale*backButton.getHeight()).padTop(game.assetManager.buttPadding);
        stage.addActor(table);

        //to allow stage to identify events
        Gdx.input.setInputProcessor(stage);
    }

    @Override
    public void show() {
        Gdx.input.setInputProcessor(stage);
    }

    private void addUIListeners(){
        backButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                returnToPrevScreen();
            }
        });

        privacyButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                openGDPRScreen();
            }
        });

        musicSlider.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                game.soundManager.mainMenuMusic.setVolume(musicSlider.getValue());
                game.soundManager.playMusic.setVolume(musicSlider.getValue());

                GameData.prefs.putFloat("musicVolume", musicSlider.getValue());
                GameData.prefs.flush();
            }
        });

        soundSlider.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                game.soundManager.soundVolume = soundSlider.getValue();

                GameData.prefs.putFloat("soundVolume", soundSlider.getValue());
                GameData.prefs.flush();

                game.soundManager.playButtonTap();
            }
        });
    }

    private void openGDPRScreen(){
        Gdx.input.setInputProcessor(null);
        game.soundManager.playButtonTap();
        game.setScreen(new PrivacyScreen(game,this));
    }

    private void returnToPrevScreen(){
        Gdx.input.setInputProcessor(null);
        game.soundManager.playButtonTap();
        dispose();
        game.setScreen(prevScreen);
//        MotionEngine.fadeOut(stage.getRoot(),this,prevScreen,true);
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
        System.out.println("Options Screen Disposed");
        stage.dispose();
    }
}
