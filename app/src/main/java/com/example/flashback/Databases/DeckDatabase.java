package com.example.flashback.Databases;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Insert;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.example.flashback.DataAccessObjects.DeckTableDAO;
import com.example.flashback.DatabaseTables.DeckEntity;

@Database(entities = {DeckEntity.class}, version = 1, exportSchema = false)
public abstract class DeckDatabase extends RoomDatabase {

    private static final String DECK_DB_NAME = "deck_database"; //Name it something else?

    private static DeckDatabase INSTANCE = null;

    public static DeckDatabase getInstance(Context context)
    {
        if(INSTANCE == null)
        {
            synchronized (DeckDatabase.class)
            {
                INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                        DeckDatabase.class,
                        DECK_DB_NAME)
                        .build();
            }
        }
        return INSTANCE;
    }

    public abstract DeckTableDAO deckTableDAO();
}
