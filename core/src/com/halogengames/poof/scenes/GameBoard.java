package com.halogengames.poof.scenes;

import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Event;
import com.badlogic.gdx.scenes.scene2d.EventListener;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.ui.Widget;
import com.badlogic.gdx.utils.Array;
import com.halogengames.poof.Data.GameData;
import com.halogengames.poof.Data.SoundManager;
import com.halogengames.poof.Poof;
import com.halogengames.poof.sprites.Tile;

import java.util.ArrayList;

/**
 * Created by Rohit on 14-07-2017.
 *
 * Class representing the game board
 * Also houses game board generation logic
 */
public class GameBoard extends Widget {

    private int numCols;
    private int numRows;
    private Array<Array<Tile>> tiles;
    private int tileMargin;
    private int tileSize;
    //tile margin = tile size * factor
    private float tileMarginFactor;

    private ArrayList<Tile> selectedTiles;

    public GameBoard(){
        numCols = GameData.numBoardCols;
        numRows = GameData.numBoardRows;

        tileMarginFactor = 0.07f;
        tileSize = (int)(getWidth()/(tileMarginFactor*numCols + numCols + tileMarginFactor));
        tileMargin = (int)(tileSize*tileMarginFactor);
        System.out.println(tileMargin);
        //set Height based on num rows
        setHeight(tileSize*GameData.numBoardRows);

        tiles = new Array<Array<Tile>>();
        for(int i=0; i<numRows; i++){
            Array<Tile> tileColumn = new Array<Tile>();
            for(int j=0; j<numCols; j++) {
                tileColumn.add(new Tile(i, j, tileSize, tileMargin, numRows));
            }
            tiles.add(tileColumn);
        }

        selectedTiles = new ArrayList<Tile>();
    }

    @Override
    protected void sizeChanged() {
        tileSize = (int)(getWidth()/(tileMarginFactor*numCols + numCols + tileMarginFactor));
        tileMargin = (int)(tileSize*tileMarginFactor);

        //adjust height based on num Rows
        setHeight(tileSize*GameData.numBoardRows);

        tiles = new Array<Array<Tile>>();
        for(int i=0; i<numRows; i++){
            Array<Tile> tileColumn = new Array<Tile>();
            for(int j=0; j<numCols; j++) {
                tileColumn.add(new Tile(i, j, tileSize, tileMargin, numRows));
            }
            tiles.add(tileColumn);
        }
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        for(int i=0; i<numRows; i++){
            for(int j=0;j<numCols; j++){
                Tile t = tiles.get(i).get(j);
                batch.draw(t.getTexture(), getX() + t.getX(), getY() + t.getY(), tileSize, tileSize);
            }
        }



        setBounds(getX(), getY(), getWidth(), getHeight());
    }

    public void update(float dt){
        for(int i=0; i<numRows; i++){
            for(int j=0;j<numCols; j++) {
                tiles.get(i).get(j).update(dt);
            }
        }
    }

    public void dispose(){
        for(int i=0; i<numRows; i++){
            for(int j=0;j<numCols; j++) {
                tiles.get(i).get(j).dispose();
            }
        }
    }

    public boolean boardTouchedDown(float screenX, float screenY) {
        for(int i=0; i<numRows; i++){
            for(int j=0;j<numCols; j++) {
                Tile t = tiles.get(i).get(j);
                if (t.getBoundingRectangle().contains(screenX, screenY)) {
                    selectedTiles = new ArrayList<Tile>();
                    t.setSelected();
                    selectedTiles.add(t);
                    break;
                }
            }
        }
        return true;
    }

    public void boardTouchedUp() {
        if(selectedTiles.size()<3) {
            SoundManager.playWrongMove();
            for (int t = selectedTiles.size() - 1; t >= 0; t--) {
                selectedTiles.get(t).setDeselected();
            }
        }else{
            SoundManager.playBlocksRemoved();
            GameData.score += selectedTiles.size();

            for(int i=numRows-1;i>=0;i--){
                for(int j=0;j<numCols;j++){
                    if(tiles.get(i).get(j).isSelected){
                        //need to remove this tile
                        for(int k=i;k<numRows-1;k++){
                            tiles.get(k).set(j, tiles.get(k+1).get(j));
                            tiles.get(k).get(j).setCoordinates(k,j);
                        }
                        //will always add one at the top
                        tiles.get(numRows-1).set(j, new Tile(numRows-1, j, tileSize, tileMargin, numRows));
                    }
                }
            }
        }

        selectedTiles = new ArrayList<Tile>();
    }

    public void boardTouchDragged(float screenX, float screenY) {
        if(selectedTiles.size() == 0){
            return;
        }

        //Selection Logic
        for(int i=0; i<numRows; i++){
            for(int j=0;j<numCols;j++) {
                Tile tile = tiles.get(i).get(j);
                if (tile.getBoundingRectangle().contains(screenX, screenY)) {
                    if (tile.isSelected) {
                        for (int t = selectedTiles.size() - 1; t >= 0; t--) {
                            if (selectedTiles.get(t) == tile) {
                                break;
                            }
                            selectedTiles.get(t).setDeselected();
                            selectedTiles.remove(selectedTiles.size() - 1);
                        }
                    } else if (tile.isConnectedTo(selectedTiles.get(selectedTiles.size() - 1))) {
                        selectedTiles.add(tile);
                        tile.setSelected();
                    }
                    break;
                }
            }
        }
    }
}
