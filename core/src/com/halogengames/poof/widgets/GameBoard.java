package com.halogengames.poof.widgets;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.ui.Widget;
import com.badlogic.gdx.utils.Array;
import com.halogengames.poof.dataLoaders.GameData;
import com.halogengames.poof.dataLoaders.SoundManager;
import com.halogengames.poof.dataLoaders.TilePower;
import com.halogengames.poof.Poof;
import com.halogengames.poof.library.AnimatedSprite;
import com.halogengames.poof.library.Value2D;
import com.halogengames.poof.sprites.Tile;
import com.halogengames.poof.sprites.Tile.TileState;

import java.util.ArrayList;

/**
 * Created by Rohit on 14-07-2017.
 *
 * Class representing the game board
 * Also houses game board generation logic
 */
public class GameBoard extends AnimatedSprite {

    //handle to game and screen
    private Poof game;

    private float boardSize;
    private int numCols;
    private int numRows;

    //todo: send this tile data to game data class
    //TODO:(Very Important) Separate game board from play mechanics (maybe mechanics can extend the gameboard)
    private float tileMargin;
    private float tileSize;
    private float tileGutter;
    private float rad;
    //tile margin is (tile size X factor)
    private float tileMarginFactor;

    private int numButtons;
    private float buttonMargin;
    private float buttonGutter;
    private float buttonSize;

    //colors and graphics
    private Color bgColor;
    private Color borderColor;
    private Color normalColor;
    private Color flashColor;
    private Color tileHighlightColor;
    private int flashStartTime;
    private float flashFreq;

    private Array<Array<Tile>> tiles;
    private ArrayList<Tile> selectedTiles;
    private ArrayList<Tile> fallingTiles;

    private boolean boardInitialized;

    public GameBoard(Poof game, float boardSize, int numButtons){
        this.game = game;
        this.numButtons = numButtons;
        this.boardSize = boardSize;

        numCols = GameData.numBoardCols;
        numRows = GameData.numBoardRows;

        bgColor = new Color(Color.WHITE);

        borderColor = new Color(Color.GRAY);
        normalColor = new Color(Color.GRAY);
        flashColor = new Color(Color.WHITE);
        tileHighlightColor = new Color(Color.WHITE);
        flashStartTime = 10;
        flashFreq = 2;

        tiles = new Array<Array<Tile>>();
        selectedTiles = new ArrayList<Tile>();
        fallingTiles = new ArrayList<Tile>();

        sizeChanged();

        boardInitialized = false;
    }

    @Override
    public float getHeight(){
        return boardSize;
    }

    @Override
    public float getWidth(){
        return boardSize;
    }

    public float getButtonMargin(){
        return buttonMargin;
    }

    public float getButtonGutter(){
        return buttonGutter;
    }

    public float getCornerRadius(){
        return rad;
    }

    public float getButtonSize(){
        return buttonSize;
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

    public boolean movesLeft(){
        if(!boardInitialized || getBoardState() != TileState.Idle){
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

    public void shuffleBoard(){
//        tileSize = (getWidth() - 2*tileGutter)/(tileMarginFactor*numCols + numCols + tileMarginFactor);
//        tileMargin = tileSize*tileMarginFactor;

        //get all tiles
        Array<Tile> allTiles = new Array<Tile>();
        for(int i=0; i<numRows; i++){
            allTiles.addAll(tiles.get(i));
        }

        while(!movesLeft()) {
            allTiles.shuffle();
            for (int i = 0; i < numRows; i++) {
                for (int j = 0; j < numCols; j++) {
                    //set new position for each tile
                    allTiles.get(i * numCols + j).setCoordinates(i, j);
                    allTiles.get(i * numCols + j).setState(TileState.Shuffling);
                    tiles.get(i).set(j, allTiles.get(i * numCols + j));
                }
            }
        }

        game.soundManager.playShuffleSound();
    }

    public TileState getBoardState(){
        boolean shuffling = false;
        boolean dropping = false;

        for(int i=0; i<numRows; i++){
            for(int j=0; j<numCols; j++) {
                //todo: remove this loop and make fetching state O(1)
                if(tiles.get(i).get(j).getState() == TileState.Shuffling){
                    shuffling = true;
                }else if(tiles.get(i).get(j).getState() == TileState.Dropping){
                    dropping = true;
                }
            }
        }

        if(shuffling){
            return TileState.Shuffling;
        }else if(dropping){
            return TileState.Dropping;
        }
        return TileState.Idle;
    }

    public void setTileAsSelected(int i, int j){
        if(!selectedTiles.contains(tiles.get(i).get(j))){
            tiles.get(i).get(j).setSelected();
            selectedTiles.add(tiles.get(i).get(j));
        }
    }

    @Override
    protected void sizeChanged() {
        tileGutter = 10.0f;
        tileMarginFactor = 0.07f;

        tileSize = (getWidth() - 2*tileGutter)/(tileMarginFactor*numCols + numCols + tileMarginFactor);
        tileMargin = tileSize*tileMarginFactor;

        rad = tileGutter+tileMargin;

        buttonSize = getWidth()/7;
        buttonGutter = getWidth()-2*rad-numButtons*buttonSize;
        if( numButtons>1){
            buttonGutter /= numButtons-1;
        }
        buttonMargin = 10;

        tiles = new Array<Array<Tile>>();
        for(int i=0; i<numRows; i++){
            Array<Tile> tileColumn = new Array<Tile>();
            for(int j=0; j<numCols; j++) {
                tileColumn.add(new Tile(this.game, i, j, tileSize, tileMargin, numRows));
            }
            tiles.add(tileColumn);
        }

        boardInitialized = true;
    }

    private Vector2 drawButtonBorder(float x, float y){
        float size = buttonSize;

        //Draw BG
        game.renderer.setColor(bgColor);
        game.renderer.rect(x-size,y-rad,size,rad);
        game.shaper.drawRoundRectFilled(x-size,y-size,size,size,rad);

        //Draw border
        game.renderer.setColor(borderColor);
        game.shaper.drawLine(x,y,x,y-size+rad);
        game.shaper.drawLineArc(x-rad,y-size+rad,rad,270,90);
        game.shaper.drawLine(x-rad,y-size,x-size+rad,y-size);
        game.shaper.drawLineArc(x-size+rad,y-size+rad,rad,180,90);
        game.shaper.drawLine(x-size,y-size+rad,x-size,y);
        game.shaper.drawLine(x-size,y,Math.max(x-size-buttonGutter,getX()+rad),y);

        return new Vector2(Math.max(x-size-buttonGutter,getX()+rad),y);
    }

    private void drawBoardFrame(){
        game.renderer.setProjectionMatrix(Poof.CAM.combined);
        game.renderer.begin(ShapeRenderer.ShapeType.Filled);

        //add bg color
        game.renderer.setColor(bgColor);
        game.shaper.drawRoundRectFilled(getX(),getY(),getWidth(),getHeight(),rad);

        //Draw Board frame starting from lower left arc
        game.renderer.setColor(borderColor);
        game.shaper.setLineWidth(5);

        game.shaper.drawLineArc(getX()+rad, getY()+rad, rad, 180, 90);
        game.shaper.drawLine(getX(),getY()+rad,getX(),getY()+getHeight()-rad);

        game.shaper.drawLineArc(getX()+rad,getY()+getHeight()-rad,rad,90,90);
        game.shaper.drawLine(getX()+rad, getY()+getHeight(), getX()+getWidth()-rad,getY()+getHeight());

        game.shaper.drawLineArc(getX()+getWidth()-rad,getY()+getHeight()-rad,rad,0,90);
        game.shaper.drawLine(getX()+getWidth(), getY()+getHeight()-rad, getX()+getWidth(),getY()+rad);

        game.shaper.drawLineArc(getX()+getWidth()-rad,getY()+rad,rad,270,90);

        //add button specific border
        Vector2 p = new Vector2(getX()+getWidth()-rad, getY());
        for(int i=0; i<numButtons; i++){
            p = drawButtonBorder(p.x, p.y);
        }

        game.shaper.drawLine(p.x,p.y,getX()+rad,p.y);

        game.renderer.end();
    }

    private void drawSelectedTileHighlight(){
        if(selectedTiles.size()==0){
            return;
        }

        game.renderer.setProjectionMatrix(Poof.CAM.combined);
        game.renderer.begin(ShapeRenderer.ShapeType.Filled);
        game.renderer.setColor(tileHighlightColor);

        int rad = 7;

        //draw circle on last tile
        Tile t = selectedTiles.get(selectedTiles.size()-1);
        float xPos = t.getX()+t.getWidth()/2+this.getX()+tileGutter;
        float yPos = t.getY()+t.getHeight()/2+this.getY()+tileGutter;
        game.shaper.drawLineArc(xPos,yPos,rad,0,360);

        for(int i=1;i<selectedTiles.size();i++){
            Tile t1 = selectedTiles.get(i-1);
            Tile t2 = selectedTiles.get(i);
            float x1Pos = t1.getX()+t1.getWidth()/2+this.getX()+tileGutter;
            float y1Pos = t1.getY()+t1.getHeight()/2+this.getY()+tileGutter;
            float x2Pos = t2.getX()+t2.getWidth()/2+this.getX()+tileGutter;
            float y2Pos = t2.getY()+t2.getHeight()/2+this.getY()+tileGutter;

            if(i == selectedTiles.size()-1){
                if(x1Pos<x2Pos){
                    x2Pos-=rad;
                }else if(x1Pos>x2Pos){
                    x2Pos+=rad;
                }else if(y1Pos<y2Pos){
                    y2Pos-=rad;
                }else{
                    y2Pos+=rad;
                }
            }

            game.shaper.drawLine(x1Pos,y1Pos,x2Pos,y2Pos);
        }

        game.renderer.end();
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {

        //Batch.begin called by parent, so end it
        batch.end();

        drawBoardFrame();

        batch.begin();

        //draw tiles using batch
        for(int i=0; i<tiles.size; i++){
            for(int j=0;j<tiles.get(0).size; j++){
                Tile t = tiles.get(i).get(j);
                t.draw(batch, getX() + tileGutter, getY() + tileGutter);
            }
        }

        //draw falling tiles
        for(int i=0;i<fallingTiles.size();i++){
            fallingTiles.get(i).draw(batch, getX()+tileGutter, getY()+tileGutter);
        }

        batch.end();

        drawSelectedTileHighlight();
//        setBounds(getX(), getY(), getWidth(), getHeight());

        batch.begin();
    }

    public void update(float dt){
        //flash board if time left is less than threshold
        if(GameData.levelTimer<=flashStartTime){
            float timeSegments = (float)GameData.levelTimer*flashFreq*2;
            if(Math.round(timeSegments)%2 == 0){
                borderColor = flashColor;
            }else{
                borderColor = normalColor;
            }
        }else{
            borderColor = normalColor;
        }

        //update board tiles
        for(int i=0; i<tiles.size; i++){
            for(int j=0;j<tiles.get(0).size; j++) {
                tiles.get(i).get(j).update(dt);
            }
        }

        //update falling tiles
        for(int i=fallingTiles.size()-1; i>=0; i--){
            if(fallingTiles.get(i).getState() == TileState.Dead){
                fallingTiles.remove(i);
            }else{
                fallingTiles.get(i).update(dt);
            }
        }
    }

    public void boardTouchedDown(float screenX, float screenY) {
        int j = (int)Math.floor((screenX-tileGutter)/(tileSize+tileMargin));
        int i = (int)Math.floor((screenY-tileGutter)/(tileSize+tileMargin));

        if(i<0 || j<0 || i>=numRows || j>=numCols){
            //return if outside the board
            return;
        }

        Tile t = tiles.get(i).get(j);
        selectedTiles = new ArrayList<Tile>();
        t.setSelected();
        selectedTiles.add(t);
    }

    public void boardTouchedUp() {
        if(selectedTiles.size()<3) {
            game.soundManager.playWrongMove();
            for (int t = selectedTiles.size() - 1; t >= 0; t--) {
                selectedTiles.get(t).setDeselected();
            }
        }else{
            game.soundManager.playBlocksRemoved();

            int chainLength = selectedTiles.size();

            //Execute powers
            //This may add more tiles to selected tiles
            for(int i=0; i<selectedTiles.size(); i++){
                TilePower.unleashPower(this, selectedTiles.get(i));
            }

            //remove and add new tiles
            for(Tile t:selectedTiles){
                //need to add this tile to falling tiles
                t.setState(TileState.Falling);
                fallingTiles.add(t);

                int i = (int)t.getCoordinates().x;
                int j = (int)t.getCoordinates().y;

                //drop all tiles by 1
                for(int k=i;k<numRows-1;k++){
                    tiles.get(k).set(j, tiles.get(k+1).get(j));

                    //below helps the tiles get in correct column in case they were not ex. during shuffle
                    tiles.get(k).get(j).setCorrectXPos();

                    tiles.get(k).get(j).setState(TileState.Dropping);
                    tiles.get(k).get(j).setCoordinates(k,j);
                }
                //will always add one at the top
                tiles.get(numRows-1).set(j, new Tile(this.game,numRows-1, j, tileSize, tileMargin, numRows));
            }

            GameData.updateData(chainLength,selectedTiles);
        }

        //flush the selected tiles array
        selectedTiles = new ArrayList<Tile>();
    }

    public void boardTouchDragged(float screenX, float screenY) {
        int j = (int)Math.floor((screenX-tileGutter)/(tileSize+tileMargin));
        int i = (int)Math.floor((screenY-tileGutter)/(tileSize+tileMargin));

        if(i<0 || j<0 || i>=numRows || j>=numCols){
            //return if outside the board
            return;
        }
        Tile tile = tiles.get(i).get(j);

        if (tile.isSelected && selectedTiles.size()>1 && tile == selectedTiles.get(selectedTiles.size() - 2)) {
            //if this is the second last selected tile, remove the last selected tile
            selectedTiles.get(selectedTiles.size() - 1).setDeselected();
            selectedTiles.remove(selectedTiles.size() - 1);
        } else if (selectedTiles.size() == 0 || ( tile.isConnectedTo(selectedTiles.get(selectedTiles.size() - 1)) && !tile.isSelected )) {
            //NOTE: order matters in above if condition
            //select the tile if no tile is selected or if this tile is connected to a selected tile
            selectedTiles.add(tile);
            tile.setSelected();
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
