package com.halogengames.poof.database;

public interface CoreDB {
    void writeHighScoreToDB(String user,String gameMode,double score);
    void readFromDB();
}
