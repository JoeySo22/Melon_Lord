package edu.utep.cs4381.melonlord.model;

import android.content.Context;
import java.util.Random;

public abstract class GameObject {

    protected int screenWidth, screenHeight, x, y, xSpeed, ySpeed;
    protected static final Random rand = new Random();
    protected boolean moving;

    public GameObject(Context context, int screenWidth, int screenHeight) {
        this.screenWidth = screenWidth;
        this.screenHeight = screenHeight;
        x = 0;
        xSpeed = 0;
        y = 0;
        ySpeed = 0;
    }

    public int getX(){ return x; }
    public int getY(){ return y; }
    public int getSpeedX(){ return xSpeed; }
    public int getSpeedY(){ return ySpeed; }
    public boolean isMoving() { return moving; }

    //Update is abstract and has its own implementation for Player and Villain
    public abstract void update(int speed, boolean flag);

}