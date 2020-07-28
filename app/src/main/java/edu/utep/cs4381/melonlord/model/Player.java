package edu.utep.cs4381.melonlord.model;

import android.content.Context;

public class Player extends GameObject{

    public Player(Context context, int screenWidth, int screenHeight) {
        super(context, screenWidth, screenHeight);
        // Setup in te middle
        x = screenWidth/2;
        // TODO : Find a good speed for this.
        xSpeed = 10;
        y = screenHeight;
        ySpeed = 0;

    }



    @Override
    protected void update() {

    }
}
