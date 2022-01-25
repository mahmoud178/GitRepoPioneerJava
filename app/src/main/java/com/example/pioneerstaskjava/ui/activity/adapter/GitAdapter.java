package com.example.pioneerstaskjava.ui.activity.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.pioneerstaskjava.R;
import com.example.pioneerstaskjava.data.model.Item;

import java.util.List;

public class GitAdapter extends RecyclerView.Adapter<GitAdapter.ViewHolder> {
    Context context;
    List<Item> list;

    public GitAdapter(Context context, List<Item> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);

        return new ViewHolder(inflater.inflate(R.layout.git_item, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Glide.with(context)
                .load(list.get(position).getOwner().getAvatarUrl())
                .into(holder.gitImage);
        holder.gitName.setText(list.get(position).getName());
        holder.gitDescription.setText(list.get(position).getLanguage());
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView gitName, gitDescription;
        ImageView gitImage;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            gitName = itemView.findViewById(R.id.git_name);
            gitDescription = itemView.findViewById(R.id.git_description);
            gitImage = itemView.findViewById(R.id.git_row_image);
        }
    }
}
