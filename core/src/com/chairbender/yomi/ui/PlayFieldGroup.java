package com.chairbender.yomi.ui;

import com.badlogic.gdx.scenes.scene2d.Group;
import com.chairbender.yomi.api.gamestate.model.PlayField;

/**
 * Group that contains the whole play field during a game (for one player).
 * Contains the deck and discard piles of the player on the left, and the
 * opponent's on the right. Contains both player's health bars and hand card counts.
 * Contains this player's hand at the bottom. Contains the status indicator
 * in the top center. Contains the play area above the hand and the phase indicator.
 * Just like the unity app.
 */
public class PlayFieldGroup extends Group {

    private PlayerPlayAreaGroup thisPlayerPlayArea = new PlayerPlayAreaGroup();
    private PlayerPlayAreaGroup opponentPlayArea = new PlayerPlayAreaGroup();
    private PhaseIndicatorGroup phaseIndicatorGroup = new PhaseIndicatorGroup();
    private HandGroup handGroup = new HandGroup();
    private StatusGroup statusGroup = new StatusGroup();

    public PlayFieldGroup() {
        this.addActor(thisPlayerPlayArea);
        this.addActor(opponentPlayArea);
        this.addActor(phaseIndicatorGroup);
        this.addActor(handGroup);
        this.addActor(statusGroup);
    }

}
