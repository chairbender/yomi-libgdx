package com.chairbender.yomi.ui;

import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.HorizontalGroup;
import com.badlogic.gdx.scenes.scene2d.utils.Align;
import com.chairbender.yomi.api.character.YomiCharacter;
import com.chairbender.yomi.api.gamevent.GameEventNotifier;

import java.util.ArrayList;
import java.util.List;

/**
 * The player's hand area.
 */
public class HandGroup extends GameEventListeningGroup {

    private static final float MARGIN = 25;

    private Group cards;

    public HandGroup(GameEventNotifier notifier) {
        super(notifier);

        cards = new Group();
        cards.setBounds(0,0,UIConstants.WORLD_WIDTH,UIConstants.WORLD_HEIGHT/2);

        int x = 0;
        for (Actor card : getChildren()) {
            card.setPosition(x,MARGIN);
            card.setOrigin(0,0);
            card.setScale(3f);
            x += card.getWidth();
        }
        addActor(cards);
    }

    /**
     * animate the card being added to the hand, and add it to the hand.
     * Removes the card from its current parent and adds it to this group.
     * Adds it to the right of the current hand.
     * @param toAdd cardgroup to add to the hand
     * @param index index to add the card at (left is 0)
     */
    public void addCard(CardGroup toAdd, int index) {
        toAdd.setOrigin(0,0);
        toAdd.setScale(3f);
        //save the old positions of the cards
        List<Vector2> oldPositions = new ArrayList<Vector2>();
        for (Actor actor : cards.getChildren()) {
            oldPositions.add(new Vector2(actor.getX(),actor.getY()));
        }

        //Determine what the new positions would be
        //by adding them to a horizontal group and checking their positions
        HorizontalGroup newGroup = new HorizontalGroup().space(30);
        newGroup.align(Align.bottom);
        oldPositions.add(index,new Vector2(toAdd.getX(),toAdd.getY()));
        cards.addActorAt(index,toAdd);

        while (cards.getChildren().size > 0) {
            newGroup.addActor(cards.getChildren().get(0));
        }
        newGroup.layout();



        List<Vector2> newPositions = new ArrayList<Vector2>();
        for (Actor card : newGroup.getChildren()) {
            newPositions.add(new Vector2(card.getX(),card.getY()));
        }
        //remove them from the horizontal group and add them back to the normal group so it doesn't try to move them around
        while (newGroup.getChildren().size > 0) {
            cards.addActor(newGroup.getChildren().get(0));
        }


        //then interpolate between them for each card.
        //They will be in a placeholder group
        for (int i = 0; i < newPositions.size(); i++) {
            CardGroup toMove = (CardGroup) cards.getChildren().get(i);
            Vector2 oldPosition = oldPositions.get(i);
            Vector2 newPosition = newPositions.get(i);
            toMove.setPosition(oldPosition.x, oldPosition.y);
            toMove.addAction(Actions.moveTo(newPosition.x,newPosition.y,0.5f,Interpolation.pow2));

        }
    }
}
