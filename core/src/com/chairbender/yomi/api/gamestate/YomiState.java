package com.chairbender.yomi.api.gamestate;

import com.chairbender.yomi.api.gamestate.model.PlayField;
import com.chairbender.yomi.api.gamevent.GameEventNotifier;

/**
 * By implementing this, the implementing class simply indicates that
 * it is a state with action methods. It's up to the using class to know what
 * methods are actions. All "action" methods are defined as methods which return
 * a class of type action, which, when executed, returns a new YomiState representing
 * the new state. YomiStates should be completely self-contained - that is, one
 * could feasibly start a game from any point by just initializing a new YomiState.
 */
public abstract class YomiState {
    private GameEventNotifier eventNotifier;
    private PlayField currentPlayField;

    /**
     *
     * @param notifier notifier which states should use to notify others of events that occur
     * @param field the current playfield for this state
     */
    public YomiState(GameEventNotifier notifier,PlayField field) {
        this.eventNotifier = notifier;
        this. currentPlayField = field;
    }
}
