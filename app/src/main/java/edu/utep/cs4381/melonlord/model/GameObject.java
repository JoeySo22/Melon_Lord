package edu.utep.cs4381.melonlord.model;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Rect;

public abstract class GameObject {
    private Context context;
    private int screenX;
    private int screenY;
    private int x;
    private int xSpeed;
    private int y;
    private int ySpeed;
    private Bitmap bitmap;
    private Rect hitbox;

    public GameObject(Context context, int screenWidth, int screenHeight) {
        context = context;
        screenX = screenWidth;
        screenY = screenHeight;
        x = 0;
        xSpeed = 0;
        y = 0;
        ySpeed = 0;
    }

}
