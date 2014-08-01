package com.chairbender.yomi.ui;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.*;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.HorizontalGroup;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.utils.Align;
import com.chairbender.yomi.api.card.Card;
import com.chairbender.yomi.api.card.Discard;
import com.chairbender.yomi.api.character.YomiCharacter;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * UI Representation of a player's discard pile. When clicked, it shows all the cards in the discard pile
 */
public class DiscardGroup extends Group {

    private static final Texture cardBackTexture = new Texture("cards/back.png");
    private Label numCardsLabel;
    private static final float MARGIN = 20;
    private boolean isExpanded = false;
    private static final float EXPANDED_HEIGHT = 320;
    private static final float EXPANDED_WIDTH = UIConstants.WORLD_WIDTH - 240;
    private final boolean isLeft;

    //for the zoom menu
    private CardGroup zoomCard;
    private int zoomReturnIndex = 0;
    private Group invisibleCard;
    private Group zoomGroup;
    private Image selectButton;
    private Image cancelButton;
    private Image nextCardImage;
    private Image previousCardImage;
    private Image rotateImage;



    private final Texture rotateTexture = new Texture("icons/rotate.png");
    private final Texture arrowTexture = new Texture("icons/arrow.png");
    private final Texture selectTexture = new Texture("icons/select.png");
    private final Texture cancelTexture = new Texture("icons/cancel.png");

    private Group cards;

    //tracks other discard groups so multiple groups aren't expanded
    private static Set<DiscardGroup> discardGroups;

    private Discard lastDiscard;

    /**
     *
     * @param isLeft whether the discard pile is on the left or right side of the screen
     */
    public DiscardGroup(boolean isLeft) {
        this.isLeft = isLeft;
        if (discardGroups == null) {
            discardGroups = new HashSet<DiscardGroup>();
        }
        discardGroups.add(this);

        Image cardBack = new Image(cardBackTexture);
        cardBack.setVisible(false); //don't show anything until cards are in the pile but use this for sizing
        addActor(cardBack);

        numCardsLabel = new Label("0", new Label.LabelStyle(new BitmapFont(), Color.WHITE));
        numCardsLabel.setFontScale(1.5f);
        addActor(numCardsLabel);

        cardBack.setPosition(MARGIN,MARGIN+10);
        numCardsLabel.setCenterPosition((cardBack.getX() + cardBack.getWidth())/2,15);

        this.setWidth(cardBack.getWidth() + MARGIN * 2);
        this.setHeight(cardBack.getHeight() + MARGIN*2);
        this.setBounds(getX(),getY(),getWidth(),getHeight());

        this.addListener(new InputListener() {
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                //only count when they click the small discard display
                if (x >= getX() && x <= getX() + getWidth() &&
                        y >= getY() && y <= getY() + getHeight()) {
                    if (!isExpanded) {
                        for (DiscardGroup otherGroup : discardGroups) {
                            otherGroup.contract();
                        }
                        expand();
                    } else {
                        for (DiscardGroup otherGroup : discardGroups) {
                            otherGroup.contract();
                        }
                    }
                }

                return true;
            }
        });

        cards = new Group();
        cards.setVisible(false);



        this.addActor(cards);
        Discard test = new Discard();
        test.addCard(YomiCharacter.GRAVE.allCards().get(40));
        test.addCard(YomiCharacter.GRAVE.allCards().get(41));
        test.addCard(YomiCharacter.GRAVE.allCards().get(43));
        test.addCard(YomiCharacter.GRAVE.allCards().get(42));
        test.addCard(YomiCharacter.GRAVE.allCards().get(40));
        test.addCard(YomiCharacter.GRAVE.allCards().get(41));
        test.addCard(YomiCharacter.GRAVE.allCards().get(43));
        test.addCard(YomiCharacter.GRAVE.allCards().get(42));
        test.addCard(YomiCharacter.GRAVE.allCards().get(40));
        test.addCard(YomiCharacter.GRAVE.allCards().get(41));
        test.addCard(YomiCharacter.GRAVE.allCards().get(43));
        test.addCard(YomiCharacter.GRAVE.allCards().get(42));
        update(test);

    }

    /**
     *
     * @param discardPile discard pile to use to update the list of cards
     */
    private void update(Discard discardPile) {
        lastDiscard = discardPile;
        cards.clear();
        if (discardPile.getCards().size() > 0) {
            List<Card> discards = discardPile.getCards();

            //to determine how to lay out the cards, we're going to create card groups for each card,
            //add those card groups to a horizontal group and calculate a spacing so that they all fit within the expanded
            // width, center that horizontal group, then save the positions of the cards and add them to the cards group.
            // We don't keep them in the horizontal group because we want to be able to control the z-index of the cards.

            HorizontalGroup spacingGroup = new HorizontalGroup();
            CardGroup testGroup = new CardGroup(discards.get(0));
            float space = Math.min(10, (EXPANDED_WIDTH - (testGroup.getWidth() * discards.size())) / (discards.size() - 1));
            spacingGroup = spacingGroup.space(space);
            spacingGroup.align(Align.bottom);
            for (Card card : discards) {
                final CardGroup newGroup = new CardGroup(card);
                //when clicked, zoom in just like with the hand group
                newGroup.addListener(new InputListener() {
                    @Override
                    public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                        zoomCard(newGroup);
                        return true;
                    }
                });
                spacingGroup.addActor(newGroup);
            }
            spacingGroup.layout();
            float spaceGroupWidth = spacingGroup.getPrefWidth();

            //now save the positions
            List<Vector2> newPositions = new ArrayList<Vector2>();
            for (Actor actor : spacingGroup.getChildren()) {
                newPositions.add(new Vector2(actor.getX(), actor.getY()));
            }

            //now add the cards back to the cards group
            for (int i = 0; i < discards.size(); i++) {
                CardGroup toAdd = (CardGroup) spacingGroup.getChildren().get(0);
                Vector2 position = newPositions.get(i);
                cards.addActor(toAdd);
                toAdd.setPosition(position.x, position.y);
            }
            if (isLeft) {
                cards.setPosition(getWidth() + (EXPANDED_WIDTH - spaceGroupWidth) / 2, getY() + 10);
            } else {
                cards.setPosition(-EXPANDED_WIDTH + ((EXPANDED_WIDTH - spaceGroupWidth) / 2) , getY() + 10);
            }
        }
    }

    /**
     * brings the card up to the front and center, leaving a space where it belongs in the discard view. Nothing
     * other than the card and its controls can be clicked during a zoom. Tapping the card unzooms it.
     * There also is a cancel button and arrows to see the next and previous cards in the hand.
     * @param toAdd card to zoom in on
     */
    private void zoomCard(CardGroup toAdd) {
        zoomCard = toAdd;
        //remove toAdd from the hand and add it to the root
        zoomReturnIndex = cards.getChildren().indexOf(toAdd,true);
        zoomCard.remove();
        addActor(zoomCard);
        makeSpace(zoomReturnIndex);

        zoomCard.getActions().clear();
        //blow the card up
        zoomCard.addAction(Actions.scaleTo(3.0f, 3.0f, 0.2f, Interpolation.pow2));
        //move it
        zoomCard.addAction(Actions.moveTo(600,100,0.2f,Interpolation.pow2));

        //make only the zoom elements touchable
        for (Actor actor : getChildren()) {
            actor.setTouchable(Touchable.disabled);
        }
        zoomGroup.setTouchable(Touchable.enabled);
        zoomGroup.setVisible(true);
        updateZoomControls();
    }

    /**
     * unzooms the currently zoomed card
     */
    private void unzoom() {
        stopMakingSpace();
        zoomCard.setTouchable(Touchable.enabled);
        zoomCard.addAction(Actions.scaleTo(1.0f,1.0f,0.2f,Interpolation.pow2));
        //TODO: interpolate the card
        update(lastDiscard);
        zoomCard = null;
        for (Actor actor : getChildren()) {
            actor.setTouchable(Touchable.enabled);
        }
        for (Actor actor : cards.getChildren()) {
            actor.setTouchable(Touchable.enabled);
        }
        zoomGroup.setTouchable(Touchable.disabled);
        zoomGroup.setVisible(false);
    }

    /**
     * decides whether to hide the arrows based on the currently zoomed card index
     */
    private void updateZoomControls() {
        if (zoomReturnIndex == cards.getChildren().size - 1) {
            nextCardImage.setTouchable(Touchable.disabled);
            nextCardImage.setVisible(false);
            previousCardImage.setTouchable(Touchable.enabled);
            previousCardImage.setVisible(true);
        } else if (zoomReturnIndex == 0) {
            previousCardImage.setTouchable(Touchable.disabled);
            previousCardImage.setVisible(false);
            nextCardImage.setTouchable(Touchable.enabled);
            nextCardImage.setVisible(true);
        } else {
            previousCardImage.setTouchable(Touchable.enabled);
            previousCardImage.setVisible(true);
            nextCardImage.setTouchable(Touchable.enabled);
            nextCardImage.setVisible(true);
        }
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



    private void expand() {
        isExpanded = true;
        cards.setVisible(true);
    }

    private void contract() {
        isExpanded = false;
        cards.setVisible(false);
    }

    ShapeRenderer shapeRenderer = new ShapeRenderer();
    @Override
    public void draw(Batch batch, float parentAlpha) {
        batch.end();
        Gdx.gl.glEnable(Gdx.gl.GL_BLEND);
        shapeRenderer.setTransformMatrix(batch.getTransformMatrix());
        shapeRenderer.setProjectionMatrix(batch.getProjectionMatrix());
        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
        shapeRenderer.setColor(0.2f, 0.2f, 0.2f, 0.4f);
        shapeRenderer.rect(getX(), getY(), getWidth(), getHeight());

        //if expanded, cover the whole play area with the black overlay
        if (isExpanded) {
            //expand right if on the left
            if (isLeft) {
                shapeRenderer.rect(getX() + getWidth(),getY(),EXPANDED_WIDTH,EXPANDED_HEIGHT);
            } else {
                //expand left
                shapeRenderer.rect(-EXPANDED_WIDTH,getY(),EXPANDED_WIDTH,EXPANDED_HEIGHT);
            }

        }

        shapeRenderer.end();
        batch.begin();
        super.draw(batch, parentAlpha);
    }
}
