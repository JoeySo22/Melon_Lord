package edu.utep.cs4381.melonlord.model;

import android.content.Context;
import android.graphics.Bitmap;

public class FireBall extends GraphicObject {

    public FireBall(Context context, int screenWidth, int screenHeight, Bitmap bitmap) {
        super(context, screenWidth, screenHeight, bitmap);
        ySpeed = 0;
    }

    // Creates a fireball for the screen with varying speeds and positions.
    protected void generateFireBall() {
        x = rand.nextInt(screenX);
        y = 0;
        ySpeed = rand.nextInt(50) + 10;
    }

    @Override
    public void update(int speed) {
        // If the fireball reaches the screen we regenerate.
        if (y >= screenY) generateFireBall();
        // Otherwise we increment its transposition
        else y += ySpeed;
    }
}
