package com.example.flashback.DataSources;

import android.content.Context;
import android.util.Log;

import com.example.flashback.DataAccessObjects.DeckTableDAO;
import com.example.flashback.DatabaseTables.DeckEntity;
import com.example.flashback.Databases.DeckDatabase;

import java.util.List;

public class DeckDataSource {
    private final DeckTableDAO deckDAO;
    private List<DeckEntity> decks;

    public DeckDataSource(Context context)
    {
        DeckDatabase deckDB = DeckDatabase.getInstance(context);
        this.deckDAO = deckDB.deckTableDAO();
    }

    public void insertDeckIntoDB(DeckEntity deck){
        Thread insertThread = new Thread(() ->
        {
            if(!isDeckDuplicate(deck))
            {
                long rowID = deckDAO.insertDeckIntoDB(deck);
            }
        });
        insertThread.start();
        try
        {
            insertThread.join();
        }catch(Exception e)
        {
            Log.d("INSERT", "error in insertDeckIntoDB");
        }
    }

    public List<DeckEntity> loadAllDecksFromDB()
    {
        return deckDAO.loadAllDecksFromDB().blockingFirst();
    }


    //Why are we creating an array of 1? Why not just create a single deck entity object?
    public DeckEntity getSingleDeckByID(long id){
        final DeckEntity[] deck = new DeckEntity[1];
        Thread getSingleDeck = new Thread(() -> deck[0] = deckDAO.getSingleDeckByID(id));
        getSingleDeck.start();
        try{
            getSingleDeck.join();
        }
        catch(Exception e){
            Log.d("GET_SINGLE_DECK_BY_ID", "error in getSingleDeckByID");
        }
        return deck[0];
    }

    public void deleteAllDecks()
    {
        Thread deleteAll = new Thread(deckDAO::deleteAllDecks);
        deleteAll.start();
        try{
            deleteAll.join();
        }catch (Exception e)
        {
            Log.d("DELETEALL", "error in deleteAllDecks");
        }
    }

    public void deleteDeckInDB(DeckEntity deck)
    {
        Thread deleteDeckInDB = new Thread(() -> deckDAO.deleteDeckInDB(deck));
        deleteDeckInDB.start();
        try{
            deleteDeckInDB.join();
        }catch (Exception e)
        {
            Log.d("DELETE DECK", "error in deleteDeckInDB");
        }
    }
    //Only checks for same deck name
    public boolean isDeckDuplicate(DeckEntity deck)
    {
        String deckName = deck.getDeckName();
        boolean isDuplicate = false;
        this.decks = deckDAO.loadAllDecksFromDB().blockingFirst();
        for (int i = 0; i < this.decks.size() && !isDuplicate; i++){
            if(this.decks.get(i).getDeckName().compareTo(deckName) == 0){
                isDuplicate = true;
            }
        }
        return isDuplicate;
    }

    public void updateDeckInDB(DeckEntity deck)
    {
        Thread updateDeckInDB = new Thread(() -> deckDAO.updateDeckInDB(deck));
        updateDeckInDB.start();
        try{
            updateDeckInDB.join();
        }catch (Exception e)
        {
            Log.d("UPDATE DECK IN DB", "error in updateDeckInDB");
        }
    }
}
