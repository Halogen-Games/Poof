package com.halogengames.poof.sprites;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Vector2;
import com.halogengames.poof.Poof;
import com.halogengames.poof.PoofEnums;
import com.halogengames.poof.dataLoaders.PoofAssetManager;
import com.halogengames.poof.dataLoaders.GameData;
import com.halogengames.poof.dataLoaders.SoundManager;
import com.halogengames.poof.dataLoaders.TilePower;

import java.util.Random;

/**
 * Created by Rohit on 14-07-2017.
 *
 * Class representing tile object for the game board
 */

public class Tile extends Sprite{
    private Poof game;

    private String color;
    public String tilePower;
    public boolean isSelected;

    private Vector2 coords;

    private float tileSize;
    private float tileMargin;

    //velocity and acceleration with which tiles move to their target location
    private float alphaAcc;
    private float alphaVel;
    private float vel;
    private float velX;
    private float velY;
    private float acc;
    private float accX;
    private float accY;
    private float gravity;
    private float shuffleAnimSpeed;

    //state of tile
    private PoofEnums.TileState state;

    public Tile(Poof game, int i, int j, float tileSize, float tileMargin, int numRows) {
        this.game = game;

        Random rand = new Random();
        color = GameData.validTileColors.get(rand.nextInt(GameData.validTileColors.size));
        tilePower = TilePower.generatePower();

        this.tileSize = tileSize;
        this.tileMargin = tileMargin;

        //setting animation related vars
        float startingYPos = tileMargin * (numRows + 1) + tileSize * numRows;
        this.vel = 0;
        this.velX = 0;
        this.velY = 0;
        this.gravity = -startingYPos * 5;
        this.acc = -startingYPos * 5;
        this.accX = 0;
        this.accY = 0;
        this.alphaAcc = 0;
        this.alphaVel = 0;

        //NOTE: don't make shuffles slow as it allows player to make use of the stopped time
        this.shuffleAnimSpeed = 5;

        this.setTexture(this.game.assetManager.tileTextures.get(color+"_tile"));
        this.setPosition(tileMargin*(j+1) + tileSize*j, startingYPos);

        this.setBounds(this.getX(), this.getY(), tileSize, tileSize);

        this.setCoordinates(i, j);

        isSelected = false;

        setState(PoofEnums.TileState.Dropping);
    }

    public void setCoordinates(int i, int j){
        coords = new Vector2(i, j);
    }

    public Vector2 getCoordinates(){
        return coords;
    }

    public void setState(PoofEnums.TileState st){
        switch(st){
            case Idle:{
                velX = 0;
                velY = 0;
                accX = 0;
                accY = 0;
                break;
            }
            case Falling:{
                setDeselected();
                float maxVel = 1000;
                accY = gravity;
                accX = 0;
                velX = (-0.5f + (float)Math.random())*maxVel;
                velY = (0.5f + (float)Math.random()*0.5f)*maxVel * 0.75f;
                break;
            }
            default: break;
        }
        state = st;
    }

    public PoofEnums.TileState getState() {
        return state;
    }

    private Vector2 getTargetPos(){
        //returns what position this tile should have given its current coordinates
        float x = tileMargin*((int)coords.y+1) + tileSize*(int)coords.y;
        float y = tileMargin*((int)coords.x+1) + tileSize*(int)coords.x;

        return(new Vector2(x, y));
    }

    public void draw(Batch batch, float parentXPos, float parentYPos){
        batch.setColor(1,1,1,getColor().a);
        batch.draw(getTexture(), parentXPos + getX(), parentYPos + getY(), tileSize, tileSize);
        if(tilePower != null ){
            batch.draw(this.game.assetManager.powerTextures.get(tilePower), parentXPos + getX(), parentYPos + getY(), tileSize, tileSize);
        }
    }

    public void setSelected(){
        this.setTexture(this.game.assetManager.tileTextures.get(color+"_tile_touched"));
        game.soundManager.playBlockTouched();
        isSelected = true;
    }

    public void setDeselected(){
        this.setTexture(this.game.assetManager.tileTextures.get(color+"_tile"));
        isSelected = false;
    }

    public boolean isConnectedTo(Tile t){
        float xCoordDiff = Math.abs(this.coords.x - t.coords.x);
        float yCoordDiff = Math.abs(this.coords.y - t.coords.y);

        return this.color.equals(t.color) && xCoordDiff<=1 && yCoordDiff<=1 && xCoordDiff!=yCoordDiff;
    }

    public void setCorrectXPos(){
        this.setX(getTargetPos().x);
    }

    public void update(float dt){
        //Todo: Improve the below code by adding acc X and Y and vel X and Y to a base class
        switch(state){
            case Idle:{
                //do nothing
                break;
            }
            case Dropping:{
                //normal tile behavior
                vel += acc * dt;
                this.setPosition(this.getX(), this.getY() + vel * dt);

                //if tile has settled, set it to idle
                if (this.getY() < getTargetPos().y) {
                    this.setPosition(getTargetPos().x, getTargetPos().y);
                    setState(PoofEnums.TileState.Idle);
                    vel = 0;
                }
                break;
            }
            case Falling:{
                this.setAlpha(Math.max(this.getColor().a - dt,0));
                if(getColor().a <= 0){
                    this.setState(PoofEnums.TileState.Dead);
                    break;
                }
                this.velY += this.accY * dt;
                this.setPosition(this.getX() + velX * dt, this.getY() + velY * dt);

                break;
            }
            case Shuffling:{
                //set x and y vel based on distance from target location
                float dx = this.getX() - getTargetPos().x;
                float dy = this.getY() - getTargetPos().y;

                float velX = shuffleAnimSpeed*dx;
                float velY = shuffleAnimSpeed*dy;

                if(Math.abs(dx)<= Math.max(0.1, velX*dt) && Math.abs(dy)<= Math.max(0.1, velY*dt)){
                    this.setPosition(getTargetPos().x, getTargetPos().y);
                    setState(PoofEnums.TileState.Idle);
                }else {
                    this.setPosition(this.getX() - velX * dt, this.getY() - velY * dt);
                }
                break;
            }
            default:throw(new RuntimeException("Unknown Tile State"));
        }
    }

    public void dispose(){

    }
}
