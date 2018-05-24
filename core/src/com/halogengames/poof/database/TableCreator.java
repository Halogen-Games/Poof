package com.halogengames.poof.database;

import com.amazonaws.regions.Regions;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClientBuilder;
import com.amazonaws.services.dynamodbv2.document.DynamoDB;
import com.amazonaws.services.dynamodbv2.document.Item;
import com.amazonaws.services.dynamodbv2.document.PutItemOutcome;
import com.amazonaws.services.dynamodbv2.document.Table;

import java.util.HashMap;
import java.util.Map;
public class TableCreator {


    public TableCreator(){
//        AmazonDynamoDB client = AmazonDynamoDBClientBuilder.standard()
//                .withRegion(Regions.US_WEST_2)
//                .build();
//
//        DynamoDB dynamoDB = new DynamoDB(client);
//
//        Table table = dynamoDB.getTable("poof-mobilehub-605537353-Highscores");
//
//        String year = "2015";
//        String title = "The Big New Movie";
//
//        final Map<String, Object> infoMap = new HashMap<String, Object>();
//        infoMap.put("score", 50);
//
//        try {
//            System.out.println("Adding a new item...");
//            PutItemOutcome outcome = table
//                    .putItem(new Item().withPrimaryKey("userId", year, "gameMode", title).withMap("info", infoMap));
//
//            System.out.println("PutItem succeeded:\n" + outcome.getPutItemResult());
//
//        }
//        catch (Exception e) {
//            System.err.println("Unable to add item: " + year + " " + title);
//            System.err.println(e.getMessage());
//        }
    }
}
