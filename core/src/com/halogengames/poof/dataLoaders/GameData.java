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

    public enum GameMode{
        Timed, Relaxed
    }

    private static Poof game;

    //final vars
    final public static int NO_NETWORK = -2;

    public static double levelTimer;
    private static int maxTime;
    private static int numTileColors;

    //todo: remove level
    private static String levelName;
    private static GameMode gameMode;

    //things like font size are hardcoded according to this base width and then scaled appropriately based on actual width of screen
    static float baseWidth;

    public static int numBoardCols;
    public static int numBoardRows;

    //GL Clear Color
    public static Color clearColor;

    //Valid Colors
    public static Array<String> tileColors;

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

        worldRank = new int[1];
        worldRank[0] = -1;
        numGlobalPlayers = new int[1];
        numGlobalPlayers[0] = -1;

        syncHighScores();

        gamesPlayed = 0;

        baseWidth = 540;

        maxTime = 60;

        numBoardCols = 6;
        numBoardRows = 6;

        clearColor = new Color(1,1,1,1);

        //move tile colors to tile class
        tileColors = new Array<String>();
        tileColors.add("blue");
        tileColors.add("green");
        tileColors.add("red");
        tileColors.add("yellow");
        tileColors.add("indigo");

        tileColors.shuffle();

        TilePower.init();
    }

    public static void syncHighScores(){
        //Todo:make this sync better
        //Sync highScore with dynamoDB
        String[] levelNamesList = {"easy","medium","hard"};
        GameMode[] gameModesList = {GameMode.Relaxed,GameMode.Timed};
        for(String level:levelNamesList) {
            for (GameMode mode : gameModesList) {
                game.db.writeHighScoreToDB(playerID, mode.toString(), level, getHighScore());
            }
        }
    }

    public static void tutorialShown(){
        showTutorial = false;
        prefs.putBoolean("showTutorial", false);
        prefs.flush();
    }

    public static int getHighScore(){
        return prefs.getInteger("highScore_" + levelName, 0);
    }

    public static void setHighScore(){
        prefs.putInteger("highScore_" + levelName, score);
        prefs.flush();
        game.db.writeHighScoreToDB(playerID,gameMode.toString(),levelName,getHighScore());
    }

    public static void getPlayerRank(){
        worldRank[0] = -1;
        game.db.getRank(gameMode+"_"+levelName,getHighScore(),worldRank);
    }

    public static void getNumPlayers(){
        numGlobalPlayers[0] = -1;
        game.db.getNumPlayers(gameMode+"_"+levelName,numGlobalPlayers);
    }

    public static void setLevel(String level){
        if(level.equals("easy")){
            levelName = level;
            numTileColors = 3;
        }else if(level.equals("medium")){
            levelName = level;
            numTileColors = 4;
        }else if(level.equals("hard")){
            levelName = level;
            numTileColors = 5;
        }else{
            throw new Error("unknown level selected: " + level);
        }
    }

    public static void setGameMode(GameMode mode){
        switch(mode){
            case Relaxed:{
                TilePower.setPowerProb("timer",0);
                break;
            }
            case Timed:{
                TilePower.setPowerProb("timer",0.03f);
                break;
            }
            default: throw new RuntimeException("Unknown Game Mode");
        }
        gameMode = mode;

        //select easy level by default
        setLevel("easy");
    }

    public static GameMode getGameMode(){
        return gameMode;
    }

    public static int getNumTileColors(){
        return numTileColors;
    }

    public static void updateData(int chainLength, ArrayList<Tile> selectedTiles){
        score += selectedTiles.size();
        if(chainLength > 5){
            score += Math.pow(chainLength-5,1.2);
        }

        if(score>600){
            setLevel("hard");
        }else if(score>300){
            setLevel("medium");
        }
    }

    public static void resetData(){
        setLevel("easy");
        levelTimer = maxTime;
        score = 0;
    }
}
