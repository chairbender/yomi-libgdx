package com.chairbender.yomi.ui;

import com.badlogic.gdx.scenes.scene2d.Group;

/**
 * The public facing UI info for a player. Includes their name, character, hand count, health, deck,
 * discard, and play area.
 */
public class PlayerPlayAreaGroup extends Group {

    private DeckGroup deck;
    private DiscardGroup discard;


    public PlayerPlayAreaGroup() {
        setPosition(0,UIConstants.WORLD_HEIGHT/2 - 40);
        this.deck = new DeckGroup();
        this.discard = new DiscardGroup();

        deck.setPosition(0,discard.getHeight() + 10);

        addActor(deck);
        addActor(discard);
    }

}
