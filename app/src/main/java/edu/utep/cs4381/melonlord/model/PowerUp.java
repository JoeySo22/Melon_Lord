package edu.utep.cs4381.melonlord.model;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Rect;
import android.util.Log;

public class PowerUp extends GraphicObject {

    public PowerUp(Context context, int screenWidth, int screenHeight, Bitmap bitmap) {
        super(context, screenWidth, screenHeight, bitmap);
        x = screenWidth;
        y = screenHeight + 10;
        moving = false;
        // In here we don't spawn powerup right away. We roll the possibility
    }

    public void setIsMoving(boolean moving){
        this.moving = moving;
    }

    @Override
    protected void spawn() {
        moving = true;
        x = rand.nextInt(screenWidth - bitMap.getWidth());
        y = 0;
        ySpeed = rand.nextInt(45) + 40;
        hitBox.set(x, y, x + bitMap.getWidth(), y + bitMap.getHeight());
    }

    @Override
    public void destroy() {
        // make no virtual hitbox
        hitBox = new Rect(x,y,x,y);
        // put off the screen
        y = screenHeight + 10;
        // set no longer to moving
        moving = false;
        hitBox.set(x, y, x + bitMap.getWidth(), y + bitMap.getHeight());
    }

    @Override
    public void update(int speed, boolean flag) {
        //Log.d("PowerUp/Update", Boolean.toString(moving));
        if (moving) {
            y += ySpeed;
            //Log.d("PowerUp/Update", "powerup incremented");
        } else {
            // Logic to get it spawned
            if (rand.nextDouble() <= .05)
                spawn();
        }
        if (y >= screenHeight) destroy();
        hitBox.set(x, y, x + bitMap.getWidth(), y + bitMap.getHeight());
    }

    public boolean isMoving() { return moving; }
}