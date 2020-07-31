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
    protected int armor;
    public boolean playerIsAlive;

    //Set moving whether left or right
    private boolean movingLeft;
    private boolean movingRight;

    public Player(Context context, int screenWidth, int screenHeight, Bitmap noArmor, Bitmap armor){
        super(context, screenWidth, screenHeight, noArmor);
        //player starts at these coordinates
        this.bitmapUnarmored = noArmor;
        this.bitMapArmored = armor;
        this.x = (screenWidth/2) - (this.bitMap.getWidth()/2);
        this.y = screenHeight - this.bitMap.getHeight();
        this.movingLeft = false;
        this.movingRight = false;
        this.xSpeed = 20;
        spawn();
    }

    @Override
    protected void spawn() {
        this.playerIsAlive = true;
        this.armor = 5;
        Log.d("Player/Spawn", String.format("\nleft = %d\n top= %d\n right = %d\n bottom = %d",
                this.x, this.y, this.x + this.bitMap.getWidth(), this.y + this.bitMap.getHeight()));
        this.hitBox = new Rect(this.x, this.y, this.x + this.bitMap.getWidth(),
                this.y + this.bitMap.getHeight());
    }

    @Override
    protected void destroy() {
        this.playerIsAlive = false;
        // No hitbox, player is gone. View will not draw
        this.hitBox = new Rect(this.x, this.y, this.x,this.y);
    }

    public void update(int s){
        //If player is moving, add speed
        if (this.playerIsAlive) {
            if (this.movingLeft) {
                if (this.x >= 0)
                    this.x -= this.xSpeed;
            }
            if (this.movingRight) {
                if (this.x <= this.screenWidth-this.bitMap.getWidth())
                    this.x += this.xSpeed;
            }
        }
        // Add more stuff
        if (this.armor == 1)
            this.bitMap = this.bitmapUnarmored;
        else this.bitMap = this.bitMapArmored;
        Log.d("Player/Update", String.format("\nleft = %d\n top= %d\n right = %d\n bottom = %d",
                this.x, this.y, this.x + this.bitMap.getWidth(), this.y + this.bitMap.getHeight()));
        this.hitBox.set(this.x, this.y, this.x + this.bitMap.getWidth(),
                this.y + this.bitMap.getHeight());
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