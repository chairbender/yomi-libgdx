package com.chairbender.yomi.ui;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;

/**
 * UI Representation of a player's deck
 */
public class DeckGroup extends Group {

    private static final Texture cardBackTexture = new Texture("cards/back.png");
    private Label numCardsLabel;
    private static final float MARGIN = 20;

    public DeckGroup() {
        Image cardBack = new Image(cardBackTexture);
        addActor(cardBack);

        numCardsLabel = new Label("47", new Label.LabelStyle(new BitmapFont(), Color.WHITE));
        numCardsLabel.setFontScale(1.5f);
        addActor(numCardsLabel);

        cardBack.setPosition(MARGIN,MARGIN+10);
        numCardsLabel.setCenterPosition((cardBack.getX() + cardBack.getWidth())/2,15);

        this.setWidth(cardBack.getWidth() + MARGIN * 2);
        this.setHeight(cardBack.getHeight() + MARGIN*2);
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
        shapeRenderer.end();
        batch.begin();
        super.draw(batch, parentAlpha);
    }
}
