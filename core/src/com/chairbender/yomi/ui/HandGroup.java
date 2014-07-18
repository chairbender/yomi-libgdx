package com.chairbender.yomi.ui;

import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.chairbender.yomi.api.character.YomiCharacter;
import com.chairbender.yomi.api.gamevent.GameEventNotifier;

import java.util.ArrayList;
import java.util.List;

/**
 * The player's hand area.
 */
public class HandGroup extends GameEventListeningGroup {

    private static final float MARGIN = 25;

    public HandGroup(GameEventNotifier notifier) {
        super(notifier);

        positionEverything();
    }

    /**
     * animate the card being added to the hand, and add it to the hand.
     * Removes the card from its current parent and adds it to this group.
     * Adds it to the right of the current hand.
     * @param toAdd cardgroup to add to the hand
     */
    public void addCard(CardGroup toAdd) {
        //TODO: This should be private
        toAdd.remove();
        addActor(toAdd);
        toAdd.addAction(Actions.moveTo((getChildren().size-1)*toAdd.getWidth(),MARGIN,0.5f, Interpolation.pow2));
    }

    private void positionEverything() {
        int x = 0;
        for (Actor card : getChildren()) {
            card.setPosition(x,MARGIN);
            x += card.getWidth();
        }
    }
}
