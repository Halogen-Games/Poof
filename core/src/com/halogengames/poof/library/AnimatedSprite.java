package com.halogengames.poof.library;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.ui.Widget;

public class AnimatedSprite extends Widget {

    public enum TweenAnimation{
        Springing, None
    }

    private TweenAnimation currentTween;

    //coordinate data
    private Vector2 anchorPos;
    private Vector2 currPos;

    private Value2D springAmplitude;
    private Value2D springFrequency;
    private Value2D springTheta;

    public AnimatedSprite(){
        springAmplitude = new Value2D(0,0);
        springFrequency = new Value2D(0,0);
        springTheta = new Value2D(0,0);

        currentTween = TweenAnimation.None;
    }

    @Override
    public void setPosition(float x, float y) {
        super.setPosition(x, y);
        anchorPos = new Vector2(getX(),getY());
    }

    public void setSpringingAnimation(Vector2 amplitude, Vector2 frequency, Boolean restart) {
        if(currentTween == TweenAnimation.Springing && !restart){
            return;
        }
        springAmplitude.set(amplitude);
        springFrequency.set(frequency);
        springTheta.set(0,90);

        anchorPos = new Vector2(getX(),getY());

        springTheta.timeDerivatives.add(new Vector2((float)Math.PI*2/frequency.x,(float)Math.PI*2/frequency.x));

        currentTween = TweenAnimation.Springing;
    }

    public void endTweenAnimation(){
        super.setPosition(anchorPos.x,anchorPos.y);
        currentTween = TweenAnimation.None;
    }

    public void applyTween(float dt){
        switch(currentTween){
            case None:break;
            case Springing:{
                //update tween params
                springTheta.update(dt);

                //get pos
                float xPos = (float)(anchorPos.x + springAmplitude.x*Math.cos(springTheta.x*Math.PI/180));
                float yPos = (float)(anchorPos.y + springAmplitude.x*Math.sin(springTheta.y*Math.PI/180));

                super.setPosition(xPos,yPos);
                break;
            }
            default:throw(new RuntimeException("Unknown Tween Animation"));
        }
    }

}
