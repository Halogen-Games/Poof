package com.halogengames.poof.widgets;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.scenes.scene2d.utils.NinePatchDrawable;
import com.halogengames.poof.dataLoaders.PoofAssetManager;
import com.halogengames.poof.dataLoaders.GameData;
import com.halogengames.poof.Poof;


/**
 * Created by Rohit on 13-08-2017.
 *
 * The hud contains the score, timer and current level details
 */

public class Hud {
    private Poof game;
    private Stage stage;

    private Label timeLabel;
    private Label scoreLabel;

    public Hud(Poof game){
        this.game = game;
        stage = new Stage(Poof.VIEW_PORT, game.batch);

        //use a table to align objects on stage
        Table table = new Table();
        //align table to top of stage
        table.top();
        //set table to size of stage
        //table.setFillParent(true);

        //define the labels
        Label timeTextLabel = new Label("TIME", this.game.assetManager.hudLabelStyle);
        timeLabel = new Label(String.valueOf((int)Math.ceil(GameData.levelTimer)), this.game.assetManager.hudLabelStyle);
        Label scoreTextLabel = new Label("SCORE", this.game.assetManager.hudLabelStyle);
        scoreLabel = new Label(String.valueOf(GameData.score), this.game.assetManager.hudLabelStyle);
        Label highscoreTextLabel = new Label("BEST", this.game.assetManager.hudLabelStyle);
        Label highscoreLabel = new Label(String.valueOf(GameData.getHighScore()), this.game.assetManager.hudLabelStyle);

        table.add(highscoreTextLabel).expandX();
        table.add(scoreTextLabel).expandX();
        if(GameData.getGameMode() == GameData.GameMode.Timed){
            table.add(timeTextLabel).expandX();
        }

        table.row();

        table.add(highscoreLabel).expandX();
        table.add(scoreLabel).expandX();
        if(GameData.getGameMode() == GameData.GameMode.Timed){
            table.add(timeLabel).expandX();
        }

        table.setBackground(game.assetManager.UISkin.get("blankNinePatchBG",NinePatchDrawable.class));

        table.pack();
        table.setWidth(Poof.V_WIDTH*0.95f);
        table.setPosition(Poof.V_WIDTH/2 - table.getWidth()/2,Poof.V_HEIGHT-Poof.V_WIDTH*0.025f-table.getHeight());

        stage.addActor(table);
    }

    public void draw(){
        update();

        stage.draw();
    }

    private void update() {
        scoreLabel.setText(String.valueOf(GameData.score));
        timeLabel.setText(String.valueOf((int)Math.ceil(GameData.levelTimer)));
    }

    public void dispose(){
        stage.dispose();
    }
}
