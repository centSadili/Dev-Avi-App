package com.example.design;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;

import java.util.ArrayList;

public class StudyActivity extends AppCompatActivity implements recycler_interface{
    RecyclerView recyclerview;
    ArrayList<model> arrayList = new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_study);
        getWindow().setStatusBarColor(ContextCompat.getColor(StudyActivity.this,R.color.statusbar));
        recyclerview = findViewById(R.id.recyclerview);
        recyclerview.setLayoutManager(new LinearLayoutManager(this));

        arrayList.add(new model(R.drawable.vscode_fill_svgrepo_com, "VSCode", "https://code.visualstudio.com/docs/java/java-tutorial"));
        arrayList.add(new model(R.drawable.brand_netbeans_svgrepo_com, "Netbeans", "https://netbeans.apache.org/tutorial/main/kb/docs/java/quickstart/"));
        arrayList.add(new model(R.drawable.logo_google_android_studio_2_svgrepo_com, "Android Studio", "https://code.tutsplus.com/creating-your-first-android-app--cms-34497t"));
        arrayList.add(new model(R.drawable.microsoft_sql_server_logo_svgrepo_com, "Microsoft SQL Server", "https://learn.microsoft.com/en-us/sql/ssms/quickstarts/ssms-connect-query-sql-server?view=sql-server-ver16"));
        arrayList.add(new model(R.drawable.github_142_svgrepo_com, "Github", "https://docs.github.com/en/get-started"));


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