package com.halogengames.poof.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.utils.Align;
import com.halogengames.poof.Data.AssetManager;
import com.halogengames.poof.Data.GameData;
import com.halogengames.poof.Data.SoundManager;
import com.halogengames.poof.Poof;
import com.halogengames.poof.widgets.GameBoard;
import com.halogengames.poof.widgets.Hud;

import java.util.ArrayList;

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

    private ImageButton pauseButton;

    PlayScreen(Poof game){
        this.game = game;
        GameData.resetData();

        SoundManager.playMusic.play();

        //stage
        stage = new Stage(Poof.VIEW_PORT, game.batch);

        hud = new Hud(game.batch);

        ArrayList<ImageButton> buttons = new ArrayList<ImageButton>();

        //Create Buttons
        pauseButton = new ImageButton(AssetManager.playScreenPauseButtonDrawable);
        buttons.add(pauseButton);

        //Create Board
        float boardSize = Poof.V_WIDTH*0.9f;
        Vector2 boardPos = new Vector2(Poof.VIEW_PORT.getWorldWidth()/2 - boardSize/2,Poof.VIEW_PORT.getWorldHeight()*0.15f);
        board = new GameBoard(game, boardSize,buttons.size());
        board.setPosition(boardPos.x, boardPos.y);
        board.setSize(boardSize,boardSize);
        stage.addActor(board);

        //Set Button Sizes
        float buttonSize = board.getButtonSize()-2*board.getButtonMargin();

        //Todo: Make the height dependent on numRows
        for(int i=0;i<buttons.size();i++){
            buttons.get(i).setSize(buttonSize, buttonSize);
            float posX = boardPos.x+board.getCornerRadius()+board.getButtonMargin()+i*(board.getButtonSize()+board.getButtonGutter());
            float posY = boardPos.y - buttonSize - board.getButtonMargin();
            buttons.get(i).setPosition(posX,posY);
            stage.addActor(buttons.get(i));
        }

        addUIListeners();

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
        stage.getRoot().setColor(1,1,1,0);
        stage.getRoot().addAction(Actions.fadeIn(0.2f));
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

        stage.act(delta);

        Gdx.gl.glClearColor(GameData.clearColor.r, GameData.clearColor.g, GameData.clearColor.b, GameData.clearColor.a);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT | GL20.GL_DEPTH_BUFFER_BIT | (Gdx.graphics.getBufferFormat().coverageSampling?GL20.GL_COVERAGE_BUFFER_BIT_NV:0));

        Poof.bg.render(delta);

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
