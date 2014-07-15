package com.chairbender.yomi.api.card;

import com.chairbender.yomi.api.card.exception.OutOfCardsException;
import com.chairbender.yomi.api.card.move.PokerRank;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * A hand of cards
 */
public class Hand {
    private List<Card> hand;
    private Card[] jokers;
    private Card[] nonJokers;

    public Hand() {
        this.hand = new ArrayList<Card>();
    }

    /**
     * Draws n cards from the deck into d
     * @param n number of cards to draw
     * @param d deck to draw from
     * @throws com.chairbender.yomi.api.card.exception.OutOfCardsException if not enough cards left in deck to draw.
     */
    public void drawFrom(int n, Deck d) throws OutOfCardsException {

        this.hand.addAll(d.drawCards(n));
    }

    @Override
    public String toString() {
        String result = "";

        for (Card card : hand) {
            result += card.toString() + "\n";
        }

        return result;
    }

    /**
     * removes the card from the hand
     * @param toDiscard card to remove
     */
    public void removeCard(Card toDiscard) {
        hand.remove(toDiscard);
    }

    /**
     *
     * @return a list of the cards. do not modify
     */
    public List<Card> getCards() {
        return hand;
    }

    /**
     *
     * @param rank rank to get
     * @return all cards of the given rank in the hand
     */
    public List<Card> getAllCardsOfRank(PokerRank rank) {
        List<Card> result = new ArrayList<Card>();
        for (Card card : hand) {
            if (card.getValue().getRank().equals(rank)) {
                result.add(card);
            }
        }
        return result;
    }

    /**
     *
     * @return A list containing the aces in the hand. empty if none
     */
    public List<Card> getAces() {
       return getAllCardsOfRank(PokerRank.ACE);

    }

    /**
     *
     * @param playedCard card to add to the player's hand
     */
    public void addCard(Card playedCard) {
        hand.add(playedCard);
    }

    /**
     *
     * @return all jokers in the hand. Empty list if none
     */
    public List<Card> getJokers() {

        return getAllCardsOfRank(PokerRank.JOKER);
    }


    /**
     *
     * @return all the non-joker cards in the player's hand
     */
    public List<Card> getNonJokers() {

        List<Card> result = new ArrayList<Card>();
        for (Card card : hand) {
            if (!card.getValue().getRank().equals(PokerRank.JOKER)) {
                result.add(card);
            }
        }
        return result;
    }


    /**
     *
     * @param n number of cards in the kind
     * @return all ways of making n of a kind in the current hand (using unique cards). for example, if
     * n is 2, would return all possible pairs in the hand
     */
    /*TODO: remove if not needed public List<Set<Card>> getNOfAKinds(int n) {
        List<Set<Card>> finalResult = new ArrayList<Set<Card>>();
        for (PokerRank rank : PokerRank.getPossibleValues()) {
            List<Card> cardsOfRank = new ArrayList<Card>();
            for (Card card : hand) {
                if (card.getValue().getRank().equals(rank)) {
                    cardsOfRank.add(card);
                }
            }
            finalResult.addAll(ActionPermutationUtil.getSubsets(cardsOfRank,n));
        }
        return finalResult;
    }*/

    /**
     * remove all cards from the hand (they don't go anywhere, they just vanish)
     */
    public void clear() {
        hand.clear();
    }
}
