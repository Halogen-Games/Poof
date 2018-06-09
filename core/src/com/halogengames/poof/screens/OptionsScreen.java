package com.halogengames.poof.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Slider;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.utils.Align;
import com.halogengames.poof.dataLoaders.PoofAssetManager;
import com.halogengames.poof.dataLoaders.GameData;
import com.halogengames.poof.dataLoaders.SoundManager;
import com.halogengames.poof.Poof;

/**
 * Defined the options screen
 */

class OptionsScreen implements Screen {

    private Poof game;

    private Screen prevScreen;

    //Sliders
    private Slider musicSlider;
    private Slider soundSlider;

    //To add the buttons on the screen
    private Stage stage;
    private TextButton backButton;
    private TextButton privacyButton;

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
        Label title = new Label("Options", this.game.assetManager.optionsTitleStyle);
        title.setPosition(Poof.VIEW_PORT.getWorldWidth()/2, Poof.VIEW_PORT.getWorldHeight()*0.9f - title.getHeight(), Align.center);

        //adding back button
        backButton = new TextButton("Back", this.game.assetManager.optionsButtonStyle);
        privacyButton = new TextButton("Privacy", this.game.assetManager.optionsButtonStyle);

        //adding sliders
        musicSlider = new Slider(0,1,0.01f,false, this.game.assetManager.optionsMusicSliderStyle);
        musicSlider.setValue(GameData.prefs.getFloat("musicVolume", 1.0f));

        soundSlider = new Slider(0,1,0.2f,false, this.game.assetManager.optionsSoundSliderStyle);
        soundSlider.setValue(GameData.prefs.getFloat("soundVolume", 1.0f));

        addUIListeners();

        //adding buttons to table and table to stage
        table.add(musicSlider).width(Poof.V_WIDTH*0.65f).padBottom(Poof.V_HEIGHT/15);
        table.row();
        table.add(soundSlider).width(Poof.V_WIDTH*0.65f).padBottom(Poof.V_HEIGHT/10);
        if(game.adInterface.isEURegion()) {
            table.row();
            table.add(privacyButton);
        }
        table.row();
        table.add(backButton);
        stage.addActor(title);
        stage.addActor(table);

        //to allow stage to identify events
        Gdx.input.setInputProcessor(stage);
    }

    @Override
    public void show() {
        Gdx.input.setInputProcessor(stage);
        stage.getRoot().setColor(1,1,1,0);
        stage.getRoot().addAction(Actions.fadeIn(0.2f));
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
