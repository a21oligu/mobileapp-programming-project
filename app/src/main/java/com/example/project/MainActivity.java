package com.example.project;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.TextView;

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
    private SharedPreferences sharedPreferences;
    private TextView textFilteringBy;

    private final String JSON_URL = "https://mobprog.webug.se/json-api?login=a21oligu";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        textFilteringBy = findViewById(R.id.main_filtering_by);
        textFilteringBy.setText("Filtering by: none");

        appleAdapter = new AppleAdapter(this, this);
        gson = new Gson();

        recyclerView = findViewById(R.id.apple_recycler);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(appleAdapter);

        intent = new Intent(this, DetailActivity.class).setAction(Intent.ACTION_SEND);
        aboutIntent = new Intent(this, AboutActivity.class).setAction(Intent.ACTION_SEND);

        sharedPreferences = getSharedPreferences("data", MODE_PRIVATE);

        JsonTask jsonTask = new JsonTask(this);
        jsonTask.execute(JSON_URL);
    }

    @Override
    public void onPostExecute(String json) {
        Type type = new TypeToken<ArrayList<Apple>>() {}.getType();
        Log.d("Response", String.format("Got response from GET: %b", json != null));

        try {
            String filterType = sharedPreferences.getString("type", "none");
            String filterBy = sharedPreferences.getString("filter", "none");
            ArrayList<Apple> newApples = gson.fromJson(json, type);
            appleAdapter.addApples(newApples);

            // Check if saved filter type, if true filter apples
            if (!filterBy.equals("none")) {
                appleAdapter.filterApples(filterType, filterBy);
                textFilteringBy.setText(String.format("Filtering by: %s:%s", filterType, filterBy));
            }
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

    private void saveFilter(String type, String filter) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("type", type).putString("filter", filter).apply();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        switch (id) {
            case R.id.action_about:
                startActivity(aboutIntent);
                return true;
            case R.id.action_filter_none:
                appleAdapter.filterApples("none", "");
                saveFilter("none", "none");
                textFilteringBy.setText("Filtering by: none");
                return true;
            case R.id.action_filter_color:
                showFilterDialog("Choose color", "color", new String[] {"red", "green", "yellow"});
                return true;
            case R.id.action_filter_characteristic:
                showFilterDialog("Choose characteristic", "characteristic", appleAdapter.getCharacteristics().toArray(new String[appleAdapter.getCharacteristics().size()]));
                return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void showFilterDialog(String title, final String type, final String[] items) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(title);

        builder.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                saveFilter(type, items[which]);
                appleAdapter.filterApples(type, items[which]);
                textFilteringBy.setText(String.format("Filtering by: %s:%s", type, items[which]));
            }
        });

        AlertDialog dialog = builder.create();
        dialog.show();
    }
}
