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
    public static String ID_OF_EDITED_CARD = "com.example.flashback.MainActivity.IdOfEditedCard";
    public static String ID_NEW_CARD = "com.example.flashback.MainActivity.IdNewCard";
    public static String LAST_KNOWN_ID = "com.example.flashback.MainActivity.LastKnownId";
    public static String POSITION_IN_MEMORY = "com.example.flashback.MainActivity.PositionInMemory";
    public static String EDITED_FLASHCARD_DETAILS = "editedFlashcardDetails";

    public static long DEFAULT_ID = -1L;
    public static int DEFAULT_POSITION = -1;
    private final static int ADD_REQUEST_CODE = 69;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //
        clearIdAndPositionInSharedPreferences();
        flashcardDS = new FlashcardsDataSource(this);
        setUpAllFlashcardsAdapter();
    }

    private void clearIdAndPositionInSharedPreferences() {
        SharedPreferences shared = this.getSharedPreferences(EDITED_FLASHCARD_DETAILS,Context.MODE_PRIVATE);
        SharedPreferences.Editor shareEdit = shared.edit();
        shareEdit.putLong(ID_OF_EDITED_CARD,DEFAULT_ID);
        shareEdit.putInt(POSITION_IN_MEMORY,DEFAULT_POSITION);
        shareEdit.apply();
    }

    private void setUpAllFlashcardsAdapter() {
        List<FlashcardEntity> allcards = flashcardDS.loadAllFlashcardsFromDB();
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
        updateAndRefreshAllCardsAdapter();
    }

    private void updateAndRefreshAllCardsAdapter() {
        SharedPreferences editedCardStuff = this.getSharedPreferences(EDITED_FLASHCARD_DETAILS,Context.MODE_PRIVATE);
        int position = editedCardStuff.getInt(POSITION_IN_MEMORY,DEFAULT_POSITION);
        long idOfChangedCard = editedCardStuff.getLong(ID_OF_EDITED_CARD,DEFAULT_ID);

        if((position != -1) && (idOfChangedCard != -1L)){
            FlashcardEntity card = adapter.mData.get(position);
            FlashcardEntity cardFromDB = flashcardDS.getSingleFlashcardById(idOfChangedCard);
            card.setFrontText(cardFromDB.getFrontText());
            card.setBackText(cardFromDB.getBackText());
            adapter.mData.set(position,card);
            adapter.notifyDataSetChanged();
            //
            clearIdAndPositionInSharedPreferences();
        }
    }

    public void addAFlashcard(View view){
        Intent intent = new Intent(this, AddNewFlashcard.class);
        int size = adapter.mData.size();
        long lastKnownId = 0L;
        if(size > 0) {
            lastKnownId = adapter.mData.get(size-1).getId();
        }
        intent.putExtra(LAST_KNOWN_ID,lastKnownId);
        startActivityForResult(intent, ADD_REQUEST_CODE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == ADD_REQUEST_CODE) {
                if (data != null){
                    long id = data.getLongExtra(ID_NEW_CARD,DEFAULT_ID);
                    if(id != DEFAULT_ID){
                        FlashcardEntity newestCard = flashcardDS.getSingleFlashcardById(id);
                        adapter.mData.add(newestCard);
                        adapter.notifyDataSetChanged();
                    }
                }
            }
        }
    }
}
