package com.example.flashback.RecyclerViewAdapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.flashback.DataSources.FlashcardsDataSource;
import com.example.flashback.DatabaseTables.FlashcardEntity;
import com.example.flashback.DeckEntity;
import com.example.flashback.EditCard.EditFlashCard;
import com.example.flashback.R;

import java.util.List;

import static com.example.flashback.MainActivity.EXTRA_FLASHCARD_ID;
import static com.example.flashback.MainActivity.POSITION_IN_MEMORY;

public class DeckRecyclerViewAdapter extends RecyclerView.Adapter<DeckRecyclerViewAdapter.MyViewHolder> {

    public List<DeckEntity> mData;
    private final Context context;

    public DeckRecyclerViewAdapter (List<DeckEntity> mData, Context context) {
        this.mData = mData;
        this.context = context;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View listItem = LayoutInflater.from(parent.getContext()).inflate(R.layout.deck_item, parent, false);
        return new MyViewHolder(listItem);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        DeckEntity deck = mData.get(position);
        holder.deckName.setText(deck.getDeckName());
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {
        private final TextView deckName;
        public MyViewHolder(View itemView) {
            super(itemView);
            deckName = itemView.findViewById(R.id.deck_item_name);
        }
    }
}
