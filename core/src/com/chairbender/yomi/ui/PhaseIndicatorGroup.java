package com.chairbender.yomi.ui;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Group;

/**
 * The phase indicator UI element (highlights the current phase)
 */
public class PhaseIndicatorGroup extends Group{
    //constants
    private static final float X = 0;
    private static final float Y = UIConstants.WORLD_HEIGHT / 2;
    private static final float WIDTH = UIConstants.WORLD_WIDTH;
    private static final float HEIGHT = 35;

    //the phases
    private static final float PHASE_WIDTH = 180;
    private static final float BUFFER = 20;
    private static final float PHASEX = 220;
    private PhaseActor drawPhase = new PhaseActor("DRAW",PHASEX,PHASE_WIDTH,BUFFER);
    private PhaseActor combatPhase = new PhaseActor("COMBAT",PHASEX + PHASE_WIDTH,PHASE_WIDTH,BUFFER);
    private PhaseActor comboPhase = new PhaseActor("COMBO",PHASEX + PHASE_WIDTH*2,PHASE_WIDTH,BUFFER);
    private PhaseActor powerupPhase = new PhaseActor("POWERUP",PHASEX + PHASE_WIDTH*3,PHASE_WIDTH,BUFFER);
    private PhaseActor cleanupPhase = new PhaseActor("CLEANUP",PHASEX + PHASE_WIDTH*4,PHASE_WIDTH,BUFFER);

    public PhaseIndicatorGroup() {

        //set up my width and height
        this.setPosition(X,Y);
        this.setWidth(WIDTH);
        this.setHeight(HEIGHT);

        //create the status bar line
        this.addActor(new BackgroundActor());

        //create the words that indicate the phase
        this.addActor(drawPhase);
        this.addActor(combatPhase);
        this.addActor(comboPhase);
        this.addActor(powerupPhase);
        this.addActor(cleanupPhase);
    }
}

/**
 * The background behind this. TODO: Change appearance depending on phase
 */
class BackgroundActor extends Actor {

    @Override
    public void draw(Batch batch, float alpha) {
        batch.end();
        ShapeRenderer shapeRenderer = new ShapeRenderer();
        shapeRenderer.setTransformMatrix(batch.getTransformMatrix());
        shapeRenderer.setProjectionMatrix(batch.getProjectionMatrix());
        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
        shapeRenderer.setColor(Color.RED);
        shapeRenderer.rect(0,0,getParent().getWidth(),getParent().getHeight());
        shapeRenderer.end();
        batch.begin();
    }
}

/**
 * A phase UI element
 */
class PhaseActor extends Group {


    private String phaseName;
    private float x;
    private float width, buffer;

    private static final float TEXTPADDING = 8;


    /**
     *
     * @param phaseName string to use for the phase
     * @param x where the phase's x position should be
     * @param width width of this phaseactor
     * @param buffer width of the line to the side of the phase text box
     */
    public PhaseActor(String phaseName, float x, float width, float buffer) {
        this.phaseName = phaseName;
        this.x = x;
        this.width = width;
        this.buffer = buffer;
    }

    @Override
    public void draw(Batch batch, float alpha) {
        batch.end();
        ShapeRenderer shapeRenderer = new ShapeRenderer();
        shapeRenderer.setTransformMatrix(batch.getTransformMatrix());
        shapeRenderer.setProjectionMatrix(batch.getProjectionMatrix());
        //draw the line that connects this phase to others
        shapeRenderer.begin(ShapeRenderer.ShapeType.Line);
        shapeRenderer.setColor(Color.WHITE);
        shapeRenderer.line(x,getParent().getHeight()/2,x + width,getParent().getHeight()/2);
        shapeRenderer.end();

        //start the rectangle that outlines the word
        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
        shapeRenderer.setColor(Color.WHITE);
        shapeRenderer.rect(x + buffer,0, boxWidth(),getParent().getHeight());
        shapeRenderer.end();

        //Draw the text
        BitmapFont font = new BitmapFont();
        batch.begin();
        BitmapFont.TextBounds bounds = font.getBounds(phaseName);
        //we want to keep the font aspect ratio but center the text in the box horizontally
        float scaleY = (getParent().getHeight() - (TEXTPADDING * 2)) / bounds.height;
        font.setScale(scaleY,scaleY);
        float newWidth = scaleY * bounds.width;
        float centerDiff = newWidth/2 - boxWidth()/2;


        font.setColor(Color.BLACK);
        font.draw(batch,phaseName, buffer + x - centerDiff, getParent().getHeight() - TEXTPADDING);
        batch.end();

        batch.begin();
    }

    /**
     *
     * @return width of just the textbox area
     */
    private float boxWidth() {
        return width - (buffer * 2);
    }
}
