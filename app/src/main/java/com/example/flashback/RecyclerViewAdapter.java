package com.example.flashback;

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
import com.example.flashback.EditCard.EditFlashCard;

import java.util.List;

import static com.example.flashback.MainActivity.EXTRA_FLASHCARD_ID;

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.MyViewHolder> {

    public List<FlashcardEntity> mData;
    private Context context;

    public RecyclerViewAdapter (List<FlashcardEntity> mData, Context context) {
        this.mData = mData;
        this.context = context;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View listItem = LayoutInflater.from(parent.getContext()).inflate(R.layout.cardviewinlist, parent, false);
        return new MyViewHolder(listItem);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        FlashcardEntity flashcard = mData.get(position);
        FlashcardsDataSource ds = new FlashcardsDataSource(this.context);
        holder.back.setText(flashcard.getBackText());
        holder.front.setText(flashcard.getFrontText());
        //
        holder.delete.setOnClickListener(v -> {
            FlashcardEntity cardToDelete = mData.remove(position);
            ds.deleteFlashcardInDB(cardToDelete);
            this.notifyDataSetChanged();
        });
        //
        holder.edit.setOnClickListener(v -> {
            Intent intent = new Intent(this.context, EditFlashCard.class);
            intent.putExtra(EXTRA_FLASHCARD_ID, flashcard.getId());
            intent.putExtra("POSITION_IN_MEMORY",position);
            this.context.startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {
        private TextView front;
        private TextView back;
        private ImageButton edit;
        private ImageButton delete;
        public MyViewHolder(View itemView) {
            super(itemView);
            back = itemView.findViewById(R.id.card_back);
            front = itemView.findViewById(R.id.card_front);
            edit = itemView.findViewById(R.id.editButtonSingleCard);
            delete = itemView.findViewById(R.id.deleteButtonSingleCard);
        }
    }
}
