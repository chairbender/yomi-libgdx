package com.chairbender.yomi.api.gamevent;

import com.chairbender.yomi.api.gamestate.Phase;

/**
 * Created by Kyle on 7/16/2014.
 */
public class PhaseChangeEvent extends GameEvent {
    private Phase phase;

    public PhaseChangeEvent(Phase phase) {
        this.phase = phase;
    }

    public Phase getPhase() {
        return phase;
    }
}
