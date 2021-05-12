package com.example.flashback.DatabaseTables;

import android.util.Log;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;
import androidx.room.TypeConverter;
import androidx.room.TypeConverters;

import com.example.flashback.Converters.DeckCardIDListConverter;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

@Entity(tableName = "deck_table")
public class DeckEntity {

    @PrimaryKey(autoGenerate = true)
    private long id;

    @TypeConverters(DeckCardIDListConverter.class)
    private final List<Long> cardIDs;

    private String deckName;
    private int size;

    public DeckEntity()
    {
        this.cardIDs = new ArrayList<>();
        this.size = 0;
    }

    public DeckEntity(String name, List<Long> cardIDs){
        this.size = 0;
        this.cardIDs = new ArrayList<>();
        setDeckName(name);
        setCardIDs(cardIDs);
    }

    public void setDeckName(String name) { this.deckName = name; }

    public String getDeckName(){return this.deckName;}

    public void setId(long id) {this.id = id;}

    public long getId() {return this.id;}

    public boolean addCardToDeck(Long ID)
    {
        boolean successfulAdd = false;
        if(!this.cardIDs.contains(ID)) {
            this.cardIDs.add(ID);
            this.size++;
            successfulAdd = true;
        }
        else
            Log.d("ADD CARD TO DECK", "Card already exists in deck.");
        return successfulAdd;
    }

    public boolean removeCardFromDeck(Long ID)
    {
        boolean removed = false;
        if(this.cardIDs.remove(ID))
        {
            this.size--;
            removed = true;
        }
        else
            {
                Log.d("REMOVE CARD FROM DECK","Failed to remove the card with ID " + ID + "from the deck.");
            }
        return removed;
    }

    public void setCardIDs(List<Long> cardIDs)
    {

        if (cardIDs != null){
            this.cardIDs.addAll(cardIDs);
            size += cardIDs.size();
        }
    }

    public List<Long> getCardIDs()
    {
        List<Long> returnedCardIDs = new ArrayList<>();
        returnedCardIDs.addAll(this.cardIDs);
        return returnedCardIDs;
    }

    public void setSize(int size)
    {
        if(size > -1)
            this.size = size;
    }

    public int getSize()
    {
        return this.size;
    }
}
