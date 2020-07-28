package edu.utep.cs4381.melonlord.model;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Rect;

public class Player extends GraphicObject {
    protected Bitmap bitMap;
    protected Rect hitBox;
    protected int lives;

    //Set moving whether left or right
    private boolean isMoving;

    public Player(Context context, int screenWidth, int screenHeight, Bitmap bitMap){
        super(context, screenWidth, screenHeight, bitMap);
        xSpeed = 10;
        this.bitMap = bitMap;
        this.hitBox = new Rect(x, y, this.bitMap.getWidth(), this.bitMap.getHeight());
    }

    //Set moveLR to flag; true if moving, false otherwise
    public void setMoveLR(boolean flag){ isMoving = flag; }

    public void update(int s){
        //If player is moving, add speed
        if(isMoving){

        }
    }

}
