package com.halogengames.poof.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.utils.Array;
import com.halogengames.poof.dataLoaders.GameData;
import com.halogengames.poof.Poof;
import com.halogengames.poof.utils.items.UpgradeBar;
import com.halogengames.poof.utils.menus.ScrollMenu;
import com.halogengames.poof.utils.menus.SelectionMenu;
import com.halogengames.poof.widgets.upgrades.BombUpgrade;
import com.halogengames.poof.widgets.upgrades.TimerUpgrade;

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

    //Stage used as an input multiplexer
    private Stage stage;

    private SelectionMenu upgradeMenu;

    private Texture titleTex;
    private float titleWidth;
    private float titleHeight;
    private Vector2 titlePos;

    private TextButton backButton;

    private float numOpsPerPage;
    private float shopTopHeight;
    private float shopBottomHeight;
    private float optionGutter;

    //Constructor
    ShopScreen(Poof game, Screen prevScr){
        this.game = game;
        this.prevScreen = prevScr;

        //Title Label
        titleTex = game.assetManager.shopSkin.get("shopTitleTex", Texture.class);
        titleWidth = Poof.V_WIDTH*2/3;
        titleHeight = titleTex.getHeight()*titleWidth/titleTex.getWidth();

        //stage is only used as an input multiplexer
        stage = new Stage(Poof.VIEW_PORT, game.batch);

        //create the menu
        upgradeMenu = new ScrollMenu(game, 20);
        upgradeMenu.setWidth(Poof.getViewPort().getWorldWidth()*0.9f);

        //create pages containing upgrades
        upgradeMenu.addCategory("Power Ups");
        upgradeMenu.addItem(new BombUpgrade(game));

        upgradeMenu.addCategory("Consumables");
        upgradeMenu.addItem(new BombUpgrade(game));

        upgradeMenu.addCategory("Consumables");
        upgradeMenu.addItem(new BombUpgrade(game));

        upgradeMenu.setPosition(Poof.getViewPort().getWorldWidth()/2 - upgradeMenu.getWidth()/2,Poof.V_HEIGHT - 2f*titleHeight-upgradeMenu.getHeight());

        shopTopHeight = Poof.V_HEIGHT/5;
        shopBottomHeight = Poof.V_HEIGHT/15;
        numOpsPerPage = 3;
        optionGutter = Poof.V_WIDTH/15;

        backButton = new TextButton("Back", this.game.assetManager.levelSelectButtonStyle);
        backButton.setPosition(Poof.V_WIDTH/2 - backButton.getWidth()/2,Poof.BANNER_AD_SIZE);

        stage.addActor(upgradeMenu);
        stage.addActor(backButton);

        addUIListeners();

        //to allow stage to identify events
        Gdx.input.setInputProcessor(stage);
    }

    private void addUIListeners(){
        backButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                goBack();
            }
        });
    }

    private void goBack(){
        Gdx.input.setInputProcessor(null);
        game.soundManager.playButtonTap();
        dispose();
        game.setScreen(prevScreen);
    }

    @Override
    public void show() {
        Gdx.input.setInputProcessor(stage);
    }

    public void update(float dt){
        upgradeMenu.setPosition(Poof.getViewPort().getWorldWidth()/2 - upgradeMenu.getWidth()/2,Poof.V_HEIGHT - 2*titleHeight-upgradeMenu.getHeight());
    }

    @Override
    public void render(float delta){
        update(delta);
        stage.act(delta);

        Gdx.gl.glClearColor(GameData.clearColor.r, GameData.clearColor.g, GameData.clearColor.b, GameData.clearColor.a);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        game.batch.setProjectionMatrix(Poof.CAM.combined);

        game.bg.render(delta);

        stage.act(delta);
        stage.draw();

        game.batch.begin();
        game.batch.draw(titleTex,Poof.V_WIDTH/2 - titleWidth/2, Poof.V_HEIGHT*0.9f - titleHeight, titleWidth, titleHeight);
        game.batch.end();
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
