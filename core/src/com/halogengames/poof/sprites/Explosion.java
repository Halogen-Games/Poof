package com.halogengames.poof.sprites;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.math.Vector2;
import com.halogengames.poof.Poof;
import com.halogengames.poof.dataLoaders.SoundManager;

import java.util.ArrayList;

public class Explosion extends Sprite {
    private Poof game;
    private ArrayList<Sprite> spritesList;

    private float elapsed;
    private Vector2 pos;

    private float currRad;
    private float maxRad;
    private float expandTime;

    private float birthTime;
    private float age;
    private float lifeLength;

    private Animation<TextureRegion> anim;

    public Explosion(Poof game, ArrayList<Sprite> spritesList,  float maxRad){
        this.game = game;
        this.spritesList = spritesList;

        elapsed = 0;
        this.pos = pos;

        currRad = 0;
        this.maxRad = maxRad;
        expandTime = 0.25f;
        birthTime = 0;
        age = 0;
        lifeLength = Float.MAX_VALUE;

        anim = game.assetManager.spriteAnims.get("explosion");
    }

    public void setLifeLength(float len){
        this.lifeLength = len;
    }

    public void update(){
        float dt = Gdx.graphics.getDeltaTime();
        elapsed += dt;
        age = elapsed - birthTime;

        if(age > 0 && age-dt < 0){
            //just born
            game.soundManager.playExplosionSound();
        }

        if(age>lifeLength){
            spritesList.remove(this);
        }

        currRad = Interpolation.pow2Out.apply(Math.min(age/expandTime,1.0f)) * maxRad;
    }

    @Override
    public void draw(Batch batch) {
        update();
        batch.setColor(1,1,1,1);
        if(age>0) {
            //draw
            batch.draw(
                    anim.getKeyFrame(age),
                    getX() - currRad/2,
                    getY() - currRad/2,
                    currRad,
                    currRad
            );
        }
    }
}
