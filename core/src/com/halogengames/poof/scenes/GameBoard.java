package com.halogengames.poof.scenes;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.scenes.scene2d.ui.Widget;
import com.badlogic.gdx.utils.Array;
import com.halogengames.poof.Data.AssetManager;
import com.halogengames.poof.Data.GameData;
import com.halogengames.poof.Data.SoundManager;
import com.halogengames.poof.Data.TilePower;
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

    Poof game;

    private int numCols;
    private int numRows;
    private Array<Array<Tile>> tiles;
    private float tileMargin;
    private float tileSize;
    private float tileGutter;

    //tile margin is (tile size X factor)
    private float tileMarginFactor;

    private ArrayList<Tile> selectedTiles;

    public GameBoard(Poof game){
        this.game = game;
        numCols = GameData.numBoardCols;
        numRows = GameData.numBoardRows;

        tileGutter = 10.0f;
        tileMarginFactor = 0.07f;

        tiles = new Array<Array<Tile>>();
        selectedTiles = new ArrayList<Tile>();
    }

    @Override
    protected void sizeChanged() {
        tileSize = (getWidth() - 2*tileGutter)/(tileMarginFactor*numCols + numCols + tileMarginFactor);
        tileMargin = tileSize*tileMarginFactor;

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

        //draw board BG
        batch.draw(AssetManager.boardBG, getX(), getY(), getWidth(), getHeight());

        //draw tiles using batch
        for(int i=0; i<tiles.size; i++){
            for(int j=0;j<tiles.get(0).size; j++){
                Tile t = tiles.get(i).get(j);
                t.draw(batch, getX() + tileGutter, getY() + tileGutter);
            }
        }

        setBounds(getX(), getY(), getWidth(), getHeight());
    }

    public void update(float dt){
        for(int i=0; i<tiles.size; i++){
            for(int j=0;j<tiles.get(0).size; j++) {
                tiles.get(i).get(j).update(dt);
            }
        }
    }

    public boolean boardTouchedDown(float screenX, float screenY) {
        for(int i=0; i<tiles.size; i++){
            for(int j=0;j<tiles.get(0).size; j++) {
                Tile t = tiles.get(i).get(j);
                if (t.getBoundingRectangle().contains(screenX, screenY)) {
                    selectedTiles = new ArrayList<Tile>();
                    t.setSelected();
                    selectedTiles.add(t);

                    //return true declaring input is handled
                    return true;
                }
            }
        }
        return false;
    }

    public void boardTouchedUp() {
        if(selectedTiles.size()<3) {
            SoundManager.playWrongMove();
            for (int t = selectedTiles.size() - 1; t >= 0; t--) {
                selectedTiles.get(t).setDeselected();
            }
        }else{
            SoundManager.playBlocksRemoved();
            //Execute powers
            for(int i=tiles.size-1;i>=0;i--){
                for(int j=0;j<tiles.get(0).size;j++){
                    if(tiles.get(i).get(j).isSelected){
                        TilePower.unleashPower(tiles.get(i).get(j).tilePower);
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

            for(int i=tiles.size-1;i>=0;i--){
                for(int j=0;j<tiles.get(0).size;j++){
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
        for(int i=0; i<tiles.size; i++){
            for(int j=0;j<tiles.get(0).size;j++) {
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

    public void dispose(){
        for(int i=0; i<tiles.size; i++){
            for(int j=0;j<tiles.get(0).size; j++) {
                tiles.get(i).get(j).dispose();
            }
        }
    }
}
