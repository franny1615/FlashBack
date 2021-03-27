package com.example.flashback.DataSources;

import android.content.Context;
import android.util.Log;

import com.example.flashback.DataAccessObjects.FlashcardTableDAO;
import com.example.flashback.DatabaseTables.FlashcardEntity;
import com.example.flashback.Databases.FlashcardsDatabase;

import java.util.List;

// this class is basically an abstraction of the data access object
// allows us to do more than simply add, update, or remove from the database
// and also makes the database connection for us.
public class FlashcardsDataSource {
    private final FlashcardTableDAO flashcardDAO;
    private List<FlashcardEntity> cards;

    /**
     * establishes the connection to the database and initializes
     * any data access objects needed for this data source
     * @param context context from where this is being created
     */
    public FlashcardsDataSource(Context context) {
        FlashcardsDatabase flashcardsDB = FlashcardsDatabase.getInstance(context);
        this.flashcardDAO = flashcardsDB.flashcardDAO();
    }

    public void insertFlashcardIntoDB(FlashcardEntity flashcard) {
        Thread insertThread = new Thread(() -> {
            if(!isFlashcardDuplicate(flashcard)) {
                long rowID = flashcardDAO.insertFlashcardIntoDB(flashcard);
            }
        });
        insertThread.start();
        try{
            insertThread.join();
        } catch (Exception e) {
            Log.d("INSERT: ", "error in insertFlashcardIntoDB");
        }
    }

    public void updateFlashcardInDB(FlashcardEntity flashcard){
        Thread updateThread = new Thread(() -> flashcardDAO.updateFlashcardInDB(flashcard));
        updateThread.start();
        try{
            updateThread.join();
        } catch (Exception e) {
            Log.d("UPDATE: ", "error in updateFlashcardIntoDB");
        }
    }

    public void deleteFlashcardInDB(FlashcardEntity flashcard) {
        Thread deleteThread = new Thread(() -> flashcardDAO.deleteFlashcardInDB(flashcard));
        deleteThread.start();
        try {
            deleteThread.join();
        } catch (Exception e) {
            Log.d("DELETE:"," error in deleteFlashcardInDB");
        }
    }

    public List<FlashcardEntity> loadAllFlashcardsFromDB() {
        return flashcardDAO.loadAllFlashcardsFromDB().blockingFirst();
    }

    public void deleteAllFlashcards() {
        Thread deleteAll = new Thread(flashcardDAO::deleteAllFlashcards);
        deleteAll.start();
        try{
            deleteAll.join();
        } catch (Exception e) {
            Log.d("DELETEALL::","error in deleteAll");
        }
    }

    public FlashcardEntity getSingleFlashcardById(long id) {
        final FlashcardEntity[] flashcard = new FlashcardEntity[1];
        Thread getSingleCard = new Thread(()-> flashcard[0] = flashcardDAO.getSingleFlashcardById(id));
        getSingleCard.start();
        try {
            getSingleCard.join();
        }catch (Exception e) {
            Log.d("GETSINGLEFLASHCARDBYID:"," error in getSingleFlashcardById");
        }
        return flashcard[0];
    }

    public boolean isFlashcardDuplicate(FlashcardEntity flashcard) {
        boolean isDuplicate = false;
        cards = flashcardDAO.loadAllFlashcardsFromDB().blockingFirst();
        for(FlashcardEntity existingFlashcard: cards) {
            if(existingFlashcard.getFrontText().equals(flashcard.getFrontText()) &&
               existingFlashcard.getBackText().equals(flashcard.getBackText())) {
                isDuplicate = true;
                break;
            }
        }
        return isDuplicate;
    }
}
