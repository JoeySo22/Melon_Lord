package edu.utep.cs4381.melonlord.model;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Rect;
import android.util.Log;

public class Player extends GraphicObject {

    protected Bitmap bitMapArmored;
    protected Bitmap bitmapUnarmored;
    protected Rect hitBox;
    protected int lives;
    public boolean playerIsAlive;

    //Set moving whether left or right
    private boolean movingLeft;
    private boolean movingRight;

    public Player(Context context, int screenWidth, int screenHeight, Bitmap noArmor, Bitmap armor){
        super(context, screenWidth, screenHeight, noArmor);
        //player starts at these coordinates
        this.bitmapUnarmored = noArmor;
        this.bitMapArmored   = armor;
        this.x               = (screenWidth/2) - (this.bitMap.getWidth()/2);
        this.y               = screenHeight - this.bitMap.getHeight();
        this.movingLeft      = false;
        this.movingRight     = false;
        this.xSpeed          = 100;
        spawn();
    }

    public int getLives(){ return lives; }
    public void removeLife(){ lives--; }

    @Override
    protected void spawn() {
        playerIsAlive = true;
        lives         = 3;

        Log.d("Player/Spawn", String.format("\nleft = %d\n top= %d\n right = %d\n bottom = %d",
                this.x, this.y, this.x + this.bitMap.getWidth(), this.y + this.bitMap.getHeight()));

        this.hitBox = new Rect(this.x, this.y, this.bitMap.getWidth(),
                this.bitMap.getHeight());
    }

    @Override
    protected void destroy() {
        this.playerIsAlive = false;
        // No hitbox, player is gone. View will not draw
    }

    public void update(int s, boolean flag){
        //If player is moving, add speed
        if (this.playerIsAlive) {
            if (this.movingLeft) {
                Log.d("Player/Update/Move", "moving left");
                if (this.x >= 0)
                    this.x -= this.xSpeed;
            }
            if (this.movingRight) {
                Log.d("Player/Update/Move", "moving right");
                if (this.x <= this.screenWidth-this.bitMap.getWidth())
                    this.x += this.xSpeed;
            }
        }
        // If the player hits an armor, change the bitmap to Armored
        if (flag)
            this.bitMap = this.bitMapArmored;
        else this.bitMap = this.bitmapUnarmored;


        Log.d("Player/Update", String.format("\nleft = %d\n top= %d\n right = %d\n bottom = %d",
                this.x, this.y, this.x + this.bitMap.getWidth(), this.y + this.bitMap.getHeight()));
        this.hitBox.set(this.x, this.y, this.x + this.bitMap.getWidth(),
                this.y + this.bitMap.getHeight());
    }

    @Override
    public Rect getHitBox() {
        return this.hitBox;
    }

    public void pressingRight(boolean setting) {
        Log.d("Player/PressingRight", String.format("pressing right = %b", setting));
        movingRight = setting;
    }
    public void pressingLeft(boolean setting) {
        Log.d("Player/PressingLeft", String.format("pressing left = %b", setting));
        movingLeft = setting;
    }
}