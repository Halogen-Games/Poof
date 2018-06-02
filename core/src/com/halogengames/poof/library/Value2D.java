package com.halogengames.poof.library;

import com.badlogic.gdx.math.Vector2;

import java.util.ArrayList;

public class Value2D extends Vector2{
    public ArrayList<Vector2> timeDerivatives;

    public Value2D(float x, float y){
        this.x = x;
        this.y = y;

        timeDerivatives = new ArrayList<Vector2>();
    }

    public void update(float dt){
        if(timeDerivatives.size()==0){
            return;
        }
        for(int i=timeDerivatives.size()-1; i>0; i--){
            timeDerivatives.get(i-1).add(timeDerivatives.get(i));
        }
        this.x += timeDerivatives.get(0).x;
        this.y += timeDerivatives.get(0).y;
    }
}
