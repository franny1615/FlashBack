package com.example.flashback;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;

import com.example.flashback.AddFlashCard.AddNewFlashcard;
import com.example.flashback.DataSources.FlashcardsDataSource;
import com.example.flashback.DatabaseTables.FlashcardEntity;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private FlashcardsDataSource flashcardDS;
    private RecyclerViewAdapter adapter;

    public static String EXTRA_FLASHCARD_ID = "com.example.flashback.MainActivity.FlashcardID";
    private final static int ADD_REQUEST_CODE = 69;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //
        flashcardDS = new FlashcardsDataSource(this);
        List<FlashcardEntity> allcards = flashcardDS.loadAllFlashcardsFromDB();
        //
        adapter = new RecyclerViewAdapter(allcards,this);
        RecyclerView flashcardRecyclerView = findViewById(R.id.recyclerview);
        flashcardRecyclerView.setHasFixedSize(true);
        flashcardRecyclerView.setAdapter(adapter);
        //
        LinearLayoutManager manager = new LinearLayoutManager(this);
        manager.setOrientation(LinearLayoutManager.VERTICAL);
        flashcardRecyclerView.setLayoutManager(manager);
        adapter.notifyDataSetChanged();
    }

    @Override
    public void onResume(){
        super.onResume();
        SharedPreferences editedCardStuff = this.getSharedPreferences("editedFlashcardDetails",Context.MODE_PRIVATE);
        int position = editedCardStuff.getInt("POSITION_IN_MEMORY",-1);
        long idOfChangedCard = editedCardStuff.getLong("ID_OF_EDITED_CARD",-1L);
        if( (position != -1) && (idOfChangedCard != -1L) ){
            FlashcardEntity card = adapter.mData.get(position);
            FlashcardEntity cardFromDB = flashcardDS.getSingleFlashcardById(idOfChangedCard);
            card.setFrontText(cardFromDB.getFrontText());
            card.setBackText(cardFromDB.getBackText());
            adapter.mData.set(position,card);
            adapter.notifyDataSetChanged();
        }
    }

    public void addAFlashcard(View view){
        Intent intent = new Intent(this, AddNewFlashcard.class);
        int size = adapter.mData.size();
        intent.putExtra("LAST_KNOWN_ID",adapter.mData.get(size-1).getId());
        startActivityForResult(intent, ADD_REQUEST_CODE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == ADD_REQUEST_CODE) {
                if (data != null){
                    FlashcardEntity newestCard = flashcardDS.getSingleFlashcardById(data.getLongExtra("ID_NEW_CARD",0L));
                    adapter.mData.add(newestCard);
                    adapter.notifyDataSetChanged();
                }
            }
        }
    }
}
