package com.halogengames.poof.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator.FreeTypeFontParameter;
import com.badlogic.gdx.scenes.scene2d.Actor;
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
import com.halogengames.poof.Data.GameData;
import com.halogengames.poof.Data.SoundManager;
import com.halogengames.poof.Poof;

/**
 * Created by Rohit on 30-09-2017.
 */

class OptionsScreen implements Screen {

    private Poof game;

    //Sliders
    private Slider musicSlider;
    private Slider soundSlider;

    //Font
    private BitmapFont titleFont;

    //To add the buttons on the screen
    //Todo: Use a texture atlas for the buttons
    private Stage stage;
    private TextButton backButton;

    OptionsScreen(Poof game){
        this.game = game;

        //stage
        stage = new Stage(Poof.VIEW_PORT, game.batch);

        //create a table to position objects on stage
        Table table = new Table();
        table.bottom();
        table.setPosition(0, Poof.V_HEIGHT/10);
        table.setFillParent(true);

        //Defining Fonts
        FreeTypeFontParameter fontParam = new FreeTypeFontParameter();
        fontParam.minFilter = Texture.TextureFilter.Linear;
        fontParam.magFilter = Texture.TextureFilter.Linear;
        //Title Font
        fontParam.size = 80;
        titleFont = Poof.labelFontGenerator.generateFont(fontParam);

        //Title Label
        LabelStyle labelStyle = new LabelStyle(titleFont, Color.DARK_GRAY);
        Label title = new Label("Options", labelStyle);
        title.setPosition(Poof.VIEW_PORT.getWorldWidth()/2, Poof.VIEW_PORT.getWorldHeight()*0.9f - title.getHeight(), Align.center);

        //Button Font
        fontParam.size = 50;
        fontParam.color = Color.GRAY;
        TextButtonStyle buttonStyle = new TextButtonStyle();
        buttonStyle.font = Poof.labelFontGenerator.generateFont(fontParam);

        //adding back button
        backButton = new TextButton("Back", buttonStyle);

        //adding sliders
        SliderStyle musicSliderStyle = new SliderStyle();
        musicSliderStyle.knob = new TextureRegionDrawable(new TextureRegion(new Texture("slider/musicKnob.png")));
        musicSliderStyle.background = new TextureRegionDrawable(new TextureRegion(new Texture("slider/musicBG.png")));
        musicSlider = new Slider(0,100,1,false, musicSliderStyle);
        musicSlider.setValue(musicSlider.getMaxValue());

        SliderStyle soundSliderStyle = new SliderStyle();
        soundSliderStyle.knob = new TextureRegionDrawable(new TextureRegion(new Texture("slider/soundKnob.png")));
        soundSliderStyle.background = new TextureRegionDrawable(new TextureRegion(new Texture("slider/soundBG.png")));
        soundSlider = new Slider(0,100,1,false, soundSliderStyle);
        soundSlider.setValue(soundSlider.getMaxValue());

        addUIListeners();

        //adding buttons to table and table to stage
        table.add(musicSlider).width(300).padBottom(Poof.V_HEIGHT/10);
        table.row();
        table.add(soundSlider).width(300).padBottom(Poof.V_HEIGHT/10);
        table.row();
        table.add(backButton);
        stage.addActor(title);
        stage.addActor(table);

        //to allow stage to identify events
        Gdx.input.setInputProcessor(stage);
    }

    @Override
    public void show() {

    }

    private void addUIListeners(){
        backButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                Gdx.input.setInputProcessor(null);
                SoundManager.playButtonTap();
                game.setScreen(new MainMenuScreen(game));
            }
        });

        musicSlider.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                SoundManager.mainMenuMusic.setVolume(musicSlider.getValue()/100);
                SoundManager.playMusic.setVolume(musicSlider.getValue()/100);

                GameData.prefs.putFloat("musicVolume", musicSlider.getValue()/100);
                GameData.prefs.flush();
            }
        });

        soundSlider.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                SoundManager.soundVolume = soundSlider.getValue()/100;

                GameData.prefs.putFloat("soundVolume", soundSlider.getValue()/100);
                GameData.prefs.flush();
            }
        });
    }

    private void handleInput(float dt){

    }

    private void update(float dt){
        handleInput(dt);
    }

    @Override
    public void render(float delta) {
        update(delta);

        Gdx.gl.glClearColor(1,1,1,1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        game.bg.render(delta);

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

    }
}
