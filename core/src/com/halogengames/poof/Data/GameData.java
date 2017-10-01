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
    public static int levelNumber;
    public static int worldNumber;

    public static int numBoardCols;
    public static int numBoardRows;
    public static Array<String> validTileColors;

    public static Preferences prefs;

    public static void init(){
        prefs = Gdx.app.getPreferences("preferences");

        maxTime = 60;
        score = 0;

        numBoardCols = 6;
        numBoardRows = 6;
    }

    public static void resetData(){
        levelTimer = maxTime;

        score = 0;

        validTileColors = new Array<String>();
        validTileColors.add("yellow");
        validTileColors.add("green");
        validTileColors.add("red");
        validTileColors.add("blue");
        validTileColors.add("purple");
        validTileColors.removeIndex((int)Math.floor(Math.random()*5));
        validTileColors.removeIndex((int)Math.floor(Math.random()*4));
    }
}
