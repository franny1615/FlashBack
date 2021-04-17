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
        FlashcardEntity card = mData.get(position);
        holder.front.setText(card.getFrontText());
        holder.me = card;
        holder.selected = false;
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private final TextView front;
        private final CardView mycard;
        private FlashcardEntity me;
        private boolean selected;
        public MyViewHolder(View itemView) {
            super(itemView);
            front = itemView.findViewById(R.id.select_item_front_text);
            mycard = itemView.findViewById(R.id.selected_card_cardview);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            if(!selected) {
                changeBackgroundColor(Color.WHITE,Color.DKGRAY);
                cardSelectListener.onSelectedCardClick(me);
            } else {
                changeBackgroundColor(Color.DKGRAY,Color.WHITE);
                cardSelectListener.onDeselectCardClick(me);
            }
            selected = !selected;
        }

        public void changeBackgroundColor(int colorFrom, int colorTo){
            ValueAnimator colorAnimation = ValueAnimator.ofObject(new ArgbEvaluator(), colorFrom, colorTo);
            colorAnimation.setDuration(400); // milliseconds
            colorAnimation.addUpdateListener(animator -> mycard.setBackgroundColor((int) animator.getAnimatedValue()));
            colorAnimation.start();
        }
    }

    public interface SelectCardClickListener {
        void onSelectedCardClick(FlashcardEntity card);
        void onDeselectCardClick(FlashcardEntity card);
    }
}
