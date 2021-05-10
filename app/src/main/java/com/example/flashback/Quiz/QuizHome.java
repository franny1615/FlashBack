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

import java.util.List;

public class QuizHome extends AppCompatActivity {

    Long deck_id;
    DeckEntity deck;
    DeckDataSource deckDS;
    String quizTitle = "Quiz: ";
    List<Long> cards;
    final static String QUIZ_HOME_KEY = "com.example.flashback.Quiz.QuizHome";

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quiz_home);

        deckDS = new DeckDataSource(this);
        this.deck_id = getIntent().getLongExtra(MainActivity.ID_OF_DECK_FOR_QUIZ, 0);
        this.deck = deckDS.getSingleDeckByID(this.deck_id);

        TextView quizDeckTitle = (TextView) findViewById(R.id.quiz_deck_title);
        quizTitle = quizTitle + deck.getDeckName();
        quizDeckTitle.setText(quizTitle);

        cards = deck.getCardIDs();
    }


    public void startQuiz(View view)
    {
        if (deck.getSize() > 0)
        {
            Intent intent = new Intent(this, DeckQuiz.class);
            intent.putExtra(QUIZ_HOME_KEY, this.deck_id);
            startActivity(intent);
        }
        else
            {
                Toast.makeText(this, "No Cards Available", Toast.LENGTH_LONG).show();
            }
    }

}
