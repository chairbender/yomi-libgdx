package com.chairbender.yomi.api.gamevent;

import com.chairbender.yomi.api.gamestate.model.PlayField;

/**
 * For all events whose state is just the current PlayField
 */
public abstract class EntirePlayFieldGameEvent extends GameEvent {
    private PlayField startingField;

    /**
     *
     * @param startingField the state of the play area at the start of the game
     */
    public EntirePlayFieldGameEvent(PlayField startingField) {
        this.startingField = startingField;
    }

    public PlayField getStartingField() {
        return startingField;
    }
}
