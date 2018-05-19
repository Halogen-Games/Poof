package com.halogengames.poof.Data;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.utils.Array;
import com.halogengames.poof.sprites.Tile;

import java.util.ArrayList;
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
    static float baseWidth;

    public static int numBoardCols;
    public static int numBoardRows;

    //GL Clear Color
    public static Color clearColor;

    //Valid Colors
    static Array<String> allTileColors;
    public static Array<String> validTileColors;

    public static Preferences prefs;

    public static void init(){
        prefs = Gdx.app.getPreferences("preferences");

        baseWidth = 540;

        maxTime = 60;

        numBoardCols = 6;
        numBoardRows = 6;
        numColors = 3 ;

        clearColor = new Color(1,1,1,1);

        allTileColors = new Array<String>();
        allTileColors.add("blue");
        allTileColors.add("green");
        allTileColors.add("indigo");
        allTileColors.add("red");
        allTileColors.add("yellow");
    }

    public static void updateScore(ArrayList<Tile> selectedTiles){
        score += selectedTiles.size();
        if( selectedTiles.size() > 5){
            score += Math.pow(selectedTiles.size()-5,1.2);
        }
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
