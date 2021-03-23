package com.example.flashback.DataAccessObjects;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import com.example.flashback.DatabaseTables.FlashcardEntity;

import java.util.List;

import io.reactivex.Flowable;

@Dao
public interface FlashcardTableDAO {

    /**
     * inserts a card into database table, if a duplicate of a flashcard is found it will replace it in the table
     * @param flashcard the object that represents a row in the SQLite table
     * @return the id of the newly created row
     * */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    long insertFlashcardIntoDB(FlashcardEntity flashcard);


    /**
     * updates a card in the database table
     * @param flashcard the object that represents a row in the SQLite table
     * @return the number of rows affected by this operation
     */
    @Update
    int updateFlashcardInDB(FlashcardEntity flashcard);

    /**
     * deletes a card from the database table
     * @param flashcard the object that represents a row in the SQLite table
     * @return the number of rows affected by this operation
     */
    @Delete
    int deleteFlashcardInDB(FlashcardEntity flashcard);

    /**
     * queries the database for all rows and places them into a list
     * of FlashcardEntity objects.
     * @return a list representation of the rows in the database
     */
    @Query("SELECT * FROM flashcard_table")
    Flowable<List<FlashcardEntity>> loadAllFlashcardsFromDB();

    @Query("DELETE FROM flashcard_table")
    void deleteAllFlashcards();
}
