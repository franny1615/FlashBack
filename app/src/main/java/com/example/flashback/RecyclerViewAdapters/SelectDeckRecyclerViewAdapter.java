package com.example.flashback.RecyclerViewAdapters;
import android.animation.ArgbEvaluator;
import android.animation.ValueAnimator;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.flashback.DatabaseTables.DeckEntity;
import com.example.flashback.DatabaseTables.FlashcardEntity;
import com.example.flashback.R;

import java.util.List;

public class SelectDeckRecyclerViewAdapter extends RecyclerView.Adapter<SelectDeckRecyclerViewAdapter.MyViewHolder> {

    final private SelectDeckRecyclerViewAdapterListener deckSelectListener;
    public List<DeckEntity> mData;
    private int lastPositionChecked = -1;

    public SelectDeckRecyclerViewAdapter (List<DeckEntity> mData, SelectDeckRecyclerViewAdapterListener cardSelectListener) {
        this.mData = mData;
        this.deckSelectListener = cardSelectListener;
    }

    @Override
    @NonNull
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View listItem = LayoutInflater.from(parent.getContext()).inflate(R.layout.select_card_item, parent, false);
        return new MyViewHolder(listItem);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        final DeckEntity deck = mData.get(position);
        holder.front.setText(deck.getDeckName());
        holder.mycard.setBackgroundColor(lastPositionChecked == holder.getAdapterPosition() ? Color.DKGRAY : Color.WHITE);
        holder.front.setOnClickListener(v -> {
            if(holder.getAdapterPosition() == lastPositionChecked) {
                notifyItemChanged(lastPositionChecked);
                lastPositionChecked = -1;
                deckSelectListener.onDeselectDeckClick();
            } else {
                notifyItemChanged(lastPositionChecked);
                lastPositionChecked = holder.getAdapterPosition();
                deckSelectListener.onSelectedDeckClick(deck);
            }
            holder.mycard.setBackgroundColor(lastPositionChecked == holder.getAdapterPosition() ? Color.DKGRAY : Color.WHITE);
        });
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {
        private final TextView front;
        private final CardView mycard;
        public MyViewHolder(View itemView) {
            super(itemView);
            front = itemView.findViewById(R.id.select_item_front_text);
            mycard = itemView.findViewById(R.id.selected_card_cardview);
        }
    }

    public interface SelectDeckRecyclerViewAdapterListener {
        void onSelectedDeckClick(DeckEntity deck);
        void onDeselectDeckClick();
    }
}
