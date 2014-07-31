package com.chairbender.yomi.ui;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;

import java.util.HashSet;
import java.util.Set;

/**
 * UI Representation of a player's discard pile. When clicked, it shows all the cards in the discard pile
 */
public class DiscardGroup extends Group {

    private static final Texture cardBackTexture = new Texture("cards/back.png");
    private Label numCardsLabel;
    private static final float MARGIN = 20;
    private boolean isExpanded = false;
    private static final float EXPANDED_HEIGHT = 360;

    //tracks other discard groups so multiple groups aren't expanded
    private static Set<DiscardGroup> discardGroups;

    public DiscardGroup() {
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

                return true;
            }
        });
    }

    private void expand() {
        isExpanded = true;
    }

    private void contract() {
        isExpanded = false;
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
            if (localToStageCoordinates(new Vector2(getX(),getY())).x < UIConstants.WORLD_WIDTH / 2) {
                shapeRenderer.rect(getX() + getWidth(),getY(),UIConstants.WORLD_WIDTH - 2 * (getX() + getWidth()),EXPANDED_HEIGHT);
            } else {
                //expand left
                shapeRenderer.rect(-UIConstants.WORLD_WIDTH + 2*(getWidth()),getY(),UIConstants.WORLD_WIDTH - 2*(getWidth()),EXPANDED_HEIGHT);
            }

        }

        shapeRenderer.end();
        batch.begin();
        super.draw(batch, parentAlpha);
    }
}
