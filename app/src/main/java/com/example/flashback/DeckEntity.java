package com.example.flashback;

import java.util.List;

public class DeckEntity {

    private String deckName;
    private List<Long> myCards;

    public DeckEntity(String deckName, List<Long> myCards){
        this.deckName = deckName;
        this.myCards = myCards;
    }

    public List<Long> getMyCards(){
        return this.myCards;
    }

    public String getDeckName(){
        return this.deckName;
    }
}
