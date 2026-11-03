package com.example.crossroadmadness.activities;

import android.os.Bundle;
import android.widget.Button;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.crossroadmadness.R;

public class MainActivity extends AppCompatActivity {

    //Klasa zakańczająca działanie programu
    private void closeGame(){
        finish();
    }
    //Klasa obsługująca przycisk instrukcji
    private void showInstructions(){
        Toast.makeText(this, R.string.instructions_soon, Toast.LENGTH_SHORT).show();
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

        Button playButton = findViewById(R.id.playButton);
        Button exitButton = findViewById(R.id.exitButton);
        Button instructionButton = findViewById(R.id.instructionButton);

        instructionButton.setOnClickListener(v -> showInstructions());
        exitButton.setOnClickListener(v -> closeGame());

        playButton.setOnClickListener(v -> Toast.makeText(this, R.string.game_started, Toast.LENGTH_SHORT).show());

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }
}