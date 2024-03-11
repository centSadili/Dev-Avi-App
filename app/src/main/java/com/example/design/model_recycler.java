package com.example.design;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class model_recycler extends RecyclerView.Adapter<model_recycler.ViewHolder> {

    private final recycler_interface recyclerInterface;

    Context context;
    ArrayList<model> arrayList = new ArrayList<>();

    public model_recycler(Context context, ArrayList<model> arrayList, recycler_interface recyclerInterface) {
        this.context = context;
        this.arrayList = arrayList;
        this.recyclerInterface = recyclerInterface;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(context).inflate(R.layout.card_item, parent, false);

        return new ViewHolder(view, recyclerInterface);

    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        holder.imageview.setImageResource(arrayList.get(position).getImg());
        holder.name.setText(arrayList.get(position).getName());
        holder.desc.setText(arrayList.get(position).getDescription());

    }

    @Override
    public int getItemCount() {

        return  arrayList.size();

    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        ImageView imageview;
        TextView name, desc;

        public ViewHolder(@NonNull View itemView, recycler_interface recyclerInterface) {
            super(itemView);

            imageview = itemView.findViewById(R.id.images);
            name = itemView.findViewById(R.id.name);
            desc = itemView.findViewById(R.id.description);

            itemView.setOnClickListener(view -> {

                if (model_recycler.this.recyclerInterface != null) {

                    int pos = getAdapterPosition();

                    if (pos != RecyclerView.NO_POSITION) {
                        model_recycler.this.recyclerInterface.onItemClick(pos);
                    }
                }

            });

            animation(itemView);
        }
    }

    public void animation(View view) {

        Animation animation = AnimationUtils.loadAnimation(context, android.R.anim.slide_in_left);
        view.setAnimation(animation);

    }
}
