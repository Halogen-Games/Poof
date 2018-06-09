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
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.halogengames.poof.dataLoaders.PoofAssetManager;
import com.halogengames.poof.dataLoaders.GameData;
import com.halogengames.poof.dataLoaders.SoundManager;
import com.halogengames.poof.Poof;
import com.halogengames.poof.sprites.Tile.TileState;
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
    private ArrayList<ImageButton> buttons;
    private float buttonSize;

    private boolean gameStarted;

    PlayScreen(Poof game){
        this.game = game;
        game.adInterface.showInterstitialAd();
        gameStarted = false;
        GameData.resetData();
        System.out.println(GameData.gamesPlayed);
        game.soundManager.playMusic.play();

        //stage
        stage = new Stage(Poof.VIEW_PORT, game.batch);

        hud = new Hud(game);

        buttons = new ArrayList<ImageButton>();

        //Create Buttons
        pauseButton = new ImageButton(this.game.assetManager.playScreenPauseButtonDrawable);
        buttons.add(pauseButton);

        //Create Board
        float boardSize = Poof.V_WIDTH*0.9f;
        //todo: make sure the board doesn't go offscreen on weird resolutions
        Vector2 boardPos = new Vector2(Poof.VIEW_PORT.getWorldWidth()/2 - boardSize/2,Poof.VIEW_PORT.getWorldHeight()*0.1f + Poof.BANNER_AD_SIZE);
        board = new GameBoard(game, boardSize,buttons.size());
        board.setPosition(boardPos.x, boardPos.y);
        board.setSize(boardSize,boardSize);
        stage.addActor(board);

        //Set Button Sizes
        buttonSize = board.getButtonSize()-2*board.getButtonMargin();

        //Todo: (Low Priority) Make the height dependent on numRows
        for(int i=0;i<buttons.size();i++){
            buttons.get(i).setSize(buttonSize, buttonSize);
            float posX = board.getX()+board.getWidth()-board.getCornerRadius()-board.getButtonSize()+board.getButtonMargin()-i*(board.getButtonSize()+board.getButtonGutter());
            float posY = board.getY() - buttonSize - board.getButtonMargin();
            buttons.get(i).setPosition(posX,posY);
            stage.addActor(buttons.get(i));
        }

        addUIListeners();

        stage.getRoot().setColor(1,1,1,0);
        stage.getRoot().addAction(Actions.fadeIn(0.2f));

        //handle input events
        Gdx.input.setInputProcessor(stage);
    }

    private void addUIListeners(){
        board.addListener(new InputListener(){
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                board.boardTouchedDown(x, y);
                return true;
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

    private void updateButtonPos(){
        //update button positions
        for(int i=0;i<buttons.size();i++){
            float posX = board.getX()+board.getWidth()-board.getCornerRadius()-board.getButtonSize()+board.getButtonMargin()-i*(board.getButtonSize()+board.getButtonGutter());
            float posY = board.getY() - buttonSize - board.getButtonMargin();
            buttons.get(i).setPosition(posX,posY);
        }
    }

    @Override
    public void show() {
        //handle input events
        if(GameData.showTutorial){
            Gdx.input.setInputProcessor(null);
            GameData.tutorialShown();
            game.setScreen(new HelpScreen(game,this));
        }else{
            Gdx.input.setInputProcessor(stage);
        }
    }

    private void update(float dt) {
        if (board.getBoardState() == TileState.Idle) {
            gameStarted = true;
        }

        //Decrease timer only during tile drops and idle states
        if( GameData.getGameMode() == GameData.GameMode.Timed) {
            if(!board.movesLeft()) {
                board.shuffleBoard();
            }

            if (board.getBoardState() == TileState.Idle) {
                GameData.levelTimer -= dt;
            } else if (board.getBoardState() == TileState.Dropping) {
                GameData.levelTimer -= dt;
            }

            if (GameData.levelTimer <= 0) {
                endGame();
            }
        }else if(GameData.getGameMode() == GameData.GameMode.Relaxed){
            if(!board.movesLeft()) {
                endGame();
            }
        }

        board.update(dt);
    }

    @Override
    public void render(float delta){
        update(delta);

        stage.act(delta);

        Gdx.gl.glClearColor(GameData.clearColor.r, GameData.clearColor.g, GameData.clearColor.b, GameData.clearColor.a);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT | GL20.GL_DEPTH_BUFFER_BIT | (Gdx.graphics.getBufferFormat().coverageSampling?GL20.GL_COVERAGE_BUFFER_BIT_NV:0));

        game.bg.render(delta);

        hud.draw();
        stage.draw();
    }

    @Override
    public void resize(int width, int height) {
        game.resize(width,height);
        updateButtonPos();
    }

    @Override
    public void pause() {
        if(gameStarted) {
            Gdx.input.setInputProcessor(null);
            game.soundManager.playButtonTap();
            game.soundManager.playMusic.pause();
            game.setScreen(new PauseScreen(game, this));
        }
    }

    @Override
    public void resume() {
        game.soundManager.playMusic.play();
        game.setScreen(this);
        Gdx.input.setInputProcessor(stage);
    }

    private void endGame() {
        Gdx.input.setInputProcessor(null);
        game.soundManager.playMusic.stop();
        dispose();
        game.setScreen(new GameOverScreen(game));
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
