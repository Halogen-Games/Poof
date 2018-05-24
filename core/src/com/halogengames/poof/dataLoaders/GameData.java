package com.halogengames.poof.dataLoaders;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.utils.Array;
import com.halogengames.poof.sprites.Tile;

import java.util.ArrayList;

/**
 * Created by Rohit on 13-08-2017.
 * All the members should ideally be static
 */

public class GameData {
    public static double levelTimer;
    private static int maxTime;
    public static int score;
    private static int numColors;
    private static String levelName;

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
        numColors = 3;

        clearColor = new Color(1,1,1,1);

        allTileColors = new Array<String>();
        allTileColors.add("blue");
        allTileColors.add("green");
        allTileColors.add("indigo");
        allTileColors.add("red");
        allTileColors.add("yellow");
    }

    public static int getHighScore(){
        //back support for the key highscore
        int defVal = 0;
        if(levelName.equals("easy")) {
            defVal = GameData.prefs.getInteger("highScore", 0);
        }
        return GameData.prefs.getInteger("highScore_" + levelName, defVal);
    }

    public static void setHighScore(){
        GameData.prefs.putInteger("highScore_" + levelName, score);
        GameData.prefs.flush();
    }

    public static void setLevel(String level){
        if(level.equals("easy")){
            levelName = level;
            numColors = 3;
        }else if(level.equals("medium")){
            levelName = level;
            numColors = 4;
        }else if(level.equals("hard")){
            levelName = level;
            numColors = 5;
        }else{
            throw new Error("unknown level selected: " + level);
        }
    }

    public static void updateScore(int chainLength, ArrayList<Tile> selectedTiles){
        score += selectedTiles.size();
        if( selectedTiles.size() > 5){
            score += Math.pow(chainLength-5,1.2);
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
