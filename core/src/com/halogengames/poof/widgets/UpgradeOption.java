package com.halogengames.poof.widgets;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.Widget;
import com.halogengames.poof.Poof;


public class UpgradeOption extends Widget {
    private Poof game;

    public UpgradeOption(Poof game, Stage stage){
        this.game = game;
        stage.addActor(this);
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        game.batch.end();

        game.renderer.setProjectionMatrix(Poof.CAM.combined);
        game.renderer.begin(ShapeRenderer.ShapeType.Filled);
        game.renderer.setColor(new Color(Color.WHITE));
        game.shaper.drawRoundRectFilled(getX(),getY(),getWidth(),getHeight(),35);
        game.renderer.setColor(new Color(Color.BLACK));
        game.shaper.setLineWidth(5);
        game.shaper.drawRoundRectLine(getX(),getY(),getWidth(),getHeight(),35);
        game.renderer.end();

        game.batch.begin();
    }

    public ImageButton getUpgradeButton(){
        return null;
    }
}
