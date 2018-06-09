package com.halogengames.poof.library;

import com.badlogic.gdx.scenes.scene2d.ui.Widget;
import com.halogengames.poof.Poof;

import java.util.Stack;

public class WidgetStack extends Stack<Widget>{

    private Poof game;

    public WidgetStack(Poof game){
        this.game = game;
    }

    public void draw(){
        for(int i=0;i<super.size();i++){
            super.get(i).draw(game.batch,1);
        }
    }
}
