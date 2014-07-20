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

        addActor(cards);
    }

    /**
     * animate the card being added to the hand, and add it to the hand.
     * Removes the card from its current parent and adds it to this group.
     * Adds it to the right of the current hand.
     * @param toAdd cardgroup to add to the hand
     * @param index index to add the card at (left is 0)
     */
    public void addCard(final CardGroup toAdd, int index) {
        //make it big enough
        toAdd.setOrigin(0,0);
        toAdd.setScale(3f);

        //click events for the card
        toAdd.addListener(new InputListener() {
            private float offsetX, offsetY;
            private boolean dragged = false;
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                Vector2 coords = toAdd.localToParentCoordinates(new Vector2(x,y));
                offsetX = coords.x - toAdd.getX();
                offsetY = coords.y - toAdd.getY();
                return true;
            }
            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                if (dragged) {
                    dragged = false;
                } else {
                    toAdd.rotate();
                }
            }

            @Override
            public void touchDragged(InputEvent event, float x, float y, int pointer) {
                dragged = true;
                Vector2 coords = toAdd.localToParentCoordinates(new Vector2(x,y));
                toAdd.setPosition(coords.x - offsetX, coords.y - offsetY);
            }
        });

        //save the old positions of the cards
        List<Vector2> oldPositions = new ArrayList<Vector2>();
        for (Actor actor : cards.getChildren()) {
            oldPositions.add(new Vector2(actor.getX(),actor.getY()));
        }

        //Determine what the new positions would be
        //by adding them to a horizontal group and checking their positions
        HorizontalGroup newGroup = new HorizontalGroup().space(calculateSpacing(cards.getChildren().size + 1,toAdd.getWidth()));
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
        //calculate what is needed to center the cards
        float centerOffset = UIConstants.WORLD_WIDTH / 2 - newGroup.getPrefWidth() / 2;
        //remove them from the horizontal group and add them back to the normal group so it doesn't try to move them around
        while (newGroup.getChildren().size > 0) {
            cards.addActor(newGroup.getChildren().get(0));
        }


        //then interpolate between them for each card.
        for (int i = 0; i < newPositions.size(); i++) {
            CardGroup toMove = (CardGroup) cards.getChildren().get(i);
            Vector2 oldPosition = oldPositions.get(i);
            Vector2 newPosition = newPositions.get(i);
            toMove.setPosition(oldPosition.x, oldPosition.y);
            toMove.addAction(Actions.moveTo(newPosition.x + centerOffset,MARGIN,0.5f,Interpolation.pow2));
        }
    }

    /**
     *
     * @param cardCount number of cards
     * @param cardWidth width of a card
     * @return a float indicating the spacing required to fit all the cards in
     */
    private float calculateSpacing(int cardCount, float cardWidth) {

        float spacing = (UIConstants.WORLD_WIDTH - (cardCount * cardWidth) )/
                (cardCount - 1);
        if (spacing > 0) {
            spacing = 5;
        }

        return spacing;
    }
}
