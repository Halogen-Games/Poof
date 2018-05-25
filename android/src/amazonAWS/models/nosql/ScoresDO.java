package amazonAWS.models.nosql;

import com.amazonaws.mobileconnectors.dynamodbv2.dynamodbmapper.DynamoDBAttribute;
import com.amazonaws.mobileconnectors.dynamodbv2.dynamodbmapper.DynamoDBHashKey;
import com.amazonaws.mobileconnectors.dynamodbv2.dynamodbmapper.DynamoDBIndexHashKey;
import com.amazonaws.mobileconnectors.dynamodbv2.dynamodbmapper.DynamoDBIndexRangeKey;
import com.amazonaws.mobileconnectors.dynamodbv2.dynamodbmapper.DynamoDBRangeKey;
import com.amazonaws.mobileconnectors.dynamodbv2.dynamodbmapper.DynamoDBTable;

import java.util.List;
import java.util.Map;
import java.util.Set;

@DynamoDBTable(tableName = "poof-mobilehub-605537353-Scores")

public class ScoresDO {
    private String _gameMode;
    private String _userId;
    private Double _score;

    @DynamoDBHashKey(attributeName = "gameMode")
    @DynamoDBAttribute(attributeName = "gameMode")
    public String getGameMode() {
        return _gameMode;
    }

    public void setGameMode(final String _gameMode) {
        this._gameMode = _gameMode;
    }
    @DynamoDBRangeKey(attributeName = "userId")
    @DynamoDBAttribute(attributeName = "userId")
    public String getUserId() {
        return _userId;
    }

    public void setUserId(final String _userId) {
        this._userId = _userId;
    }
    @DynamoDBAttribute(attributeName = "score")
    public Double getScore() {
        return _score;
    }

    public void setScore(final Double _score) {
        this._score = _score;
    }

}
