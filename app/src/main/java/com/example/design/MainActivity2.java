package com.example.design;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.widget.SearchView;
import android.widget.Toast;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;

public class MainActivity2 extends AppCompatActivity implements RecyclerViewInterface {
    private static final String TAG = "MainActivity2";
    private Context context;
    private RecyclerView recyclerView;
    private ArrayList<String> wordList;
    private ArrayList<String> categoryList;
    private MyAdapter myAdapter;
    private SearchView searchView;
    private DBAdapter dbAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
        getWindow().setStatusBarColor(ContextCompat.getColor(MainActivity2.this,R.color.statusbar));

        context = this;
        dbAdapter = new DBAdapter(context);

        // Check if the database exists, if not, copy it from assets
        File database = getApplicationContext().getDatabasePath(DBAdapter.DBName);
        if (!database.exists()) {
            dbAdapter.getReadableDatabase();
            if (copyDatabase(context)) {
                Toast.makeText(context, "Database copied successfully", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(context, "Error copying database", Toast.LENGTH_SHORT).show();
                return;
            }
        }

        wordList = new ArrayList<>();
        categoryList = new ArrayList<>();
        myAdapter = new MyAdapter(context, wordList, categoryList, this);

        recyclerView = findViewById(R.id.recyclerview);
        recyclerView.setAdapter(myAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(context));

        searchView = findViewById(R.id.searchBar);
        setupSearchView();

        displayData();
    }

    private void setupSearchView() {
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                // Search for the query and handle the result
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                // Filter the list based on the new text
                filter(newText);
                return true;
            }

        });

    }

    private void displayData() {
        Cursor cursor = dbAdapter.getData();

        if (cursor == null) {
            Log.e(TAG, "Cursor is null");
            Toast.makeText(context, "Data not available", Toast.LENGTH_SHORT).show();
            return;
        }

        while (cursor.moveToNext()) {
            wordList.add(cursor.getString(1));
            categoryList.add(cursor.getString(5));
        }

        cursor.close();
        myAdapter.notifyDataSetChanged();
    }

    private void filter(String text) {
        ArrayList<String> filteredList = new ArrayList<>();

        for (String item : wordList) {
            if (item.toLowerCase().contains(text.toLowerCase())) {
                filteredList.add(item);
            }
        }

        myAdapter.filterList(filteredList);
    }

    private boolean copyDatabase(Context context) {
        try {
            InputStream inputStream = context.getAssets().open(DBAdapter.DBName);
            String outFileName = DBAdapter.DBPath + DBAdapter.DBName;
            OutputStream outputStream = new FileOutputStream(outFileName);
            byte[] buff = new byte[1024];
            int length;
            while ((length = inputStream.read(buff)) > 0) {
                outputStream.write(buff, 0, length);
            }
            outputStream.flush();
            outputStream.close();
            Log.d(TAG, "Database copied");
            return true;
        } catch (Exception e) {
            Log.e(TAG, "Error copying database", e);
            return false;
        }
    }

    @Override
    public void onItemClick(int position) {
        String clickedWord = wordList.get(position);
        Intent intent = new Intent(MainActivity2.this, WordActivity.class);
        intent.putExtra("clickedWord", clickedWord);
        Dictionary.setWord(clickedWord);
        Dictionary.setDefinition(dbAdapter.getMeaning(clickedWord));
        Dictionary.setSyntax(dbAdapter.getSyntax(clickedWord));
        Dictionary.setExplanation(dbAdapter.getExplanation(clickedWord));
        Dictionary.setCategory(dbAdapter.getCategory(clickedWord));
        dbAdapter.insertIntoHistory(clickedWord);
        startActivity(intent);
    }
}
