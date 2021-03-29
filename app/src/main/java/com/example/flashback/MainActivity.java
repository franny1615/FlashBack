package com.example.flashback;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import com.example.flashback.EditCard.EditFlashCardActivity;

public class MainActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void openBoilerActivity(View view){
        Intent intent = new Intent(this, BoilerPlateActivity.class);
        startActivity(intent);
    }

    public void editFlashCard(View view)
    {
        Intent intent = new Intent(this, EditFlashCardActivity.class);
        startActivity(intent);
    }


}