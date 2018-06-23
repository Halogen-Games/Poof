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

        game.analyticsManager.addUser(playerID);

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

    private static void syncHighScores(){
        //Todo:make this sync better
        //Sync highScore with dynamoDB
        GameMode[] gameModesList = {GameMode.Relaxed,GameMode.Timed};
        for (GameMode mode : gameModesList) {
            game.db.writeHighScoreToDB(playerID, mode, getHighScore(mode));
        }
    }

    public static void tutorialShown(){
        showTutorial = false;
        prefs.putBoolean("showTutorial", showTutorial);
        prefs.flush();
    }

    public static int getHighScore(){
        return getHighScore(gameMode);
    }

    private static int getHighScore(GameMode gameMode){
        return prefs.getInteger("highScore_" + gameMode, 0);
    }

    public static void setHighScore(){
        prefs.putInteger("highScore_" + gameMode, score);
        prefs.flush();
        game.db.writeHighScoreToDB(playerID,gameMode,getHighScore());
    }

    public static void getPlayerRank(){
        worldRank[0] = -1;
        game.db.getRank(gameMode,getHighScore(),worldRank);
    }

    public static void getNumPlayers(){
        numGlobalPlayers[0] = -1;
        game.db.getNumPlayers(gameMode,numGlobalPlayers);
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
    }

    public static GameMode getGameMode(){
        return gameMode;
    }

    public static int getNumTileColors(){
        return numTileColors;
    }

    public static void updateData(int chainLength, ArrayList<Tile> selectedTiles){
        int oldScore = score;
        score += selectedTiles.size();
        if(chainLength > 5){
            score += Math.pow(chainLength-5,1.2);
        }

        //new colors
        if(score>2000){
            numTileColors = 4;
        }
        if(score>=500 && oldScore/200 != score/200){
            tileColors.shuffle();
        }

        //power addition
        //Note: I don't expect the score to rise so much in a move so that one of the below blocks is skipped as score increases
        if(score>=400 && oldScore<400){
            TilePower.setPowerProb("bomb",0.02f);
        }else if(score>=200 && oldScore<200){
            TilePower.setPowerProb("rock",0.01f);
        }
    }

    public static void resetData(){
        numTileColors = 3;
        levelTimer = maxTime;
        score = 0;

        //set power probs
        TilePower.setPowerProb("rock",0);
        TilePower.setPowerProb("bomb",0);

        switch(gameMode){
            case Relaxed:{
                TilePower.setPowerProb("timer",0);
                break;
            }
            case Timed:{
                TilePower.setPowerProb("timer",0.04f);
                break;
            }
            default: throw new RuntimeException("Unknown Game Mode");
        }
    }
}
