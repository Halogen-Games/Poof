package com.halogengames.poof.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.halogengames.poof.dataLoaders.PoofAssetManager;
import com.halogengames.poof.dataLoaders.GameData;
import com.halogengames.poof.dataLoaders.SoundManager;
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
    private TextButton replayButton;
    private TextButton mainMenuButton;

    //Constructor
    public GameOverScreen(Poof game){
        this.game = game;

        game.adInterface.showInterstitialAd();
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

        //Add highscore label
        //Todo:Sync setting high score with retrieving rank
        int highScore = GameData.getHighScore();
        if(GameData.score>highScore){
            GameData.setHighScore();
            Label highScoreMsg = new Label("High score made!", this.game.assetManager.gameOverLabelStyle);
            scoreTable.row();
            scoreTable.add(highScoreMsg).expandX().padTop(Poof.V_HEIGHT/10);
        }else{
            Label highScoreText = new Label("Highest Score", this.game.assetManager.gameOverLabelStyle);
            Label highScoreVal = new Label(String.valueOf(highScore), this.game.assetManager.gameOverLabelStyle);
            scoreTable.row();
            scoreTable.add(highScoreText).expandX().padTop(Poof.V_HEIGHT/10);
            scoreTable.row();
            scoreTable.add(highScoreVal).expandX();
        }

        //Add world rank label
        GameData.getPlayerRank();
        GameData.getNumPlayers();
        Label rankText = new Label("World Rank", this.game.assetManager.gameOverLabelStyle);
        rankVal = new Label("Calculating", this.game.assetManager.gameOverLabelStyle);
        scoreTable.row();
        scoreTable.add(rankText).expandX();
        scoreTable.row();
        scoreTable.add(rankVal).expandX();

        //Add table to stage
        stage.addActor(scoreTable);

        //create a table to position Buttons on stage
        Table table = new Table();
        table.bottom();
        table.setPosition(0, Poof.BANNER_AD_SIZE);
        table.setFillParent(true);

        //Add buttons
        replayButton = new TextButton("Replay", this.game.assetManager.gameOverButtonStyle);
        mainMenuButton = new TextButton("Main Menu", this.game.assetManager.gameOverButtonStyle);

        addUIListeners();

        //Add buttons to table and table to stage
        table.add(replayButton);
        table.row();
        table.add(mainMenuButton);
        stage.addActor(table);
    }

    private void addUIListeners(){
        replayButton.addListener(new ChangeListener() {
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
        stage.getRoot().setColor(1,1,1,0);
        stage.getRoot().addAction(Actions.fadeIn(0.2f));
    }

    private void update(float dt){
        if(GameData.worldRank[0]>0 && GameData.numGlobalPlayers[0]>0) {
            rankVal.setText(String.valueOf(GameData.worldRank[0])+" of "+String.valueOf(GameData.numGlobalPlayers[0]));
        }

        if(GameData.worldRank[0]==GameData.NO_NETWORK || GameData.numGlobalPlayers[0]==GameData.NO_NETWORK) {
            rankVal.setText("No Internet!");
        }
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
