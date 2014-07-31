package com.chairbender.yomi.ui;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.chairbender.yomi.api.character.YomiCharacter;
import com.chairbender.yomi.api.gamestate.model.PlayerField;
import com.chairbender.yomi.api.gamevent.*;

/**
 * Represents a player's current health
 */
public class HealthBarGroup extends GameEventListeningGroup {

    private Label healthIndicator;
    private static final float WIDTH = 350;
    private static final float HEIGHT = 30;
    private float currentHealth = 0;
    private float maxHealth = 0;
    private boolean forPlayer;

    private final HealthBarGroup me = this;

    /**
     *
     * @param notifier game event notifier to use
     * @param forPlayer true if for the player. false if for the opponent
     */
    public HealthBarGroup(GameEventNotifier notifier, final boolean forPlayer) {
        super(notifier);
        this.forPlayer = forPlayer;

        healthIndicator = new Label(currentHealth + " / " + maxHealth,
                new Label.LabelStyle(new BitmapFont(), Color.WHITE));
        healthIndicator.setFontScale(1.5f);
        healthIndicator.setCenterPosition(WIDTH / 2, HEIGHT / 2);

        addActor(healthIndicator);

        //initialize hp when game starts
        notifier.addEventHandler(new EventHandler<GameStartEvent>(GameStartEvent.class) {
            @Override
            public void handle(GameStartEvent event) {
                if (forPlayer) {
                    maxHealth = event.getStartingField().getThisPlayerField().getYomiCharacter().getMaxHealth();
                } else {
                    maxHealth = event.getStartingField().getOpponentField().getYomiCharacter().getMaxHealth();
                }

                currentHealth = maxHealth;
                healthIndicator.setText(currentHealth + " / " + maxHealth);
            }
        });

        //change health when health changed
        notifier.addEventHandler(new EventHandler<HealthChangedEvent>(HealthChangedEvent.class) {
            @Override
            public void handle(HealthChangedEvent event) {
                if (forPlayer) {
                    currentHealth = event.getStartingField().getThisPlayerField().getCurrentHealth();
                } else {
                    currentHealth = event.getStartingField().getOpponentField().getCurrentHealth();
               }
               healthIndicator.setText(currentHealth + " / " + maxHealth);
            }
        });
    }

    ShapeRenderer shapeRenderer = new ShapeRenderer();
    @Override
    public void draw(Batch batch, float parentAlpha) {
        batch.end();
        Gdx.gl.glEnable(Gdx.gl.GL_BLEND);

        //draw health bar background
        shapeRenderer.setTransformMatrix(batch.getTransformMatrix());
        shapeRenderer.setProjectionMatrix(batch.getProjectionMatrix());
        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
        shapeRenderer.setColor(Color.BLACK);
        shapeRenderer.rect(getX(), getY(), WIDTH, HEIGHT);

        //draw health bar
        shapeRenderer.setColor(Color.RED);
        if (forPlayer) {
            shapeRenderer.rect(getX(), getY(), WIDTH * (currentHealth / maxHealth), HEIGHT);
        } else {
            shapeRenderer.rect(getX() + (WIDTH * (1 - (currentHealth / maxHealth))), getY(), WIDTH * (currentHealth / maxHealth), HEIGHT);
        }


        shapeRenderer.end();
        batch.begin();
        super.draw(batch, parentAlpha);
    }

}
