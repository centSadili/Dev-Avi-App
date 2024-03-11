package com.example.design;

import static android.content.ContentValues.TAG;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageButton;
import android.widget.SearchView;
import android.widget.Toast;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;

public class HistoryActivity extends AppCompatActivity implements RecyclerViewInterface{
    Context context;
    RecyclerView recyclerView;
    ArrayList<String> history;
    ArrayList<String> category;
    DBAdapter dbAdapter;
    DBAdapter2 dbAdapter2;
    HistoryAdapter myAdapter;
    SearchView searchView;
    ImageButton removeBtn,deleteBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);
        getWindow().setStatusBarColor(ContextCompat.getColor(HistoryActivity.this,R.color.statusbar));
        context = this;
        dbAdapter = new DBAdapter(context);
        dbAdapter2 = new DBAdapter2(context);
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
        removeBtn=findViewById(R.id.removeBtn);
        deleteBtn=findViewById(R.id.deleteBtn);
        // Initialize word ArrayList
        history = new ArrayList<>();
        category = new ArrayList<>();
        myAdapter = new HistoryAdapter(this, history, this); // Pass the interface reference

        recyclerView = findViewById(R.id.recyclerview);

        recyclerView.setAdapter(myAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        displayData();
        removeBtn.setOnClickListener(view -> {
            // Check if the history database has any data
            if (dbAdapter.getHistoryRowCount() == 0) {
                // Show a message to the user indicating that there is no item to delete
                Toast.makeText(context, "No items to delete", Toast.LENGTH_SHORT).show();
            } else {
                // Show the confirmation dialog for deletion
                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setTitle("Confirm Deletion");
                builder.setMessage("Are you sure you want to delete this item?");
                builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // Call the deleteFromHistory method to remove the selected item from the history table
                        dbAdapter.deleteFromHistory(myAdapter.getSelectedText());
                        dialog.dismiss();
                        // Refresh the activity by recreating it
                        Intent intent = getIntent();
                        finish();
                        startActivity(intent);
                    }
                });
                builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
                AlertDialog alertDialog = builder.create();
                alertDialog.show();
            }
        });
        deleteBtn.setOnClickListener(view->{
            AlertDialog.Builder builder = new AlertDialog.Builder(context);
            builder.setTitle("Confirm");
            builder.setMessage("Are you sure you want to delete all history?");
            builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    // Get the count of rows before deletion
                    int initialRowCount = dbAdapter.getHistoryRowCount();

                    // Delete all history
                    dbAdapter.deleteAllHistory();

                    // Get the count of rows after deletion
                    int finalRowCount = dbAdapter.getHistoryRowCount();

                    // Check if rows have been deleted or if there was no history data
                    if (initialRowCount > 0 && finalRowCount == 0) {
                        // Rows have been deleted or there was no history data
                        // Notify the user that rows have been deleted
                        Toast.makeText(context, "All history has been deleted", Toast.LENGTH_SHORT).show();

                        // Refresh the activity by recreating it
                        Intent intent = getIntent();
                        finish();
                        startActivity(intent);
                    } else {
                        // Rows have not been deleted
                        Toast.makeText(context, "Failed to delete history", Toast.LENGTH_SHORT).show();
                    }
                }
            });
            builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    // User clicked No, do nothing
                    dialog.dismiss();
                }
            });
            AlertDialog dialog = builder.create();
            dialog.show();
        });
        setupSearchView();
    }
    private void filter(String text) {
        ArrayList<String> filteredList = new ArrayList<>();

        for (String item : history) {
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
        Cursor cursor = dbAdapter.getAllDataFromHistory();
        if (cursor == null) {
            Log.e(TAG, "Cursor is null");
            Toast.makeText(HistoryActivity.this, "Failed to retrieve data", Toast.LENGTH_SHORT).show();
            return;
        }

        // Clear existing data from the list
        history.clear();

        // Move cursor to the first row
        if (cursor.moveToFirst()) {
            do {
                // Extract data from cursor and add it to the history list
                history.add(cursor.getString(1));
                category.add(cursor.getString(0));
            } while (cursor.moveToNext());
        }

        // Close cursor after use
        cursor.close();

        // Notify adapter that data set has changed
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
/*        String clickedWord = history.get(position);
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
        }*/

    }
}