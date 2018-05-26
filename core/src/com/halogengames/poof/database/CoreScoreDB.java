package com.halogengames.poof.database;

public interface CoreScoreDB {
    void writeHighScoreToDB(String user,String gameMode,double score);

    void getRank(String gameMode,int score,int[] rank);
    void getNumPlayers(String gameMode,int[] numPlayers);
}
