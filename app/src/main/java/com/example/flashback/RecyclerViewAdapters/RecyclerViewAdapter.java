package com.example.flashback.RecyclerViewAdapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.flashback.DataSources.DeckDataSource;
import com.example.flashback.DataSources.FlashcardsDataSource;
import com.example.flashback.DatabaseTables.DeckEntity;
import com.example.flashback.DatabaseTables.FlashcardEntity;
import com.example.flashback.EditCard.EditFlashCard;
import com.example.flashback.R;
import com.wajahatkarim3.easyflipview.EasyFlipView;

import java.util.List;

import static com.example.flashback.MainActivity.EXTRA_FLASHCARD_ID;
import static com.example.flashback.MainActivity.POSITION_IN_MEMORY;

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.MyViewHolder> {

    public List<FlashcardEntity> mData;
    private final Context context;

    public RecyclerViewAdapter (List<FlashcardEntity> mData, Context context) {
        this.mData = mData;
        this.context = context;
    }

    @Override
    @NonNull
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View listItem = LayoutInflater.from(parent.getContext()).inflate(R.layout.cardviewinlist, parent, false);
        return new MyViewHolder(listItem);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        FlashcardEntity flashcard = mData.get(position);
        FlashcardsDataSource ds = new FlashcardsDataSource(this.context);
        DeckDataSource deckDS = new DeckDataSource(this.context);
        holder.back.setText(flashcard.getBackText());
        holder.front.setText(flashcard.getFrontText());
        //
        holder.delete.setOnClickListener(v -> {
            // TODO doesn't work in decks null object reference
            FlashcardEntity cardToDelete = mData.remove(position);
            if(cardToDelete.getInDeck()){
                DeckEntity deck = deckDS.getSingleDeckByID(cardToDelete.getAssociatedDeck());
                deck.removeCardFromDeck(cardToDelete.getId());
                deckDS.updateDeckInDB(deck);
            }
            ds.deleteFlashcardInDB(cardToDelete);
            this.notifyDataSetChanged();
        });
        //
        holder.edit.setOnClickListener(v -> {
            Intent intent = new Intent(this.context, EditFlashCard.class);
            intent.putExtra(EXTRA_FLASHCARD_ID, flashcard.getId());
            intent.putExtra(POSITION_IN_MEMORY,position);
            this.context.startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        private final TextView front;
        private final TextView back;
        private final ImageButton edit;
        private ImageButton delete;
        private EasyFlipView theCardItself;

        public MyViewHolder(View itemView) {
            super(itemView);
            back = itemView.findViewById(R.id.card_back);
            front = itemView.findViewById(R.id.card_front);
            edit = itemView.findViewById(R.id.editButtonSingleCard);
            delete = itemView.findViewById(R.id.deleteButtonSingleCard);
            theCardItself = itemView.findViewById(R.id.the_card_itself);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            theCardItself.flipTheView();
        }
    }
}
