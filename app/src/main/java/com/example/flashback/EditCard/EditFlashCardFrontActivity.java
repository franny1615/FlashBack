package com.example.flashback.EditCard;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.flashback.R;

public class EditFlashCardFrontActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_front);

        //put the front of card on text view
        Intent intent = getIntent();
        String frontString = intent.getStringExtra(EditFlashCardActivity.EXTRA_FRONT_CARD);

        EditText editFront = (EditText) findViewById(R.id.front_card_editscreen);
        editFront.setText(frontString);
    }

//    public void saveFrontEdit(View view)
//    {
//        Intent intent =
//    }
}
