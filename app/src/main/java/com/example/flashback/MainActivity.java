package com.example.flashback;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.example.flashback.AddFlashCard.AddNewFlashcard;
import com.example.flashback.DataSources.DeckDataSource;
import com.example.flashback.DataSources.FlashcardsDataSource;
import com.example.flashback.DatabaseTables.DeckEntity;
import com.example.flashback.DatabaseTables.FlashcardEntity;
import com.example.flashback.DeckClasses.AddDeck;
import com.example.flashback.DeckClasses.DeckScreen;
import com.example.flashback.RecyclerViewAdapters.DeckRecyclerViewAdapter;
import com.example.flashback.RecyclerViewAdapters.RecyclerViewAdapter;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements DeckRecyclerViewAdapter.DeckClickListener {
    private FlashcardsDataSource flashcardDS;
    private DeckDataSource decksDS;
    private RecyclerViewAdapter adapter;
    private DeckRecyclerViewAdapter deckAdapter;

    public static String EXTRA_FLASHCARD_ID = "com.example.flashback.MainActivity.FlashcardID";
    public static String ID_OF_EDITED_CARD = "com.example.flashback.MainActivity.IdOfEditedCard";
    public static String ID_OF_DECK = "com.example.flashback.MainActivity.IdOfDeck";
    public static String ID_NEW_CARD = "com.example.flashback.MainActivity.IdNewCard";
    public static String LAST_KNOWN_ID = "com.example.flashback.MainActivity.LastKnownId";
    public static String POSITION_IN_MEMORY = "com.example.flashback.MainActivity.PositionInMemory";
    public static String EDITED_FLASHCARD_DETAILS = "editedFlashcardDetails";
    public static String DECK_DELETED_FLAG = "deckDeletedFlagFile";
    public static String ID_OF_DELETED_DECK = "com.example.flashback.MainActivity.IdOfDeletedDeck";
    public static String CURRENT_RUNNING_DECK_ID = "com.example.flashback.MainActivity.CurrentRunningDeckId";

    public static long DEFAULT_ID = -1L;
    public static int DEFAULT_POSITION = -1;
    private final static int ADD_REQUEST_CODE = 69;
    private final static int DECK_REQUEST_CODE = 420;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //
        clearIdAndPositionInSharedPreferences();
        clearIdOfDeckInSharedPreferences();
        flashcardDS = new FlashcardsDataSource(this);
        decksDS = new DeckDataSource(this);
        //
//        decksDS.deleteAllDecks();
//        flashcardDS.deleteAllFlashcards();
//        for(int i = 0; i < 20; i++){
//            FlashcardEntity c = new FlashcardEntity();
//            c.setFrontText("Card"+i);
//            c.setBackText("desc"+i);
//            flashcardDS.insertFlashcardIntoDB(c);
//        }
        //
        setUpAllFlashcardsAdapter();
        setUpAllDecksAdapter();
    }

    private void clearIdAndPositionInSharedPreferences() {
        SharedPreferences shared = this.getSharedPreferences(EDITED_FLASHCARD_DETAILS,Context.MODE_PRIVATE);
        SharedPreferences.Editor shareEdit = shared.edit();
        shareEdit.putLong(ID_OF_EDITED_CARD,DEFAULT_ID);
        shareEdit.putInt(POSITION_IN_MEMORY,DEFAULT_POSITION);
        shareEdit.apply();
    }

    private void clearIdOfDeckInSharedPreferences(){
        SharedPreferences shared = this.getSharedPreferences(DECK_DELETED_FLAG,Context.MODE_PRIVATE);
        SharedPreferences.Editor shareEdit = shared.edit();
        shareEdit.putLong(ID_OF_DELETED_DECK,DEFAULT_ID);
        shareEdit.apply();
    }

    private void setUpAllFlashcardsAdapter() {
        List<FlashcardEntity> allcards = flashcardDS.loadAllFlashcardsFromDB();
        adapter = new RecyclerViewAdapter(createNotInDeckList(allcards),this);
        RecyclerView flashcardRecyclerView = findViewById(R.id.recyclerview);
        flashcardRecyclerView.setHasFixedSize(true);
        flashcardRecyclerView.setAdapter(adapter);
        adapter.notifyDataSetChanged();
    }

    private List<FlashcardEntity> createNotInDeckList(List<FlashcardEntity> allcards){
        List<FlashcardEntity> toDisplay = new ArrayList<>();
        for(int i = 0; i < allcards.size(); i++){
            if(!allcards.get(i).getInDeck()) {
                toDisplay.add(allcards.get(i));
            }
        }
        return toDisplay;
    }

    private void setUpAllDecksAdapter(){
        RecyclerView decksRecyclerView = findViewById(R.id.deck_recycler_view);
        List<DeckEntity> decks = decksDS.loadAllDecksFromDB();
        // adapter
        deckAdapter = new DeckRecyclerViewAdapter(decks,this);
        decksRecyclerView.setHasFixedSize(true);
        decksRecyclerView.setAdapter(deckAdapter);
    }

    @Override
    public void onResume(){
        super.onResume();
        updateAndRefreshAllCardsAdapter();
        updateIfDeckDeleted();
    }

    private void updateAndRefreshAllCardsAdapter() {
        SharedPreferences editedCardStuff = this.getSharedPreferences(EDITED_FLASHCARD_DETAILS,Context.MODE_PRIVATE);
        int position = editedCardStuff.getInt(POSITION_IN_MEMORY,DEFAULT_POSITION);
        long idOfChangedCard = editedCardStuff.getLong(ID_OF_EDITED_CARD,DEFAULT_ID);

        if((position != DEFAULT_POSITION) && (idOfChangedCard != DEFAULT_ID)){
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

    private void updateIfDeckDeleted(){
        SharedPreferences editedCardStuff = this.getSharedPreferences(DECK_DELETED_FLAG,Context.MODE_PRIVATE);
        long idOfPotentialDeath = editedCardStuff.getLong(ID_OF_DELETED_DECK,DEFAULT_ID);

        if(idOfPotentialDeath != DEFAULT_ID) {
            updateDeckAdapterWhenOneDeleted(idOfPotentialDeath);
            updateCardsWhenDeckDeleted();
            clearIdOfDeckInSharedPreferences();
        }
    }

    private void updateDeckAdapterWhenOneDeleted(long idOfPotentialDeath) {
        List<DeckEntity> list = deckAdapter.mData;
        int indexOfRemoval = -1;
        for(int i = 0; i < list.size(); i++) {
            if(list.get(i).getId() == idOfPotentialDeath){
                indexOfRemoval = i;
                break;
            }
        }
        if(indexOfRemoval >= 0){
            deckAdapter.mData.remove(indexOfRemoval);
            deckAdapter.notifyDataSetChanged();
        }
    }

    private void updateCardsWhenDeckDeleted(){
        List<FlashcardEntity> cards = flashcardDS.loadAllFlashcardsFromDB();
        List<FlashcardEntity> current = adapter.mData;
        // filter out already existing ones in adapter
        for(int i = 0; i < current.size(); i++) {
            for(int j = 0; j < cards.size(); j++){
                if(cards.get(j).getId() == current.get(i).getId()){
                    cards.remove(j);
                    break;
                }
            }
        }
        // filter out ones that are in decks
        cards = createNotInDeckList(cards);
        // insert back into adapter
        adapter.mData.addAll(cards);
        adapter.notifyDataSetChanged();
    }

    public void addAFlashcard(View view){
        Intent intent = new Intent(this, AddNewFlashcard.class);
        List<FlashcardEntity> cards = flashcardDS.loadAllFlashcardsFromDB();
        int size = cards.size();
        long lastKnownId = 0L;
        if(size > 0) {
            lastKnownId = cards.get(size-1).getId();
        }
        intent.putExtra(LAST_KNOWN_ID,lastKnownId);
        startActivityForResult(intent, ADD_REQUEST_CODE);
    }

    public void addDeck(View view){
        Intent intent = new Intent(this, AddDeck.class);
        long id = 0L;
        if(!deckAdapter.mData.isEmpty()) {
            id = deckAdapter.mData.get(deckAdapter.mData.size()-1).getId();
        }
        intent.putExtra(CURRENT_RUNNING_DECK_ID, id);
        startActivityForResult(intent, DECK_REQUEST_CODE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK && (data != null)) {
            if (requestCode == ADD_REQUEST_CODE) {
                cameBackFromAddingACard(data);
            }
            if(requestCode == DECK_REQUEST_CODE) {
                cameBackFromAddingADeck(data);
            }
        }
    }

    private void cameBackFromAddingACard(Intent data){
        long id = data.getLongExtra(ID_NEW_CARD,DEFAULT_ID);
        if(id != DEFAULT_ID){
            FlashcardEntity newestCard = flashcardDS.getSingleFlashcardById(id);
            adapter.mData.add(newestCard);
            adapter.notifyDataSetChanged();
        }
    }

    private void cameBackFromAddingADeck(Intent data){
        long id = data.getLongExtra(ID_OF_DECK, DEFAULT_ID);
        if(id != DEFAULT_ID){
            DeckEntity newestDeck = decksDS.getSingleDeckByID(id);
            deckAdapter.mData.add(newestDeck);
            deckAdapter.notifyDataSetChanged();
            //
            updateAdapterList(newestDeck.getCardIDs());
            adapter.notifyDataSetChanged();
        }
    }

    private void updateAdapterList(List<Long> ids){
        for(int i = 0; i < ids.size(); i++){
            for(int j = 0; j < adapter.mData.size(); j++) {
                if(ids.get(i) == adapter.mData.get(j).getId()){
                    adapter.mData.remove(j);
                    break;
                }
            }
        }
    }

    @Override
    public void sendToDeckScreen(long id) {
        Intent intent = new Intent(this, DeckScreen.class);
        intent.putExtra(ID_OF_DECK,id);
        startActivity(intent);
    }
}
