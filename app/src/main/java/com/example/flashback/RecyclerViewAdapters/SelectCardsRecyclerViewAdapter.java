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

import com.example.flashback.DatabaseTables.FlashcardEntity;
import com.example.flashback.R;

import java.util.List;

public class SelectCardsRecyclerViewAdapter extends RecyclerView.Adapter<SelectCardsRecyclerViewAdapter.MyViewHolder> {

    final private SelectCardClickListener cardSelectListener;
    public List<FlashcardEntity> mData;

    public SelectCardsRecyclerViewAdapter (List<FlashcardEntity> mData, SelectCardClickListener cardSelectListener) {
        this.mData = mData;
        this.cardSelectListener = cardSelectListener;
    }

    @Override
    @NonNull
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View listItem = LayoutInflater.from(parent.getContext()).inflate(R.layout.select_card_item, parent, false);
        return new MyViewHolder(listItem);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        final FlashcardEntity card = mData.get(position);
        holder.front.setText(card.getFrontText());
        holder.mycard.setBackgroundColor(card.isSelected() ? Color.DKGRAY : Color.WHITE);
        holder.front.setOnClickListener(v -> {
            if(!card.isSelected()){
                cardSelectListener.onSelectedCardClick(card);
            } else {
                cardSelectListener.onDeselectCardClick(card);
            }
            card.setSelected(!card.isSelected());
            holder.mycard.setBackgroundColor(card.isSelected() ? Color.DKGRAY : Color.WHITE);
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

    public interface SelectCardClickListener {
        void onSelectedCardClick(FlashcardEntity card);
        void onDeselectCardClick(FlashcardEntity card);
    }
}
