package com.halogengames.poof.Data;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.utils.Array;

/**
 * Created by Rohit on 13-08-2017.
 * All the members should ideally be static
 */


public class GameData {
    public static double levelTimer;
    private static int maxTime;
    public static int score;
    private static int numColors;

    //things like font size are sized based on this ideal wodth and height and then scaled appropriately
    public static float baseWidth;
    public static float baseHeight;

    public static int numBoardCols;
    public static int numBoardRows;
    public static Array<String> validTileColors;

    public static Preferences prefs;

    public static void init(){
        prefs = Gdx.app.getPreferences("preferences");

        baseWidth = 540;
        baseHeight = 960;

        maxTime = 60;

        numBoardCols = 6;
        numBoardRows = 6;
        numColors = 3;
    }

    public static void resetData(){
        levelTimer = maxTime;

        score = 0;

        validTileColors = new Array<String>();
        validTileColors.add("blue");
        validTileColors.add("green");
        validTileColors.add("indigo");
        validTileColors.add("red");
        validTileColors.add("yellow");
        while(validTileColors.size>numColors){
            validTileColors.removeIndex((int)Math.floor(Math.random()*validTileColors.size));
        }
    }
}
