package com.example.design;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
import android.widget.Toast;

import java.util.ArrayList;

/**
 * Created by Baz on 10/02/2019.
 */

public class DBAdapter extends SQLiteOpenHelper{
    private SQLiteDatabase mDatabase;
    public static final String DBName = "devavidb 2 2";
    public static String DBPath = "";
    private Context mContext;

    public DBAdapter(Context context){
        super(context, DBName, null, 1);
        this.mContext = context;
        this.DBPath = "/data/data/" + context.getPackageName() + "/" + "databases/";
    }
    @Override
    public void onCreate(SQLiteDatabase db) {

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
    }

    public void openDatabase() {
        String dbPath = mContext.getDatabasePath(DBName).getPath();
        if(mDatabase != null && mDatabase.isOpen()) {
            return;
        }
        mDatabase = SQLiteDatabase.openDatabase(dbPath, null, SQLiteDatabase.OPEN_READWRITE);
    }

    public void closeDatabase() {
        if(mDatabase!=null) {
            mDatabase.close();
        }
    }

    public Cursor getData(){
        openDatabase();
        Cursor cursor = mDatabase.rawQuery("SELECT * FROM javatable",null);
       return cursor;
    }

    //Returning the meaning of the words
    public String getMeaning(String meanW) {
        openDatabase();
        Cursor cursor = mDatabase.rawQuery("SELECT * FROM javatable WHERE word='" + meanW + "';", null);

        // Check if cursor has results
        if (cursor != null && cursor.moveToFirst()) {
            meanW = cursor.getString(2);
            cursor.close();
            closeDatabase();
            return meanW;
        } else {
            cursor.close();
            closeDatabase();
            return "Word not found"; // Return a message indicating the word is not found
        }
    }

    public String getSyntax(String syntax) {
        openDatabase();
        Cursor cursor = mDatabase.rawQuery("SELECT * FROM javatable WHERE word='" + syntax + "';", null);

        // Check if cursor has results
        if (cursor != null && cursor.moveToFirst()) {
            syntax = cursor.getString(3);
            cursor.close();
            closeDatabase();
            return syntax;
        } else {
            cursor.close();
            closeDatabase();
            return "Word not found"; // Return a message indicating the word is not found
        }
    }

    public String getExplanation(String expla) {
        openDatabase();
        Cursor cursor = mDatabase.rawQuery("SELECT * FROM javatable WHERE word='" + expla + "';", null);

        // Check if cursor has results
        if (cursor != null && cursor.moveToFirst()) {
            expla = cursor.getString(4);
            cursor.close();
            closeDatabase();
            return expla;
        } else {
            cursor.close();
            closeDatabase();
            return "Word not found"; // Return a message indicating the word is not found
        }
    }
    public String getCategory(String expla) {
        openDatabase();
        Cursor cursor = mDatabase.rawQuery("SELECT * FROM javatable WHERE word='" + expla + "';", null);

        // Check if cursor has results
        if (cursor != null && cursor.moveToFirst()) {
            expla = cursor.getString(5);
            cursor.close();
            closeDatabase();
            return expla;
        } else {
            cursor.close();
            closeDatabase();
            return "Word not found"; // Return a message indicating the word is not found
        }
    }

    public void insertIntoHistory(String word) {
        openDatabase();
        ContentValues values = new ContentValues();
        values.put("word", word);
        long result = mDatabase.insert("history", null, values);
        closeDatabase();
        if (result == -1) {
            // Insertion failed
            Log.e("DBAdapter", "Failed to insert word into history table");
        } else {
            // Insertion successful
            Log.i("DBAdapter", "Inserted word into history table: " + word);
        }
    }

    public Cursor getAllDataFromHistory() {
        openDatabase();
        // Modified query to include a default row
        Cursor cursor = mDatabase.rawQuery("SELECT Distinct * FROM history",null);
        return cursor;
    }
    public void deleteAllHistory() {
        openDatabase();
        mDatabase.execSQL("DELETE FROM history");
        closeDatabase();
    }
    public int getHistoryRowCount() {
        int count = 0;
        openDatabase();
        Cursor cursor = mDatabase.rawQuery("SELECT COUNT(*) FROM history", null);
        if (cursor != null && cursor.moveToFirst()) {
            count = cursor.getInt(0);
        }
        cursor.close();
        closeDatabase();
        return count;
    }
    public void deleteFromHistory(String word) {
        openDatabase();
        // Use the delete method with a WHERE clause to specify the row to delete
        int result = mDatabase.delete("history", "word = ?", new String[]{word});
        closeDatabase();

        if (result > 0) {
            // Deletion successful
            Log.i("DBAdapter", "Deleted word from history table: " + word);
        } else {
            // Deletion failed
            Log.e("DBAdapter", "Failed to delete word from history table: " + word);
        }
    }

    public void insertIntoFavorites(String word, String category) {
        openDatabase();

        // Check if the word already exists in favorites
        Cursor cursor = mDatabase.rawQuery("SELECT * FROM favorites WHERE word = ?", new String[]{word});


        // Word does not exist in favorites, proceed with insertion
        ContentValues values = new ContentValues();
        values.put("word", word);
        values.put("catergory", category);
        long result = mDatabase.insert("favorites", null, values);

        if (result == -1) {
            // Insertion failed
            Log.e("DBAdapter", "Failed to insert word into favorites table");
        } else {
            // Insertion successful
            Log.i("DBAdapter", "Inserted word into favorites table: " + word + ", Category: " + category);
            Toast.makeText(mContext, "Inserted word into favorites table: " + word, Toast.LENGTH_SHORT).show();
        }

        cursor.close();
        closeDatabase();
    }
    public Cursor getAllDataFromFavorites() {
        openDatabase();
        // Modified query to include a default row
        Cursor cursor = mDatabase.rawQuery("SELECT Distinct * FROM favorites",null);
        return cursor;
    }
    public void removeFromFavorites(String word) {
        openDatabase();
        int result = mDatabase.delete("favorites", "word = ?", new String[]{word});
        closeDatabase();

        if (result > 0) {
            // Deletion successful
            Log.i("DBAdapter", "Removed word from favorites table: " + word);
            Toast.makeText(mContext, "Removed word from favorites table: " + word, Toast.LENGTH_SHORT).show();
        } else {
            // Deletion failed
            Log.e("DBAdapter", "Failed to remove word from favorites table: " + word);
        }
    }
    public boolean isWordInFavorites(String word) {
        openDatabase();
        Cursor cursor = mDatabase.rawQuery("SELECT * FROM favorites WHERE word = ?", new String[]{word});
        boolean isInFavorites = cursor.getCount() > 0;
        cursor.close();
        closeDatabase();
        return isInFavorites;
    }
    public boolean isWordInJavatable(String word) {
        openDatabase();

        // Query the javatable to check if the word exists
        Cursor cursor = mDatabase.rawQuery("SELECT * FROM javatable WHERE word = ?", new String[]{word});

        // Check if the word exists in the javatable
        boolean isInJavatable = cursor.getCount() > 0;

        cursor.close();
        closeDatabase();

        return isInJavatable;
    }
    public boolean isWordInAndroidtable(String word) {
        openDatabase();

        // Query the androidtable to check if the word exists
        Cursor cursor = mDatabase.rawQuery("SELECT * FROM androidtable WHERE word = ?", new String[]{word});

        // Check if the word exists in the androidtable
        boolean isInAndroidtable = cursor.getCount() > 0;

        cursor.close();
        closeDatabase();

        return isInAndroidtable;
    }



}