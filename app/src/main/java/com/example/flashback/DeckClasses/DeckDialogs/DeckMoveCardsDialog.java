package com.example.flashback.DeckClasses.DeckDialogs;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.fragment.app.DialogFragment;
import androidx.recyclerview.widget.RecyclerView;

import com.example.flashback.DataSources.DeckDataSource;
import com.example.flashback.DataSources.FlashcardsDataSource;
import com.example.flashback.DatabaseTables.DeckEntity;
import com.example.flashback.DatabaseTables.FlashcardEntity;
import com.example.flashback.R;
import com.example.flashback.RecyclerViewAdapters.SelectCardsRecyclerViewAdapter;

import java.util.ArrayList;
import java.util.List;

import static com.example.flashback.MainActivity.ID_OF_DECK;

public class DeckMoveCardsDialog extends DialogFragment implements SelectCardsRecyclerViewAdapter.SelectCardClickListener {

    private List<Long> idsToMove;
    private List<Long> allMyIds;
    private DeckEntity me;

    public interface DeckMoveCardsDialogListener {
        void onPositiveMoveMultipleCardsClick(List<Long> idsThatMoved, long deckTheyCameFrom, long deckTheyGoingTo);
    }

    private DeckMoveCardsDialog.DeckMoveCardsDialogListener listener;

    @Override
    @NonNull
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        idsToMove = new ArrayList<>();
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        long deckId = -1L;
        if(getArguments() != null){
            deckId = getArguments().getLong(ID_OF_DECK);
        }
        //
        View customView = requireActivity().getLayoutInflater().inflate(R.layout.move_cards_out_dialog,null);
        RecyclerView selectCards = customView.findViewById(R.id.deckScreen_move_cards_select_recyclerview);
        SelectCardsRecyclerViewAdapter cardsAdapter = new SelectCardsRecyclerViewAdapter(getMyCards(deckId),this);
        selectCards.setHasFixedSize(true);
        selectCards.setAdapter(cardsAdapter);
        //
        builder.setView(customView);
        builder.setPositiveButton("Done", (dialog, id) -> listener.onPositiveMoveMultipleCardsClick(idsToMove, me.getId(),-1L));
        builder.setNegativeButton("Cancel", (dialog, id) -> dialog.cancel());
        return builder.create();
    }

    private List<FlashcardEntity> getMyCards(Long deckId){
        List<FlashcardEntity> theCards = null;
        if(deckId != -1L) {
            DeckDataSource deckDS = new DeckDataSource(getContext());
            me = deckDS.getSingleDeckByID(deckId);
            allMyIds = me.getCardIDs();
            //
            FlashcardsDataSource flashDS = new FlashcardsDataSource(getContext());
            //
            theCards = new ArrayList<>();
            for(int i = 0; i < me.getSize(); i++) {
                FlashcardEntity card = flashDS.getSingleFlashcardById(allMyIds.get(i));
                theCards.add(card);
            }
        }
        return theCards;
    }

    @Override
    public void onSelectedCardClick(FlashcardEntity card) {

    }

    @Override
    public void onDeselectCardClick(FlashcardEntity card) {

    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        try {
            listener = (DeckMoveCardsDialog.DeckMoveCardsDialogListener) context;
        } catch (ClassCastException e) {
            throw new ClassCastException("You must implement NoticeDialogListener");
        }
    }
}
