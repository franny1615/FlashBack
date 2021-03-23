package com.example.flashback.Databases;

import android.content.Context;

import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.example.flashback.DataAccessObjects.FlashcardTableDAO;
import com.example.flashback.DatabaseTables.FlashcardEntity;

@androidx.room.Database(entities = {FlashcardEntity.class}, version = 1, exportSchema = false)
public abstract class FlashcardsDatabase extends RoomDatabase {

    private static final String DB_NAME = "flashcard_project_database";

    private static FlashcardsDatabase INSTANCE = null;

    public static FlashcardsDatabase getInstance(Context context) {
        // basically make the database if it doesn't already exist
        if(INSTANCE == null) {
            synchronized (FlashcardsDatabase.class) {
                INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                        FlashcardsDatabase.class,
                        DB_NAME
                ).build();
            }
        }
        return INSTANCE;
    }

    // abstract methods for exposing our data access objects
    // for each table in our database
    public abstract FlashcardTableDAO flashcardDAO();

}
