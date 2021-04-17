package com.example.flashback.RecyclerViewAdapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.flashback.DatabaseTables.DeckEntity;
import com.example.flashback.R;

import java.util.List;


public class DeckRecyclerViewAdapter extends RecyclerView.Adapter<DeckRecyclerViewAdapter.MyViewHolder> {

    final private DeckClickListener deckClicked;
    public List<DeckEntity> mData;

    public DeckRecyclerViewAdapter (List<DeckEntity> mData, DeckClickListener deckClicked) {
        this.mData = mData;
        this.deckClicked = deckClicked;
    }

    @Override
    @NonNull
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

    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private final TextView deckName;
        public MyViewHolder(View itemView) {
            super(itemView);
            deckName = itemView.findViewById(R.id.deck_item_name);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            deckClicked.sendToDeckScreen(mData.get(getAdapterPosition()).getId());
        }
    }

    public interface DeckClickListener {
        void sendToDeckScreen(long id);
    }
}
