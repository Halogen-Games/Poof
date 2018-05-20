package com.halogengames.poof.Data;

import com.badlogic.gdx.utils.ArrayMap;
import com.halogengames.poof.widgets.GameBoard;

import java.util.Random;

/**
 * Created by Rohit on 05-10-2017.
 * Handles effects of powerups when used in a match
 */

public class TilePower {
    static ArrayMap<String,Float> powerProbs;

    public static void init(){
        //todo: check if below ArrayMap.keys call stays ordered
        powerProbs = new ArrayMap<String, Float>();
        powerProbs.put("timer", 0.03f);
        //powerProbs.put("coin", 0.01f);
        //powerProbs.put("gem", 0.001f);

        float probSum = 0;
        for (String power:powerProbs.keys()) {
            probSum += powerProbs.get(power);
            powerProbs.put(power,probSum);
        }
        if(probSum>1){
            throw new Error("Sum of probs should be < 1");
        }
        powerProbs.put(null,1.0f);
    }

    public static String generatePower(){
        Random generator = new Random();
        float rand = generator.nextFloat();

        for(String power:powerProbs.keys()){
            if(rand<powerProbs.get(power)){
                return power;
            }
        }
        throw new Error("rand is larger than total cumulative prob of powers");
    }

    //todo: make it board dependent (for ex in bombs)
    public static void unleashPower(GameBoard board, String power){
        if( power == null ){
            return;
        }

        if(power.equals("timer")){
            GameData.levelTimer += 5;
        }
    }
}
