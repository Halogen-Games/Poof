package com.halogengames.poof.utils.menus;

import com.badlogic.gdx.scenes.scene2d.ui.WidgetGroup;
import com.halogengames.poof.utils.items.PurchasableItem;

import java.util.ArrayList;

public abstract class SelectionMenu extends WidgetGroup {

    public abstract void addCategory(String category);
    public abstract ArrayList<String> getCategoryList();

    public abstract void addItem(PurchasableItem item);
}
