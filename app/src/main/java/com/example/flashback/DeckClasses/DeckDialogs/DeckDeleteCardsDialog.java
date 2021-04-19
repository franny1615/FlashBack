package com.example.flashback.DeckClasses.DeckDialogs;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.DialogFragment;

import com.example.flashback.DataSources.DeckDataSource;
import com.example.flashback.DataSources.FlashcardsDataSource;
import com.example.flashback.DatabaseTables.DeckEntity;
import com.example.flashback.DatabaseTables.FlashcardEntity;

import java.util.ArrayList;
import java.util.List;

public class DeckDeleteCardsDialog extends DialogFragment {

    private List<Long> idsToDelete;
    private List<Long> allMyIds;
    private DeckEntity me;

    public interface DeckDeleteCardsDialogListener {
        void onPositiveClick(List<Long> idsToDelete, long deck);
    }

    private DeckDeleteCardsDialogListener listener;

    @Override
    @NonNull
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        idsToDelete = new ArrayList<>();
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("Select Cards to Delete: ");
        long deckId = -1L;
        if(getArguments() != null){
            deckId = getArguments().getLong("ID_OF_DECK");
        }
        builder.setMultiChoiceItems(getMyCards(deckId), null, (DialogInterface.OnMultiChoiceClickListener) (dialog, which, isChecked) -> {
            if(!isChecked){
                idsToDelete.remove(allMyIds.get(which));
            } else {
                idsToDelete.add(allMyIds.get(which));
            }
        });
        builder.setPositiveButton("Delete", (dialog, id) -> listener.onPositiveClick(idsToDelete, me.getId()));
        builder.setNegativeButton("Cancel", (dialog, id) -> dialog.cancel());
        return builder.create();
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        try {
            listener = (DeckDeleteCardsDialogListener) context;
        } catch (ClassCastException e) {
            throw new ClassCastException("You must implement NoticeDialogListener");
        }
    }

    private String[] getMyCards(Long deckId){
        String[] theCardsStrings = null;
        if(deckId != -1L) {
            DeckDataSource deckDS = new DeckDataSource(getContext());
            me = deckDS.getSingleDeckByID(deckId);
            allMyIds = me.getCardIDs();
            //
            FlashcardsDataSource flashDS = new FlashcardsDataSource(getContext());
            //
            theCardsStrings = new String[me.getSize()];
            for(int i = 0; i < me.getSize(); i++) {
                FlashcardEntity card = flashDS.getSingleFlashcardById(allMyIds.get(i));
                theCardsStrings[i] = card.getFrontText();
            }
        }
        return theCardsStrings;
    }
}
