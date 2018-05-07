package no.woact.martob16;

import android.database.Cursor;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;

import java.util.ArrayList;

public class stats extends AppCompatActivity
{
    DatabaseHelper mDatabaseHelper;
    private ListView mListView;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_stats);
        mDatabaseHelper = new DatabaseHelper(this);
        mListView = findViewById(R.id.listView);

        populateStatsList();
    }

    private void populateStatsList()
    {
        Cursor data = mDatabaseHelper.getData();
        ArrayList <String> listData = new ArrayList<>();

        while(data.moveToNext())
        {
            listData.add(data.getString(1));
        }

        ListAdapter adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, listData);
        mListView.setAdapter(adapter);
    }
}
