package edu.utep.cs4381.melonlord;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        SharedPreferences sharedPreferences;
        sharedPreferences = getSharedPreferences("Hiscore", MODE_PRIVATE);
        Button playButton = findViewById(R.id.startButton);
        playButton.setOnClickListener(view -> {
            startActivity(new Intent(this, GameActivity.class));
            finish();
        });


    }
}