package edu.utep.cs4381.melonlord.model;

import android.content.Context;
import java.util.Random;

public abstract class GameObject {

    protected int screenX, screenY, x, y, xSpeed, ySpeed;
    protected static final Random rand = new Random();

    public GameObject(Context context, int screenWidth, int screenHeight) {
        screenX = screenWidth;
        screenY = screenHeight;
        x = 0;
        xSpeed = 0;
        y = 0;
        ySpeed = 0;
    }

    public int getX(){ return x; }
    public int getY(){ return y; }
    public int getSpeedX(){ return xSpeed; }
    public int getSpeedY(){ return ySpeed; }

    //Update is abstract and has its own implementation for Player and Villain
    public abstract void update(int speed);

}