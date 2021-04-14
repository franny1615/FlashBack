package com.example.flashback.RecyclerViewAdapters;

import android.content.Context;
import android.content.Intent;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.RadioButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.flashback.DataSources.FlashcardsDataSource;
import com.example.flashback.DatabaseTables.FlashcardEntity;
import com.example.flashback.DeckEntity;
import com.example.flashback.EditCard.EditFlashCard;
import com.example.flashback.R;

import java.util.List;

import static com.example.flashback.MainActivity.EXTRA_FLASHCARD_ID;
import static com.example.flashback.MainActivity.POSITION_IN_MEMORY;

public class SelectCardsRecyclerViewAdapter extends RecyclerView.Adapter<SelectCardsRecyclerViewAdapter.MyViewHolder> {

    public List<FlashcardEntity> mData;
    private final Context context;

    public SelectCardsRecyclerViewAdapter (List<FlashcardEntity> mData, Context context) {
        this.mData = mData;
        this.context = context;
    }

    @Override
    @NonNull
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View listItem = LayoutInflater.from(parent.getContext()).inflate(R.layout.select_card_item, parent, false);
        return new MyViewHolder(listItem);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        FlashcardEntity deck = mData.get(position);
        holder.front.setText(deck.getFrontText());
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {
        private final TextView front;
        private final RadioButton selected;
        public MyViewHolder(View itemView) {
            super(itemView);
            front = itemView.findViewById(R.id.select_item_front_text);
            selected = itemView.findViewById(R.id.select_item_radio_button);
        }
    }
}
