package com.example.flashback.EditCard;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.flashback.DataSources.FlashcardsDataSource;
import com.example.flashback.DatabaseTables.FlashcardEntity;
import com.example.flashback.R;

import static com.example.flashback.MainActivity.EXTRA_FLASHCARD_ID;

public class EditFlashCard extends AppCompatActivity {

    public long DEFAULT_ID = 0;
    FlashcardEntity flashCard = null;
    FlashcardsDataSource flashcardsDS = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_flashcard);

        this.flashcardsDS = new FlashcardsDataSource(this);
        TextView frontView = findViewById(R.id.front_screen_edit);
        TextView backView = findViewById(R.id.back_screen_edit);

        Intent intent = getIntent();
        long id = getIntent().getLongExtra(EXTRA_FLASHCARD_ID,DEFAULT_ID);
        Log.d("Activity", "Retrieved Flashcard with ID: " + id);
        this.flashCard = this.flashcardsDS.getSingleFlashcardById(id);

        frontView.setText(this.flashCard.getFrontText().toString());
        backView.setText(this.flashCard.getBackText().toString());
    }

    public void saveFlashCard(View view){
        EditText front = findViewById(R.id.front_screen_edit);
        EditText back = findViewById(R.id.back_screen_edit);

        String frontText = front.getText().toString();
        String backText = back.getText().toString();

        this.flashCard.setFrontText(frontText);
        this.flashCard.setBackText(backText);
        flashcardsDS.updateFlashcardInDB(flashCard);
        finish();
    }

    public void cancelFlashCard(View view){
        finish();
    }
}
