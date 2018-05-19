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

    private Poof game;

    private int numCols;
    private int numRows;

    private float tileMargin;
    private float tileSize;
    private float tileGutter;

    //tile margin is (tile size X factor)
    private float tileMarginFactor;

    private Array<Array<Tile>> tiles;
    private ArrayList<Tile> selectedTiles;

    private boolean boardInitialized;

    public GameBoard(Poof game){
        this.game = game;
        numCols = GameData.numBoardCols;
        numRows = GameData.numBoardRows;

        tileGutter = 10.0f;
        tileMarginFactor = 0.07f;

        tiles = new Array<Array<Tile>>();
        selectedTiles = new ArrayList<Tile>();

        boardInitialized = false;
    }

    private int movesLeftHelper(int i, int j, int compSize, boolean[][] checked){
        //return if already checked
        if(checked[i][j]){
            return compSize;
        }

        checked[i][j] = true;

        compSize++;

        //check up
        if( i>0 && !checked[i-1][j] && tiles.get(i-1).get(j).isConnectedTo(tiles.get(i).get(j))){
            compSize = movesLeftHelper(i-1,j,compSize,checked);
        }

        //check down
        if( i<numRows-1 && !checked[i+1][j] && tiles.get(i+1).get(j).isConnectedTo(tiles.get(i).get(j))){
            compSize = movesLeftHelper(i+1,j,compSize,checked);
        }

        //check left
        if( j>0 && !checked[i][j-1] && tiles.get(i).get(j-1).isConnectedTo(tiles.get(i).get(j))){
            compSize = movesLeftHelper(i,j-1,compSize,checked);
        }

        //check right
        if( j<numCols-1 && !checked[i][j+1] && tiles.get(i).get(j+1).isConnectedTo(tiles.get(i).get(j))){
            compSize = movesLeftHelper(i,j+1,compSize,checked);
        }

        return compSize;
    }

    private boolean movesLeft(){
        if(!boardInitialized || !isBoardIdle()){
            return true;
        }

        boolean[][] checked = new boolean[numRows][numCols];

        //iterate on each block and see if it comes in a group of three or more
        for(int i=0; i<numRows; i++){
            for(int j=0; j<numCols; j++){
                if(!checked[i][j]){
                    int componentSize = movesLeftHelper(i, j, 0, checked);
                    if( componentSize>=3){
                        return true;
                    }
                }
            }
        }
        return false;
    }

    private void shuffleBoard(){
        tileSize = (getWidth() - 2*tileGutter)/(tileMarginFactor*numCols + numCols + tileMarginFactor);
        tileMargin = tileSize*tileMarginFactor;

        //get all tiles
        Array<Tile> allTiles = new Array<Tile>();
        for(int i=0; i<numRows; i++){
            allTiles.addAll(tiles.get(i));
        }

        allTiles.shuffle();

        for(int i=0; i<numRows; i++){
            for(int j=0; j<numCols; j++) {
                //set new position for each tile
                allTiles.get(i*numCols + j).setCoordinates(i,j);
                allTiles.get(i*numCols + j).setState("shuffle");
                tiles.get(i).set(j,allTiles.get(i*numCols + j));
            }
        }
    }

    private boolean isBoardIdle(){
        for(int i=0; i<numRows; i++){
            for(int j=0; j<numCols; j++) {
                if(!tiles.get(i).get(j).getState().equals("idle")){
                    return false;
                }
            }
        }
        return true;
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

        boardInitialized = true;
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
        if(!movesLeft()) {
            shuffleBoard();
        }

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
            //todo: remove loop and use selectedTiles array so as to use powers like bomb chaining
            //Execute powers
            for(int i=tiles.size-1;i>=0;i--){
                for(int j=0;j<tiles.get(0).size;j++){
                    if(tiles.get(i).get(j).isSelected){
                        TilePower.unleashPower(this, tiles.get(i).get(j).tilePower);
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

            //fixme: why is this loop added?
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
            GameData.updateScore(selectedTiles);
        }

        selectedTiles = new ArrayList<Tile>();
    }

    public void boardTouchDragged(float screenX, float screenY) {
        if(selectedTiles.size() == 0){
            //fixme: remove this if(), it causes touch to not get registered if initial touchdown doesn't select anything
            //possible need to add touchdown code here but not sure
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
