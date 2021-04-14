package com.example.flashback.DataAccessObjects;

import androidx.core.provider.FontsContractCompat;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.SkipQueryVerification;
import androidx.room.Update;

import com.example.flashback.DatabaseTables.DeckEntity;

import java.util.List;

import io.reactivex.Flowable;

@Dao
public interface DeckTableDAO {

    @Insert
    long insertDeckIntoDB(DeckEntity deck);

    @Update
    int updateDeckInDB(DeckEntity deck);

    @Delete
    int deleteDeckInDB(DeckEntity deck);

    @Query("SELECT * FROM deck_table")
    Flowable<List<DeckEntity>> loadAllDecksFromDB();

    @Query("DELETE FROM deck_table")
    void deleteAllDecks();

    @Query("SELECT * FROM deck_table WHERE id=:id")
    DeckEntity getSingleDeckByID(long id);

}
