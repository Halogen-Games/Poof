package com.halogengames.poof.scenes;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.halogengames.poof.Data.AssetManager;
import com.halogengames.poof.Poof;

/**
 * renders a dynamic background which is shared between menu screen, options screen etc.
 */

public class Background {

    private  Poof game;

    public Background(Poof game){
        this.game = game;

    }

    public void render(float delta) {
        game.batch.begin();
        game.batch.draw(AssetManager.commonBG,0,0,Poof.VIEW_PORT.getWorldWidth(),Poof.VIEW_PORT.getWorldHeight());
        game.batch.end();
    }
}
