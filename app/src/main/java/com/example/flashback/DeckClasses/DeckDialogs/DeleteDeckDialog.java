package com.example.flashback.DeckClasses.DeckDialogs;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.DialogFragment;

import com.example.flashback.R;

public class DeleteDeckDialog extends DialogFragment {

    public interface DeleteDeckDialogListener{
        void onDeleteEverything();
        void onDeleteKeepCards();
    }

    private DeleteDeckDialog.DeleteDeckDialogListener listener;

    @Override
    @NonNull
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setMessage("How would you like to delete this deck:")
                .setNeutralButton("Cancel", (dialog,id) -> dialog.cancel())
                .setPositiveButton("Delete Everything", (dialog, id) -> listener.onDeleteEverything())
                .setNegativeButton("Keep Cards", (dialog, id) -> listener.onDeleteKeepCards());
        return builder.create();
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        try {
            listener = (DeleteDeckDialog.DeleteDeckDialogListener) context;
        } catch (ClassCastException e) {
            throw new ClassCastException("You must implement NoticeDialogListener");
        }
    }
}
