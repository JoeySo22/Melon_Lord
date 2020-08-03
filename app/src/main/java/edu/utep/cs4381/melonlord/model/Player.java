package edu.utep.cs4381.melonlord.model;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Rect;
import android.util.Log;

public class Player extends GraphicObject {

    private static final int MAX_ARMOR = 5;
    protected Bitmap bitMapArmored;
    protected Bitmap bitmapUnarmored;
    protected Rect hitBox;
    protected int lives;
    public boolean playerIsAlive;

    //Set moving whether left or right
    private boolean movingLeft;
    private boolean movingRight;
    private boolean powerUpActivated;
    private int powerUpCount;

    public Player(Context context, int screenWidth, int screenHeight, Bitmap noArmor, Bitmap armor){
        super(context, screenWidth, screenHeight, noArmor);
        //player starts at these coordinates
        this.bitmapUnarmored = noArmor;
        this.bitMapArmored = armor;
        this.x = (screenWidth/2) - (this.bitMap.getWidth()/2);
        this.y = screenHeight - this.bitMap.getHeight();
        this.movingLeft = false;
        this.movingRight = false;
        this.xSpeed = 100;
        spawn();
    }

    @Override
    protected void spawn() {
        bitMap = bitmapUnarmored;
        this.playerIsAlive = true;
        this.lives = 2;
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

    @Override
    public void update(int s){
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
        Log.d("Player/Update", String.format("\nleft = %d\n top= %d\n right = %d\n bottom = %d",
                this.x, this.y, this.x + this.bitMap.getWidth(), this.y + this.bitMap.getHeight()));
        this.hitBox.set(this.x, this.y, this.x + this.bitMap.getWidth(),
                this.y + this.bitMap.getHeight());

        // updates the counter when powered up
        if (powerUpActivated) {
            powerUpCount--;
            if (powerUpCount == 0)  {
                powerUpActivated = false;
                bitMap = bitmapUnarmored;
            }
        }
    }

    // false if max armor has been reached
    public void powerUpActivate() {
        powerUpCount = 20;
        bitMap = bitMapArmored;
        powerUpActivated = true;
    }

    // true if player died from powerdown



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

    public int getLives() {
        return lives;
    }

    public boolean isPoweredUp() {
        return powerUpActivated;
    }

    public boolean isAlive() { return playerIsAlive; }

    // takes Hit decrements the life. returns if player is dead. Player is dead == Game is Over
    public boolean takesHit() {
        lives--;
        if (lives == 0) {
            destroy();
            return true;
        }
        else return false;
    }
}