package com.example.flashback.DeckClasses;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.example.flashback.DataSources.DeckDataSource;
import com.example.flashback.DataSources.FlashcardsDataSource;
import com.example.flashback.DatabaseTables.DeckEntity;
import com.example.flashback.DatabaseTables.FlashcardEntity;
import com.example.flashback.DeckClasses.DeckDialogs.DeckConfirmEmptyListDialog;
import com.example.flashback.R;
import com.example.flashback.RecyclerViewAdapters.SelectCardsRecyclerViewAdapter;

import java.util.ArrayList;
import java.util.List;

import static com.example.flashback.MainActivity.CURRENT_RUNNING_DECK_ID;
import static com.example.flashback.MainActivity.DEFAULT_ID;
import static com.example.flashback.MainActivity.ID_OF_DECK;

public class AddDeck extends AppCompatActivity implements
        SelectCardsRecyclerViewAdapter.SelectCardClickListener,
        DeckConfirmEmptyListDialog.ConfirmEmptyDialogListener {

    private List<Long> selectedIds;
    private FlashcardsDataSource flashDS;
    private List<FlashcardEntity> allcards;
    private long newId;
    private EditText deckName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_deck);
        selectedIds = new ArrayList<>();
        //
        flashDS = new FlashcardsDataSource(this);
        SelectCardsRecyclerViewAdapter adapter = new SelectCardsRecyclerViewAdapter(createNotInDeckList(), this);
        RecyclerView selectRV = findViewById(R.id.deck_createscreen_recyclerview);
        selectRV.setHasFixedSize(true);
        selectRV.setAdapter(adapter);
        //
        newId = getIntent().getLongExtra(CURRENT_RUNNING_DECK_ID,0L);
        //
        deckName = findViewById(R.id.deck_createscreen_deckname_edittext);
    }

    private List<FlashcardEntity> createNotInDeckList(){
        allcards = flashDS.loadAllFlashcardsFromDB();
        List<FlashcardEntity> toDisplay = new ArrayList<>();
        for(int i = 0; i < allcards.size(); i++){
            if(!allcards.get(i).getInDeck()) {
                toDisplay.add(allcards.get(i));
            }
        }
        return toDisplay;
    }

    public void cancelDeckAdd(View view) {
        finish();
    }

    public void saveDeckAdd(View view) {
        if(deckName.getText().toString().equals("")){
            Toast.makeText(this,"Plase add a Name",Toast.LENGTH_LONG).show();
        } else {
            if(selectedIds.isEmpty()) {
                DeckConfirmEmptyListDialog dialog = new DeckConfirmEmptyListDialog();
                dialog.show(getSupportFragmentManager(), "Confirm");
            } else {
                finishAddingDeck();
            }
        }
    }

    @Override
    public void onDialogPositiveClick(DialogFragment fragment) {
        fragment.dismiss();
        finishAddingDeck();
    }

    @Override
    public void onDialogNegativeClick(DialogFragment fragment) {
        fragment.dismiss();
    }

    private void finishAddingDeck(){
        DeckEntity newDeck = new DeckEntity(deckName.getText().toString(),selectedIds);
        DeckDataSource deckDS = new DeckDataSource(this);
        newDeck.setId(newId+1);
        deckDS.insertDeckIntoDB(newDeck);
        //
        Intent intent = new Intent();
        if(deckDS.getSingleDeckByID(newDeck.getId()) == null) {
            updateCardsAffected();
            intent.putExtra(ID_OF_DECK,DEFAULT_ID);
        } else {
            intent.putExtra(ID_OF_DECK,newId+1);
        }
        setResult(RESULT_OK, intent);
        finish();
    }

    private void updateCardsAffected(){
        for(int i = 0; i < selectedIds.size(); i++){
            for(int j = 0; j < allcards.size(); j++){
                FlashcardEntity card = allcards.get(i);
                if(selectedIds.get(i) == card.getId()){
                    card.setInDeck(false);
                    card.setAssociatedDeck(-1L);
                    flashDS.updateFlashcardInDB(card);
                    break;
                }
            }
        }
    }

    @Override
    public void onSelectedCardClick(FlashcardEntity card){
        card.setInDeck(true);
        selectedIds.add(card.getId());
        card.setAssociatedDeck(newId+1);
        updateCardInDB(card);
    }

    @Override
    public void onDeselectCardClick(FlashcardEntity card){
        for(int i = 0; i < selectedIds.size(); i++){
            if(card.getId() == selectedIds.get(i)){
                selectedIds.remove(i);
                card.setInDeck(false);
                card.setAssociatedDeck(0L);
                updateCardInDB(card);
                break;
            }
        }
    }

    private void updateCardInDB(FlashcardEntity card) {
        FlashcardsDataSource ds = new FlashcardsDataSource(this);
        ds.updateFlashcardInDB(card);
    }
}