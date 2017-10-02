package com.halogengames.poof.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Label.LabelStyle;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.utils.Align;
import com.halogengames.poof.Data.GameData;
import com.halogengames.poof.Data.SoundManager;
import com.halogengames.poof.Poof;

/**
 * Defines the pause screen
 */

class PauseScreen implements Screen {

    //for renderer handles
    private final Poof game;

    //to return back to the game
    private final Screen playScr;

    //Font
    private final BitmapFont titleFont;

    //buttons and fonts
    private final Stage stage;
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

        //Defining Fonts
        FreeTypeFontGenerator.FreeTypeFontParameter fontParam = new FreeTypeFontGenerator.FreeTypeFontParameter();
        fontParam.minFilter = Texture.TextureFilter.Linear;
        fontParam.magFilter = Texture.TextureFilter.Linear;
        //Title Font
        fontParam.size = (int)(80 * Poof.V_WIDTH/GameData.baseWidth);
        titleFont = Poof.labelFontGenerator.generateFont(fontParam);

        //Button Font
        fontParam.size = (int)(50 * Poof.V_WIDTH/GameData.baseWidth);
        fontParam.color = Color.DARK_GRAY;
        TextButton.TextButtonStyle buttonStyle = new TextButton.TextButtonStyle();
        buttonStyle.font = Poof.labelFontGenerator.generateFont(fontParam);

        //Title Label
        LabelStyle labelStyle = new LabelStyle(titleFont, Color.DARK_GRAY);
        Label title = new Label("Paused!!!", labelStyle);
        title.setPosition(Poof.VIEW_PORT.getWorldWidth()/2, Poof.VIEW_PORT.getWorldHeight()*0.9f - title.getHeight(), Align.center);

        //Adding buttons
        resumeButton = new TextButton("Resume", buttonStyle);
        optionsButton = new TextButton("Options", buttonStyle);
        mainMenuButton = new TextButton("Main Menu", buttonStyle);
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
                SoundManager.playButtonTap();
                openOptions();
            }
        });

        mainMenuButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                SoundManager.playButtonTap();
                openMainMenu();
            }
        });

        stage.addListener(new InputListener(){
            @Override
            public boolean keyDown(InputEvent event, int keycode) {
                if(keycode == Input.Keys.BACK){
                    resumeGame();
                    return true;
                }
                return false;
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
        game.setScreen(new OptionsScreen(game, this));
    }

    private void openMainMenu(){
        Gdx.input.setInputProcessor(null);
        SoundManager.playButtonTap();
        playScr.dispose();
        this.dispose();
        game.setScreen(new MainMenuScreen(game));
    }

    @Override
    public void show() {

    }

    @Override
    public void render(float delta) {
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
        Gdx.input.setInputProcessor(stage);
        game.setScreen(this);
    }

    @Override
    public void hide() {

    }

    @Override
    public void dispose() {
        System.out.println("Pause Screen Disposed");
        titleFont.dispose();
        stage.dispose();
    }
}
