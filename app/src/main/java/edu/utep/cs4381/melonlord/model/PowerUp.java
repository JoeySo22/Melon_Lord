package edu.utep.cs4381.melonlord.model;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Rect;

public class PowerUp extends GraphicObject {

    public PowerUp(Context context, int screenWidth, int screenHeight, Bitmap bitmap) {
        super(context, screenWidth, screenHeight, bitmap);
        x = screenX;
        y = screenY + 10;
        moving = false;
        // In here we don't spawn fireball right away. We roll the possibility
    }

    @Override
    protected void spawn() {
        moving = true;
        x = rand.nextInt(screenX - bitMap.getWidth()) + bitMap.getWidth();
        xSpeed = rand.nextInt(70) + 10;
        y = 0;
        hitBox = new Rect(x, y, bitMap.getWidth(), bitMap.getHeight());
    }

    @Override
    protected void destroy() {
        // make no virtual hitbox
        hitBox = new Rect(x,y,x,y);
        // put off the screen
        y += 10;
        // set no longer to moving
        moving = false;
    }

    @Override
    public void update(int speed) {
        if (moving) {
            y += ySpeed;
        } else {
            // Logic to get it spawned
            if (rand.nextDouble() <= .01)
                spawn();
        }
        if (y >= screenY) destroy();
    }
}
