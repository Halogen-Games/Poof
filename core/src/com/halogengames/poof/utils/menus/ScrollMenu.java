package com.halogengames.poof.utils.menus;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.halogengames.poof.Poof;
import com.halogengames.poof.utils.items.PurchasableItem;

import java.util.ArrayList;

public class ScrollMenu extends SelectionMenu {
    protected Poof game;

    private float margin;
    private float itemWidth;
    private float itemHeight;

    private ArrayList<PurchasableItem> items;

    public ScrollMenu(Poof game, float margin){
        super();
        this.game = game;
        this.margin = margin;
        itemWidth = this.getWidth() - 2*margin;
        itemHeight = itemWidth/6;

        items = new ArrayList<PurchasableItem>();
    }

    @Override
    public void addCategory(String category) {

    }

    @Override
    public ArrayList<String> getCategoryList() {
        return null;
    }

    @Override
    public void addItem(PurchasableItem item) {
        this.addActor(item);
        items.add(item);

        this.setHeight(margin + (margin+itemHeight)*items.size());

        item.setDimensions(margin,this.getHeight()-(margin+itemHeight)*(items.size()-1), itemWidth,itemHeight);
    }

    @Override
    public void act(float delta) {
        itemWidth = this.getWidth() - 2*margin;
        itemHeight = itemWidth/6;
        this.setHeight(margin + (margin+itemHeight)*items.size());

        super.act(delta);
        for(int i=0;i<items.size();i++){
            items.get(i).setDimensions(margin,this.getHeight()-(margin+itemHeight)*(i+1), itemWidth,itemHeight);
        }
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        super.draw(batch, parentAlpha);
    }
}
