package com.example.flashback;

import android.content.Context;

import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.platform.app.InstrumentationRegistry;

import com.example.flashback.DataSources.DeckDataSource;
import com.example.flashback.DataSources.FlashcardsDataSource;
import com.example.flashback.DatabaseTables.DeckEntity;
import com.example.flashback.DatabaseTables.FlashcardEntity;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.ArrayList;
import java.util.List;

@RunWith(AndroidJUnit4.class)
public class DeckDatabaseTesting {
    private DeckDataSource deckDS;
    private FlashcardsDataSource flashcardDS;

    @Before public void getAppContext()
    {
        Context context = InstrumentationRegistry.getInstrumentation().getTargetContext();

        deckDS = new DeckDataSource(context);
        flashcardDS = new FlashcardsDataSource(context);

        flashcardDS.deleteAllFlashcards();
        deckDS.deleteAllDecks();
    }

    @Test
    public void addDeckName()
    {
        String deckName = "My First Deck";
        DeckEntity deck = new DeckEntity();
        deck.setDeckName(deckName);
        assertEquals(deck.getDeckName(), deckName);
    }

    @Test
    public void addDeckToDB()
    {
        String deckName = "My First Deck";
        DeckEntity deck = new DeckEntity();
        deck.setDeckName(deckName);
        deckDS.insertDeckIntoDB(deck);

        List<DeckEntity> decks = deckDS.loadAllDecksFromDB();
        assertEquals(decks.get(0).getDeckName(), deckName);
    }

    @Test
    public void addListToDeck()
    {
        DeckEntity deck = new DeckEntity();
        String deckName = "My Deck With Cards";
        deck.setDeckName(deckName);

        deck.addCardToDeck(0L);
        deck.addCardToDeck(1L);
        deck.addCardToDeck(2L);
        deck.addCardToDeck(3L);

        String cardsInDeckString = deck.toString();

        deckDS.insertDeckIntoDB(deck);
        List<DeckEntity> decks = deckDS.loadAllDecksFromDB();
        assertEquals(decks.get(0).toString(), cardsInDeckString);
    }

    @Test
    public void getDeckFromDB()
    {
        DeckEntity deck1 = new DeckEntity();
        DeckEntity deck2 = new DeckEntity();
        String deckName1 = "Deck 1";
        String deckName2 = "Deck 2";

        deck1.setDeckName(deckName1);
        deck2.setDeckName(deckName2);

        deckDS.insertDeckIntoDB(deck1);
        deckDS.insertDeckIntoDB(deck2);

        List<DeckEntity> decks = deckDS.loadAllDecksFromDB();

        Long deck2id = decks.get(1).getId();
        DeckEntity deckTemp = deckDS.getSingleDeckByID(deck2id);
        assertEquals(deck2.getDeckName(), deckTemp.getDeckName());
    }

    @Test
    public void deleteDeckFromDB()
    {
        DeckEntity deck1 = new DeckEntity();
        DeckEntity deck2 = new DeckEntity();
        String deckName1 = "Deck 1";
        String deckName2 = "Deck 2";

        deck1.setDeckName(deckName1);
        deck2.setDeckName(deckName2);

        deckDS.insertDeckIntoDB(deck1);
        deckDS.insertDeckIntoDB(deck2);

        List<DeckEntity> decks = deckDS.loadAllDecksFromDB();

        deckDS.deleteDeckInDB(decks.get(1));
        decks = deckDS.loadAllDecksFromDB();
        assertEquals(1, decks.size());
    }

    @Test
    public void updateDeckInDB()
    {
        String deckName1 = "My Deck";
        String deckName2 = "My Updated Deck";

        int cardCount = 0;
        DeckEntity deck = new DeckEntity();
        deck.setDeckName(deckName1);

        List<Long> cards = new ArrayList<>();
        cards.add(1L);
        cardCount++;
        cards.add(2L);
        cardCount++;
        cards.add(3L);
        cardCount++;
        deck.setCardIDs(cards);

        deckDS.insertDeckIntoDB(deck);
        List<DeckEntity> decks = deckDS.loadAllDecksFromDB();
        deck = decks.get(0);

        deck.addCardToDeck(4L);
        cardCount++;
        deck.setDeckName(deckName2);
        deckDS.updateDeckInDB(deck);

        DeckEntity updatedDeckFromDB = decks.get(0);
        assertEquals(cardCount,updatedDeckFromDB.getSize());
        assertEquals(deckName2, updatedDeckFromDB.getDeckName());
    }

    @Test
    public void getSingleDeckByID()
    {
        String deckName = "New Deck";
        List<Long> cards = new ArrayList<>();
        cards.add(1L);
        DeckEntity deck = new DeckEntity(deckName,cards);


        deckDS.insertDeckIntoDB(deck);
        List<DeckEntity> decks = deckDS.loadAllDecksFromDB();
        Long deckIDToRetrieve = decks.get(0).getId();

        DeckEntity deckRetrieved = deckDS.getSingleDeckByID(deckIDToRetrieve);

        assertEquals(deck.getDeckName(), deckRetrieved.getDeckName());
        assertEquals(deck.toString(), deckRetrieved.toString());
    }

}
