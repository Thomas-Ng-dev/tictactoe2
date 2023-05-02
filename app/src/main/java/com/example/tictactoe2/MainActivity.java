package com.example.tictactoe2;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class MainActivity extends AppCompatActivity{
    private EditText player1;
    private EditText player2;
    private Button submitPlayerNames;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        player1 = findViewById(R.id.menuSetPlayer1);
        player2 = findViewById(R.id.menuSetPlayer2);
        submitPlayerNames = (Button) findViewById(R.id.submitPlayerNames);

    }

    /**
     * Take user inputted names and send them to the next activity
     * @param view
     */
    public void submitName(View view)
    {
        String player1Name = player1.getText().toString();
        String player2Name = player2.getText().toString();
        Intent intent = new Intent(this, GameActivity.class);
        intent.putExtra("PLAYER_1_NAME", player1Name);
        intent.putExtra("PLAYER_2_NAME", player2Name);
        startActivity(intent);
    }
}