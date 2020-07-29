package edu.utep.cs4381.melonlord.model;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Rect;

public class Player extends GraphicObject {

    private static final int MAX_ARMOR = 5;
    protected Bitmap bitMap;
    protected Rect hitBox;
    protected int armor;
    public boolean playerIsAlive;

    //Set moving whether left or right
    private boolean movingLeft;
    private boolean movingRight;

    public Player(Context context, int screenWidth, int screenHeight, Bitmap bitMap){
        super(context, screenWidth, screenHeight, bitMap);
        //player starts at these coordinates
        this.x = (screenWidth/2) - 150;
        this.y = (screenHeight/2) + 840;
        movingLeft = false;
        movingRight = false;
        xSpeed = 20;
        this.bitMap = bitMap;
        spawn();
    }

    @Override
    protected void spawn() {
        playerIsAlive = true;
        armor = 5;
        this.hitBox = new Rect(x, y, this.bitMap.getWidth(), this.bitMap.getHeight());
    }

    @Override
    protected void destroy() {
        playerIsAlive = false;
        // No hitbox, player is gone. View will not draw
        hitBox = new Rect(x, y, x,y);
    }

    public void update(int s){
        //If player is moving, add speed
        if (playerIsAlive) {
            if (movingLeft) {
                if (x >= 0)
                    x -= xSpeed;
            }
            if (movingRight) {
                if (x <= screenX)
                    x += xSpeed;
            }
        }

        // Add more stuff
    }

    // false if max armor has been reached
    public boolean powerUp() {
        if (armor == MAX_ARMOR)
            return false;
        armor++;
        return true;
    }
    // true if player died from powerdown
    public boolean powerDown() {
        if (armor == 1){
            armor--;
            destroy();
            return true;
        }
        armor--;
        return false;
    }
}