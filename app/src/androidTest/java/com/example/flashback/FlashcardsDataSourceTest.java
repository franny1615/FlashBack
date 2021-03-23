package com.example.flashback;

import android.content.Context;
import android.util.Log;

import androidx.test.platform.app.InstrumentationRegistry;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import com.example.flashback.DataSources.FlashcardsDataSource;
import com.example.flashback.DatabaseTables.FlashcardEntity;

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
    public void writeFlashcardAndReadItBack() throws Exception {
        flashcardsDS.deleteAllFlashcards();
        //
        FlashcardEntity flashcard = new FlashcardEntity();
        flashcard.setFrontText("first");
        flashcard.setBackText("firstback");

        flashcardsDS.insertFlashcardIntoDB(flashcard);
        List<FlashcardEntity> cards = flashcardsDS.loadAllFlashcardsFromDB();
        //
        assertEquals(cards.get(0).getFrontText(),flashcard.getFrontText());
        assertEquals(cards.get(0).getBackText(),flashcard.getBackText());
    }

    @Test
    public void deleteCard() throws Exception {
        flashcardsDS.deleteAllFlashcards();
        //
        FlashcardEntity flashcard = new FlashcardEntity();
        flashcard.setFrontText("first");
        flashcard.setBackText("firstback");

        flashcardsDS.insertFlashcardIntoDB(flashcard);

        flashcardsDS.deleteFlashcardInDB(flashcard);
        List<FlashcardEntity> cards = flashcardsDS.loadAllFlashcardsFromDB();
        //
        assertEquals(0,cards.size());
    }
}