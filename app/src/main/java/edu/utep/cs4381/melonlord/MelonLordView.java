package edu.utep.cs4381.melonlord;

import android.content.Context;
import android.view.SurfaceView;

import edu.utep.cs4381.melonlord.model.Player;

public class MelonLordView extends SurfaceView {

    private final Context context;
    private final int screenWidth;
    private final int screenHeight;

    private Player sokka;

    public MelonLordView(Context context, int screenWidth, int screenHeight) {
        super(context);
        this.context = context;
        this.screenWidth = screenWidth;
        this.screenHeight = screenHeight;
    }

    public void resume() {
    }

    public void pause() {
    }
}
