package com.halogengames.poof.dataLoaders;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.utils.Array;
import com.halogengames.poof.Poof;
import com.halogengames.poof.sprites.Tile;

import java.util.ArrayList;
import java.util.UUID;

/**
 * Created by Rohit on 13-08-2017.
 * All the members should ideally be static
 */

public class GameData {
    private static Poof game;

    //final vars
    final public static int NO_NETWORK = -2;

    public static double levelTimer;
    private static int maxTime;
    private static int numColors;
    private static String levelName;
    private static String gameMode;

    //things like font size are hardcoded according to this base width and then scaled appropriately based on actual width of screen
    static float baseWidth;

    public static int numBoardCols;
    public static int numBoardRows;

    //GL Clear Color
    public static Color clearColor;

    //Valid Colors
    static Array<String> allTileColors;
    public static Array<String> validTileColors;

    //player data
    public static Preferences prefs;
    private static String playerID;
    public static int score;

    //todo: find a better way than using arrays below
    public static int[] worldRank;
    public static int[] numGlobalPlayers;

    public static int gamesPlayed;
    public static boolean showTutorial;

    public static void init(Poof game_handle){
        game = game_handle;

        prefs = Gdx.app.getPreferences("preferences");

        //Init player id
        if(prefs.contains("playerID")){
            playerID = prefs.getString("playerID");
        }else{
            playerID = UUID.randomUUID().toString();
            prefs.putString("playerID",playerID);
            prefs.flush();
        }

        showTutorial = prefs.getBoolean("showTutorial", true);

        gameMode = "classic";
        worldRank = new int[1];
        worldRank[0] = -1;
        numGlobalPlayers = new int[1];
        numGlobalPlayers[0] = -1;

        gamesPlayed = 0;

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

    public static void tutorialShown(){
        showTutorial = false;
        prefs.putBoolean("showTutorial", false);
        prefs.flush();
    }

    public static int getHighScore(){
        int highScore = prefs.getInteger("highScore_" + levelName, 0);
        //Todo: remove this update high score in db (added for back support)
        game.db.writeHighScoreToDB(playerID,gameMode+"_"+levelName,highScore);
        return highScore;
    }

    public static void setHighScore(){
        prefs.putInteger("highScore_" + levelName, score);
        prefs.flush();
        game.db.writeHighScoreToDB(playerID,gameMode+"_"+levelName,score);
    }

    public static void getPlayerRank(){
        worldRank[0] = -1;
        game.db.getRank(gameMode+"_"+levelName,score,worldRank);
    }

    public static void getNumPlayers(){
        numGlobalPlayers[0] = -1;
        game.db.getNumPlayers(gameMode+"_"+levelName,numGlobalPlayers);
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
        if(chainLength > 5){
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
