package com.halogengames.poof.widgets.upgrades;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.halogengames.poof.Poof;
import com.halogengames.poof.utils.items.UpgradeBar;

import static com.halogengames.poof.dataLoaders.GameData.prefs;

public class BombUpgrade extends UpgradeBar {

    public BombUpgrade(Poof game){
        super(game, 5, 10);
    }

    @Override
    public void init() {
        this.setIcon(skin.get("bombUpgradeIcon", Texture.class));

        this.setDescriptionByLevel(0,"Unlock Bomb");
        this.setDescriptionByLevel(1,"Bomb frequency increased");
        this.setDescriptionByLevel(2,"Bomb radius increase");
        this.setDescriptionByLevel(3,"Bomb frequency increased");
        this.setDescriptionByLevel(4,"Bomb radius increase");
        this.setDescriptionByLevel(5,"Fully Unlocked");

        this.setPriceByLevel(0,100);
        this.setPriceByLevel(1,500);
        this.setPriceByLevel(2,2000);
        this.setPriceByLevel(2,5000);
        this.setPriceByLevel(4,10000);

        this.setCurrLevel(prefs.getInteger("bombLvl",0));
    }

    @Override
    public void addListeners() {
        this.purchaseButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                setCurrLevel(getCurrLevel()+1);
                prefs.putInteger("bombLvl",getCurrLevel());
                prefs.flush();
            }
        });
    }
}
