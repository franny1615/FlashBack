package com.example.flashback.DeckClasses.DeckDialogs;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.fragment.app.DialogFragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.flashback.DataSources.DeckDataSource;
import com.example.flashback.DataSources.FlashcardsDataSource;
import com.example.flashback.DatabaseTables.DeckEntity;
import com.example.flashback.DatabaseTables.FlashcardEntity;
import com.example.flashback.R;
import com.example.flashback.RecyclerViewAdapters.SelectItemRecyclerViewAdapter;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static com.example.flashback.MainActivity.ID_OF_DECK;

public class DeckMoveCardsDialog extends Dialog implements SelectItemRecyclerViewAdapter.SelectItemRecyclerViewAdapterListener {

    private List<Long> idsToMove;
    private DeckEntity me;
    private long deckIdToMoveTo;

    private final DeckMoveCardsDialog.DeckMoveCardsDialogListener listener;
    private final long deckId;
    private final DeckDataSource deckDS;

    public interface DeckMoveCardsDialogListener {
        void onPositiveMoveMultipleCardsClick(List<Long> idsThatMoved, long deckTheyCameFrom, long deckTheyGoingTo);
    }

    public DeckMoveCardsDialog(@NonNull Context context, long deckId, DeckMoveCardsDialogListener listener) {
        super(context);
        this.deckDS = new DeckDataSource(context);
        this.me = deckDS.getSingleDeckByID(deckId);
        this.deckId = deckId;
        this.listener = listener;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.move_cards_out_dialog);
        //
        idsToMove = new ArrayList<>();
        RecyclerView decksRV = findViewById(R.id.deckScreen_move_cards_selectDeck_recyclerview);
        RecyclerView cardsRV = findViewById(R.id.deckScreen_move_cards_select_recyclerview);
        //
        Button move = findViewById(R.id.deckScreen_move_cards_button);
        move.setOnClickListener(this::move);
        Button cancel = findViewById(R.id.deckScreen_move_cards_cancel_button);
        cancel.setOnClickListener(this::cancel);
        //
        SelectItemRecyclerViewAdapter<FlashcardEntity> cardsAdapter = new SelectItemRecyclerViewAdapter<>(getMyCards(deckId), this);
        cardsRV.setHasFixedSize(true);
        cardsRV.setAdapter(cardsAdapter);
        //
        List<DeckEntity> decks = deckDS.loadAllDecksFromDB();
        for (int i = 0; i < decks.size(); i++) {
            if (decks.get(i).getDeckName().equals(me.getDeckName())) {
                decks.remove(i);
                break;
            }
        }
        //
        SelectItemRecyclerViewAdapter<DeckEntity> decksAdapter = new SelectItemRecyclerViewAdapter<>(decks, this);
        decksRV.setHasFixedSize(true);
        decksRV.setAdapter(decksAdapter);
    }

    private List<FlashcardEntity> getMyCards(Long deckId) {
        List<FlashcardEntity> theCards = null;
        if (deckId != -1L) {
            me = deckDS.getSingleDeckByID(deckId);
            List<Long> allMyIds = me.getCardIDs();
            //
            FlashcardsDataSource flashDS = new FlashcardsDataSource(getContext());
            //
            theCards = new ArrayList<>();
            for (int i = 0; i < me.getSize(); i++) {
                FlashcardEntity card = flashDS.getSingleFlashcardById(allMyIds.get(i));
                theCards.add(card);
            }
        }
        return theCards;
    }

    @Override
    public void onSelectItem(Object item) {
        if(item instanceof DeckEntity) {
            DeckEntity deck = (DeckEntity) item;
            deckIdToMoveTo = deck.getId();
        }
        if(item instanceof FlashcardEntity){
            FlashcardEntity card = (FlashcardEntity) item;
            idsToMove.add(card.getId());
        }
    }

    @Override
    public void onDeselectItem(Object item) {
        if(item instanceof DeckEntity) {
            deckIdToMoveTo = -1L;
        }
        if(item instanceof FlashcardEntity){
            FlashcardEntity card = (FlashcardEntity) item;
            idsToMove.remove(card.getId());
        }
    }

    public void move(View view) {
        listener.onPositiveMoveMultipleCardsClick(idsToMove,me.getId(),deckIdToMoveTo);
        dismiss();
    }

    public void cancel(View view) {
        cancel();
    }
}
