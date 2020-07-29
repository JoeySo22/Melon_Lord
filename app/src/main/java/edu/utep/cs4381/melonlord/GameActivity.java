package edu.utep.cs4381.melonlord;

import android.graphics.Point;
import android.os.Bundle;
import android.view.Display;

import androidx.appcompat.app.AppCompatActivity;

public class GameActivity extends AppCompatActivity {

    private MelonLordView mlView;

    @Override
    protected void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        Display display = getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        mlView = new MelonLordView(this, size.x, size.y);
        setContentView(mlView);
    }
    @Override
    protected void onResume() {
        super.onResume();
        mlView.resume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        mlView.pause();
    }
}