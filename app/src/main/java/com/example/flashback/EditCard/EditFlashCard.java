package com.example.flashback.EditCard;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.flashback.DataSources.FlashcardsDataSource;
import com.example.flashback.DatabaseTables.FlashcardEntity;
import com.example.flashback.R;

import static com.example.flashback.MainActivity.DEFAULT_POSITION;
import static com.example.flashback.MainActivity.EDITED_FLASHCARD_DETAILS;
import static com.example.flashback.MainActivity.EXTRA_FLASHCARD_ID;
import static com.example.flashback.MainActivity.ID_OF_EDITED_CARD;
import static com.example.flashback.MainActivity.POSITION_IN_MEMORY;

public class EditFlashCard extends AppCompatActivity {

    public long DEFAULT_ID = 0;
    FlashcardEntity flashCard = null;
    FlashcardsDataSource flashcardsDS = null;
    private int position;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_flashcard);

        this.flashcardsDS = new FlashcardsDataSource(this);
        TextView frontView = findViewById(R.id.front_screen_edit);
        TextView backView = findViewById(R.id.back_screen_edit);

        long id = getIntent().getLongExtra(EXTRA_FLASHCARD_ID, DEFAULT_ID);
        Log.d("Activity", "Retrieved Flashcard with ID: " + id);
        this.flashCard = this.flashcardsDS.getSingleFlashcardById(id);
        //
        position = getIntent().getIntExtra(POSITION_IN_MEMORY, DEFAULT_POSITION);
        //
        frontView.setText(this.flashCard.getFrontText());
        backView.setText(this.flashCard.getBackText());
    }


    public void saveFlashCard(View view) {
        EditText front = findViewById(R.id.front_screen_edit);
        EditText back = findViewById(R.id.back_screen_edit);

        String frontText = front.getText().toString();
        String backText = back.getText().toString();

        this.flashCard.setFrontText(frontText);
        this.flashCard.setBackText(backText);
        flashcardsDS.updateFlashcardInDB(flashCard);

        SharedPreferences sharedPref = this.getSharedPreferences(EDITED_FLASHCARD_DETAILS, Context.MODE_PRIVATE);
        SharedPreferences.Editor prefEditor = sharedPref.edit();
        prefEditor.putInt(POSITION_IN_MEMORY, position);
        prefEditor.putLong(ID_OF_EDITED_CARD, flashCard.getId());
        prefEditor.apply();
        finish();
    }

    public void cancelFlashCard(View view) {
        finish();
    }

}