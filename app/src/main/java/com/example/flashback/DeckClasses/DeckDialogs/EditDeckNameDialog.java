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

public class EditDeckNameDialog extends DialogFragment {

    public interface EditDeckNameDialogListener{
        void onDoneEditing(String newName);
    }

    private EditDeckNameDialog.EditDeckNameDialogListener listener;

    @Override
    @NonNull
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = requireActivity().getLayoutInflater();
        View mView = inflater.inflate(R.layout.edit_deck_name_dialog,null);
        EditText name = mView.findViewById(R.id.deck_dialog_editDeckName);
        builder.setView(mView)
                .setPositiveButton("Done", (dialog, id) -> checkEmptiness(mView.getContext(),name.getText().toString()))
                .setNegativeButton("Cancel", (dialog, id) -> dialog.cancel());
        return builder.create();
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        try {
            listener = (EditDeckNameDialog.EditDeckNameDialogListener) context;
        } catch (ClassCastException e) {
            throw new ClassCastException("You must implement NoticeDialogListener");
        }
    }

    private void checkEmptiness(Context context, String newName) {
        if(newName.equals("")){
            Toast.makeText(context,"Empty new name, no edit done",Toast.LENGTH_LONG).show();
        } else {
            listener.onDoneEditing(newName);
        }
    }
}
