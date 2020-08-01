package edu.utep.cs4381.melonlord;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.IBinder;
import android.os.PowerManager;
import android.widget.Button;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity{


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        SharedPreferences preferences;
        SharedPreferences.Editor editor;
        preferences = getSharedPreferences("HISCORE", MODE_PRIVATE);

        //Code already in Melon Lord
        Button playButton = findViewById(R.id.startButton);
        TextView textView = (TextView) findViewById(R.id.hiscore);
        long longestTime = preferences.getLong("longestTime", 0);
        textView.setText(String.format("Longest Time: %s seconds", longestTime));
        playButton.setOnClickListener(view -> {
            startActivity(new Intent(this, GameActivity.class));
            finish();
        });
    }//end onCreate
}