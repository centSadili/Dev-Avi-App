package com.example.design;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;

import java.util.ArrayList;

public class CompilerActivity extends AppCompatActivity implements recycler_interface{
    RecyclerView recyclerview;
    ArrayList<model> arrayList = new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_compiler);
        recyclerview = findViewById(R.id.recyclerview);
        recyclerview.setLayoutManager(new LinearLayoutManager(this));

        arrayList.add(new model(R.drawable.python, "Python", "https://onecompiler.com/python"));
        arrayList.add(new model(R.drawable.java_svgrepo, "Java", "https://onecompiler.com/java"));
        arrayList.add(new model(R.drawable.c_svgrepo_com, "C", "https://onecompiler.com/c"));
        arrayList.add(new model(R.drawable.cplusplus_svgrepo_com, "C++", "https://onecompiler.com/cpp"));
        arrayList.add(new model(R.drawable.c_sharp_16_svgrepo_com, "C#", "https://onecompiler.com/csharp"));
        arrayList.add(new model(R.drawable.sql_file_format_symbol_svgrepo_com, "SQL", "https://onecompiler.com/sqlserver"));
        arrayList.add(new model(R.drawable.swift_svgrepo_com, "Swift", "https://onecompiler.com/swift"));
        arrayList.add(new model(R.drawable.ruby_svgrepo_com, "Ruby", "https://onecompiler.com/ruby"));
        arrayList.add(new model(R.drawable.kotlin_svgrepo_com, "Kotlin", "https://onecompiler.com/kotlin"));
        arrayList.add(new model(R.drawable.javascript_svgrepo_com, "JavaScript", "https://onecompiler.com/javascript"));

        model_recycler model_recycler = new model_recycler(this, arrayList, this);
        recyclerview.setAdapter(model_recycler);
    }

    @Override
    public void onItemClick(int position) {
        Intent in = new Intent(this, WebActivity.class);
        in.putExtra("Name", arrayList.get(position).getName());
        in.putExtra("Description", arrayList.get(position).getDescription());
        in.putExtra("Image", arrayList.get(position).getImg());
        startActivity(in);

    }



}