package com.chairbender.yomi.ui;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.HorizontalGroup;
import com.badlogic.gdx.scenes.scene2d.utils.Align;
import com.badlogic.gdx.scenes.scene2d.utils.DragListener;
import com.badlogic.gdx.utils.SnapshotArray;
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

    private Group invisibleCard;

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
        //click events for the card
        toAdd.getListeners().clear();
        toAdd.addListener(new DragListener() {
            public boolean dragged = false;
            public boolean hasRotated = false;
            public int returnIndex = -1;
            public float offsetX;
            public float offsetY;

            @Override
            public void dragStart(InputEvent event, float x, float y, int pointer) {
                Vector2 coords = toAdd.localToParentCoordinates(new Vector2(x,y));
                offsetX = coords.x - toAdd.getX();
                offsetY = coords.y - toAdd.getY();
                if (getTouchDownY() > y) {
                    if (!hasRotated) {
                        toAdd.rotate();
                        hasRotated = true;
                    }

                }
            }

            @Override
            public void dragStop(InputEvent event, float x, float y, int pointer) {
                hasRotated = false;
                if (dragged) {
                    dragged = false;
                    stopMakingSpace();
                    addCard(toAdd,returnIndex);
                }
            }



            @Override
            public void drag(InputEvent event, float x, float y, int pointer) {
                //Only start dragging the card if we are not dragging down or rotated
                if ((dragged || getTouchDownY() <= y) && !hasRotated) {
                    if (dragged == false) {
                        dragged = true;
                        returnIndex = cards.getChildren().indexOf(toAdd,true);
                        removeCard(toAdd);
                        addActor(toAdd);
                    }
                    Vector2 coords = toAdd.localToParentCoordinates(new Vector2(x,y));
                    toAdd.setPosition(coords.x - offsetX, coords.y - offsetY);

                    //check for making space in the hand
                    boolean stillNeedsSpace = false;
                    for (int i = 0; i < cards.getChildren().size; i++) {
                        Actor card = cards.getChildren().get(i);
                        Rectangle cardBounds = new Rectangle(card.getX(), card.getY(), card.getWidth(), card.getHeight());
                        Rectangle thisBounds = new Rectangle(toAdd.getX(), toAdd.getY(), toAdd.getWidth(), toAdd.getHeight());
                        if (cardBounds.overlaps(thisBounds)) {
                            stillNeedsSpace = true;
                            if (card != invisibleCard) {
                                //figure out which side. If the center of the dragged card is to the right
                                //of the center of the collided with card, make space to the right
                                if ((thisBounds.getX() + thisBounds.getWidth() / 2) >
                                        (cardBounds.getX() + cardBounds.getWidth() / 2)) {
                                    returnIndex = i + 1;
                                    makeSpace(i + 1);
                                } else {
                                    returnIndex = i;
                                    makeSpace(i);
                                }
                            }
                            break;
                        }
                    }
                    if (!stillNeedsSpace) {
                        stopMakingSpace();
                    }
                }
            }
        });


        //save the old positions of the cards
        List<Vector2> oldPositions = getPositions(cards.getChildren());
        oldPositions.add(index,new Vector2(toAdd.getX(),toAdd.getY()));

        //Determine what the new positions would be
        //by adding them to a horizontal group and checking their positions
        HorizontalGroup newGroup = new HorizontalGroup().space(calculateSpacing(cards.getChildren().size + 1,toAdd.getWidth()));
        newGroup.align(Align.bottom);

        cards.addActorAt(index,toAdd);

        while (cards.getChildren().size > 0) {
            newGroup.addActor(cards.getChildren().get(0));
        }
        newGroup.layout();



        List<Vector2> newPositions = getPositions(newGroup.getChildren());
        //calculate what is needed to center the cards
        float centerOffset = UIConstants.WORLD_WIDTH / 2 - newGroup.getPrefWidth() / 2;
        //remove them from the horizontal group and add them back to the normal group so it doesn't try to move them around
        while (newGroup.getChildren().size > 0) {
            cards.addActor(newGroup.getChildren().get(0));
        }

        interpolateActorPositions(cards.getChildren(), oldPositions, newPositions, centerOffset,Interpolation.pow2,0.5f);


    }

    /**
     *
     * @param actors actors to move
     * @param oldPositions starting positions of the actors
     * @param newPositions ending positions of the actors
     * @param xOffset x offset for the ending positions of the actors (for centering)
     */
    private void interpolateActorPositions(SnapshotArray<Actor> actors,
                                           List<Vector2> oldPositions,
                                           List<Vector2> newPositions, float xOffset,Interpolation interp, float speed) {
        //interpolate between them for each card.
        for (int i = 0; i < newPositions.size(); i++) {
            Actor toMove = actors.get(i);
            Vector2 oldPosition = oldPositions.get(i);
            Vector2 newPosition = newPositions.get(i);
            toMove.setPosition(oldPosition.x, oldPosition.y);
            //this action overrides all others
            toMove.getActions().clear();
            toMove.addAction(Actions.moveTo(newPosition.x + xOffset, MARGIN, speed, interp));
        }
    }


    private void removeCard(Actor toRemove) {
        int removeIndex = cards.getChildren().indexOf(toRemove, true);

        //save the old positions of the cards
        SnapshotArray<Actor> currentCards = cards.getChildren();
        currentCards.removeIndex(removeIndex);
        List<Vector2> oldPositions = getPositions(currentCards);


        //Determine what the new positions would be
        //by adding them to a horizontal group and checking their positions
        HorizontalGroup newGroup = new HorizontalGroup().space(calculateSpacing(cards.getChildren().size - 1,toRemove.getWidth()));
        newGroup.align(Align.bottom);
        cards.removeActor(toRemove);
        while (cards.getChildren().size > 0) {
            newGroup.addActor(cards.getChildren().get(0));
        }
        newGroup.layout();
        List<Vector2> newPositions = getPositions(newGroup.getChildren());

        //calculate what is needed to center the cards
        float centerOffset = UIConstants.WORLD_WIDTH / 2 - newGroup.getPrefWidth() / 2;
        //remove them from the horizontal group and add them back to the normal group so it doesn't try to move them around
        while (newGroup.getChildren().size > 0) {
            cards.addActor(newGroup.getChildren().get(0));
        }

        interpolateActorPositions(cards.getChildren(), oldPositions, newPositions, centerOffset, Interpolation.pow2,0.5f);
    }

    /**
     * makes space for a card (adds an invisible card)
     * @param index index to make space for a card at
     */
    private void makeSpace(int index) {
        if (cards.getChildren().size > 0) {
            stopMakingSpace();
            invisibleCard = new Group();
            invisibleCard.setBounds(0,0,cards.getChildren().get(0).getWidth(),cards.getChildren().get(0).getHeight());

            //save the old positions of the cards
            List<Vector2> oldPositions = getPositions(cards.getChildren());
            oldPositions.add(index,new Vector2(invisibleCard.getX(),invisibleCard.getY()));

            //Determine what the new positions would be
            //by adding them to a horizontal group and checking their positions
            HorizontalGroup newGroup = new HorizontalGroup().space(calculateSpacing(cards.getChildren().size + 1,invisibleCard.getWidth()));
            newGroup.align(Align.bottom);

            cards.addActorAt(index,invisibleCard);

            while (cards.getChildren().size > 0) {
                newGroup.addActor(cards.getChildren().get(0));
            }
            newGroup.layout();



            List<Vector2> newPositions = getPositions(newGroup.getChildren());
            //calculate what is needed to center the cards
            float centerOffset = UIConstants.WORLD_WIDTH / 2 - newGroup.getPrefWidth() / 2;
            //remove them from the horizontal group and add them back to the normal group so it doesn't try to move them around
            while (newGroup.getChildren().size > 0) {
                cards.addActor(newGroup.getChildren().get(0));
            }

            //interpolate all but the invisible card
            cards.getChildren().get(index).setPosition(newPositions.get(index).x,newPositions.get(index).y);
            cards.removeActor(invisibleCard);
            oldPositions.remove(index);
            newPositions.remove(index);

            interpolateActorPositions(cards.getChildren(), oldPositions, newPositions, centerOffset, Interpolation.linear,0.2f);
            cards.addActor(invisibleCard);
        }
    }

    private void stopMakingSpace() {
        if (invisibleCard != null && cards.getChildren().contains(invisibleCard,true)) {
            removeCard(invisibleCard);
        }
        invisibleCard = null;
    }

    /**
     *
     * @return the positions of the actors in the list
     * @param actors
     */
    private List<Vector2> getPositions(SnapshotArray<Actor> actors) {
        List<Vector2> oldPositions = new ArrayList<Vector2>();
        for (Actor actor : actors) {
            oldPositions.add(new Vector2(actor.getX(),actor.getY()));
        }
        return oldPositions;
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
