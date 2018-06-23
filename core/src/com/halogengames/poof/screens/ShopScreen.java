package com.halogengames.poof.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.utils.Align;
import com.halogengames.poof.dataLoaders.PoofAssetManager;
import com.halogengames.poof.dataLoaders.GameData;
import com.halogengames.poof.dataLoaders.SoundManager;
import com.halogengames.poof.Poof;
import com.halogengames.poof.widgets.UpgradeOption;

import java.util.ArrayList;

/**
 * Created by Rohit on 20-05-2018.
 *
 * Screen to select the difficulty
 */

class ShopScreen implements Screen{
    //the game for sprite batch
    private Poof game;
    private Screen prevScreen;

    //Syage used as an input multiplexer
    private Stage stage;

    private ImageButton next;
    private ImageButton prev;
    private ImageButton back;

    private ArrayList<UpgradeOption> upgradeOptions;
    private UpgradeOption timer;
    private UpgradeOption bomb;
    private UpgradeOption bomb1;
    private UpgradeOption bomb2;

    private float numOpsPerPage;
    private float shopTopHeight;
    private float shopBottomHeight;
    private float optionGutter;

    //Constructor
    ShopScreen(Poof game, Screen prevScr){
        this.game = game;
        this.prevScreen = prevScr;

        //stage is only used as an input multiplexer
        stage = new Stage(Poof.VIEW_PORT, game.batch);
        upgradeOptions = new ArrayList<UpgradeOption>();

        timer = new UpgradeOption(game, stage);
        bomb = new UpgradeOption(game, stage);
        bomb1 = new UpgradeOption(game, stage);
        upgradeOptions.add(timer);
        upgradeOptions.add(bomb);
        upgradeOptions.add(bomb1);

        shopTopHeight = Poof.V_HEIGHT/5;
        shopBottomHeight = Poof.V_HEIGHT/15;
        numOpsPerPage = 3;
        optionGutter = Poof.V_WIDTH/15;


        setOptionsPosData();
        //addUIListeners();

        //to allow stage to identify events
        Gdx.input.setInputProcessor(stage);
    }

    private void addUIListeners(){
        bomb.getUpgradeButton().addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                game.soundManager.playButtonTap();
                //todo add data here
            }
        });

        timer.getUpgradeButton().addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                game.soundManager.playButtonTap();
                //todo add data here
            }
        });

        prev.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                game.soundManager.playButtonTap();
                //todo add data here
            }
        });

        next.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                game.soundManager.playButtonTap();
                //todo add data here
            }
        });

        back.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                game.soundManager.playButtonTap();
                //todo add data here
            }
        });
    }

    private void setOptionsPosData(){
        float opWidth = Poof.V_WIDTH - 2*optionGutter;
        float opHeight = (Poof.V_HEIGHT - Poof.BANNER_AD_SIZE - shopBottomHeight - shopTopHeight - optionGutter)/numOpsPerPage - optionGutter;
        for(int i=0; i<upgradeOptions.size();i++){
            upgradeOptions.get(i).setSize(opWidth,opHeight);
            upgradeOptions.get(i).setPosition(optionGutter,shopBottomHeight + Poof.BANNER_AD_SIZE + (i+1)*(optionGutter) + i*opHeight);
        }
    }

    @Override
    public void show() {
        Gdx.input.setInputProcessor(stage);
        stage.getRoot().setColor(1,1,1,0);
        stage.getRoot().addAction(Actions.fadeIn(0.2f));
    }

    @Override
    public void render(float delta){
        stage.act(delta);

        Gdx.gl.glClearColor(GameData.clearColor.r, GameData.clearColor.g, GameData.clearColor.b, GameData.clearColor.a);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        game.bg.render(delta);

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
    public void resume() {}

    @Override
    public void hide() {

    }

    @Override
    public void dispose() {
        System.out.println("shop Screen Disposed");
        stage.dispose();
    }
}
