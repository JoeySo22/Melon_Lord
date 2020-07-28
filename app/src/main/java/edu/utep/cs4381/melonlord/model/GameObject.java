package edu.utep.cs4381.melonlord.model;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Rect;

import androidx.annotation.Nullable;

public abstract class GameObject {
    protected Context context;
    protected int screenX;
    protected int screenY;
    protected int x;
    protected int xSpeed;
    protected int y;
    protected int ySpeed;
    @Nullable
    protected Bitmap bitmap;
    @Nullable
    protected Rect hitbox;

    public GameObject(Context context, int screenWidth, int screenHeight) {
        context = context;
        screenX = screenWidth;
        screenY = screenHeight;
        x = 0;
        xSpeed = 0;
        y = 0;
        ySpeed = 0;
    }

    protected void setBitmap(@Nullable Bitmap bitmap) {
        bitmap = bitmap;
    }

    protected abstract void update();

    public Rect getHitbox() {
        return hitbox;
    }
}
