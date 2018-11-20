package com.halogengames.poof.utils.items;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.ui.WidgetGroup;

public abstract class PurchasableItem extends WidgetGroup {
    private Texture icon;
    private int price;
    private String description;

    protected void setIcon(Texture icon){
        this.icon = icon;
    }

    Texture getIcon() {
        return icon;
    }

    void setPrice(int price){
        this.price = price;
    }

    int getPrice(){
        return price;
    }

    void setDescription(String desc){
        this.description = desc;
    }

    String getDescription() {
        return description;
    }

    public abstract void setDimensions(float x, float y, float width, float height);
}
