package com.example.flashback.EditCard;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.flashback.BoilerTestFlashCard;
import com.example.flashback.R;

import org.w3c.dom.Text;

public class EditFlashCardActivity extends AppCompatActivity {
    public static String EXTRA_FRONT_CARD = "com.example.flashback.EditCard.FrontText";
    public static String EXTRA_BACK_CARD = "com.example.flashback.EditCard.BackText";
    public BoilerTestFlashCard flashCard = new BoilerTestFlashCard("","");
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_card);

        TextView frontView = findViewById(R.id.front_card_textView);
        TextView backView = findViewById(R.id.back_card_textView);

        frontView.setText(flashCard.getFront().toString());
        backView.setText(flashCard.getBack().toString());
    }

    public void editFront(View view)
    {
        //Editing the front of card
        //Todo : First Ticket
        Intent intent = new Intent(this, EditFlashCardFrontActivity.class);
        TextView frontTextView = findViewById(R.id.front_card_textView);
        String frontText = frontTextView.getText().toString();
        intent.putExtra(EXTRA_FRONT_CARD, frontText);
        startActivity(intent);
    }

    public void editBack(View view)
    {
        //Editing the back of card
        //Todo : Second Ticket
    }

    public void saveFront(View view)
    {
        //Saving method for edit
        //Capture intent -> get message -> save front
        //Todo : First ticket
    }

    public void saveBack(View view)
    {
        //Saving method for edit back
        //capture intent -> get message -> save back
        //Todo : Second Ticket
    }

    private void save(Intent intent)
    {
        String message = intent.getExtras().toString();

    }
}
