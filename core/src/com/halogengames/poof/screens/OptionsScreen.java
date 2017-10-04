package com.halogengames.poof.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator.FreeTypeFontParameter;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Label.LabelStyle;
import com.badlogic.gdx.scenes.scene2d.ui.Slider;
import com.badlogic.gdx.scenes.scene2d.ui.Slider.SliderStyle;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton.TextButtonStyle;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Align;
import com.halogengames.poof.Data.AssetManager;
import com.halogengames.poof.Data.GameData;
import com.halogengames.poof.Data.SoundManager;
import com.halogengames.poof.Poof;
import com.halogengames.poof.libs.MotionEngine;

/**
 * Defined the options screen
 */

class OptionsScreen implements Screen {

    private final Poof game;

    private final Screen prevScreen;

    //Sliders
    private final Slider musicSlider;
    private final Slider soundSlider;

    //Font


    //To add the buttons on the screen
    //Todo: Use a texture atlas for the buttons
    private final Stage stage;
    private final TextButton backButton;

    OptionsScreen(Poof game, Screen prevScr){
        this.game = game;

        this.prevScreen = prevScr;

        //stage
        stage = new Stage(Poof.VIEW_PORT, game.batch);

        //create a table to position objects on stage
        Table table = new Table();
        table.bottom();
        table.setPosition(0, Poof.V_HEIGHT/10);
        table.setFillParent(true);

        //Add Label
        Label title = new Label("Options", AssetManager.optionsTitleStyle);
        title.setPosition(Poof.VIEW_PORT.getWorldWidth()/2, Poof.VIEW_PORT.getWorldHeight()*0.9f - title.getHeight(), Align.center);

        //adding back button
        backButton = new TextButton("Back", AssetManager.optionsButtonStyle);

        //adding sliders
        musicSlider = new Slider(0,1,0.01f,false, AssetManager.optionsMusicSliderStyle);
        musicSlider.setValue(GameData.prefs.getFloat("musicVolume", 1.0f));

        soundSlider = new Slider(0,1,0.2f,false, AssetManager.optionsSoundSliderStyle);
        soundSlider.setValue(GameData.prefs.getFloat("soundVolume", 1.0f));

        addUIListeners();

        //adding buttons to table and table to stage
        table.add(musicSlider).width(Poof.V_WIDTH*0.65f).padBottom(Poof.V_HEIGHT/10);
        table.row();
        table.add(soundSlider).width(Poof.V_WIDTH*0.65f).padBottom(Poof.V_HEIGHT/10);
        table.row();
        table.add(backButton);
        stage.addActor(title);
        stage.addActor(table);

        //to allow stage to identify events
        Gdx.input.setInputProcessor(stage);
    }

    @Override
    public void show() {
        MotionEngine.fadeIn(stage.getRoot());
    }

    private void addUIListeners(){
        backButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                returnToPrevScreen();
            }
        });

        musicSlider.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                SoundManager.mainMenuMusic.setVolume(musicSlider.getValue());
                SoundManager.playMusic.setVolume(musicSlider.getValue());

                GameData.prefs.putFloat("musicVolume", musicSlider.getValue());
                GameData.prefs.flush();
            }
        });

        soundSlider.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                SoundManager.soundVolume = soundSlider.getValue();

                GameData.prefs.putFloat("soundVolume", soundSlider.getValue());
                GameData.prefs.flush();

                SoundManager.playButtonTap();
            }
        });
    }

    private void returnToPrevScreen(){
        Gdx.input.setInputProcessor(null);
        SoundManager.playButtonTap();
        MotionEngine.fadeOut(stage.getRoot(),this,prevScreen,true);
    }

    @Override
    public void render(float delta) {
        stage.act();

        Gdx.gl.glClearColor(1,1,1,1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        Poof.bg.render(delta);

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
        System.out.println("Options Screen Disposed");
        stage.dispose();
    }
}
