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
import com.halogengames.poof.Data.AssetManager;
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
    private Poof game;

    //To add the buttons on the screen
    //Todo: Use a texture atlas for the buttons
    private Stage stage;

    //Buttons
    private TextButton replayButton;
    private TextButton mainMenuButton;

    //Constructor
    GameOverScreen(Poof game){
        this.game = game;

        stage = new Stage(Poof.VIEW_PORT, game.batch);

        //Add score table
        Table scoreTable = new Table();
        scoreTable.top();
        scoreTable.setFillParent(true);



        //Add score label
        Label scoreTextLabel = new Label("Score", AssetManager.gameOverLabelStyle);
        Label scoreLabel = new Label(String.valueOf(GameData.score), AssetManager.gameOverLabelStyle);

        //add the labels to the table
        scoreTable.add(scoreTextLabel).expandX().padTop(Poof.V_HEIGHT*0.05f);
        scoreTable.row();
        scoreTable.add(scoreLabel).expandX();

        //Add highscore label
        int highScore = GameData.prefs.getInteger("highScore", 0);
        if(GameData.score>highScore){
            GameData.prefs.putInteger("highScore", GameData.score);
            GameData.prefs.flush();
            Label highScoreMsg = new Label("High score made!", AssetManager.gameOverLabelStyle);
            scoreTable.row();
            scoreTable.add(highScoreMsg).expandX();
        }else{
            Label highScoreText = new Label("Highest Score", AssetManager.gameOverLabelStyle);
            Label highScoreVal = new Label(String.valueOf(highScore), AssetManager.gameOverLabelStyle);
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
        replayButton = new TextButton("Replay", AssetManager.gameOverButtonStyle);
        mainMenuButton = new TextButton("Main Menu", AssetManager.gameOverButtonStyle);

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
        stage.getRoot().setColor(1,1,1,0);
        stage.getRoot().addAction(Actions.fadeIn(0.2f));
    }

    @Override
    public void render(float delta){

        stage.act(delta);

        Gdx.gl.glClearColor(GameData.clearColor.r, GameData.clearColor.g, GameData.clearColor.b, GameData.clearColor.a);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        Poof.bg.render(delta);

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
        stage.dispose();
    }
}
