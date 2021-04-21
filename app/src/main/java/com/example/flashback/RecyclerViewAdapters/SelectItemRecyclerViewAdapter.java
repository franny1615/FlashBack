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

public class SelectItemRecyclerViewAdapter<T> extends RecyclerView.Adapter<SelectItemRecyclerViewAdapter.MyViewHolder> {

    final private SelectItemRecyclerViewAdapterListener selectListener;
    public List<T> mData;
    private int lastPositionChecked = -1;

    public SelectItemRecyclerViewAdapter (List<T> mData, SelectItemRecyclerViewAdapterListener selectListener) {
        this.mData = mData;
        this.selectListener = selectListener;
    }

    @Override
    @NonNull
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View listItem = LayoutInflater.from(parent.getContext()).inflate(R.layout.select_item, parent, false);
        return new MyViewHolder(listItem);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        final Object item = mData.get(position);
        if(item instanceof DeckEntity){
            bindForDeckSelection((DeckEntity) item, holder);
        }
        if(item instanceof FlashcardEntity){
            bindForCardSelection((FlashcardEntity) item, holder, position);
        }
    }

    private void bindForDeckSelection(DeckEntity item, MyViewHolder holder) {
        holder.front.setText(item.getDeckName());
        holder.front.setBackgroundColor(lastPositionChecked == holder.getAdapterPosition() ? Color.DKGRAY : Color.WHITE);
        holder.front.setOnClickListener(v -> {
            if(holder.getAdapterPosition() == lastPositionChecked) {
                notifyItemChanged(lastPositionChecked);
                lastPositionChecked = -1;
                selectListener.onDeselectItem(item);
            } else {
                notifyItemChanged(lastPositionChecked);
                lastPositionChecked = holder.getAdapterPosition();
                selectListener.onSelectItem(item);
            }
            holder.front.setBackgroundColor(lastPositionChecked == holder.getAdapterPosition() ? Color.DKGRAY : Color.WHITE);
        });
    }

    private void bindForCardSelection(FlashcardEntity item, MyViewHolder holder, int position) {
        holder.front.setText(item.getFrontText());
        holder.front.setBackgroundColor(item.isSelected() ? Color.DKGRAY : Color.WHITE);
        holder.front.setOnClickListener(v -> {
            if(!item.isSelected()){
                selectListener.onSelectItem(item);
            } else {
                selectListener.onDeselectItem(item);
            }
            item.setSelected(!item.isSelected());
            holder.front.setBackgroundColor(item.isSelected() ? Color.DKGRAY : Color.WHITE);
        });
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {
        private final TextView front;
        public MyViewHolder(View itemView) {
            super(itemView);
            front = itemView.findViewById(R.id.select_item_textview);
        }
    }

    public interface SelectItemRecyclerViewAdapterListener {
        void onSelectItem(Object item);
        void onDeselectItem(Object item);
    }
}
