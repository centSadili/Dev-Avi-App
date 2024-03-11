package com.example.design;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentManager;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageButton;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class AndWordActivity extends AppCompatActivity {
    Button backBtn;
    DBAdapter dbAdapter;
    ImageButton saveBtn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_andword);
        getWindow().setStatusBarColor(ContextCompat.getColor(AndWordActivity.this,R.color.statusbar));
        BottomNavigationView btn = findViewById(R.id.bottomNavigationView);
        backBtn=findViewById(R.id.backBtn);
        saveBtn=findViewById(R.id.saveBtn);
        dbAdapter=new DBAdapter(this);
        String word = Dictionary.getWord();
        if (dbAdapter.isWordInFavorites(word)) {
            // Word is already in favorites, change the icon of saveBtn to "already saved" icon
            saveBtn.setImageResource(R.drawable.save_2_svgrepo_com);
        } else {
            // Word is not in favorites, set the default icon for saveBtn
            saveBtn.setImageResource(R.drawable.save_add_svgrepo_com__1_);
        }

        saveBtn.setOnClickListener(view -> {
            if (dbAdapter.isWordInFavorites(word)) {
                // If the word is already in favorites, remove it
                dbAdapter.removeFromFavorites(word);
                // Change the icon to the "removed" icon
                saveBtn.setImageResource(R.drawable.save_minus_svgrepo_com);
            } else {
                // If the word is not in favorites, add it
                dbAdapter.insertIntoFavorites(word, Dictionary.getCategory());
                // Change the icon to the "already saved" icon
                saveBtn.setImageResource(R.drawable.save_2_svgrepo_com);
            }
        });
        backBtn.setOnClickListener(View->{
            Intent in = new Intent(AndWordActivity.this,MainActivity.class);
            startActivity(in);

        });
        if (savedInstanceState == null) {
            // If savedInstanceState is null, it means the activity is created for the first time
            // Add the BlankFragment as the default fragment
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.fragmentContainerView, new AndDescriptionFragment())
                    .commit();
        }
        btn.setOnNavigationItemSelectedListener(item ->{
            if (item.getItemId() == R.id.description) { // Replace R.id.nav_home with your actual home menu item ID
                // Handle home button click
                // Add your logic for the home button navigation here
                // For example, replace the fragment with the home fragment
                FragmentManager fragmentManager = getSupportFragmentManager();
                fragmentManager.beginTransaction()
                        .replace(R.id.fragmentContainerView, AndDescriptionFragment.class, null)
                        .setReorderingAllowed(true)
                        .addToBackStack("name")
                        .commit();
                return true; // Return true to indicate that the click has been handled
            } else if (item.getItemId() == R.id.syntax){
                // Handle home button click
                // Add your logic for the home button navigation here
                // For example, replace the fragment with the home fragment
                FragmentManager fragmentManager = getSupportFragmentManager();
                fragmentManager.beginTransaction()
                        .replace(R.id.fragmentContainerView, AndSyntaxFragment.class, null)
                        .setReorderingAllowed(true)
                        .addToBackStack("name")
                        .commit();
                return true; // Return true to indicate that the click has been handled
            }
            return false;
        });
    }
}