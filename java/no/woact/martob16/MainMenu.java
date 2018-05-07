package no.woact.martob16;

import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.content.Intent;
import android.widget.Button;
import android.widget.TextView;

public class MainMenu extends AppCompatActivity implements SetNameDialog.SetNameDialogListener
{
    private String playerName;
    private Button buttonSetName;
    private TextView welcomeText;

    private String loadWelcomeText;
    public static final String SHARED_PREF = "sharedPrefs";
    public static final String welcomeTextShared = "text";
    public static final String playerNameTextShared = "text1";

    DatabaseHelper mDatabaseHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_menu);

        mDatabaseHelper = new DatabaseHelper(this);

        welcomeText = findViewById(R.id.textview_Welcome);
        buttonSetName = findViewById(R.id.button_setName);
        buttonSetName.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                setNameDialog();
            }
        });
        loadPlayerName();
        updateViews();
    }

    @Override
    public void applyTexts(String playername)
    {
        // implementer: Brukeren må skrive in noe, kan ikke være tom.
        playerName = playername;
        welcomeText.setText("Welcome " + playerName + ", Trump will never fail!");
        savePlayerName();
    }

    public void setNameDialog()
    {
        SetNameDialog setnamedialog = new SetNameDialog();
        setnamedialog.show(getSupportFragmentManager(),"Eksempel dialog");
    }

    public void onClickPvAI(View view)
    {
        Intent toPvAI = new Intent(MainMenu.this, pvai.class);
        startActivity(toPvAI);
    }

    public void onClickPVP(View view)
    {
        Intent toPVP = new Intent(MainMenu.this, pvp.class);
        startActivity(toPVP);
    }

    public void onClickAbout(View view)
    {
        Intent toABOUT = new Intent(MainMenu.this, about.class);
        startActivity(toABOUT);
    }

    public void onClickResetDatabase(View view)
    {
        deleteDatabase();
    }

    public void onClickStats(View view)
    {
        // Flytter data over til ny activity
        Intent toSTATS = new Intent(MainMenu.this, stats.class);
        startActivity(toSTATS);
    }

    public void deleteDatabase()
    {
        mDatabaseHelper.deleteDatabase();
    }

    public void savePlayerName()
    {
        SharedPreferences sharedPreferences = getSharedPreferences(SHARED_PREF, MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();

        editor.putString(welcomeTextShared, welcomeText.getText().toString());
        editor.putString(playerNameTextShared, playerName);

        editor.apply();
    }

    public void loadPlayerName()
    {
        SharedPreferences sharedPreferences = getSharedPreferences(SHARED_PREF, MODE_PRIVATE);
        loadWelcomeText = sharedPreferences.getString(welcomeTextShared, welcomeText.getText().toString());
    }

    public void updateViews()
    {
        welcomeText.setText(loadWelcomeText);
    }

}
