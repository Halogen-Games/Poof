package com.halogengames.poof.scenes;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.halogengames.poof.Poof;

/**
 * renders a dynamic background which is shared between menu screen, options screen etc.
 */

public class Background {

    private final Poof game;
    private float x,y;
    private int vel;

    public Background(Poof game){
        this.game = game;
        x=0;
        y=0;
        vel = 30;
    }

    private void update(float dt){
        x += dt*vel;
        y += dt*vel;
    }

    public void render(float delta) {
//        update(delta);
//
//        game.renderer.setProjectionMatrix(Poof.CAM.combined);
//        game.renderer.begin(ShapeRenderer.ShapeType.Filled);
//        game.renderer.setColor(Color.GREEN);
//        game.renderer.rect(x,y,100,100);
//        game.renderer.end();
    }
}
