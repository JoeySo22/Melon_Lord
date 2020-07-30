package edu.utep.cs4381.melonlord.model;

import android.content.Context;
import android.graphics.Bitmap;

import java.io.File;

public class VillainBackground {
    protected int screenX, screenY, x, y;
    protected Bitmap bitMap;

    public VillainBackground(Context context, int w, int h, Bitmap bitMap){
        screenX = w;
        screenY = h;
        x = 0;
        y = 0;
        this.bitMap = bitMap;
    }

    public int getX(){ return x; }
    public int getY(){ return y; }
    public Bitmap getBitMap(){ return bitMap; }

}
