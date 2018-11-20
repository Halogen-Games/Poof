package com.halogengames.poof.widgets;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;
import com.halogengames.poof.dataLoaders.PoofAssetManager;
import com.halogengames.poof.Poof;

import java.util.ArrayList;
import java.util.Vector;

/**
 * renders a dynamic background which is shared between menu screen, options screen etc.
 */

public class Background {

    private Poof game;
    private ArrayList<Sprite> spriteList;
    //todo: make a custom sprite object class and remove below
    private ArrayList<Vector2> velocities;
    private ArrayList<Float> rotations;

    int numShapes;

    public Background(Poof game){
        this.game = game;

        spriteList = new ArrayList<Sprite>();
        velocities = new ArrayList<Vector2>();
        rotations = new ArrayList<Float>();
        numShapes = 50;


        for(int i=0; i<numShapes;i++){
            Sprite s = new Sprite();
            s.setTexture(this.game.assetManager.bgShapes.get((int)(Math.random()*4)));
            float spSize = (float)(0.5+Math.random()/2);
            s.setSize(spSize*Poof.EXTEND_VIEW_PORT.getWorldWidth()/20, spSize*Poof.EXTEND_VIEW_PORT.getWorldWidth()/20 );

            s.setPosition((float)Math.random()*(Poof.EXTEND_VIEW_PORT.getWorldWidth()-s.getWidth()),(float)Math.random()*(Poof.EXTEND_VIEW_PORT.getWorldHeight()-s.getHeight()));

            spriteList.add(s);

            rotations.add((float)Math.random()*360);
            velocities.add(new Vector2((float)(Math.random()*2 - 1)*Poof.EXTEND_VIEW_PORT.getWorldWidth()/100, (float)(Math.random()*2 - 1)*Poof.EXTEND_VIEW_PORT.getWorldHeight()/100 ));
        }
    }

    private void update(float dt){
        for(int i=0;i<numShapes;i++){
            Sprite s = spriteList.get(i);

            s.translate(velocities.get(i).x*dt,velocities.get(i).y*dt);

            if(s.getX()<0 || s.getX() > Poof.EXTEND_VIEW_PORT.getWorldWidth() - s.getWidth()){
                velocities.get(i).x *= -1;
            }
            if(s.getY()<0 || s.getY() > Poof.EXTEND_VIEW_PORT.getWorldHeight() - s.getHeight()){
                velocities.get(i).y *= -1;
            }
        }
    }

    public void render(float delta) {
        update(delta);
        Poof.EXTEND_VIEW_PORT.apply();

        //change cam resolution
        Poof.CAM.setToOrtho(false, Poof.EXTEND_VIEW_PORT.getWorldWidth(), Poof.EXTEND_VIEW_PORT.getWorldHeight());
        game.batch.setProjectionMatrix(Poof.CAM.combined);

        game.batch.begin();
        game.batch.draw(game.assetManager.gradientBg,0,0,Poof.EXTEND_VIEW_PORT.getWorldWidth(), Poof.EXTEND_VIEW_PORT.getWorldHeight());

        for(int i=0;i<numShapes;i++){
            Sprite s = spriteList.get(i);
            TextureRegion texReg = new TextureRegion(s.getTexture());
            game.batch.draw(texReg,s.getX(),s.getY(),s.getWidth()/2,s.getHeight()/2,s.getWidth(),s.getHeight(),1,1,rotations.get(i));
        }
        game.batch.end();

        Poof.VIEW_PORT.apply();
        //set cam resolution back
        Poof.CAM.setToOrtho(false, Poof.V_WIDTH, Poof.V_HEIGHT);
    }
}
