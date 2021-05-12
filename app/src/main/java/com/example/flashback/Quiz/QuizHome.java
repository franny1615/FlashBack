package com.example.flashback.Quiz;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.flashback.DataSources.DeckDataSource;
import com.example.flashback.DatabaseTables.DeckEntity;
import com.example.flashback.MainActivity;
import com.example.flashback.R;
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton;

import java.util.List;

public class QuizHome extends AppCompatActivity {

    private Long deck_id;
    private DeckEntity deck;
    private DeckDataSource deckDS;
    private String quizTitle = "Quiz: ";
    private List<Long> cards;
    final static String QUIZ_HOME_KEY_DECK_ID = "com.example.flashback.Quiz.QuizHome_ID";
    final static String QUIZ_HOME_KEY_SHUFFLE = "com.example.flashback.Quiz.QuizHome_SHUFFLE";
    private ExtendedFloatingActionButton shuffleButton;
    private boolean shufflePressed;

    private void setupShuffleViewAndValue() {
        shuffleButton = findViewById(R.id.shuffle_button);
        shuffleButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                shuffleButton();
            }
        });
        shufflePressed = false;
    }

    private void setupDeckQuizTitle() {
        TextView quizDeckTitle = (TextView) findViewById(R.id.quiz_deck_title);
        quizTitle = quizTitle + deck.getDeckName();
        quizDeckTitle.setText(quizTitle);
    }

    private void setupDeck() {
        deckDS = new DeckDataSource(this);
        this.deck_id = getIntent().getLongExtra(MainActivity.ID_OF_DECK_FOR_QUIZ, 0);
        this.deck = deckDS.getSingleDeckByID(this.deck_id);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quiz_home);

        setupDeck();
        setupDeckQuizTitle();
        setupShuffleViewAndValue();

    }



    public void startQuiz(View view)
    {
        if (deck.getSize() > 0)
        {
            Intent intent = new Intent(this, DeckQuiz.class);
            intent.putExtra(QUIZ_HOME_KEY_DECK_ID, this.deck_id);
            intent.putExtra(QUIZ_HOME_KEY_SHUFFLE, this.shufflePressed);
            startActivity(intent);
        }
        else
            {
                Toast.makeText(this, "No Cards Available", Toast.LENGTH_LONG).show();
            }
    }


    public void shuffleButton()
    {
        if (shufflePressed)
        {
            shufflePressed = false;
            shuffleButton.setIconResource(R.drawable.shuffle_button);
        }
        else
        {
            shufflePressed = true;
            shuffleButton.setIconResource(R.drawable.shuffle_button_pressed);
        }
    }

    public void backButton(View view)
    {
        finish();
    }
}
