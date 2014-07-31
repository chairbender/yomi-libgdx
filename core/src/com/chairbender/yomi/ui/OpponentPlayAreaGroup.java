package com.chairbender.yomi.ui;

import com.badlogic.gdx.scenes.scene2d.Group;
import com.chairbender.yomi.api.gamevent.GameEventNotifier;

/**
 * The public facing UI info for a player. Includes their name, character, hand count, health, deck,
 * discard, and play area.
 */
public class OpponentPlayAreaGroup extends GameEventListeningGroup {

    private DeckGroup deck;
    private DiscardGroup discard;
    private HealthBarGroup healthBar;


    public OpponentPlayAreaGroup(GameEventNotifier notifier) {
        super(notifier);

        this.deck = new DeckGroup();
        this.discard = new DiscardGroup();
        setPosition(UIConstants.WORLD_WIDTH - this.deck.getWidth(),UIConstants.WORLD_HEIGHT/2 - 40);

        deck.setPosition(0,discard.getHeight() + 10);

        healthBar = new HealthBarGroup(notifier,false);

        healthBar.setPosition(-250,UIConstants.WORLD_HEIGHT - getY() - 50);

        addActor(deck);
        addActor(discard);
        addActor(healthBar);
    }

}
