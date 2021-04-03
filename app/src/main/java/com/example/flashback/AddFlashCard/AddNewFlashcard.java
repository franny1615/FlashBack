package com.example.flashback.AddFlashCard;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

import com.example.flashback.DataSources.FlashcardsDataSource;
import com.example.flashback.DatabaseTables.FlashcardEntity;
import com.example.flashback.R;

public class AddNewFlashcard extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_new_flashcard);
    }

    public void saveNewCard(View view)
    {
        FlashcardEntity flashcard = new FlashcardEntity();
        FlashcardsDataSource flashcardDS = new FlashcardsDataSource(this);

        EditText frontCard = findViewById(R.id.new_card_front_screen);
        EditText backCard = findViewById(R.id.new_card_back_screen);
        flashcard.setFrontText(frontCard.getText().toString());
        flashcard.setBackText(backCard.getText().toString());

        flashcardDS.insertFlashcardIntoDB(flashcard);
        finish();
    }

    public void cancelNewCard(View view)
    {
        finish();
    }
}
