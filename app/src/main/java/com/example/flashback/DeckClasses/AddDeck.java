package com.example.flashback.DeckClasses;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import com.example.flashback.DataSources.FlashcardsDataSource;
import com.example.flashback.DatabaseTables.FlashcardEntity;
import com.example.flashback.R;
import com.example.flashback.RecyclerViewAdapters.SelectCardsRecyclerViewAdapter;

import java.util.ArrayList;
import java.util.List;

public class AddDeck extends AppCompatActivity implements SelectCardsRecyclerViewAdapter.SelectCardClickListener {

    private List<Long> selectedIds;
    private RecyclerView selectRV;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_deck);
        selectedIds = new ArrayList<>();

        FlashcardsDataSource ds = new FlashcardsDataSource(this);
        List<FlashcardEntity> cards = ds.loadAllFlashcardsFromDB();
        // TODO remove cards that are in a deck already by checking their inDeck status
        SelectCardsRecyclerViewAdapter adapter = new SelectCardsRecyclerViewAdapter(cards, this);
        selectRV = findViewById(R.id.deck_createscreen_recyclerview);
        selectRV.setHasFixedSize(true);
        selectRV.setAdapter(adapter);
    }

    public void cancelDeckAdd(View view) {
        finish();
    }

    public void saveDeckAdd(View view) {
        Intent intent = new Intent();
        EditText deckName = findViewById(R.id.deck_createscreen_deckname_edittext);
        // TODO: use travis insert method to add new deck in
        // TODO: if selectedIds is empty show a confirmation dialog for empty deck.
        setResult(RESULT_OK, intent);
        finish();
    }

    @Override
    public void onSelectedCardClick(long id){
        selectedIds.add(id);
    }

    @Override
    public void onDeselectCardClick(long id){
        for(int i = 0; i < selectedIds.size(); i++){
            if(id == selectedIds.get(i)){
                selectedIds.remove(i);
                break;
            }
        }
    }
}