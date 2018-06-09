package amazonDB;

import android.util.AndroidRuntimeException;

import com.amazonaws.AmazonServiceException;
import com.amazonaws.mobileconnectors.dynamodbv2.dynamodbmapper.DynamoDBMapperConfig;
import com.amazonaws.mobileconnectors.dynamodbv2.dynamodbmapper.DynamoDBQueryExpression;
import com.amazonaws.mobileconnectors.dynamodbv2.dynamodbmapper.PaginatedList;
import com.halogengames.poof.AndroidLauncher;
import com.halogengames.poof.dataLoaders.GameData;
import com.halogengames.poof.database.CoreScoreDB;

import java.io.IOException;
import java.util.List;

import amazonAWS.models.nosql.ScoresDO;

public class AndroidScoreDB implements CoreScoreDB {
    @Override
    public void writeHighScoreToDB(String user, GameData.GameMode gameMode, double score){
        if(gameMode==null){
            throw new RuntimeException("gameMode or level can't be null");
        }
        final ScoresDO scoreItem = new ScoresDO();
        scoreItem.setUserId(user);
        scoreItem.setGameMode(gameMode.toString());
        scoreItem.setScore(score);

        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    //the second arg below allows for updates of provided attributes, without it, other attributes if present will be set to null
                    AndroidLauncher.dynamoDBMapper.save(scoreItem, new DynamoDBMapperConfig(DynamoDBMapperConfig.SaveBehavior.UPDATE_SKIP_NULL_ATTRIBUTES));
                } catch (Exception e) {
                    System.out.println("DB Save call failed");
                    System.out.println("Error:" + e.getMessage());
                }
            }
        }).start();
    }

    @Override
    public void getRank(final GameData.GameMode gameMode, final int score, final int[] rank) {
        //rank needs to be an array as it is final and can't be reassigned
        //Todo: make sure get rank doesn't get run before write high score to db
        final ScoresDO scoreItem = new ScoresDO();

        new Thread(new Runnable(){
            @Override
            public void run(){
                scoreItem.setGameMode(gameMode.toString()); //partition key

                DynamoDBQueryExpression<ScoresDO> queryExpression = new DynamoDBQueryExpression<ScoresDO>()
                        .withHashKeyValues(scoreItem)
                        .withConsistentRead(false);

                try {
                    PaginatedList<ScoresDO> result = AndroidLauncher.dynamoDBMapper.query(ScoresDO.class, queryExpression);

                    int better = 0;
                    //Todo: remove this loop through query results and write the condition in query itself
                    for (int i = 0; i < result.size(); i++) {
                        if (score < result.get(i).getScore()) {
                            better++;
                        }
                    }
                    rank[0] = better + 1;
                } catch (Exception e) {
                    System.out.println("DB get rank call failed");
                    System.out.println("Error:" + e.getMessage());
                    rank[0] = GameData.NO_NETWORK;
                    getRank(gameMode, score, rank);
                }
            }
        }).start();
    }

    @Override
    public void getNumPlayers(final GameData.GameMode gameMode, final int[] numPlayers){
        //numPlayers needs to be an array as it is final and can't be reassigned

        final ScoresDO scoreItem = new ScoresDO();

        new Thread(new Runnable(){
            @Override
            public void run(){
                scoreItem.setGameMode(gameMode.toString()); //partition key

                DynamoDBQueryExpression<ScoresDO> queryExpression = new DynamoDBQueryExpression<ScoresDO>()
                        .withHashKeyValues(scoreItem)
                        .withConsistentRead(false);

                try {
                    PaginatedList<ScoresDO> result = AndroidLauncher.dynamoDBMapper.query(ScoresDO.class, queryExpression);
                    //Todo: see if we can just get the count of rows instead of the data itself
                    numPlayers[0] = result.size();
                } catch(Exception e){
                    System.out.println("DB get num players call failed");
                    System.out.println("Error:" + e.getMessage());
                    numPlayers[0] = GameData.NO_NETWORK;
                    getNumPlayers(gameMode, numPlayers);
                }
            }
        }).start();
    }
}
