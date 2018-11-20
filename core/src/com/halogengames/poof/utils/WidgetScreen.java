//package com.halogengames.poof.utils;
//
//import com.badlogic.gdx.graphics.g2d.Batch;
//import com.badlogic.gdx.graphics.g2d.NinePatch;
//import com.badlogic.gdx.math.Interpolation;
//import com.badlogic.gdx.math.Vector2;
//import com.badlogic.gdx.scenes.scene2d.Actor;
//import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
//import com.badlogic.gdx.scenes.scene2d.ui.WidgetGroup;
//import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
//import com.halogen.freegames.Poof.Poof;
//import com.halogengames.poof.Poof;
//
///*
//Defines a closable window
// */
//
//public class WidgetScreen extends WidgetGroup {
//
//    private Poof game;
//    private ImageButton closeButton;
//    private NinePatch bg;
//
//    private Interpolation interpolator;
//    private float elapsed;
//
//    //The total time taken to reach from init to final pos. If negative, reverses direction
//    private float totalTime;
//    private Vector2 initPos;
//    private Vector2 targetPos;
//
//    public WidgetScreen(Poof game){
//        this.game = game;
//
//        //todo: override setSize func and add size dependent position data (can we handle this by using scale?)
//        this.setSize(game.getViewPort().getWorldWidth()*0.8f,game.getViewPort().getWorldHeight()*0.8f);
//
//        //define params for ease in/out motion
//        initPos = new Vector2(game.getViewPort().getWorldWidth()*0.1f, -this.getHeight());
//        targetPos = new Vector2(game.getViewPort().getWorldWidth()*0.1f, game.getViewPort().getWorldHeight()*0.1f);
//        this.setPosition(initPos.x,initPos.y);
//
//        interpolator = Interpolation.sineOut;
//        elapsed = 0;
//        totalTime = 0.3f;
//
//        //create bg
//        bg = new NinePatch(game.getAssetManager().windowScrBG);
//
//        //hardcoded because widgetScreenBG is a 9 point tex whose corner won't scale up based on scr size
//        float buttSize = 25;
//        closeButton = new ImageButton(game.getAssetManager().windowCloseButtonDrawable);
//        closeButton.setSize(buttSize,buttSize);
//        float closeButtonOffset = game.getAssetManager().windowCircCenterOffset + buttSize/2;
//        closeButton.setPosition(this.getWidth() - closeButtonOffset, this.getHeight() - closeButtonOffset);
//        addUIListener();
//
//        this.addActor(closeButton);
//    }
//
//    private void addUIListener(){
//        closeButton.addListener(new ChangeListener() {
//            @Override
//            public void changed(ChangeEvent event, Actor actor) {
//                elapsed = 1;
//                totalTime *= -1;
//            }
//        });
//    }
//
//    @Override
//    public void act(float delta) {
//        super.act(delta);
//
//        //ease the window in and out
//        elapsed += delta / totalTime;
//        if(elapsed<0){
//            //form out of screen, close form
//            game.getWidgetScreenStack().pop();
//        }
//        float interpVal = interpolator.apply(Math.min(1.0f,elapsed));
//        this.setPosition((1-interpVal)*initPos.x + interpVal*targetPos.x, (1-interpVal)*initPos.y + interpVal*targetPos.y);
//    }
//
//    @Override
//    public void draw(Batch batch, float parentAlpha) {
//        bg.draw(batch,this.getX(),this.getY(),this.getWidth(),this.getHeight());
//        super.draw(batch, parentAlpha);
//    }
//}
