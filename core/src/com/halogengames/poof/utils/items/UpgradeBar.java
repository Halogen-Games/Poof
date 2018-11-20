package com.halogengames.poof.utils.items;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.NinePatch;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.ui.ImageTextButton;
import com.badlogic.gdx.scenes.scene2d.ui.ImageTextButton.ImageTextButtonStyle;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Label.LabelStyle;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Slider;
import com.badlogic.gdx.scenes.scene2d.ui.Slider.SliderStyle;
import com.badlogic.gdx.utils.Align;
import com.halogengames.poof.Poof;

import java.util.ArrayList;
import java.util.Collections;

public abstract class UpgradeBar extends PurchasableItem {
    protected Poof game;
    protected Skin skin;

    private int maxLevels;
    private int currLevel = -1;

    private ArrayList<String> descriptionsByLevel;
    private ArrayList<Integer> pricesByLevel;

    //UI elements
    private NinePatch bg;
    private Slider levelMeter;
    protected ImageTextButton purchaseButton;
    private Label descLabel;

    private float margin;
    private float unitSize;

    public UpgradeBar(Poof game, int maxLvls, float innerMargin){
        this.game = game;
        this.margin = innerMargin;

        //skin is just like a hash map storing multiple items
        this.skin = game.getAssetManager().shopSkin;
        bg = skin.getPatch("upgradeBG");

        //create arrays of length 1 + mexLevel as curr level can take values from 0 to maxLevel
        this.maxLevels = maxLvls;
        this.descriptionsByLevel = new ArrayList<String>(Collections.nCopies(maxLevels+1,"Unknown Description"));
        this.pricesByLevel = new ArrayList<Integer>(Collections.nCopies(maxLevels+1,0));

        this.levelMeter = new Slider(0,maxLvls,1,false,skin.get("upgradeSliderStyle",SliderStyle.class));
        levelMeter.setTouchable(Touchable.disabled);
        this.addActor(levelMeter);

        purchaseButton = new ImageTextButton(Integer.toString(getPrice()),skin.get("purchaseButtonStyle", ImageTextButtonStyle.class));
        purchaseButton.align(Align.left);
        this.addActor(purchaseButton);

        descLabel = new Label(getDescription(),skin.get("descriptionStyle",LabelStyle.class));
        descLabel.setWrap(true);
        this.addActor(descLabel);

        init();
        addListeners();
    }

    public void setCurrLevel(int level){
        if(level <= maxLevels) {
            this.currLevel = level;
            this.setDescription(descriptionsByLevel.get(level));
            this.setPrice(pricesByLevel.get(level));

            //set the value of the meter
            if(this.levelMeter != null){
                this.levelMeter.setValue(level);
            }

            if(currLevel == maxLevels){
                this.removeActor(purchaseButton);
            }
        }else{
            throw(new RuntimeException("trying to set a level higher than max level"));
        }
    }

    public int getCurrLevel() {
        return currLevel;
    }

    protected void setDescriptionByLevel(int level,String desc) {
        if(level <= maxLevels) {
            this.descriptionsByLevel.set(level,desc);
        }
    }

    protected void setPriceByLevel(int level,int price) {
        if(level <= maxLevels) {
            this.pricesByLevel.set(level,price);
        }
    }

    public abstract void init();
    public abstract void addListeners();

    @Override
    public void setDimensions(float x, float y, float width, float height){
        unitSize = height - 2*margin;

        this.setPosition(x,y);
        this.setSize(width,height);

        //set purchase button dimension
        this.purchaseButton.setSize(this.unitSize*2, this.unitSize/1.5f);
        this.purchaseButton.setPosition(getWidth() - margin - 2*unitSize,(this.getHeight()-this.purchaseButton.getHeight())/2);

        //set level meter dimension
        this.levelMeter.setSize(width - 3*unitSize - 4*margin,levelMeter.getPrefHeight());
        this.levelMeter.setPosition(unitSize + margin*2,margin);

        //label
        this.descLabel.setSize(width - 3*unitSize - 4*margin,unitSize - levelMeter.getHeight());
        this.descLabel.setPosition(unitSize + margin*2,margin + unitSize - this.descLabel.getHeight());
    }

    @Override
    public void act(float delta) {
        super.act(delta);

        levelMeter.setValue(getCurrLevel());
        purchaseButton.setText(Integer.toString(getPrice()));
        descLabel.setText(getDescription());
    }



    @Override
    public void draw(Batch batch, float parentAlpha) {
        //draw bg
        bg.draw(batch,getX(),getY(),getWidth(),getHeight());

        super.draw(batch, parentAlpha);

        //Draw Icon
        batch.draw(getIcon(),getX()+ margin,getY() + margin,unitSize,unitSize);
    }
}
