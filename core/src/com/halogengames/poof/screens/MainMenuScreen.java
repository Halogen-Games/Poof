package com.halogengames.poof.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture.TextureFilter;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator.FreeTypeFontParameter;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Label.LabelStyle;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton.TextButtonStyle;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.utils.Align;
import com.halogengames.poof.Data.GameData;
import com.halogengames.poof.Data.SoundManager;
import com.halogengames.poof.Poof;

/**
 * Created by Rohit on 01-08-2017.
 *
 * Opening screen for the game
 */

public class MainMenuScreen implements Screen {

    //the game for sprite batch
    private final Poof game;

    //Font
    private final BitmapFont titleFont;

    //To add the buttons on the screen
    //Todo: Use a texture atlas for the buttons
    private final Stage stage;
    private final TextButton playButton;
    private final TextButton fameButton;
    private final TextButton optionsButton;

    //Constructor
    public MainMenuScreen(Poof game){
        this.game = game;

        //start music
        SoundManager.mainMenuMusic.play();

        //stage
        stage = new Stage(Poof.VIEW_PORT, game.batch);

        //create a table to position objects on stage
        Table table = new Table();
        table.bottom();
        table.setPosition(0, Poof.V_HEIGHT/10);
        table.setFillParent(true);

        //Defining Fonts
        FreeTypeFontParameter fontParam = new FreeTypeFontParameter();
        fontParam.minFilter = TextureFilter.Linear;
        fontParam.magFilter = TextureFilter.Linear;
        //Title Font
        fontParam.size = (int)(100 * Poof.V_WIDTH/GameData.baseWidth);
        titleFont = Poof.labelFontGenerator.generateFont(fontParam);

        //Button Font
        fontParam.size = (int)(50 * Poof.V_WIDTH/GameData.baseWidth);
        fontParam.color = Color.DARK_GRAY;
        TextButtonStyle buttonStyle = new TextButtonStyle();
        buttonStyle.font = Poof.labelFontGenerator.generateFont(fontParam);

        //Title Label
        LabelStyle labelStyle = new LabelStyle(titleFont, Color.DARK_GRAY);
        Label title = new Label("Poof", labelStyle);
        title.setPosition(Poof.VIEW_PORT.getWorldWidth()/2, Poof.VIEW_PORT.getWorldHeight()*0.9f - title.getHeight(), Align.center);

        //Adding buttons
        playButton = new TextButton("Play", buttonStyle);
        fameButton = new TextButton("Hall of Fame", buttonStyle);
        optionsButton = new TextButton("Options", buttonStyle);
        addUIListeners();

        //adding buttons to table and table to stage
        table.add(playButton);
        table.row();
        //table.add(fameButton);
        //table.row();
        table.add(optionsButton);
        stage.addActor(title);
        stage.addActor(table);

        //to allow stage to identify events
        Gdx.input.setInputProcessor(stage);
    }

    private void addUIListeners(){
        playButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                SoundManager.playButtonTap();
                startGame();
            }
        });

        fameButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                SoundManager.playButtonTap();
                //openHallOfFame();
            }
        });

        optionsButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                SoundManager.playButtonTap();
                openOptions();
            }
        });

        stage.addListener(new InputListener(){
            @Override
            public boolean keyDown(InputEvent event, int keycode) {
                if(keycode == Input.Keys.BACK){
                    Gdx.app.exit();
                    return true;
                }
                return false;
            }
        });
    }

    private void startGame(){
        Gdx.input.setInputProcessor(null);
        SoundManager.mainMenuMusic.stop();
        dispose();
        game.setScreen(new PlayScreen(game));
    }

    private void openHallOfFame(){
        Gdx.input.setInputProcessor(null);
        dispose();
        game.setScreen(new FameScreen(game));
    }

    private void openOptions(){
        Gdx.input.setInputProcessor(null);
        game.setScreen(new OptionsScreen(game, this));
    }

    @Override
    public void show() {

    }

//    public void update(float dt){
//        Poof.CAM.update();
//    }

    @Override
    public void render(float delta){
//        //no need for below as cam not moving
//        update(delta);

        Gdx.gl.glClearColor(1,1,1,1);
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
    public void resume() {
        Gdx.input.setInputProcessor(stage);
        game.setScreen(this);
    }

    @Override
    public void hide() {

    }

    @Override
    public void dispose() {
        System.out.println("Main Menu Screen Disposed");
        titleFont.dispose();
        stage.dispose();
    }
}
