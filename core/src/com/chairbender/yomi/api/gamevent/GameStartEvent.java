package com.chairbender.yomi.api.gamevent;

import com.chairbender.yomi.api.gamestate.model.PlayField;

/**
 * Event that occurs when the game starts
 */
public class GameStartEvent extends EntirePlayFieldGameEvent {
    /**
     * @param startingField the state of the play area at the start of the game
     */
    public GameStartEvent(PlayField startingField) {
        super(startingField);
    }
}
