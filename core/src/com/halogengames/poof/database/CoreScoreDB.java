package com.halogengames.poof.database;

import com.halogengames.poof.dataLoaders.GameData;

public interface CoreScoreDB {
    void writeHighScoreToDB(String user, GameData.GameMode gameMode, double score);

    void getRank(GameData.GameMode gameMode, int score, int[] rank);
    void getNumPlayers(GameData.GameMode gameMode, int[] numPlayers);
}
