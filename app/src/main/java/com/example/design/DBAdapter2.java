package com.example.design;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;

/**
 * Created by Baz on 10/02/2019.
 */

public class DBAdapter2 extends SQLiteOpenHelper{
    private SQLiteDatabase mDatabase;
    public static final String DBName = "devavidb 2 2";
    public static String DBPath = "";
    private Context mContext;

    public DBAdapter2(Context context){
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
        Cursor cursor = mDatabase.rawQuery("SELECT * FROM androidtable",null);
        return cursor;
    }

    //Returning the meaning of the words
    public String getMeaning(String meanW) {
        openDatabase();
        Cursor cursor = mDatabase.rawQuery("SELECT * FROM androidtable WHERE word='" + meanW + "';", null);

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
        Cursor cursor = mDatabase.rawQuery("SELECT * FROM androidtable WHERE word='" + syntax + "';", null);

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
    public String getCategory(String expla) {
        openDatabase();
        Cursor cursor = mDatabase.rawQuery("SELECT * FROM androidtable WHERE word='" + expla + "';", null);

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
        Cursor cursor = mDatabase.rawQuery("SELECT * FROM history",null);
        return cursor;
    }
}
