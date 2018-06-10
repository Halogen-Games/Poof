package com.halogengames.poof.library;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.utils.Queue;
import com.halogengames.poof.Poof;

import java.util.ArrayList;

public class SpriteList extends ArrayList<Sprite> {

    private Poof game;

    public SpriteList(Poof game){
        this.game = game;
    }

    public void remove(Sprite sp){
        if(super.indexOf(sp)!= -1) {
            super.remove(sp);
        }
    }

    public void draw(){
        if(super.size() > 0){
            game.batch.begin();
            game.batch.setProjectionMatrix(Poof.CAM.combined);
            for(int i=0;i<super.size();i++){
                super.get(i).draw(game.batch);
            }
            game.batch.end();
        }
    }
}
