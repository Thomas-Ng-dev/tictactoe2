package com.example.tictactoe2;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class GameActivity extends AppCompatActivity implements View.OnClickListener{
    private TextView playerTurn;
    private Button[] grid = new Button[9];
    private Button newGame;
    private int turnCount = 0;
    private String player1Name;
    private String player2Name;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);

        // Get Player Names
        player1Name = getIntent().getStringExtra("PLAYER_1_NAME");
        if(player1Name.equals(""))
        {
            player1Name = "Player 1";
        }
        player2Name = getIntent().getStringExtra("PLAYER_2_NAME");
        if(player2Name.equals(""))
        {
            player2Name = "Player 2";
        }

        // Get UI elements

        // TextView to see whose turn it is
        playerTurn = (TextView) findViewById(R.id.playerTurn);
        playerTurn.setText(String.format(getString(R.string.playerTurn), player1Name));

        // Get the buttons that form the grid and attach listeners to them
        for(int i = 0; i < grid.length; i++)
        {
            // Cannot use string the findViewByID directly
            String buttonID = "grid" + (i);
            int resourceID = getResources().getIdentifier(buttonID, "id", getPackageName());
            grid[i] = (Button) findViewById(resourceID);
            grid[i].setOnClickListener(this);
        }

        // New Game button
        newGame = (Button) findViewById(R.id.newGame);
        newGame.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                newGame();
            }
        });
    }

    /**
     * When rotating screen, state is destroyed. Save the state of variables to send
     * to onRestoreInstancedState
     * @param savedInstanceState
     */
    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        // Save the user's current game state.
        savedInstanceState.putString("playerTurnState", playerTurn.getText().toString());
        savedInstanceState.putInt("turnCountState", turnCount);
        for(int i = 0; i < grid.length; i++)
        {
            savedInstanceState.putString("gridValue" + i, grid[i].getText().toString());
        }


        // Always call the superclass so it can save the view hierarchy state.
        super.onSaveInstanceState(savedInstanceState);
    }

    /**
     * Load the variables from onSaveInstanceState
     * @param savedInstanceState
     */
    @Override
    public void onRestoreInstanceState(Bundle savedInstanceState)
    {
        // Always call the superclass so it can restore the view hierarchy.
        super.onRestoreInstanceState(savedInstanceState);

        // Restore state members from saved instance.
        playerTurn.setText(savedInstanceState.getString("playerTurnState"));
        turnCount = savedInstanceState.getInt("turnCountState");
        for(int i = 0; i < grid.length; i++)
        {
            grid[i].setText(savedInstanceState.getString("gridValue" + i));
            if(!grid[i].getText().toString().equals(""))
            {
                disableButton(grid[i]);
            }
        }
    }

    @Override
    public void onClick(View view)
    {
        setPlayerMessage();
        Log.i("test", "Button is active");
        Button gridHex = (Button) view;
        if(!gridHex.getText().toString().equals(""))
        {
            return;
        }
        gridHex.setText(playerInput());
        disableButton(gridHex);
        Log.i("Current turn", String.valueOf(turnCount));
        turnCount++;
        Log.i("Next turn", String.valueOf(turnCount));
        if(isGameComplete() == true)
        {
            disableButtons();
        }
    }

    /**
     * Set player's name for each turn.
     */
    public void setPlayerMessage()
    {
        if((turnCount % 2) == 0)
        {
            playerTurn.setText(String.format(getString(R.string.playerTurn), player2Name));
        }
        else
        {
            playerTurn.setText(String.format(getString(R.string.playerTurn), player1Name));
        }

    }
    /**
     * Check is the game is complete because the grid is filled
     * or because a winner has been decided
     * @return
     */
    public boolean isGameComplete()
    {
        boolean isGameComplete = false;

        /*
                0   1   2
                3   4   5
                6   7   8
         */
        // Winning conditions(rows, columns, diagonals)
        //  0, 1, 2     3, 4, 5     6, 7, 8
        //  0, 3, 6     1, 4, 7     2, 5, 8
        //  0, 4, 8     2, 4, 7
        if(turnCount > 4)
        {
            if(grid[0].getText().toString().equals(grid[1].getText().toString()) && grid[1].getText().toString().equals(grid[2].getText().toString()) && !grid[0].getText().toString().equals("") ||
                    grid[3].getText().toString().equals(grid[4].getText().toString()) && grid[4].getText().toString().equals(grid[5].getText().toString()) && !grid[3].getText().toString().equals("") ||
                    grid[6].getText().toString().equals(grid[7].getText().toString()) && grid[7].getText().toString().equals(grid[8].getText().toString()) && !grid[6].getText().toString().equals("") ||
                    grid[0].getText().toString().equals(grid[3].getText().toString()) && grid[3].getText().toString().equals(grid[6].getText().toString()) && !grid[0].getText().toString().equals("") ||
                    grid[1].getText().toString().equals(grid[4].getText().toString()) && grid[4].getText().toString().equals(grid[7].getText().toString()) && !grid[3].getText().toString().equals("") ||
                    grid[2].getText().toString().equals(grid[5].getText().toString()) && grid[5].getText().toString().equals(grid[8].getText().toString()) && !grid[2].getText().toString().equals("") ||
                    grid[0].getText().toString().equals(grid[4].getText().toString()) && grid[4].getText().toString().equals(grid[8].getText().toString()) && !grid[0].getText().toString().equals("") ||
                    grid[2].getText().toString().equals(grid[4].getText().toString()) && grid[4].getText().toString().equals(grid[6].getText().toString()) && !grid[2].getText().toString().equals(""))
            {
                isGameComplete = true;
                String message = "%s wins!";
                String winner = "";
                if(turnCount % 2 == 0)
                {
                    winner = player2Name;
                }
                else
                {
                    winner = player1Name;
                }
                playerTurn.setText(String.format(message, winner));
                return isGameComplete;
            }
        }
        if(turnCount > 8)
        {
            Toast.makeText(this, "Draw!", Toast.LENGTH_LONG).show();
            isGameComplete = true;
            playerTurn.setText("");
        }
        Log.i("Game Status", String.valueOf(isGameComplete));

        return isGameComplete;
    }

    /**
     * Resets the game state
     */
    public void newGame()
    {
        playerTurn.setText(String.format(getString(R.string.playerTurn), player1Name));
        turnCount = 0;
        enableButtons();
        for(int i = 0; i < grid.length; i++)
        {
            grid[i].setText("");
        }
    }
    /**
     * Check which player's turn it is and gives X or O character output
     * @return
     */
    private String playerInput()
    {
        if((turnCount % 2) == 0)
        {
            return "X";
        }
        else
        {
            return "O";
        }
    }

    /**
     * Disable button once a value is set in the button
     * @param button
     */
    private void disableButton(Button button)
    {
        button.setEnabled(false);
    }

    /**
     * Disable all buttons when the game is complete
     */
    private void disableButtons()
    {
        for(int i = 0; i < grid.length; i++)
        {
            grid[i].setEnabled(false);
        }
    }

    /**
     * Enable all buttons when a new game begins
     */
    private void enableButtons()
    {
        for(int i = 0; i < grid.length; i++)
        {
            grid[i].setEnabled(true);
        }
    }
}