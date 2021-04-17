package com.example.flashback.DeckClasses;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.example.flashback.AddFlashCard.AddNewFlashcard;
import com.example.flashback.DataSources.DeckDataSource;
import com.example.flashback.DataSources.FlashcardsDataSource;
import com.example.flashback.DatabaseTables.DeckEntity;
import com.example.flashback.DatabaseTables.FlashcardEntity;
import com.example.flashback.DeckClasses.DeckDialogs.EditDeckNameDialog;
import com.example.flashback.R;
import com.example.flashback.RecyclerViewAdapters.RecyclerViewAdapter;

import java.util.ArrayList;
import java.util.List;

import static com.example.flashback.MainActivity.DEFAULT_ID;
import static com.example.flashback.MainActivity.ID_NEW_CARD;
import static com.example.flashback.MainActivity.ID_OF_DECK;
import static com.example.flashback.MainActivity.LAST_KNOWN_ID;

public class DeckScreen extends AppCompatActivity implements EditDeckNameDialog.EditDeckNameDialogListener {

    public static int DECK_WANTS_NEW_CARD = 6969;

    private List<FlashcardEntity> myCards;
    private FlashcardsDataSource flashDS;
    private DeckDataSource deckDS;
    private TextView myName;
    private RecyclerView myCardsRecyclerView;
    private RecyclerViewAdapter myCardsAdapter;
    private DeckEntity me;
    private List<FlashcardEntity> allcards;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_deck_screen);
        //
        flashDS = new FlashcardsDataSource(this);
        deckDS = new DeckDataSource(this);
        //
        me = deckDS.getSingleDeckByID(getIntent().getLongExtra(ID_OF_DECK,0L));
        myName = findViewById(R.id.deck_screen_deckname_textview);
        myName.setText(me.getDeckName());
        //
        myCardsRecyclerView = findViewById(R.id.deck_screen_recyclerview);
        getMyCardsOnly(me.getCardIDs());
        myCardsAdapter = new RecyclerViewAdapter(myCards,this);
        myCardsRecyclerView.setAdapter(myCardsAdapter);
    }

    private void getMyCardsOnly(List<Long> myIds){
        allcards = flashDS.loadAllFlashcardsFromDB();
        myCards = new ArrayList<>();
        for(int i = 0; i < myIds.size(); i++){
            for(FlashcardEntity card: allcards) {
                if(card.getId() == myIds.get(i)){
                    myCards.add(card);
                }
            }
        }
    }

    public void editDeckName(View view){
        EditDeckNameDialog dialog = new EditDeckNameDialog();
        dialog.show(getSupportFragmentManager(), "Confirm");
    }

    public void deleteDeck(View view){
        // TODO do the cards go along with the deck removal, or just update each card inDeck status
    }

    @Override
    public void onDoneEditing(String newName) {
        myName.setText(newName);
        me.setDeckName(newName);
        deckDS.updateDeckInDB(me);
    }

    public void onAddCardToDeck(View view){
        long lastKnownId = 0L;
        Intent intent = new Intent(this, AddNewFlashcard.class);
        allcards = flashDS.loadAllFlashcardsFromDB();
        int size = allcards.size();
        if(size > 0) {
            lastKnownId = allcards.get(size-1).getId();
        }
        intent.putExtra(LAST_KNOWN_ID,lastKnownId);
        startActivityForResult(intent, DECK_WANTS_NEW_CARD);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK && (data != null)) {
            if(requestCode == DECK_WANTS_NEW_CARD) {
                cameBackFromAddingACard(data);
            }
        }
    }

    public void cameBackFromAddingACard(Intent data) {
        long id = data.getLongExtra(ID_NEW_CARD, DEFAULT_ID);
        if (id != DEFAULT_ID) {
            FlashcardEntity newestCard = flashDS.getSingleFlashcardById(id);
            newestCard.setInDeck(true);
            newestCard.setAssociatedDeck(me.getId());
            //
            me.addCardToDeck(newestCard.getId());
            //
            myCardsAdapter.mData.add(newestCard);
            myCardsAdapter.notifyDataSetChanged();
            //
            flashDS.updateFlashcardInDB(newestCard);
            deckDS.updateDeckInDB(me);
        }
    }
}