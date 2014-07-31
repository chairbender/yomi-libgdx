package com.chairbender.yomi.api;

import com.chairbender.yomi.api.character.YomiCharacter;
import com.chairbender.yomi.api.gamestate.DrawPhaseState;
import com.chairbender.yomi.api.gamestate.Phase;
import com.chairbender.yomi.api.gamestate.model.PlayField;
import com.chairbender.yomi.api.gamestate.model.PlayerField;
import com.chairbender.yomi.api.gamevent.EventHandler;
import com.chairbender.yomi.api.gamevent.GameEventNotifier;
import com.chairbender.yomi.api.gamevent.GameStartEvent;
import com.chairbender.yomi.api.gamevent.PhaseChangeEvent;

/**
 * Class which provides the starting point for initiating a game of Yomi
 */
public class Game {

    private GameEventNotifier eventNotifier;
    private YomiCharacter playerCharacter, opponentCharacter;

    public Game(YomiCharacter playerCharacter, YomiCharacter opponentCharacter) {
        this.eventNotifier = new GameEventNotifier();
        this.playerCharacter = playerCharacter;
        this.opponentCharacter = opponentCharacter;
    }

    /**
     *
     * @return the notifier to use to add event handlers listening for this game state
     */
    public GameEventNotifier getEventNotifier() {
        return eventNotifier;
    }

    public DrawPhaseState startGame() {
        //create the play field
        PlayField startingField = PlayField.getForNewGame(playerCharacter,opponentCharacter);

        eventNotifier.notifyEvent(new GameStartEvent(startingField));
        eventNotifier.notifyEvent(new PhaseChangeEvent(Phase.DRAW));


        return new DrawPhaseState(eventNotifier, startingField);
    }
}
