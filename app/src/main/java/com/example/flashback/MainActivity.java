package com.example.flashback;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.animation.AnimatorInflater;
import android.animation.AnimatorSet;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.example.flashback.DataSources.FlashcardsDataSource;
import com.example.flashback.DatabaseTables.FlashcardEntity;

import java.util.ArrayList;
import java.util.List;


public class MainActivity extends AppCompatActivity {
    FlashcardsDataSource flashcardDS;
    List<FlashcardEntity> allcards = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        flashcardDS = new FlashcardsDataSource(this);
        FlashcardEntity card1 = new FlashcardEntity();

        flashcardDS.insertFlashcardIntoDB(card1);
        allcards = flashcardDS.loadAllFlashcardsFromDB();

        RecyclerViewAdapter adapter = new RecyclerViewAdapter(allcards);
        RecyclerView myView = findViewById(R.id.recyclerview);
        myView.setHasFixedSize(true);
        myView.setAdapter(adapter);
        LinearLayoutManager manager = new LinearLayoutManager(this);
        manager.setOrientation(LinearLayoutManager.VERTICAL);
        myView.setLayoutManager(manager);
        adapter.notifyDataSetChanged();
    }

}
