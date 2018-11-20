package com.halogengames.poof.widgets.upgrades;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.halogengames.poof.Poof;
import com.halogengames.poof.utils.items.UpgradeBar;

import static com.halogengames.poof.dataLoaders.GameData.prefs;

public class TimerUpgrade extends UpgradeBar {

    public TimerUpgrade(Poof game){
        super(game, 5, 10);
    }

    @Override
    public void init() {
        this.setIcon(skin.get("timerUpgradeIcon", Texture.class));

        this.setDescriptionByLevel(0,"Clock gives 3 seconds");
        this.setDescriptionByLevel(1,"Clocks appear more often");
        this.setDescriptionByLevel(2,"Clock gives 5 seconds");
        this.setDescriptionByLevel(3,"Frequency increased");
        this.setDescriptionByLevel(4,"Clock gives 7 seconds");
        this.setDescriptionByLevel(5,"Fully Unlocked");

        this.setPriceByLevel(0,100);
        this.setPriceByLevel(1,500);
        this.setPriceByLevel(2,2000);
        this.setPriceByLevel(2,5000);
        this.setPriceByLevel(4,10000);

        this.setCurrLevel(prefs.getInteger("timerLvl",0));
    }

    @Override
    public void addListeners() {
        this.purchaseButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                setCurrLevel(getCurrLevel()+1);
                prefs.putInteger("timerLvl",getCurrLevel());
                prefs.flush();
            }
        });
    }
}
