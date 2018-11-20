//package com.halogengames.poof.utils;
//
///*
//Defines a widget cards. Widget cards can be added to a widget card list and can be flipped through like a deck
// */
//
//import com.badlogic.gdx.Gdx;
//import com.badlogic.gdx.graphics.Color;
//import com.badlogic.gdx.graphics.GL20;
//import com.badlogic.gdx.graphics.g2d.Batch;
//import com.badlogic.gdx.scenes.scene2d.Actor;
//import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
//import com.badlogic.gdx.scenes.scene2d.ui.WidgetGroup;
//import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
//import com.halogen.freegames.Poof.Poof;
//import com.halogengames.poof.Poof;
//
//public class WidgetPage extends WidgetGroup{
//    public Poof game;
//    private WidgetFlipBook parentBook;
//
//    private ImageButton prev;
//    private ImageButton next;
//
//    public WidgetPage(Poof game, WidgetFlipBook book){
//        this.game = game;
//        this.parentBook = book;
//
//        this.setSize(game.getViewPort().getWorldWidth(), game.getViewPort().getWorldHeight());
//
//        prev = new ImageButton(game.getAssetManager().prevButtonDrawable);
//        next = new ImageButton(game.getAssetManager().nextButtonDrawable);
//
//        prev.setSize(100,100);
//        prev.setPosition(100,100);
//
//        next.setSize(100,100);
//        next.setPosition(200,200);
//
//        this.addActor(prev);
//        this.addActor(next);
//
//        addUIListers();
//    }
//
//    private void addUIListers(){
//        prev.addListener(new ChangeListener() {
//            @Override
//            public void changed(ChangeEvent event, Actor actor) {
//                parentBook.showPreviousPage();
//            }
//        });
//
//        next.addListener(new ChangeListener() {
//            @Override
//            public void changed(ChangeEvent event, Actor actor) {
//                parentBook.showNextPage();
//            }
//        });
//    }
//
//    public void setNextButtonVisible(Boolean status){
//        //next.setVisible(status);
//    }
//
//    public void setPrevButtonVisible(Boolean status){
//        //prev.setVisible(status);
//    }
//
//    @Override
//    public void draw(Batch batch, float parentAlpha) {
//        Color clearColor = Color.RED;
//        Gdx.gl.glClearColor(clearColor.r, clearColor.g, clearColor.b, clearColor.a);
//        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
//        super.draw(batch, parentAlpha);
//        this.drawChildren(batch,parentAlpha);
//    }
//}
