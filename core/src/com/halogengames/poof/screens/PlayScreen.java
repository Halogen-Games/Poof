package com.halogengames.poof.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Align;
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

    private final Poof game;
    private final Hud hud;
    private final GameBoard board;

    private final Stage stage;

    private final ImageButton pauseButton;

    PlayScreen(Poof game){
        this.game = game;
        GameData.resetData();

        SoundManager.playMusic.play();

        //stage
        stage = new Stage(Poof.VIEW_PORT, game.batch);

        //Table for board, Buttons and Labels
        Table table = new Table();
        table.bottom();
        table.setFillParent(true);

        hud = new Hud(game.batch);

        board = new GameBoard();

        //Pause Button
        Texture buttonTex = new Texture("buttons/pause.png");
        buttonTex.setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);
        pauseButton = new ImageButton(new TextureRegionDrawable(new TextureRegion(buttonTex)));

        addUIListeners();

        table.add(pauseButton).align(Align.right).size(Poof.V_HEIGHT*0.05f);
        table.row();
        //Todo: Make the height dependent on numRows
        table.add(board).size(Poof.V_WIDTH*0.9f).padBottom(Poof.V_HEIGHT*0.2f);
        stage.addActor(table);

        //handle input events
        Gdx.input.setInputProcessor(stage);
    }

    private void addUIListeners(){
        board.addListener(new InputListener(){
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                return board.boardTouchedDown(x, y);
            }

            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                board.boardTouchedUp();
            }

            @Override
            public void touchDragged(InputEvent event, float x, float y, int pointer) {
                board.boardTouchDragged(x, y);
            }
        });

        pauseButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                pause();
            }
        });
    }

    @Override
    public void show() {
    }

    private void update(float dt) {
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
        dispose();
        game.setScreen(new GameOverScreen(game));
    }

    @Override
    public void render(float delta){
        update(delta);

        Gdx.gl.glClearColor(1,1,1,1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        hud.draw();
        stage.draw();
    }

    @Override
    public void resize(int width, int height) {
        Poof.VIEW_PORT.update( width, height);
    }

    @Override
    public void pause() {
        Gdx.input.setInputProcessor(null);
        SoundManager.playButtonTap();
        SoundManager.playMusic.pause();
        game.setScreen(new PauseScreen(game, this));
    }

    @Override
    public void resume() {
        SoundManager.playMusic.play();
        game.setScreen(this);
        Gdx.input.setInputProcessor(stage);
    }

    @Override
    public void hide() {

    }

    @Override
    public void dispose() {
        System.out.println("Play Screen Disposed");
        board.dispose();
        hud.dispose();
        stage.dispose();
    }
}
