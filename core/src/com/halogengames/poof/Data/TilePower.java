package com.halogengames.poof.Data;

import com.badlogic.gdx.utils.ArrayMap;
import com.badlogic.gdx.utils.IntFloatMap;
import com.badlogic.gdx.utils.ObjectMap;
import com.halogengames.poof.scenes.GameBoard;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

/**
 * Created by Rohit on 05-10-2017.
 */

public class TilePower {
    public static ArrayMap<String,Float> powerProbs;

    public static void init(){
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

    public static void unleashPower(String power){
        if(power == "timer"){
            GameData.levelTimer += 5;
        }else if(power == "coin"){
            GameData.coins += 5;
        }else if (power == "gem"){
            GameData.gems++;
        }

        GameData.score++;
    }
}
