package com.example.design;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import android.graphics.Color;

public class HistoryAdapter extends RecyclerView.Adapter<HistoryAdapter.HistoryViewHolder> {
    private Context context;
    private ArrayList<String> history;
    private final RecyclerViewInterface recyclerViewInterface;
    private int selectedItem = RecyclerView.NO_POSITION;

    public HistoryAdapter(Context context, ArrayList<String> history, RecyclerViewInterface recyclerViewInterface) {
        this.context = context;
        this.history = history;
        this.recyclerViewInterface = recyclerViewInterface;
    }

    @NonNull
    @Override
    public HistoryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.history_entry, parent, false);
        return new HistoryViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull HistoryViewHolder holder, int position) {
        holder.bind(history.get(position), position); // Pass position to bind method
    }

    @Override
    public int getItemCount() {
        return history.size();
    }

    public class HistoryViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView historyTextView;
        String clickedWord; // Variable to store the clicked word
        CardView cardView;
         LinearLayout llayout,llayout2;

        public HistoryViewHolder(@NonNull View itemView) {
            super(itemView);
            historyTextView = itemView.findViewById(R.id.word);
            cardView = itemView.findViewById(R.id.cardView);
            llayout = itemView.findViewById(R.id.llayout); // assuming linear layout's id is llayout
            llayout2= itemView.findViewById(R.id.llayout2);
            itemView.setOnClickListener(this); // Set click listener to item view
            animation(itemView);
        }

        public void bind(String historyText, int position) {
            historyTextView.setText(historyText);
            clickedWord = historyText; // Store the clicked word

            // Update background color based on selected item
            if (selectedItem == position) {
                cardView.setCardBackgroundColor(Color.RED);
                llayout.setBackgroundColor(Color.RED);
                llayout2.setBackgroundColor(Color.RED);
            } else {
                cardView.setCardBackgroundColor(Color.WHITE);
                llayout.setBackgroundColor(Color.WHITE);
                llayout2.setBackgroundColor(Color.WHITE);
            }
        }

        @Override
        public void onClick(View v) {
            // Handle the click event here, pass the position to the interface
            int position = getAdapterPosition();
            if (position != RecyclerView.NO_POSITION) {
                recyclerViewInterface.onItemClick(position);

                // Update selected item and notify adapter of the change
                selectedItem = position;
                notifyDataSetChanged();
            }
        }
    }

    public void filterList(ArrayList<String> filteredList) {
        // Update the dataset with the filtered list
        history.clear(); // Clear the existing data
        history.addAll(filteredList); // Add all elements from the filtered list
        // Notify adapter about data change
        notifyDataSetChanged();
    }

    public void animation(View view) {
        Animation animation = AnimationUtils.loadAnimation(context, android.R.anim.slide_in_left);
        view.setAnimation(animation);
    }

    // Method to get the text of the selected item
    public String getSelectedText() {
        if (selectedItem != RecyclerView.NO_POSITION) {
            return history.get(selectedItem);
        }
        return null;
    }
}

