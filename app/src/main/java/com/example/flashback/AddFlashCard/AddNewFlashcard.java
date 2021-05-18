package com.example.flashback.AddFlashCard;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.flashback.DataSources.FlashcardsDataSource;
import com.example.flashback.DatabaseTables.FlashcardEntity;
import com.example.flashback.R;

import static com.example.flashback.MainActivity.DEFAULT_ID;
import static com.example.flashback.MainActivity.ID_NEW_CARD;
import static com.example.flashback.MainActivity.LAST_KNOWN_ID;

public class AddNewFlashcard extends AppCompatActivity {
    Button BSelectImage;
    ImageView IVPreviewImage;
    int SELECT_PICTURE = 200;
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_new_flashcard);
        BSelectImage = findViewById(R.id.BSelectImage);
        IVPreviewImage = findViewById(R.id.IVPreviewImage);
        BSelectImage.setOnClickListener(v -> imageChooser());
    }
    void imageChooser() {
        Intent i = new Intent();
        i.setType("image/*");
        i.setAction(Intent.ACTION_GET_CONTENT);

        startActivityForResult(Intent.createChooser(i, "Select Picture"), SELECT_PICTURE);
    }

    public void saveNewCard(View view)
    {
        FlashcardEntity flashcard = new FlashcardEntity();
        FlashcardsDataSource flashcardDS = new FlashcardsDataSource(this);

        long lastKnownId = getIntent().getLongExtra(LAST_KNOWN_ID,0L);

        EditText frontCard = findViewById(R.id.new_card_front_screen);
        EditText backCard = findViewById(R.id.new_card_back_screen);
        flashcard.setFrontText(frontCard.getText().toString());
        flashcard.setBackText(backCard.getText().toString());
        flashcard.setId(lastKnownId+1);

        flashcardDS.insertFlashcardIntoDB(flashcard);

        Intent intent = new Intent();
        if(flashcardDS.getSingleFlashcardById(flashcard.getId()) == null) {
            intent.putExtra(ID_NEW_CARD,DEFAULT_ID);
        }else{
            intent.putExtra(ID_NEW_CARD,lastKnownId+1);
        }
        setResult(RESULT_OK, intent);
        finish();
    }

    public void cancelNewCard(View view)
    {
        setResult(RESULT_CANCELED,null);
        finish();
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (requestCode == SELECT_PICTURE) {
                Uri selectedImageUri = data.getData();
                if (null != selectedImageUri) {
                    IVPreviewImage.setImageURI(selectedImageUri);
                }
            }
        }
    }
}

