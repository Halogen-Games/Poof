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
import com.halogengames.poof.Data.GameData;
import com.halogengames.poof.Data.SoundManager;
import com.halogengames.poof.Poof;

/**
 * Created by Rohit on 13-08-2017.
 *
 * Comes up at the game end
 * Shows the rank of the player for this game
 *
 */

class GameOverScreen implements Screen{
    //the game for sprite batch
    private final Poof game;

    //To add the buttons on the screen
    //Todo: Use a texture atlas for the buttons
    private final Stage stage;
    private final BitmapFont buttonFont;
    private final BitmapFont textLabelFont;
    private final BitmapFont valLabelFont;

    //Buttons
    private final TextButton replayButton;
    private final TextButton mainMenuButton;

    //Constructor
    GameOverScreen(Poof game){
        this.game = game;

        stage = new Stage(Poof.VIEW_PORT, game.batch);

        //Add score table
        Table scoreTable = new Table();
        scoreTable.top();
        scoreTable.setFillParent(true);

        //Defining Fonts and Styles
        FreeTypeFontParameter fontParam = new FreeTypeFontParameter();
        fontParam.minFilter = TextureFilter.Linear;
        fontParam.magFilter = TextureFilter.Linear;

        //Label Font
        fontParam.size = (int)(30 * Poof.V_WIDTH/GameData.baseWidth);
        textLabelFont = Poof.valueFontGenerator.generateFont(fontParam);
        valLabelFont = Poof.labelFontGenerator.generateFont(fontParam);

        //Button Font
        fontParam.size = (int)(50 * Poof.V_WIDTH/GameData.baseWidth);
        fontParam.borderWidth = 0;
        fontParam.color = Color.DARK_GRAY;
        buttonFont = Poof.valueFontGenerator.generateFont(fontParam);
        TextButtonStyle buttonStyle = new TextButtonStyle();
        buttonStyle.font = buttonFont;

        //Add score label
        Label scoreTextLabel = new Label("Score", new LabelStyle(textLabelFont, Color.DARK_GRAY));
        Label scoreLabel = new Label(String.valueOf(GameData.score), new LabelStyle(valLabelFont, Color.DARK_GRAY));

        //add the labels to the table
        scoreTable.add(scoreTextLabel).expandX().padTop(Poof.V_HEIGHT*0.05f);
        scoreTable.row();
        scoreTable.add(scoreLabel).expandX();

        //Add highscore label
        int highScore = GameData.prefs.getInteger("highScore", 0);
        if(GameData.score>highScore){
            GameData.prefs.putInteger("highScore", GameData.score);
            GameData.prefs.flush();
            Label highScoreMsg = new Label("High score made!", new LabelStyle(textLabelFont, Color.DARK_GRAY));
            scoreTable.row();
            scoreTable.add(highScoreMsg).expandX();
        }else{
            Label highScoreText = new Label("Highest Score", new LabelStyle(textLabelFont, Color.DARK_GRAY));
            Label highScoreVal = new Label(String.valueOf(highScore), new LabelStyle(valLabelFont, Color.DARK_GRAY));
            scoreTable.row();
            scoreTable.add(highScoreText).expandX();
            scoreTable.row();
            scoreTable.add(highScoreVal).expandX();
        }

        //Add table to stage
        stage.addActor(scoreTable);

        //create a table to position Buttons on stage
        Table table = new Table();
        table.bottom();
        table.setFillParent(true);

        //Add buttons
        replayButton = new TextButton("Replay", buttonStyle);
        mainMenuButton = new TextButton("Main Menu", buttonStyle);

        addUIListeners();

        //Add buttons to table and table to stage
        table.add(replayButton);
        table.row();
        table.add(mainMenuButton).padBottom(Poof.V_HEIGHT*0.1f);
        stage.addActor(table);
    }

    private void addUIListeners(){
        replayButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                SoundManager.playButtonTap();
                startGame();
            }
        });


        mainMenuButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                SoundManager.playButtonTap();
                openMainMenu();
            }
        });
    }

    private void startGame(){
        Gdx.input.setInputProcessor(null);
        dispose();
        game.setScreen(new PlayScreen(game));
    }

    private void openMainMenu(){
        Gdx.input.setInputProcessor(null);
        dispose();
        game.setScreen(new MainMenuScreen(game));
    }

    @Override
    public void show() {
        //to allow stage to identify events
        Gdx.input.setInputProcessor(stage);
    }

    @Override
    public void render(float delta){
        Gdx.gl.glClearColor(1,1,1,1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        game.batch.setProjectionMatrix(Poof.CAM.combined);
        game.batch.begin();
        game.batch.end();

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

    }

    @Override
    public void hide() {

    }

    @Override
    public void dispose() {
        System.out.println("Game Over Screen Disposed");
        textLabelFont.dispose();
        valLabelFont.dispose();
        buttonFont.dispose();

        stage.dispose();
    }
}
