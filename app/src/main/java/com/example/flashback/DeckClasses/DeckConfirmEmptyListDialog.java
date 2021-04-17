package com.example.flashback.DeckClasses;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.DialogFragment;

import com.example.flashback.R;

public class DeckConfirmEmptyListDialog extends DialogFragment {

    public interface ConfirmEmptyDialogListener {
        void onDialogPositiveClick(DialogFragment fragment);
        void onDialogNegativeClick(DialogFragment fragment);
    }

    private ConfirmEmptyDialogListener listener;

    @Override
    @NonNull
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setMessage("Confirm empty deck")
                .setPositiveButton("Yes", (dialog, id) -> listener.onDialogPositiveClick(DeckConfirmEmptyListDialog.this))
                .setNegativeButton("No", (dialog, id) -> listener.onDialogNegativeClick(DeckConfirmEmptyListDialog.this));
        return builder.create();
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        try {
            listener = (ConfirmEmptyDialogListener) context;
        } catch (ClassCastException e) {
            throw new ClassCastException("You must implement NoticeDialogListener");
        }
    }
}
