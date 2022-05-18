package com.example.project;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

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
    private Intent intent;
    private Intent aboutIntent;
    private Gson gson;

    private final String JSON_URL = "https://mobprog.webug.se/json-api?login=a21oligu";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        appleAdapter = new AppleAdapter(this, this);
        gson = new Gson();

        recyclerView = findViewById(R.id.apple_recycler);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(appleAdapter);

        intent = new Intent(this, DetailActivity.class).setAction(Intent.ACTION_SEND);
        aboutIntent = new Intent(this, AboutActivity.class).setAction(Intent.ACTION_SEND);

        JsonTask jsonTask = new JsonTask(this);
        jsonTask.execute(JSON_URL);
    }

    @Override
    public void onPostExecute(String json) {
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
        intent.putExtra("data", gson.toJson(apple));
        startActivity(intent);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        switch (id) {
            case R.id.action_about:
                startActivity(aboutIntent);
                return true;
            case R.id.action_filter_az:
                appleAdapter.filterApples("A_Z");
                return true;
            case R.id.action_filter_za:
                appleAdapter.filterApples("Z_A");
                return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
