package com.example.project;

import android.content.Context;
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

public class AppleAdapter extends RecyclerView.Adapter<AppleAdapter.ViewHolder>{

    private ArrayList<Apple> listOfApples;
    private LayoutInflater layoutInflater;
    private OnClickListener onClickListener;

    public AppleAdapter(Context context, OnClickListener onClickListener) {
        listOfApples = new ArrayList<>();
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
        holder.getTextView().setText(listOfApples.get(position).getName());
        ImageView imageView = holder.getImage();
        new ImageDownloader(imageView).execute(listOfApples.get(position).getAuxdata().getImg());
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
            textView = view.findViewById(R.id.text_apple_name);
            image = view.findViewById(R.id.image_apple);
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
            notifyDataSetChanged();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void filterApples(final String filter) {
        Collections.sort(listOfApples, new Comparator<Apple>() {

            @Override
            public int compare(Apple apple1, Apple apple2) {
                switch (filter) {
                    case "A_Z":
                        return apple1.getName().toLowerCase().compareTo(apple2.getName().toLowerCase());
                    case "Z_A":
                        return apple2.getName().toLowerCase().compareTo(apple1.getName().toLowerCase());
                }

                return 0;
            }
        });

        notifyDataSetChanged();
    }

    public interface OnClickListener {
        void onClick(Apple apple);
    }
}
