package com.halogengames.poof.scenes;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture.TextureFilter;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator.FreeTypeFontParameter;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Label.LabelStyle;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.halogengames.poof.Data.GameData;
import com.halogengames.poof.Poof;


/**
 * Created by Rohit on 13-08-2017.
 *
 * The hud contains the score, timer and current level details
 */

public class Hud {

    //Font
    private final BitmapFont textLabelFont;
    private final BitmapFont valLabelFont;

    private final Stage stage;

    private final Label timeLabel;
    private final Label scoreLabel;

    public Hud(SpriteBatch sb){

        stage = new Stage(Poof.VIEW_PORT, sb);

        //use a table to align objects on stage
        Table table = new Table();
        //align table to top of stage
        table.top();
        //set table to size of stage
        table.setFillParent(true);

        //Font
        FreeTypeFontParameter fontParam = new FreeTypeFontParameter();
        fontParam.minFilter = TextureFilter.Linear;
        fontParam.magFilter = TextureFilter.Linear;
        fontParam.size = (int)(20 * Poof.V_WIDTH/GameData.baseWidth);
        textLabelFont = Poof.valueFontGenerator.generateFont(fontParam);
        valLabelFont = Poof.labelFontGenerator.generateFont(fontParam);

        //Styles
        LabelStyle textLabelStyle = new LabelStyle(textLabelFont, Color.DARK_GRAY);
        LabelStyle valLabelStyle = new LabelStyle(valLabelFont, Color.DARK_GRAY);

        //define the labels
        Label timeTextLabel = new Label("TIME", textLabelStyle);
        timeLabel = new Label(String.valueOf((int)Math.ceil(GameData.levelTimer)), valLabelStyle);
        Label scoreTextLabel = new Label("SCORE", textLabelStyle);
        scoreLabel = new Label(String.valueOf(GameData.score), valLabelStyle);
        Label highscoreTextLabel = new Label("Best", textLabelStyle);
        Label highscoreLabel = new Label(String.valueOf(GameData.prefs.getInteger("highScore", 0)), valLabelStyle);

        table.add(highscoreTextLabel).expandX().padTop(Poof.V_HEIGHT*0.01f);
        table.add(scoreTextLabel).expandX().padTop(Poof.V_HEIGHT*0.01f);
        table.add(timeTextLabel).expandX().padTop(Poof.V_HEIGHT*0.01f);
        table.row();
        table.add(highscoreLabel).expandX();
        table.add(scoreLabel).expandX();
        table.add(timeLabel).expandX();

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
        textLabelFont.dispose();
        valLabelFont.dispose();
        stage.dispose();
    }
}
