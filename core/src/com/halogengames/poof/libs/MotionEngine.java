package com.halogengames.poof.libs;

import com.badlogic.gdx.Screen;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.actions.SequenceAction;
import com.halogengames.poof.Poof;

/**
 * Created by Rohit on 05-10-2017.
 */

public class MotionEngine {

    private static Poof game;

    public static void init(Poof game){
        MotionEngine.game = game;
    }

    public static void fadeIn(Actor actor){
        actor.setColor(1,1,1,0);
        actor.addAction(Actions.fadeIn(0.2f));
    }

    public static void fadeOut(Actor actor, Screen oldScr, Screen newScr, boolean disposeOld){
        final boolean dspsOld = disposeOld;
        final Screen oldScreen = oldScr;
        final Screen newScreen = newScr;
        actor.getColor().a = 1;
        SequenceAction sequenceAction = new SequenceAction();
        sequenceAction.addAction(Actions.fadeOut(0.2f));
        sequenceAction.addAction(Actions.run(new Runnable() {
            @Override
            public void run() {
                if(dspsOld){
                    oldScreen.dispose();
                }
                MotionEngine.game.setScreen(newScreen);
            }
        }));
        actor.addAction(sequenceAction);
    }
}
