package com.example.flashback;

import android.content.Context;
import android.util.Log;

import androidx.room.Room;
import androidx.test.platform.app.InstrumentationRegistry;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import com.example.flashback.DataAccessObjects.FlashcardTableDAO;
import com.example.flashback.DataSources.FlashcardsDataSource;
import com.example.flashback.DatabaseTables.FlashcardEntity;
import com.example.flashback.Databases.FlashcardsDatabase;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;


import java.util.List;

import static org.junit.Assert.*;

/**
 * Test the DataSource class which interacts with all components
 * of database classes, so if all of its methods work, all good.
 */
@RunWith(AndroidJUnit4.class)
public class FlashcardsDataSourceTest {
    private FlashcardsDataSource flashcardsDS;

    @Before
    public void useAppContext() {
        // Context of the app under test.
        Context appContext = InstrumentationRegistry.getInstrumentation().getTargetContext();
        flashcardsDS = new FlashcardsDataSource(appContext);
    }

    @Test
    public void writeFlashcardAndReadItBack() {
        flashcardsDS.deleteAllFlashcards();
        //
        FlashcardEntity flashcard = new FlashcardEntity();
        flashcard.setFrontText("first");
        flashcard.setBackText("firstback");
        //
        flashcardsDS.insertFlashcardIntoDB(flashcard);
        List<FlashcardEntity> cards = flashcardsDS.loadAllFlashcardsFromDB();
        //
        assertEquals(cards.get(0).getFrontText(),flashcard.getFrontText());
        assertEquals(cards.get(0).getBackText(),flashcard.getBackText());
    }

    @Test
    public void deleteCard() {
        flashcardsDS.deleteAllFlashcards();
        //
        FlashcardEntity flashcard = new FlashcardEntity();
        flashcard.setFrontText("first");
        flashcard.setBackText("firstback");
        //
        flashcardsDS.insertFlashcardIntoDB(flashcard);
        //
        List<FlashcardEntity> cards = flashcardsDS.loadAllFlashcardsFromDB();
        flashcard.setId(cards.get(0).getId());
        //
        flashcardsDS.deleteFlashcardInDB(flashcard);
        cards = flashcardsDS.loadAllFlashcardsFromDB();
        //
        assertEquals(0,cards.size());
    }

    @Test
    public void duplicateCardTest() {
        flashcardsDS.deleteAllFlashcards();
        //
        FlashcardEntity flashcard = new FlashcardEntity();
        flashcard.setFrontText("first");
        flashcard.setBackText("firstback");
        flashcard.setId(1);
        //
        flashcardsDS.insertFlashcardIntoDB(flashcard);
        flashcardsDS.insertFlashcardIntoDB(flashcard);
        //
        List<FlashcardEntity> cards = flashcardsDS.loadAllFlashcardsFromDB();
        //
        assertEquals(1,cards.size());
    }

    @Test
    public void updateCardTest() {
        flashcardsDS.deleteAllFlashcards();
        List<FlashcardEntity> cards;
        //
        FlashcardEntity flashcard = new FlashcardEntity();
        flashcard.setFrontText("first");
        flashcard.setBackText("firstback");
        flashcard.setId(1);
        //
        flashcardsDS.insertFlashcardIntoDB(flashcard);
        cards = flashcardsDS.loadAllFlashcardsFromDB();
        assertEquals("first",cards.get(0).getFrontText());
        assertEquals("firstback",cards.get(0).getBackText());
        //
        flashcard.setFrontText("dude");
        flashcard.setBackText("card");
        //
        flashcardsDS.updateFlashcardInDB(flashcard);
        cards = flashcardsDS.loadAllFlashcardsFromDB();
        //
        assertEquals("dude",cards.get(0).getFrontText());
        assertEquals("card",cards.get(0).getBackText());
    }
}