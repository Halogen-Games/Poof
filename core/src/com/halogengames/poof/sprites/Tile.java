package com.halogengames.poof.sprites;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Vector2;
import com.halogengames.poof.Data.GameData;
import com.halogengames.poof.Data.SoundManager;

import java.util.Random;

/**
 * Created by Rohit on 14-07-2017.
 *
 * Class representing tile object for the game board
 */

public class Tile extends Sprite{

    private String color;

    public boolean isSelected;

    private Vector2 coords;

    private int tileSize;
    private int tileMargin;
    //velocity and acceleration with which tiles move to their target location
    private int vel;
    private int acc;
    //pos where a new tile is placed (from where it falls to its correct location)
    private int startingYPos;

    public Tile(int i, int j, int tileSize, int tileMargin, int numRows) {
        Random rand = new Random();
        color = GameData.validTileColors.get(rand.nextInt(GameData.validTileColors.size));

        this.tileSize = tileSize;
        this.tileMargin = tileMargin;

        //setting animation related vars
        this.startingYPos = tileMargin*(numRows+1) + tileSize*numRows;
        this.vel = 0;
        this.acc = -startingYPos * 3;

        this.setTexture(new Texture("tile_" + color + ".png"));
        this.setPosition(tileMargin*(j+1) + tileSize*j, startingYPos);

        this.setBounds(this.getX(), this.getY(), tileSize, tileSize);

        this.setCoordinates(i, j);

        isSelected = false;
    }

    public void setCoordinates(int i, int j){
        coords = new Vector2(i, j);
    }

    private Vector2 getTargetPos(){
        //returns what position this tile should have given its current coordinates
        int x = tileMargin*((int)coords.y+1) + tileSize*(int)coords.y;
        int y = tileMargin*((int)coords.x+1) + tileSize*(int)coords.x;

        return(new Vector2(x, y));
    }

    public void setSelected(){
        this.setTexture(new Texture("tile_" + color + "_touched.png"));
        SoundManager.playBlockTouched();
        isSelected = true;
    }

    public void setDeselected(){
        this.setTexture(new Texture("tile_" + color + ".png"));
        isSelected = false;
    }

    public boolean isConnectedTo(Tile t){
        if(!this.color.equals(t.color)){
            return false;
        }else if((Math.abs(this.coords.x - t.coords.x) == 1) && (this.coords.y == t.coords.y)){
            return true;
        }else if((Math.abs(this.coords.y - t.coords.y) == 1) && (this.coords.x == t.coords.x)){
            return true;
        }
        return false;
    }

    public void update(double dt){
        if(this.getY() > getTargetPos().y) {
            vel += acc * dt;
            this.setPosition(this.getX(), this.getY() + vel * (float) dt);
            if (this.getY() < getTargetPos().y) {
                this.setPosition(getTargetPos().x, getTargetPos().y);
                vel = 0;
            }
        }
    }

    public void dispose(){
        this.getTexture().dispose();
    }
}
