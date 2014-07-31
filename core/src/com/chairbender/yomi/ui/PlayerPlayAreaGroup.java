package com.chairbender.yomi.ui;

import com.badlogic.gdx.scenes.scene2d.Group;
import com.chairbender.yomi.api.character.YomiCharacter;
import com.chairbender.yomi.api.gamestate.model.PlayField;
import com.chairbender.yomi.api.gamestate.model.PlayerField;
import com.chairbender.yomi.api.gamevent.GameEventNotifier;

/**
 * The public facing UI info for a player. Includes their name, character, hand count, health, deck,
 * discard, and play area.
 */
public class PlayerPlayAreaGroup extends GameEventListeningGroup {

    private DeckGroup deck;
    private DiscardGroup discard;
    private HealthBarGroup healthBar;


    public PlayerPlayAreaGroup(GameEventNotifier notifier) {
        super(notifier);
        setPosition(0,UIConstants.WORLD_HEIGHT/2 - 40);
        this.deck = new DeckGroup();
        this.discard = new DiscardGroup(true);

        deck.setPosition(0,discard.getHeight() + 10);

        healthBar = new HealthBarGroup(notifier,true);

        healthBar.setPosition(10,UIConstants.WORLD_HEIGHT - getY() - 50);

        addActor(deck);
        addActor(discard);
        addActor(healthBar);
    }

}
