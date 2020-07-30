package edu.utep.cs4381.melonlord.model;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Rect;

public class FireBall extends GraphicObject {

    //Move vertically from up to down
    private int maxY, minY;
    private int maxX, minX;
    public FireBall(Context context, int screenWidth, int screenHeight, Bitmap bitmap) {
        super(context, screenWidth, screenHeight, bitmap);
        maxX = screenX;
        maxY = screenY;
        minX = 0;
        minY = 0;
        spawn();
    }

    @Override
    protected void spawn() {
        x = rand.nextInt(screenX - bitMap.getWidth()) + bitMap.getWidth();
        ySpeed = rand.nextInt(45) + 40;
        y = 0;
        hitBox = new Rect(x, y, bitMap.getWidth(), bitMap.getHeight());
    }

    @Override
    protected void destroy() {
        // Just call spawn again. We always need fireballs on the screen so this is logical.
        spawn();
    }

    @Override
    public void update(int speed) {
        // If the fireball reaches the screen we regenerate.
        if (y >= screenY) destroy();
            // Otherwise we increment its transposition
        else y += ySpeed;
    }
}