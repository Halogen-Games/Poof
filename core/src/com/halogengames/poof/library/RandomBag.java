package com.halogengames.poof.library;

import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.ArrayMap;

import java.util.Random;

public class RandomBag {

    private ArrayMap<String,Float> itemProbs;

    private Array<String> bag;
    private int bagSize;
    private int currItemInd;

    private float itemCountDeviation;

    public RandomBag(int bagSize){
        this.bagSize = bagSize;
        this.itemCountDeviation = 0;

        itemProbs = new ArrayMap<String, Float>();
        bag = new Array<String>();
        fillBag();
    }

    public RandomBag(int bagSize, float itemCountDeviation){
        this.bagSize = bagSize;
        this.itemCountDeviation = itemCountDeviation;

        itemProbs = new ArrayMap<String, Float>();
        bag = new Array<String>();
        fillBag();
    }

    private void shuffleBag(){
        for(int i=0; i<10; i++){
            bag.shuffle();
        }
    }

    private void initBag(){
        bag = new Array<String>();
        for(int i=0;i<bagSize;i++){
            bag.add(null);
        }
    }

    private void fillBag(){
        Random rand = new Random();

        //empty the bag
        initBag();

        //add the items in the bag
        for(String item:itemProbs.keys()){
            //number of this item in bag
            int numItems = (int)(itemProbs.get(item)*bagSize);

            //fudge number of items
            numItems = (int)(numItems*(1-itemCountDeviation) + rand.nextFloat()*2*itemCountDeviation);

            addItemToBag(item,numItems);
        }

        shuffleBag();
    }

    private void addItemToBag(String item, int count){
        for(int i=0; i<bag.size && count>0; i++){
            if(bag.get(i)==null){
                bag.set(i,item);
                count--;
            }
        }
        if(count>0){
            throw new Error("Can't add item to bag, no space left");
        }
    }

    public void setItemProb(String item, float prob){
        float oldProb = 0;
        if(itemProbs.containsKey(item)){
            oldProb = itemProbs.get(item);
        }

        //check to ensure that prob*bagSize is whole number
        if(prob*bagSize%1 != 0){
            throw new Error("prob * bagSize should be whole number");
        }

        itemProbs.put(item,prob);
        addItemToBag(item,(int)((prob-oldProb)*bagSize));

        shuffleBag();
    }

    public String getItem(){
        if(bag.size == 0){
            fillBag();
        }
        return bag.pop();
    }
}
