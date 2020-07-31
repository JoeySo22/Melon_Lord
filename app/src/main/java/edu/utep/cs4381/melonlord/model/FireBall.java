package edu.utep.cs4381.melonlord.model;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Rect;
import android.util.Log;

public class FireBall extends GraphicObject {

    //Move vertically from up to down
    private int maxY, minY;
    private int maxX, minX;
    public FireBall(Context context, int screenWidth, int screenHeight, Bitmap bitmap) {
        super(context, screenWidth, screenHeight, bitmap);
        this.maxX = this.screenWidth;
        this.maxY = this.screenHeight;
        this.minX = 0;
        this.minY = 0;

        this.bitMap = bitmap;
        hitBox = new Rect(this.x, this.y, this.bitMap.getWidth(), this.bitMap.getHeight());
        spawn();
    }

    @Override
    protected void spawn() {
        this.ySpeed = rand.nextInt(45) + 40;
        this.x = rand.nextInt(this.screenWidth - this.bitMap.getWidth());
        y = 0;
        //Log.d("Fireball/Spawn", String.format("\nleft = %d\n top= %d\n right = %d\n bottom = %d",
        //      this.x, this.y, this.x + this.bitMap.getWidth(), this.y + this.bitMap.getHeight()));

    }

    @Override
    public void destroy() {
        // Just call spawn again. We always need fireballs on the screen so this is logical.
        spawn();
    }

    @Override
    public void update(int speed) {
        // If the fireball reaches the screen we regenerate.
        if (this.y >= this.screenHeight) destroy();
            // Otherwise we increment its transposition
        else this.y += this.ySpeed;
        //Log.d("Fireball/Update", String.format("\nleft = %d\n top= %d\n right = %d\n bottom = %d",
        //      this.x, this.y, this.x + this.bitMap.getWidth(), this.y + this.bitMap.getHeight()));
        hitBox.set(this.x, this.y, this.x + this.bitMap.getWidth(),
                this.y + this.bitMap.getHeight());
    }
}//end FireBall