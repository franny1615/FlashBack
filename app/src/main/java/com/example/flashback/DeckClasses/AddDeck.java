package com.example.flashback.DeckClasses;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;

import com.example.flashback.DataSources.FlashcardsDataSource;
import com.example.flashback.DatabaseTables.FlashcardEntity;
import com.example.flashback.R;
import com.example.flashback.RecyclerViewAdapters.SelectCardsRecyclerViewAdapter;

import java.util.List;

public class AddDeck extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_deck);

        FlashcardsDataSource ds = new FlashcardsDataSource(this);
        List<FlashcardEntity> cards = ds.loadAllFlashcardsFromDB();
        SelectCardsRecyclerViewAdapter adapter = new SelectCardsRecyclerViewAdapter(cards,this);
        RecyclerView res = findViewById(R.id.deck_createscreen_recyclerview);
        res.setHasFixedSize(true);
        res.setAdapter(adapter);

    }

    public void cancelDeckAdd(View view) {
        finish();
    }
}