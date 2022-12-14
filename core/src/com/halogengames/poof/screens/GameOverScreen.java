package com.halogengames.poof.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.halogengames.poof.dataLoaders.GameData;
import com.halogengames.poof.Poof;

/**
 * Created by Rohit on 13-08-2017.
 *
 * Comes up at the game end
 * Shows the rank of the player for this game
 *
 */

public class GameOverScreen implements Screen{
    //the game for sprite batch
    private Poof game;

    //To add the buttons on the screen
    private Stage stage;

    //Labels
    private Label rankVal;

    //Buttons
    private ImageButton restartButton;
    private ImageButton mainMenuButton;

    //Constructor
    public GameOverScreen(Poof game){
        this.game = game;

        game.adManager.showInterstitialAd();
        stage = new Stage(Poof.VIEW_PORT, game.batch);

        //Add score table
        Table scoreTable = new Table();
        scoreTable.top();
        scoreTable.setFillParent(true);

        //Add score label
        Label scoreTextLabel = new Label("Score", this.game.assetManager.gameOverLabelStyle);
        Label scoreLabel = new Label(String.valueOf(GameData.score), this.game.assetManager.gameOverLabelStyle);

        //add the labels to the table
        scoreTable.add(scoreTextLabel).expandX().padTop(Poof.V_HEIGHT*0.05f);
        scoreTable.row();
        scoreTable.add(scoreLabel).expandX();

        GameData.saveScore();

        //Add highscore label
        int highScore = GameData.getHighScore();
        if(GameData.score>highScore){
            GameData.setHighScore();
            Label highScoreMsg = new Label("High score made!", this.game.assetManager.gameOverLabelStyle);
            scoreTable.row();
            scoreTable.add(highScoreMsg).expandX().padTop(Poof.V_HEIGHT/10);
        }

        //Add table to stage
        stage.addActor(scoreTable);

        //create a table to position Buttons on stage
        Table table = new Table();
        table.bottom();
        table.setPosition(0, Poof.BANNER_AD_SIZE);
        table.setFillParent(true);

        //Add buttons
        restartButton = new ImageButton(this.game.assetManager.UISkin.get("restartButton", ImageButton.ImageButtonStyle.class));
        mainMenuButton = new ImageButton(this.game.assetManager.UISkin.get("mainMenuButton", ImageButton.ImageButtonStyle.class));

        addUIListeners();

        //assuming all buttons are same size
        float buttWidth = game.assetManager.buttWidth;
        float buttHeight = restartButton.getHeight()*buttWidth/restartButton.getWidth();

        //Add buttons to table and table to stage
        table.add(restartButton).size(buttWidth,buttHeight).padTop(game.assetManager.buttPadding);
        table.row();
        table.add(mainMenuButton).size(buttWidth,buttHeight).padTop(game.assetManager.buttPadding);
        stage.addActor(table);
    }

    private void addUIListeners(){
        restartButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                game.soundManager.playButtonTap();
                startGame();
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

    private void update(float dt){
    }

    @Override
    public void render(float delta){

        update(delta);

        stage.act(delta);

        Gdx.gl.glClearColor(GameData.clearColor.r, GameData.clearColor.g, GameData.clearColor.b, GameData.clearColor.a);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        game.bg.render(delta);

        game.batch.setProjectionMatrix(Poof.CAM.combined);
        game.batch.begin();
        game.batch.end();

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
        System.out.println("Game Over Screen Disposed");
        stage.dispose();
    }
}
