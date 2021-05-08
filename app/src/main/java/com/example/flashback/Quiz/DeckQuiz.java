package com.example.flashback.Quiz;

import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.flashback.DataSources.DeckDataSource;
import com.example.flashback.DataSources.FlashcardsDataSource;
import com.example.flashback.DatabaseTables.DeckEntity;
import com.example.flashback.DatabaseTables.FlashcardEntity;
import com.example.flashback.R;
import com.wajahatkarim3.easyflipview.EasyFlipView;

import java.util.ArrayList;
import java.util.List;

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
    private Button nextButton, prevButton;

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
    private void populateQuizCard() {
        currFlashCard = (FlashcardEntity) flashcards.get(index);
        front.setText(currFlashCard.getFrontText());
        back.setText(currFlashCard.getBackText());
    }

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.deck_quiz_activity);
        index = 0;
        setupDeckAndDS();
        initializeViews();
        setupCardsAndDS();
        size = flashcardIDs.size();
        populateQuizCard();

        //Grab the next button and set it up
        nextButton = findViewById(R.id.next_button);
        prevButton = findViewById(R.id.prev_button);
        isNextValid();
        isPrevValid();
    }

    //Next Button Methods
    public void nextButton(View view)
    {
        if (++index < size) { populateQuizCard(); }
        isNextValid();
        isPrevValid();
    }
    private void isNextValid()
    {
        if (index == size-1)
        {
            nextButton.setEnabled(false);
            nextButton.setBackgroundColor(Color.GRAY);
        }
        else{
            nextButton.setEnabled(true);
            nextButton.setBackgroundColor(Color.BLUE);
        }
    }

    //Previous Button Methods
    public void prevButton(View view){
        if (--index >= 0){ populateQuizCard();}
        isPrevValid();
        isNextValid();
    }

    private void isPrevValid()
    {
        if (index == 0){
            prevButton.setEnabled(false);
            prevButton.setBackgroundColor(Color.GRAY);
        }
        else{
            prevButton.setEnabled(true);
            prevButton.setBackgroundColor(Color.BLUE);
            }
    }

    @Override
    public void onClick(View view)
    {
        this.easyFlipCard.flipTheView(true);
    }
}
