package com.example.flashback.DatabaseTables;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "flashcard_table")
public class FlashcardEntity {

    @PrimaryKey(autoGenerate = true)
    private long id;

    // the two fields that we will
    // populate from user info
    private String frontText;
    private String backText;
    private boolean inDeck;
    // id methods
    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    // frontText methods
    public String getFrontText() {
        return frontText;
    }

    public void setFrontText(String frontText) {
        this.frontText = frontText;
    }

    // backText methods
    public String getBackText() {
        return backText;
    }

    public void setBackText(String backText) {
        this.backText = backText;
    }

    public void setInDeck(boolean inDeck){this.inDeck = inDeck;}

    public boolean getInDeck(){return this.inDeck;}
}
