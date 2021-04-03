package com.example.flashback;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.flashback.DatabaseTables.FlashcardEntity;

import java.util.ArrayList;
import java.util.List;

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.MyViewHolder> {

    public List<FlashcardEntity> mData;

    public RecyclerViewAdapter (List<FlashcardEntity> mData) {
        this.mData = mData;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View listItem = LayoutInflater.from(parent.getContext()).inflate(R.layout.cardviewinlist, parent, false);
        return new MyViewHolder(listItem);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        FlashcardEntity flashcard = mData.get(position);
        holder.front.setText(flashcard.getBackText());
        holder.back.setText(flashcard.getFrontText());

    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {
        private TextView front;
        private TextView back;
        public MyViewHolder(View itemView) {
            super(itemView);
            back = itemView.findViewById(R.id.card_back);
            front = itemView.findViewById(R.id.card_front);
        }
    }
}
