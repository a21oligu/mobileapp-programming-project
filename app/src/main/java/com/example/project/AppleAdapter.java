package com.example.project;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.Set;

public class AppleAdapter extends RecyclerView.Adapter<AppleAdapter.ViewHolder>{

    private ArrayList<Apple> listOfApples;
    private LayoutInflater layoutInflater;
    private OnClickListener onClickListener;
    private ArrayList<Apple> originalListOfApples;
    private ArrayList<String> characteristics;

    public AppleAdapter(Context context, OnClickListener onClickListener) {
        listOfApples = new ArrayList<>();
        originalListOfApples = new ArrayList<>();
        characteristics = new ArrayList<>();
        layoutInflater = LayoutInflater.from(context);
        this.onClickListener = onClickListener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(layoutInflater.inflate(R.layout.apple_item, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull AppleAdapter.ViewHolder holder, int position) {
        Apple apple = listOfApples.get(position);
        holder.getTextView().setText(apple.getName());
        ImageView imageView = holder.getImage();
        new ImageDownloader(imageView).execute(apple.getAuxdata().getImg());
    }

    @Override
    public int getItemCount() {
        return listOfApples.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private TextView textView;
        private ImageView image;

        public ViewHolder(View view) {
            super(view);
            view.setOnClickListener(this);
            this.textView = view.findViewById(R.id.text_apple_name);
            this.image = view.findViewById(R.id.image_apple);
        }

        public TextView getTextView() {
            return textView;
        }

        @Override
        public void onClick(View view) {
            onClickListener.onClick(listOfApples.get(getAdapterPosition()));
        }

        public ImageView getImage() {
            return image;
        }
    }

    public ArrayList<Apple> getApples () {
        return listOfApples;
    }

    public void addApples(ArrayList<Apple> apples) {
        try {
            listOfApples.addAll(apples);
            originalListOfApples.addAll(apples);
            HashSet<String> set = new HashSet<>();
            for (Apple apple : originalListOfApples) {
                set.addAll(apple.getAuxdata().getCharacteristics());
            }
            characteristics.addAll(set);

            notifyDataSetChanged();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public ArrayList<String> getCharacteristics() {
        return characteristics;
    }

    public void filterApples(final String type, final String filter) {
        ArrayList<Apple> temp = new ArrayList<>();

        switch (type) {
            case "color":
                System.out.println(1);
                for (Apple apple : originalListOfApples) {
                    if (apple.getAuxdata().getColors().contains(filter)) {
                        temp.add(apple);
                    }
                }
                listOfApples.clear();
                listOfApples.addAll(temp);
                break;
            case "characteristic":
                System.out.println(2);
                for (Apple apple : originalListOfApples) {
                    if (apple.getAuxdata().getCharacteristics().contains(filter)) {
                        temp.add(apple);
                    }
                }
                listOfApples.clear();
                listOfApples.addAll(temp);
                break;
            default:
                System.out.println(3333);
                listOfApples.clear();
                listOfApples.addAll(originalListOfApples);
                break;
        }

        notifyDataSetChanged();
    }

    public interface OnClickListener {
        void onClick(Apple apple);
    }
}
