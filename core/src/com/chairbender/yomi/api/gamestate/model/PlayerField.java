package com.chairbender.yomi.api.gamestate.model;

import com.chairbender.yomi.api.card.Deck;
import com.chairbender.yomi.api.card.Discard;
import com.chairbender.yomi.api.card.Hand;
import com.chairbender.yomi.api.card.PlayArea;
import com.chairbender.yomi.api.character.YomiCharacter;

/**
 * Class which tracks an individual player's
 * deck, discard, hand, play area, health, and character card.
 */
public class PlayerField {

    private Deck deck;
    private Discard discard;
    private Hand hand;
    private int currentHealth;
    private YomiCharacter character;
    private PlayArea playArea;

    private PlayerField(Deck deck, Discard discard, Hand hand, int currentHealth, YomiCharacter character, PlayArea playArea) {
        this.deck = deck;
        this.discard = discard;
        this.hand = hand;
        this.currentHealth = currentHealth;
        this.character = character;
        this.playArea = playArea;
    }



    /**
     *
     * @param chosenCharacter character the player will play as
     * @return a new player field with a full deck and empty hand,
     * discard, play area, and full health, and the character card set to be chosenCharacter
     */
    public static PlayerField getFieldForNewGame(YomiCharacter chosenCharacter) {
        Deck startingDeck = new Deck(chosenCharacter);

        return new PlayerField(startingDeck,new Discard(),new Hand(),chosenCharacter.getMaxHealth(),chosenCharacter,new PlayArea());


    }

    public YomiCharacter getYomiCharacter() {
        return character;
    }
}
