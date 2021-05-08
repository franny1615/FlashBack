package com.example.flashback.Quiz;

import android.icu.util.ICUUncheckedIOException;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.flashback.DataSources.DeckDataSource;
import com.example.flashback.DataSources.FlashcardsDataSource;
import com.example.flashback.DatabaseTables.DeckEntity;
import com.example.flashback.DatabaseTables.FlashcardEntity;
import com.example.flashback.R;
import com.wajahatkarim3.easyflipview.EasyFlipView;

import java.util.ArrayList;
import java.util.List;
import java.util.ListIterator;

public class DeckQuiz extends AppCompatActivity implements View.OnClickListener{
    private DeckDataSource deckDS;
    private FlashcardsDataSource flashcardDS;
    private DeckEntity deck;
    private EasyFlipView easyFlipCard;
    private TextView front, back;
    private List<Long> flashcardIDs;
    private FlashcardEntity currFlashCard;
    private List<FlashcardEntity> flashcards;
    private int index, size;

    //OnCreate Methods
    private void setupCardsAndDS() {
        flashcards = new ArrayList<>();
        flashcardIDs = deck.getCardIDs();
        flashcardDS = new FlashcardsDataSource(this);
        for (Long flashcardID: flashcardIDs) {
            flashcards.add(flashcardDS.getSingleFlashcardById(flashcardID));
        }
    }
    private void setupDeckAndDS(){
        deckDS = new DeckDataSource(this);
        Long deckID = (Long) getIntent().getLongExtra(QuizHome.QUIZ_HOME_KEY, 0);
        deck = deckDS.getSingleDeckByID(deckID);
    }
    private void initializeViews() {
        easyFlipCard = findViewById(R.id.quiz_card);
        front = findViewById(R.id.quiz_card_front_text);
        back = findViewById(R.id.quiz_card_back_text);
    }
    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.deck_quiz_activity);

        setupDeckAndDS();
        initializeViews();
        setupCardsAndDS();

        index = 0;
        size = flashcardIDs.size();

        populateQuizCard();
    }

    private void populateQuizCard() {
        currFlashCard = (FlashcardEntity) flashcards.get(index);
        front.setText(currFlashCard.getFrontText());
        back.setText(currFlashCard.getBackText());
    }

    

    @Override
    public void onClick(View view)
    {
        this.easyFlipCard.flipTheView(true);
    }
}
