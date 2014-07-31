package com.chairbender.yomi.api.gamevent;

import com.chairbender.yomi.api.gamestate.model.PlayField;

/**
 * Event that occurs when either player's health is changed
 */
public class HealthChangedEvent extends EntirePlayFieldGameEvent {
    /**
     * @param startingField the state of the play area after the health has changed
     */
    public HealthChangedEvent(PlayField startingField) {
        super(startingField);
    }
}
