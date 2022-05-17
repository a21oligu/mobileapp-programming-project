package com.example.project;

import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity  implements JsonTask.JsonTaskListener, AppleAdapter.OnClickListener {

    private AppleAdapter appleAdapter;
    private RecyclerView recyclerView;

    private final String JSON_URL = "https://mobprog.webug.se/json-api?login=a21oligu";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        appleAdapter = new AppleAdapter(this, this);

        recyclerView = findViewById(R.id.apple_recycler);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(appleAdapter);

        JsonTask jsonTask = new JsonTask(this);
        jsonTask.execute(JSON_URL);
    }

    @Override
    public void onPostExecute(String json) {
        Gson gson = new Gson();
        Type type = new TypeToken<ArrayList<Apple>>() {}.getType();

        Log.d("Response", String.format("Got response from GET: %b", json != null));
        System.out.println(json);

        try {
            ArrayList<Apple> newApples = gson.fromJson(json, type);
            appleAdapter.addApples(newApples);
            appleAdapter.notifyDataSetChanged();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onClick(Apple apple) {
        System.out.println(apple.toString());
    }
}
