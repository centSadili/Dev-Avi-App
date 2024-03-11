package com.example.design;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageButton;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements recycler_interface {

    RecyclerView recyclerview;
    ArrayList<model> arrayList = new ArrayList<>();
    ImageButton historyBtn,faveBtn,studyBtn,compBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getWindow().setStatusBarColor(ContextCompat.getColor(MainActivity.this,R.color.statusbar));
        historyBtn= findViewById(R.id.historyBtn);
        faveBtn= findViewById(R.id.faveBtn);
        studyBtn= findViewById(R.id.studyBtn);
        compBtn= findViewById(R.id.compilerBtn);

        recyclerview = findViewById(R.id.recyclerview);
        recyclerview.setLayoutManager(new LinearLayoutManager(this));

        arrayList.add(new model(R.drawable.android_svgrepo_com, "ANDROID STUDIO", "(IDE) used for developing Android applications"));
        arrayList.add(new model(R.drawable.java_svgrepo_com, "JAVA", "Java is a versatile, object-oriented programming language"));


        model_recycler model_recycler = new model_recycler(this, arrayList, this);
        recyclerview.setAdapter(model_recycler);




        faveBtn.setOnClickListener(view-> {
            Intent in = new Intent(MainActivity.this,FavoritesActivity.class);
            startActivity(in);
        });
        historyBtn.setOnClickListener(view-> {
            Intent in = new Intent(MainActivity.this,HistoryActivity.class);
            startActivity(in);
        });
        compBtn.setOnClickListener(view-> {
            Intent in = new Intent(MainActivity.this,CompilerActivity.class);
            startActivity(in);
        });
        studyBtn.setOnClickListener(view-> {
            Intent in = new Intent(MainActivity.this,StudyActivity.class);
            startActivity(in);
        });





    }

    @Override
    public void onItemClick(int position) {
if(arrayList.get(position).getName().equalsIgnoreCase("java")){
    Intent in = new Intent(this, MainActivity2.class);
    in.putExtra("Name", arrayList.get(position).getName());
    in.putExtra("Description", arrayList.get(position).getDescription());
    in.putExtra("Image", arrayList.get(position).getImg());
    startActivity(in);
}else{
    Intent in = new Intent(this, MainActivity3.class);
    in.putExtra("Name", arrayList.get(position).getName());
    in.putExtra("Description", arrayList.get(position).getDescription());
    in.putExtra("Image", arrayList.get(position).getImg());
    startActivity(in);
}

    }
}