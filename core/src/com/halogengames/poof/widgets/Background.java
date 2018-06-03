package com.halogengames.poof.widgets;

import com.halogengames.poof.dataLoaders.PoofAssetManager;
import com.halogengames.poof.Poof;

/**
 * renders a dynamic background which is shared between menu screen, options screen etc.
 */

public class Background {

    private Poof game;

    public Background(Poof game){
        this.game = game;
    }

    public void render(float delta) {
        Poof.EXTEND_VIEW_PORT.apply();
        game.batch.begin();
        game.batch.draw(this.game.assetManager.commonBG,0,0,Poof.EXTEND_VIEW_PORT.getWorldWidth(),Poof.EXTEND_VIEW_PORT.getWorldHeight());
        game.batch.end();

        Poof.VIEW_PORT.apply();
    }
}
