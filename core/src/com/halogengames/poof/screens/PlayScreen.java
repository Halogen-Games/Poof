package com.halogengames.poof.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.halogengames.poof.Data.GameData;
import com.halogengames.poof.Data.SoundManager;
import com.halogengames.poof.Poof;
import com.halogengames.poof.scenes.GameBoard;
import com.halogengames.poof.scenes.Hud;

/**
 * Created by Rohit on 14-07-2017.
 *
 * The main play area of the game
 * This contains a Hud, an options menu and a game board
 */

class PlayScreen implements Screen {

    private Poof game;
    private Hud hud;
    private GameBoard board;

    private Stage stage;

    PlayScreen(Poof game){
        this.game = game;
        GameData.resetData();

        //stage
        stage = new Stage(Poof.VIEW_PORT, game.batch);

        SoundManager.playMusic.play();

        hud = new Hud(game.batch);

        board = new GameBoard();

        //handle input events
        InputMultiplexer inputMultiplexer = new InputMultiplexer();
        inputMultiplexer.addProcessor(board);
        inputMultiplexer.addProcessor(stage);
        Gdx.input.setInputProcessor(inputMultiplexer);

        addUIListeners();
    }

    private void addUIListeners(){

    }

    @Override
    public void show() {
    }

    private void handleInput(float dt) {
        board.handleInput(dt);
    }

    private void update(float dt) {
        handleInput(dt);

        board.update(dt);

        GameData.levelTimer -= dt;
        if(GameData.levelTimer<=0){
            endGame();
        }
        Poof.CAM.update();
    }

    private void endGame() {
        Gdx.input.setInputProcessor(null);
        SoundManager.playMusic.stop();
        game.setScreen(new GameOverScreen(game));
    }

    @Override
    public void render(float delta){
        update(delta);

        Gdx.gl.glClearColor(1,1,1,1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        board.render(game.batch);
        hud.draw();
    }

    @Override
    public void resize(int width, int height) {
        Poof.VIEW_PORT.update( width, height);
    }

    @Override
    public void pause() {
        SoundManager.playMusic.pause();
        game.setScreen(new PauseScreen(this, game));
    }

    @Override
    public void resume() {

    }

    @Override
    public void hide() {

    }

    @Override
    public void dispose() {
        board.dispose();
        hud.dispose();
    }
}
