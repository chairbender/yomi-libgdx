package com.chairbender.yomi.ui;

import com.badlogic.gdx.scenes.scene2d.Group;

/**
 * The public facing UI info for a player. Includes their name, character, hand count, health, deck,
 * discard, and play area.
 */
public class OpponentPlayAreaGroup extends Group {

    private DeckGroup deck;
    private DiscardGroup discard;


    public OpponentPlayAreaGroup() {

        this.deck = new DeckGroup();
        this.discard = new DiscardGroup();
        setPosition(UIConstants.WORLD_WIDTH - this.deck.getWidth(),UIConstants.WORLD_HEIGHT/2 - 40);

        deck.setPosition(0,discard.getHeight() + 10);

        addActor(deck);
        addActor(discard);
    }

}
