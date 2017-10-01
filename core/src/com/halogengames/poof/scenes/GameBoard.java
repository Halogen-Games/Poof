package com.halogengames.poof.scenes;

import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector3;
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

public class GameBoard implements InputProcessor{

    private int numCols;
    private int numRows;
    private Array<Array<Tile>> tiles;
    private int tileMargin;
    private int tileSize;

    private ArrayList<Tile> selectedTiles;

    public GameBoard(){
        numCols = GameData.numBoardCols;
        numRows = GameData.numBoardRows;

        tileMargin = 0;
        tileSize = ((int)Poof.VIEW_PORT.getWorldWidth() - (numCols + 1)*tileMargin )/numCols;

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

    public void render(SpriteBatch sb){
        sb.begin();
        for(int i=0; i<numRows; i++){
            for(int j=0;j<numCols; j++){
                Tile t = tiles.get(i).get(j);
                sb.draw(t.getTexture(), t.getX(), t.getY(), tileSize, tileSize);
            }
        }
        sb.end();
    }

    public void handleInput(float dt){

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

    @Override
    public boolean keyDown(int keycode) {
        return false;
    }

    @Override
    public boolean keyUp(int keycode) {
        return false;
    }

    @Override
    public boolean keyTyped(char character) {
        return false;
    }

    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button) {
        Vector3 pos = Poof.CAM.unproject(new Vector3(screenX, screenY, 0));

        for(int i=0; i<numRows; i++){
            for(int j=0;j<numCols; j++) {
                Tile t = tiles.get(i).get(j);
                if (t.getBoundingRectangle().contains(pos.x, pos.y)) {
                    selectedTiles = new ArrayList<Tile>();
                    t.setSelected();
                    selectedTiles.add(t);
                    break;
                }
            }
        }
        return true;
    }

    @Override
    public boolean touchUp(int screenX, int screenY, int pointer, int button) {
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
        return true;
    }

    @Override
    public boolean touchDragged(int screenX, int screenY, int pointer) {
        //Map screen coordinates to world coordinates
        Vector3 pos = Poof.CAM.unproject(new Vector3(screenX, screenY, 0));

        if(selectedTiles.size() == 0){
            return true;
        }

        //Selection Logic
        for(int i=0; i<numRows; i++){
            for(int j=0;j<numCols;j++) {
                Tile tile = tiles.get(i).get(j);
                if (tile.getBoundingRectangle().contains(pos.x, pos.y)) {
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
        return true;
    }

    @Override
    public boolean mouseMoved(int screenX, int screenY) {
        return false;
    }

    @Override
    public boolean scrolled(int amount) {
        return false;
    }
}
