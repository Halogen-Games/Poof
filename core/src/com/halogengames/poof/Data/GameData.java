package com.halogengames.poof.Data;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.utils.Array;

import java.util.Arrays;

/**
 * Created by Rohit on 13-08-2017.
 * All the members should ideally be static
 */


public class GameData {
    public static double levelTimer;
    private static int maxTime;
    public static int score;
    private static int numColors;

    //things like font size are hardcoded according to this base width and then scaled appropriately based on actual width of screen
    public static float baseWidth;

    public static int numBoardCols;
    public static int numBoardRows;
    public static Array<String> validTileColors;
    public static Array<String> allTileColors;

    public static Preferences prefs;

    public static void init(){
        prefs = Gdx.app.getPreferences("preferences");

        baseWidth = 540;

        maxTime = 2;

        numBoardCols = 6;
        numBoardRows = 6;
        numColors = 3;

        allTileColors = new Array<String>();
        allTileColors.add("blue");
        allTileColors.add("green");
        allTileColors.add("indigo");
        allTileColors.add("red");
        allTileColors.add("yellow");
    }

    public static void resetData(){
        levelTimer = maxTime;

        score = 0;

        validTileColors = new Array<String>(allTileColors);
        while(validTileColors.size>numColors){
            validTileColors.removeIndex((int)Math.floor(Math.random()*validTileColors.size));
        }
    }
}
