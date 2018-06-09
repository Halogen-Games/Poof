package com.halogengames.poof.dataLoaders;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.ArrayMap;
import com.halogengames.poof.sprites.Tile;
import com.halogengames.poof.widgets.GameBoard;

import java.util.ArrayList;
import java.util.Random;

/**
 * Created by Rohit on 05-10-2017.
 * Handles effects of powerups when used in a match
 */

//ove tile powers to tile class
public class TilePower {
    private static ArrayMap<String,Float> powerProbs;
    private static ArrayMap<String,Float> cumPowerProbs;

    public static void init(){
        powerProbs = new ArrayMap<String, Float>();
        cumPowerProbs = new ArrayMap<String, Float>();

        setPowerProb("timer", 0.03f);
        //powerProbs.put("bomb", 0.0f);
        //powerProbs.put("gem", 0.001f);
    }

    public static ArrayMap.Keys<String> getPowersList(){
        return powerProbs.keys();
    }

    private static void resetCumPowerProbs(){
        float probSum = 0;
        //below foreach always runs through same order because it's an ArrayMap
        for (String power: powerProbs.keys()) {
            probSum += powerProbs.get(power);
            cumPowerProbs.put(power,probSum);
        }
        if(probSum>1){
            throw new Error("Sum of probs should be < 1");
        }
        cumPowerProbs.put(null,1.0f);
    }

    public static void setPowerProb(String power, float prob){
        powerProbs.put(power,prob);
        resetCumPowerProbs();
    }

    public static String generatePower(){
        Random generator = new Random();
        float rand = generator.nextFloat();

        for(String power: cumPowerProbs.keys()){
            if(rand< cumPowerProbs.get(power)){
                return power;
            }
        }
        throw new Error("rand is larger than total cumulative prob of powers");
    }

    public static void unleashPower(GameBoard board, Tile tile){
        //Todo: add bomb sound and animation(will possibly need sound and sprite queue with delayed addition)
        String power = tile.tilePower;
        if( power == null ){
            return;
        }

        if(power.equals("timer")){
            GameData.levelTimer += 5;
        }

        if(power.equals("bomb")){
            Vector2 pos = tile.getCoordinates();

            ArrayList<Integer> validIMoves = new ArrayList<Integer>();
            ArrayList<Integer> validJMoves = new ArrayList<Integer>();
            validIMoves.add(0);
            validJMoves.add(0);

            if(pos.x>0){
                validIMoves.add(-1);
            }

            if(pos.x<GameData.numBoardRows-1){
                validIMoves.add(1);
            }

            if(pos.y>0){
                validJMoves.add(-1);
            }

            if(pos.y<GameData.numBoardCols-1){
                validJMoves.add(1);
            }

            for(int i:validIMoves){
                for(int j:validJMoves){
                    board.setTileAsSelected((int)pos.x+i,(int)pos.y+j);
                }
            }
        }
    }
}
