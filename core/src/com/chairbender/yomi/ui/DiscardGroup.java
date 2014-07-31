package com.chairbender.yomi.ui;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
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

    private Group cards;

    //tracks other discard groups so multiple groups aren't expanded
    private static Set<DiscardGroup> discardGroups;

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
                CardGroup newGroup = new CardGroup(card);
                //when clicked, zoom in just like with the hand group
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
