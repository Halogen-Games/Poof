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
import com.badlogic.gdx.utils.Align;
import com.halogengames.poof.advertisement.AdInterface;
import com.halogengames.poof.dataLoaders.PoofAssetManager;
import com.halogengames.poof.dataLoaders.GameData;
import com.halogengames.poof.dataLoaders.SoundManager;
import com.halogengames.poof.Poof;

/**
 * Defined the options screen
 */

public class PrivacyScreen implements Screen {

    private Poof game;

    private Screen prevScreen;

    //To add the buttons on the screen
    private Stage stage;
    private Label title;
    private TextButton policyButton;
    private TextButton personalizedButton;
    private TextButton nonPersonalizedButton;

    public PrivacyScreen(Poof game, Screen prevScr){
        this.game = game;

        this.prevScreen = prevScr;

        //stage
        stage = new Stage(Poof.VIEW_PORT, game.batch);

        //create a table to position objects on stage
        Table table = new Table();
        table.bottom();
        table.setPosition(0, Poof.BANNER_AD_SIZE);
        table.setFillParent(true);

        //Add Label
        title = new Label("Privacy", this.game.assetManager.privacyTitleStyle);
        title.setPosition(Poof.VIEW_PORT.getWorldWidth()/2, Poof.VIEW_PORT.getWorldHeight()*0.9f - title.getHeight(), Align.center);

        //adding buttons
        personalizedButton = new TextButton("Yes", this.game.assetManager.privacyButtonStyle);
        nonPersonalizedButton = new TextButton("No", this.game.assetManager.privacyButtonStyle);
        policyButton = new TextButton("Privacy Policy", this.game.assetManager.privacyPolicyButtonStyle);

        personalizedButton.setPosition(Poof.VIEW_PORT.getWorldWidth()/4-personalizedButton.getWidth()/2, Poof.BANNER_AD_SIZE );
        nonPersonalizedButton.setPosition(3*Poof.VIEW_PORT.getWorldWidth()/4-personalizedButton.getWidth()/2, Poof.BANNER_AD_SIZE );
        policyButton.setPosition(Poof.VIEW_PORT.getWorldWidth()/2-policyButton.getWidth()/2, personalizedButton.getY() + 2*personalizedButton.getHeight() );

        addUIListeners();

        stage.addActor(title);
        stage.addActor(personalizedButton);
        stage.addActor(nonPersonalizedButton);
        stage.addActor(policyButton);

        //to allow stage to identify events
        Gdx.input.setInputProcessor(stage);
    }

    @Override
    public void show() {
        stage.getRoot().setColor(1,1,1,0);
        stage.getRoot().addAction(Actions.fadeIn(0.2f));
    }

    private void addUIListeners(){

        personalizedButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                game.adInterface.setConsentStatus(AdInterface.PERSONALIZED_AD);
                returnToPrevScreen();
            }
        });

        nonPersonalizedButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                game.adInterface.setConsentStatus(AdInterface.NON_PERSONALIZED_AD);
                returnToPrevScreen();
            }
        });

        policyButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                Gdx.net.openURI("https://halogengames.wordpress.com/");
            }
        });
    }

    private void returnToPrevScreen(){
        Gdx.input.setInputProcessor(null);
        game.soundManager.playButtonTap();
        dispose();
        game.setScreen(prevScreen);
//        MotionEngine.fadeOut(stage.getRoot(),this,prevScreen,true);
    }

    @Override
    public void render(float delta) {
        stage.act();

        Gdx.gl.glClearColor(GameData.clearColor.r, GameData.clearColor.g, GameData.clearColor.b, GameData.clearColor.a);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        game.bg.render(delta);

        String text = "We use some of your data to improve user experience, save high scores and show ads.\nCan we continue to use your data to tailor ads for you?";

        game.batch.begin();
        this.game.assetManager.helpTextFont.draw(
                game.batch,
                text,
                Poof.VIEW_PORT.getWorldWidth()*0.05f,
                title.getY() - title.getHeight(),
                Poof.VIEW_PORT.getWorldWidth()*0.9f,
                Align.center,
                true
        );
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
        System.out.println("Options Screen Disposed");
        stage.dispose();
    }
}
