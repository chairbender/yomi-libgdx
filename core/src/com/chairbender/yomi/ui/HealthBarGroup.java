package com.chairbender.yomi.ui;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.chairbender.yomi.api.character.YomiCharacter;
import com.chairbender.yomi.api.gamestate.model.PlayerField;

/**
 * Represents a player's current health
 */
public class HealthBarGroup extends Group {

    private Label healthIndicator;

    public HealthBarGroup(int maxHealth) {
        healthIndicator = new Label(maxHealth + " / " + maxHealth,
                new Label.LabelStyle(new BitmapFont(), Color.WHITE));
        healthIndicator.setFontScale(2.0f);

        addActor(healthIndicator);
    }

}
