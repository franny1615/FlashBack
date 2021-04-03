package com.example.flashback.AddFlashCard;

import android.content.Intent;
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

        long lastKnownId = getIntent().getLongExtra("LAST_KNOWN_ID",0L);

        EditText frontCard = findViewById(R.id.new_card_front_screen);
        EditText backCard = findViewById(R.id.new_card_back_screen);
        flashcard.setFrontText(frontCard.getText().toString());
        flashcard.setBackText(backCard.getText().toString());
        flashcard.setId(lastKnownId+1);

        flashcardDS.insertFlashcardIntoDB(flashcard);

        Intent intent = new Intent();
        // if there was a duplicate, new card doesn't exist
        if(flashcardDS.getSingleFlashcardById(flashcard.getId()) == null) {
            intent.putExtra("ID_NEW_CARD",-1L);
        }else{
            intent.putExtra("ID_NEW_CARD",lastKnownId+1);
        }
        setResult(RESULT_OK, intent);
        finish();
    }

    public void cancelNewCard(View view)
    {
        setResult(RESULT_CANCELED,null);
        finish();
    }
}
