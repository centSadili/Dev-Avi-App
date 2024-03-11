package com.example.design;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class MyAdapter extends RecyclerView.Adapter<MyAdapter.MyViewHolder>  {
    private Context context;
    private ArrayList<String> word;
    private ArrayList<String> category; // Added category ArrayList<String>
    private final RecyclerViewInterface recyclerViewInterface;

    public MyAdapter(Context context, ArrayList<String> word, ArrayList<String> category, RecyclerViewInterface recyclerViewInterface) {
        this.context = context;
        this.word = word;
        this.category = category;
        this.recyclerViewInterface = recyclerViewInterface;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.wordentry, parent, false);
        return new MyViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        holder.bind(word.get(position), category.get(position)); // Pass category to bind method
    }

    @Override
    public int getItemCount() {
        return word.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView wordTextView;
        TextView categoryTextView; // Added categoryTextView
        String clickedWord; // Variable to store the clicked word

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            wordTextView = itemView.findViewById(R.id.word);
            categoryTextView = itemView.findViewById(R.id.category); // Initialize categoryTextView
            itemView.setOnClickListener(this); // Set click listener to item view
            animation(itemView);
        }

        public void bind(String wordText, String categoryText) {
            wordTextView.setText(wordText);
            categoryTextView.setText(categoryText); // Set category text
            clickedWord = wordText; // Store the clicked word
        }

        @Override
        public void onClick(View v) {
            // Handle the click event here, pass the position to the interface
            int position = getAdapterPosition();
            if (position != RecyclerView.NO_POSITION) {
                recyclerViewInterface.onItemClick(position);
            }
        }
    }

    public void filterList(ArrayList<String> filteredList) {
        // Update the dataset with the filtered list
        word.clear(); // Clear the existing data
        if (filteredList != null && !filteredList.isEmpty()) {
            word.clear();
            word.addAll(filteredList); // Add all elements from the filtered list
        }else{
            word.addAll(filteredList);
        }
        // Notify adapter about data change
        notifyDataSetChanged();
    }
    public void animation(View view) {

        Animation animation = AnimationUtils.loadAnimation(context, android.R.anim.slide_in_left);
        view.setAnimation(animation);

    }
}
