package com.chairbender.yomi.ui;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.chairbender.yomi.api.character.YomiCharacter;
import com.chairbender.yomi.api.gamestate.model.PlayField;
import com.chairbender.yomi.api.gamevent.GameEventNotifier;

/**
 * Group that contains the whole play field during a game (for one player).
 * Contains the deck and discard piles of the player on the left, and the
 * opponent's on the right. Contains both player's health bars and hand card counts.
 * Contains this player's hand at the bottom. Contains the status indicator
 * in the top center. Contains the play area above the hand and the phase indicator.
 * Just like the unity app.
 */
public class PlayFieldGroup extends GameEventListeningGroup {

    private PlayerPlayAreaGroup thisPlayerPlayArea = new PlayerPlayAreaGroup();
    private OpponentPlayAreaGroup opponentPlayArea = new OpponentPlayAreaGroup();
    private PhaseIndicatorGroup phaseIndicatorGroup;
    private HandGroup handGroup;
    private StatusGroup statusGroup = new StatusGroup();

    private static final Color BACKGROUND = new Color(0.86f,0.48f,0.04f,0.5f);

    public PlayFieldGroup(GameEventNotifier notifier) {
        super(notifier);

        phaseIndicatorGroup = new PhaseIndicatorGroup(notifier);
        handGroup = new HandGroup(notifier);

        this.addActor(thisPlayerPlayArea);
        this.addActor(opponentPlayArea);
        this.addActor(phaseIndicatorGroup);
        this.addActor(handGroup);
        this.addActor(statusGroup);


        handGroup.addCard(new CardGroup(YomiCharacter.GRAVE.allCards().get(44)),0);
        handGroup.addCard(new CardGroup(YomiCharacter.GRAVE.allCards().get(45)),1);
        handGroup.addCard(new CardGroup(YomiCharacter.GRAVE.allCards().get(40)),1);
        handGroup.addCard(new CardGroup(YomiCharacter.GRAVE.allCards().get(23)),1);
        handGroup.addCard(new CardGroup(YomiCharacter.GRAVE.allCards().get(42)),1);
    }

    ShapeRenderer renderer = new ShapeRenderer();
    @Override
    public void draw(Batch batch, float alpha) {
        batch.end();
        //draw the brown background
        renderer.begin(ShapeRenderer.ShapeType.Filled);
        renderer.setColor(BACKGROUND);
        renderer.rect(0,0,UIConstants.WORLD_WIDTH,UIConstants.WORLD_HEIGHT);
        renderer.end();
        batch.begin();
        super.draw(batch,alpha);
    }

}
