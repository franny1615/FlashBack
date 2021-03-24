package com.example.flashback.DataSources;

import android.content.Context;

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
        if(!isFlashcardDuplicate(flashcard)) {
            long rowID = flashcardDAO.insertFlashcardIntoDB(flashcard);
        }
    }

    public void updateFlashcardInDB(FlashcardEntity flashcard){
        int rowsAffected = flashcardDAO.updateFlashcardInDB(flashcard);
    }

    public void deleteFlashcardInDB(FlashcardEntity flashcard) {
        int rowsAffected = flashcardDAO.deleteFlashcardInDB(flashcard);
    }

    public List<FlashcardEntity> loadAllFlashcardsFromDB() {
        return flashcardDAO.loadAllFlashcardsFromDB().blockingFirst();
    }

    public void deleteAllFlashcards() {
        flashcardDAO.deleteAllFlashcards();
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
