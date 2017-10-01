package com.halogengames.poof.scenes;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.utils.Array;
import com.halogengames.poof.Poof;

/**
 * Created by Rohit on 30-09-2017.
 */

public class Background {

    private Poof game;
    private Array<Sprite> objects;
    private int maxNumObj;
    private int x,y;

    public Background(Poof game){
        this.game = game;
        x = 0;
        y = 0;
        maxNumObj = 10;
    }

    private void update(float dt){

    }

    public void render(float delta) {
        update(delta);

        x++;
        y++;

        game.renderer.setProjectionMatrix(Poof.CAM.combined);

        game.renderer.begin(ShapeRenderer.ShapeType.Filled);
        game.renderer.setColor(Color.GREEN);
        game.renderer.rect(x,y,30,30);
        game.renderer.end();
    }
}
