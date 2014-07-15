package com.chairbender.yomi.api.card;


import com.chairbender.yomi.api.card.exception.OutOfCardsException;
import com.chairbender.yomi.api.card.move.PokerRank;
import com.chairbender.yomi.api.character.YomiCharacter;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Stack;

/**
 * Represents a deck of cards (for a specific hero) that is in play in a game.
 */
public class Deck {

    private Stack<Card> deck;
    /***
     * Create a shuffled deck using the character's cards
     * @param character character whose deck should be created
     */
    public Deck(YomiCharacter character) {
        List<Card> cards = character.allCards();
        Collections.shuffle(cards);
        this.deck = new Stack<Card>();
        for (Card card : cards) {
            this.deck.push(card);
        }
    }

    /***
     * Has the side effect of removing the drawn cards from the deck.
     * @param num number of cards to draw from top of deck
     * @return the cards that were drawn from the top of the deck, in the order they were drawn.
     * @throws com.chairbender.yomi.api.card.exception.OutOfCardsException if num is greater than cardsLeft
     */
    public List<Card> drawCards(int num) throws OutOfCardsException {
        List<Card> result = new ArrayList<Card>();
        if (num > cardsLeft()) {
            throw new OutOfCardsException();
        }
        for (int i = 0; i < num ; i++) {
            result.add(deck.pop());
        }
        return result;
    }

    /***
     *
     * @return the number of cards left in the deck
     */
    public int cardsLeft() {

        return deck.size();
    }

    /**
     *
     * @return all the aces in the deck
     */
    public List<Card> getAllAces() {
        return getAllOfRank(PokerRank.ACE);
    }

    /**
     * Remove the card from the deck
     * @param toRemove card to remove
     */
    public void removeCard(Card toRemove) {
        deck.remove(toRemove);
    }

    /**
     *
     * @param card card to put on top of deck
     */
    public void putCardOnTop(Card card) {
        deck.add(card);
    }

    public List<Card> getAllOfRank(PokerRank rank) {
        List<Card> result = new ArrayList<Card>();

        for (Card card : deck) {
            if (card.getValue().getRank().equals(rank)) {
                result.add(card);
            }
        }

        return result;
    }
}



