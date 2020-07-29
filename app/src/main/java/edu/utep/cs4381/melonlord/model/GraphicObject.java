package edu.utep.cs4381.melonlord.model;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Rect;

public abstract class GraphicObject extends GameObject {

    protected Bitmap bitMap;
    protected Rect hitBox;

    public GraphicObject(Context context, int screenWidth, int screenHeight, Bitmap bitMap){
        super(context, screenWidth, screenHeight);
        this.bitMap = bitMap;
        this.hitBox = new Rect(this.x, this.y, this.bitMap.getWidth(), this.bitMap.getHeight());
    }

    public Bitmap getBitMap(){ return bitMap; }
    public Rect getHitBox(){ return hitBox; }
    protected abstract void spawn();
    protected abstract void destroy();

}