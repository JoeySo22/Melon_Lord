package edu.utep.cs4381.melonlord.model;

        import android.content.Context;
        import android.graphics.Bitmap;
        import android.graphics.Rect;

public class Player extends GraphicObject {
    protected Bitmap bitMap;
    protected Rect hitBox;
    protected int lives;

    //Range of speed
    private static final int MIN_X_SPEED = 1;
    private static final int MAX_X_SPEED = 20;

    //Set moving whether left or right
    private boolean isMoving;

    public Player(Context context, int screenWidth, int screenHeight, Bitmap bitMap){
        super(context, screenWidth, screenHeight, bitMap);
        //player starts at these coordinates
        this.x = (screenWidth/2) - 150;
        this.y = (screenHeight/2) + 840;


        xSpeed = 1;
        this.bitMap = bitMap;
        this.hitBox = new Rect(x, y, this.bitMap.getWidth(), this.bitMap.getHeight());
    }

    //Set moveLR to flag; true if moving, false otherwise
    public void setMoveLR(boolean flag){ isMoving = flag; }

    public void update(int s){
        //If player is moving, add speed
        if(isMoving){ xSpeed += 3; }
        else { xSpeed = 0; }

        if( xSpeed < MIN_X_SPEED){ xSpeed = MIN_X_SPEED; }
        if( xSpeed > MAX_X_SPEED){ xSpeed = MAX_X_SPEED; }

        //idk how to fix if it goes out of the screen :/
    }

}