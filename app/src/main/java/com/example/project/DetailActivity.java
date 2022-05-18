package com.example.project;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;

public class DetailActivity extends AppCompatActivity {

    private Gson gson;
    private Apple apple;
    private TextView title;
    private ImageView image;
    private TextView origin;
    private TextView characteristics;

    private String arrayListToString(ArrayList<String> list) {
        String listAsString = list.toString();
        return listAsString.substring(1, listAsString.length() - 1);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        title = findViewById(R.id.detail_title);
        image = findViewById(R.id.detail_image);
        origin = findViewById(R.id.detail_origin);
        characteristics = findViewById(R.id.detail_characteristics);

        gson = new Gson();

        Bundle extras = getIntent().getExtras();

        if (extras.size() != 0) {
            String json = extras.getString("data");
            Type type = new TypeToken<Apple>() {}.getType();

            apple = gson.fromJson(json, type);
            title.setText(apple.getName());
            origin.setText(String.format("\uD83D\uDCCD Origin: %s", apple.getLocation()));
            characteristics.setText(String.format("\uD83D\uDCD3 Characteristics: %s", arrayListToString(apple.getAuxdata().getCharacteristics())));
            new ImageDownloader(image).execute(apple.getAuxdata().getImg());
        }

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        // handle arrow click here
        if (item.getItemId() == android.R.id.home) {
            finish(); // close this activity and return to preview activity (if there is any)
        }

        return super.onOptionsItemSelected(item);
    }
}