package amazonDB;

import com.halogengames.poof.AndroidLauncher;
import com.halogengames.poof.database.CoreDB;

import amazonAWS.models.nosql.ScoresDO;

public class AndroidDB implements CoreDB{

    private ScoresDO scoreItem;

    public AndroidDB(){
        scoreItem = new ScoresDO();
    }

    @Override
    public void writeHighScoreToDB(String user, String gameMode, double score){
        scoreItem.setUserId(user);
        scoreItem.setGameMode(gameMode);
        scoreItem.setScore(score);

        new Thread(new Runnable() {
            @Override
            public void run() {
                AndroidLauncher.dynamoDBMapper.save(scoreItem);
                // Item saved
            }
        }).start();
    }

    @Override
    public void readFromDB() {

    }
}
