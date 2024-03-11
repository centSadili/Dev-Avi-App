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

public class MainActivity3 extends AppCompatActivity implements RecyclerViewInterface {
    Context context;
    RecyclerView recyclerView, historyView;
    ArrayList<String> word;
    ArrayList<String> category;// Initialize this ArrayList

    DBAdapter2 dbAdapter;
    MyAdapter myAdapter;
    SearchView searchView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main3);
        getWindow().setStatusBarColor(ContextCompat.getColor(MainActivity3.this,R.color.statusbar));
        context = this;
        dbAdapter = new DBAdapter2(context);
        File database = getApplicationContext().getDatabasePath(DBAdapter.DBName);
        if (!database.exists()) {
            dbAdapter.getReadableDatabase();
            //Copy db
            if (copyDatabase(this)) {
                Toast.makeText(this, "Copy database success", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "Copy data error", Toast.LENGTH_SHORT).show();
                return;
            }
        }
        searchView = findViewById(R.id.searchBar);
        // Initialize word ArrayList
        word = new ArrayList<>();
        category = new ArrayList<>();


        myAdapter = new MyAdapter(this, word,category, this); // Pass the interface reference

        recyclerView = findViewById(R.id.recyclerview);

        recyclerView.setAdapter(myAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        displayData();

        // Set up SearchView listener
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                // Filter the dataset based on the new text
                filter(newText);
                return true;
            }
        });
    }

    private void displayData() {
        Cursor cursor = dbAdapter.getData();
        Cursor cursor1 = dbAdapter.getAllDataFromHistory();

        if (cursor == null || cursor1 == null) {
            // Handle case where cursor is null
            Toast.makeText(MainActivity3.this, "Cursor is null", Toast.LENGTH_SHORT).show();
            return;
        }

        if (cursor.getCount() == 0) {
            Toast.makeText(MainActivity3.this, "Entry not Exist", Toast.LENGTH_SHORT).show();
        } else {
            while (cursor.moveToNext()) {
                word.add(cursor.getString(1)); // Add the word (2nd column) to the 'word' ArrayList
                category.add(cursor.getString(4)); // Add the category (1st column) to the 'category' ArrayList
            }
        }

       /* if (cursor1.getCount() == 0) {
            Toast.makeText(MainActivity2.this, "History entry not Exist", Toast.LENGTH_SHORT).show();
        } else {
            // Resetting cursor1 position to the first row
            cursor1.moveToFirst();
            while (!cursor1.isAfterLast()) { // Iterate until cursor1 reaches the last row
                // Add the 1st column of history data to the 'history' ArrayList
//                history.add(cursor1.getString(1));
                // Add the 2nd column of history data to the 'category' ArrayList
                category.add(cursor1.getString(0));
                cursor1.moveToNext(); // Move to the next row
            }
        }*/

        // Close the cursors after use to avoid memory leaks
        cursor.close();
        /* cursor1.close();*/

        // Notify the adapter that the data set has changed

    }




    private void filter(String text) {
        ArrayList<String> filteredList = new ArrayList<>();

        // Loop through the dataset and add items that contain the search text
        for (String item : word) {
            if (item.toLowerCase().contains(text.toLowerCase())) {
                filteredList.add(item);
            }
        }

        // Update the adapter's dataset with the filtered list
        myAdapter.filterList(filteredList);
    }

    private boolean copyDatabase(Context context) {
        try {
            InputStream inputStream = context.getAssets().open(DBAdapter.DBName);
            String outFileName = DBAdapter.DBPath + DBAdapter.DBName;
            OutputStream outputStream = new FileOutputStream(outFileName);
            byte[] buff = new byte[1024];
            int length = 0;
            while ((length = inputStream.read(buff)) > 0) {
                outputStream.write(buff, 0, length);
            }
            outputStream.flush();
            outputStream.close();
            Log.w("MainActivity", "DB copied");
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public void onItemClick(int position) {
        String clickedWord = word.get(position);
        Intent intent = new Intent(MainActivity3.this, AndWordActivity.class);
        intent.putExtra("clickedWord", clickedWord);
        Dictionary.setWord(clickedWord);
        Dictionary.setDefinition(dbAdapter.getMeaning(clickedWord));
        Dictionary.setSyntax(dbAdapter.getSyntax(clickedWord));
        Dictionary.setCategory(dbAdapter.getCategory(clickedWord));

        dbAdapter.insertIntoHistory(clickedWord);
        startActivity(intent);
    }


    // Implementing onItemClick method from RecyclerViewInterface




}

