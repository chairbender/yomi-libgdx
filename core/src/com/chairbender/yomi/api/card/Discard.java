package com.chairbender.yomi.api.card;

import java.util.ArrayList;
import java.util.List;

/**
 * Tracks a player's discard pile
 */
public class Discard {
    private List<Card> cards;

    public Discard() {
        cards = new ArrayList<Card>();
    }

    /**
     *
     * @param toAdd card to add to the top of the discard pile, face up
     */
    public void addCard(Card toAdd) {
        cards.add(toAdd);
    }


    /**
     *
     * @return the cards in the discard pile as a list, with the first card being the card at the bottom of the pile.
     */
    public List<Card> getCards() {
        return cards;
    }
}
