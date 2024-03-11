package com.example.design;

import static android.content.ContentValues.TAG;

import androidx.appcompat.app.AppCompatActivity;
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

public class FavoritesActivity extends AppCompatActivity implements RecyclerViewInterface{
    Context context;
    RecyclerView recyclerView;
    ArrayList<String> favorites;
    ArrayList<String> category;
    DBAdapter dbAdapter;
    DBAdapter2 dbAdapter2;
    MyAdapter myAdapter;
    SearchView searchView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favorites);
        context = this;
        dbAdapter = new DBAdapter(context);
        dbAdapter2= new DBAdapter2(context);
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
        favorites=new ArrayList<>();
        category=new ArrayList<>();
        myAdapter = new MyAdapter(this, favorites, category,this); // Pass the interface reference

        recyclerView = findViewById(R.id.recyclerview);

        recyclerView.setAdapter(myAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        displayData();

    }
    private void filter(String text) {
        ArrayList<String> filteredList = new ArrayList<>();

        for (String item : favorites) {
            if (item.toLowerCase().contains(text.toLowerCase())) {
                filteredList.add(item);
            }
        }

        myAdapter.filterList(filteredList);
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
        Cursor cursor = dbAdapter.getAllDataFromFavorites();

        if (cursor == null) {
            Log.e(TAG, "Cursor is null");
            Toast.makeText(context, "Data not available", Toast.LENGTH_SHORT).show();
            return;
        }

        while (cursor.moveToNext()) {
            favorites.add(cursor.getString(1));
            category.add(cursor.getString(2));
        }

        cursor.close();
        myAdapter.notifyDataSetChanged();
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
            Log.w("HistoryActivity", "DB copied");
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public void onItemClick(int position) {
        String clickedWord = favorites.get(position);
        if(dbAdapter.isWordInJavatable(clickedWord)){
            Intent androidIntent = new Intent(context, WordActivity.class);
            androidIntent.putExtra("word", clickedWord);
            Dictionary.setWord(clickedWord);
            Dictionary.setDefinition(dbAdapter.getMeaning(clickedWord));
            Dictionary.setSyntax(dbAdapter.getSyntax(clickedWord));
            Dictionary.setExplanation(dbAdapter.getExplanation(clickedWord));
            Dictionary.setCategory(dbAdapter.getCategory(clickedWord));
            context.startActivity(androidIntent);
        }
        else if(dbAdapter.isWordInAndroidtable(clickedWord)){
            Intent javaIntent = new Intent(context, AndWordActivity.class);
            javaIntent.putExtra("word", clickedWord);
            Dictionary.setWord(clickedWord);
            Dictionary.setDefinition(dbAdapter2.getMeaning(clickedWord));
            Dictionary.setSyntax(dbAdapter2.getSyntax(clickedWord));
            Dictionary.setCategory(dbAdapter2.getCategory(clickedWord));
            context.startActivity(javaIntent);

        }
        else{
            Toast.makeText(context, "Word not found", Toast.LENGTH_SHORT).show();
        }


    }
}