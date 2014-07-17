package com.chairbender.yomi.ui;

import com.badlogic.gdx.scenes.scene2d.Group;
import com.chairbender.yomi.api.gamevent.GameEventNotifier;

/**
 * A group that needs to listen to game events using the notifier
 */
public abstract class GameEventListeningGroup extends Group {

    private final GameEventNotifier eventNotifier;

    public GameEventListeningGroup(GameEventNotifier notifier) {
        this.eventNotifier = notifier;
    }
}
