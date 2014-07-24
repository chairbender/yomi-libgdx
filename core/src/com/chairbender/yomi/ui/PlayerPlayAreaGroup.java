package com.chairbender.yomi.ui;

import com.badlogic.gdx.scenes.scene2d.Group;
import com.chairbender.yomi.api.character.YomiCharacter;
import com.chairbender.yomi.api.gamestate.model.PlayField;
import com.chairbender.yomi.api.gamestate.model.PlayerField;

/**
 * The public facing UI info for a player. Includes their name, character, hand count, health, deck,
 * discard, and play area.
 */
public class PlayerPlayAreaGroup extends Group {

    private DeckGroup deck;
    private DiscardGroup discard;
    private HealthBarGroup healthBar;


    public PlayerPlayAreaGroup() {
        setPosition(0,UIConstants.WORLD_HEIGHT/2 - 40);
        this.deck = new DeckGroup();
        this.discard = new DiscardGroup();

        deck.setPosition(0,discard.getHeight() + 10);

        //TODO: unhardcode when needed
        healthBar = new HealthBarGroup(90);

        healthBar.setPosition(0,UIConstants.WORLD_HEIGHT - getY() - 50);

        addActor(deck);
        addActor(discard);
        addActor(healthBar);
    }

}
