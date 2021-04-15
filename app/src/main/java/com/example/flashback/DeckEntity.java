package com.example.flashback;

import java.util.List;

public class DeckEntity {

    private long id;
    private String deckName;
    private List<Long> myCards;

    public DeckEntity(String deckName, List<Long> myCards, long id){
        this.deckName = deckName;
        this.myCards = myCards;
        this.id = id;
    }

    public List<Long> getMyCards(){
        return this.myCards;
    }

    public long getDeckId() {return this.id; }

    public String getDeckName(){
        return this.deckName;
    }
}
