package no.woact.martob16;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class pvp extends AppCompatActivity implements View.OnClickListener
{
    DatabaseHelper mDatabaseHelper;

    private Button[][] currentBoard = new Button[3][3];
    private Button resetBoardButton;

    private TextView textViewPlayerOne;
    private TextView textViewPlayerTwo;

    private int playerOneScore = 0;
    private int playerTwoScore = 0;

    private int roundCounter;

    private boolean playerOneTurn = true;

    private String playerOneName;

    private String currentGameSessionData;

    public static final String SHARED_PREF = "sharedPrefs";
    public static final String playerNameTextShared = "text1";
    String loadPlayerText;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pvp);

        mDatabaseHelper = new DatabaseHelper(this);

        textViewPlayerOne = findViewById(R.id.text_view_player1);
        textViewPlayerTwo = findViewById(R.id.text_view_player2);

        savePlayerName();
        loadPlayerName();

        for(int j = 0; j < 3; j++)
        {
            for(int i = 0; i < 3; i++)
            {
                String buttonID = "button_" + j + i;
                int resID = getResources().getIdentifier(buttonID, "id", getPackageName());
                currentBoard[j][i] = findViewById(resID);
                currentBoard[j][i].setOnClickListener(this);

            }
        }

        resetBoardButton = findViewById(R.id.button_reset);
        resetBoardButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                newGame();
            }
        });

    }

    @Override
    public void onClick(View view)
    {
        if(!((Button) view).getText().toString().equals(""))
        {
            return;
        }

        if(playerOneTurn)
        {
            ((Button) view).setText("X");
        }
        else
            {
                ((Button) view).setText("O");
            }
            roundCounter++;

        if (checkForWinner()) {
            if (playerOneTurn) {
                playerOneWins();
            } else {
                playerTwoWins();
            }
        } else if (roundCounter == 9) {
            matchDraw();
        } else {
            playerOneTurn = !playerOneTurn;
        }

        if(playerOneScore == 4)
        {
            currentGameSessionData = playerOneName + " Wins at Score: " + playerOneScore + " and Player 2 Score: " + playerTwoScore;
            moveToStats(currentGameSessionData);
        }

        if(playerTwoScore == 4)
        {
            currentGameSessionData = "Player 2 Wins at Score: " + playerTwoScore + " and " + playerOneName + " got " +"Score: " + playerOneScore;
            moveToStats(currentGameSessionData);
        }
    }

    private boolean checkForWinner()
    {
        String[][] currentBoardCopy = new String[3][3];

        for(int j = 0; j < 3; j++)
        {
            for(int i = 0; i < 3; i++)
            {
                currentBoardCopy[j][i] = currentBoard[j][i].getText().toString();
            }
        }

        for(int j = 0; j < 3; j++)
        {
            if(currentBoardCopy[j][0].equals(currentBoardCopy[j][1])
                    && currentBoardCopy[j][0].equals(currentBoardCopy[j][2])
                    && !currentBoardCopy[j][0].equals(""))
                return true;
        }

        for(int j = 0; j < 3; j++)
        {
            if(currentBoardCopy[0][j].equals(currentBoardCopy[1][j])
                    && currentBoardCopy[0][j].equals(currentBoardCopy[2][j])
                    && !currentBoardCopy[0][j].equals(""))
                return true;
        }

        if(currentBoardCopy[0][0].equals(currentBoardCopy[1][1])
            && currentBoardCopy[0][0].equals(currentBoardCopy[2][2])
            && !currentBoardCopy[0][0].equals(""))
        return true;

        if(currentBoardCopy[0][2].equals(currentBoardCopy[1][1])
                && currentBoardCopy[0][2].equals(currentBoardCopy[2][0])
                && !currentBoardCopy[0][2].equals(""))
            return true;

        return false;
    }

    private void playerOneWins()
    {
        playerOneScore++;
        updateScoreTxt();
        Toast.makeText(this, playerOneName+" Wins!", Toast.LENGTH_SHORT).show();
        resetCurrentBoard();
    }

    private void playerTwoWins()
    {
        playerTwoScore++;
        updateScoreTxt();
        Toast.makeText(this, "Player Two Wins!", Toast.LENGTH_SHORT).show();
        resetCurrentBoard();
    }

    private void matchDraw()
    {
        Toast.makeText(this, "Match Draw!", Toast.LENGTH_SHORT).show();
        resetCurrentBoard();
    }

    private void resetCurrentBoard()
    {
        for(int j = 0; j < 3; j++)
        {
            for(int i = 0; i < 3; i++)
            {
                currentBoard[j][i].setText("");
            }
        }
        roundCounter = 0;
        playerOneTurn = true;
    }

    private void updateScoreTxt()
    {
        textViewPlayerOne.setText(playerOneName + " Wins: " + playerOneScore + "/4");
        textViewPlayerTwo.setText("Player 2 Wins: " + playerTwoScore + "/4");
    }

    private void newGame()
    {
        playerOneScore = 0;
        playerTwoScore = 0;
        updateScoreTxt();
        resetCurrentBoard();
    }

    @Override
    public void onRestoreInstanceState(Bundle savedInstanceState)
    {
        super.onRestoreInstanceState(savedInstanceState);

        playerOneName = savedInstanceState.getString("playerOneName");
        roundCounter = savedInstanceState.getInt("roundCounter");
        playerOneScore = savedInstanceState.getInt("playerOneScore");
        playerTwoScore = savedInstanceState.getInt("playerTwoScore");
        playerOneTurn = savedInstanceState.getBoolean("playerOneTurn");
    }

    @Override
    public void onSaveInstanceState(Bundle outState)
    {
        super.onSaveInstanceState(outState);

        outState.putString("playerOneName", playerOneName);
        outState.putInt("roundCounter", roundCounter);
        outState.putInt("playerOneScore", playerOneScore);
        outState.putInt("playerTwoScore", playerTwoScore);
        outState.putBoolean("playerOneTurn", playerOneTurn);
    }

    public void savePlayerName()
    {
        SharedPreferences sharedPreferences = getSharedPreferences(SHARED_PREF, MODE_PRIVATE);
        loadPlayerText = sharedPreferences.getString(playerNameTextShared, "Player1");
    }

    public void loadPlayerName()
    {
        playerOneName = loadPlayerText;
        textViewPlayerOne.setText(playerOneName + " Wins: " + playerOneScore + "/4");
    }

    private void toastMessage(String message)
    {
        Toast.makeText(this,message, Toast.LENGTH_SHORT).show();
    }

    public void AddData(String data)
    {
        boolean insertData = mDatabaseHelper.addData(data);

        if (insertData) {
            toastMessage("Data Successfully Added!");
        } else {
            toastMessage("Something went wrong!");
        }
    }

    public void moveToStats(String gameSessionData)
    {
        String data = gameSessionData;
        AddData(data);
        newGame();
        Intent intent = new Intent(pvp.this, stats.class);
        startActivity(intent);
    }
}

