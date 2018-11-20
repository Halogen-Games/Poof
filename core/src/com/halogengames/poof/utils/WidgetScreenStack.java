//package com.halogengames.poof.utils;
//
//import com.badlogic.gdx.Gdx;
//import com.badlogic.gdx.scenes.scene2d.Stage;
//import com.badlogic.gdx.scenes.scene2d.Touchable;
//import com.halogengames.poof.Poof;
//
//import java.util.Stack;
//
//public class WidgetScreenStack {
//    private Poof game;
//    private Stack<WidgetScreen> _stack;
//    private Stage stage;
//
//    public WidgetScreenStack(Poof game){
//        this.game = game;
//        _stack = new Stack<WidgetScreen>();
//        stage = new Stage(game.getViewPort(),game.getBatch());
//    }
//
//    private void update(float dt){
//        //only update the top most element as others are supposed to be paused
//        _stack.peek().act(dt);
//    }
//
//    public void draw(){
//        if(!_stack.isEmpty()) {
//            //draw stack if not empty
//            update(Gdx.graphics.getDeltaTime());
//            game.getBatch().setProjectionMatrix(game.getCam().combined);
//            stage.draw();
//        }
//    }
//
//    //fixme: pause and resume of screen are also called when screen turns off, this causes bugs. Handle this
//
//    //push a widget to draw stack and set input processor to it
//    public void push(WidgetScreen w){
//        System.out.println("Widget Pushed");
//        if(w != null) {
//            if(_stack.isEmpty()) {
//                //pushing first widget, pause underlying screen, make stage active
//                game.getScreen().pause();
//                Gdx.input.setInputProcessor(stage);
//            }else{
//                //set the last child untouchable
//                _stack.peek().setTouchable(Touchable.disabled);
//            }
//            _stack.push(w);
//            stage.addActor(w);
//            w.setTouchable(Touchable.enabled);
//        }
//    }
//
//    public void pop(){
//        System.out.println("Widget Popped");
//
//        //remove top from stage
//        stage.getRoot().removeActor(_stack.pop());
//
//        if(_stack.isEmpty()){
//            //no widgets left to draw, resume current screen
//            Gdx.input.setInputProcessor(null);
//            game.getScreen().resume();
//        }else{
//            //enable top's input if stack non empty
//            _stack.peek().setTouchable(Touchable.enabled);
//        }
//    }
//}
