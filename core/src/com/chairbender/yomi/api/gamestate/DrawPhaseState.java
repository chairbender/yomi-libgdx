package com.chairbender.yomi.api.gamestate;

import com.chairbender.yomi.api.gamestate.model.PlayField;
import com.chairbender.yomi.api.gamevent.GameEventNotifier;

/**
 * The draw phase at the start of the match. Only action
 * is to draw a card.
 */
public class DrawPhaseState extends YomiState {

    /**
     * @param notifier notifier which states should use to notify others of events that occur
     * @param field    the current playfield for this state
     */
    public DrawPhaseState(GameEventNotifier notifier, PlayField field) {
        super(notifier, field);
    }
}
