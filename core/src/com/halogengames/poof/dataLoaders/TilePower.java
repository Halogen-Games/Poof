package com.halogengames.poof.dataLoaders;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.ArrayMap;
import com.halogengames.poof.library.RandomBag;
import com.halogengames.poof.sprites.Tile;
import com.halogengames.poof.widgets.GameBoard;

import java.util.ArrayList;
import java.util.Random;

import static com.halogengames.poof.dataLoaders.GameData.prefs;

/**
 * Created by Rohit on 05-10-2017.
 * Handles effects of powerups when used in a match
 */

//ove tile powers to tile class
public class TilePower {
    private static RandomBag powers;

    private static int bombRad;
    private static int timerVal;

    public static void init(){
        powers = new RandomBag(1000);

        //initPowerProbs();

        bombRad = 0;
        timerVal = 0;
    }

    public static ArrayList<String> getPossiblePowersList(){
        ArrayList<String> rv = new ArrayList<String>();

        rv.add("bomb");
        rv.add("timer");
        rv.add("rock");

        return rv;
    }

    public static void setPowerProb(String power, float prob){
        powers.setItemProb(power, prob);
    }

    private static void initPowerProbs(){

        initTimer();

        initBomb();
    }

    private static void initTimer(){
        int currTimerLvl = prefs.getInteger("timerLvl",0);
        switch(currTimerLvl){
            case 0:
                setPowerProb("timer",0);
                break;
            case 1:
                setPowerProb("timer",0.015f);
                timerVal = 3;
                break;
            case 2:
                setPowerProb("timer",0.03f);
                timerVal = 3;
                break;
            case 3:
                setPowerProb("timer",0.03f);
                timerVal = 5;
                break;
            case 4:
                setPowerProb("timer",0.04f);
                timerVal = 5;
                break;
            case 5:
                setPowerProb("timer",0.04f);
                timerVal = 7;
                break;
            default:throw new RuntimeException("Unknown level for timer upgrade");
        }
    }

    private static void initBomb(){
        int currBombLvl = prefs.getInteger("bombLvl",0);
        switch(currBombLvl){
            case 0:
                setPowerProb("bomb",0);
                bombRad = 0;
                break;
            case 1:
                setPowerProb("bomb",0.01f);
                bombRad = 1;
                break;
            case 2:
                setPowerProb("bomb",0.02f);
                bombRad = 1;
                break;
            case 3:
                setPowerProb("bomb",0.02f);
                bombRad = 2;
                break;
            case 4:
                setPowerProb("bomb",0.03f);
                bombRad = 2;
                break;
            case 5:
                setPowerProb("bomb",0.03f);
                bombRad = 3;
                break;
            default:throw new RuntimeException("Unknown level for bomb upgrade");
        }
    }

    public static String generatePower(){
        return powers.getItem();
    }

    public static void unleashPower(GameBoard board, Tile tile){
        //Todo: add bomb sound and animation(will possibly need sound and sprite queue with delayed addition)
        String power = tile.tilePower;
        if( power == null || power == "rock"){
            return;
        }

        if(power.equals("timer")){
            GameData.levelTimer += 5;
        }

        if(power.equals("bomb")){
            Vector2 pos = tile.getCoordinates();

            board.addExplosion(tile);

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
