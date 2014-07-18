package com.chairbender.yomi.ui;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.actions.RotateByAction;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.chairbender.yomi.api.card.Card;
import com.chairbender.yomi.api.card.move.*;

/**
 * Represents a card. The position of this should be set by the parent, but the width should not be modified.
 * Position is relative to the bottom left
 */
public class CardGroup extends Group {


    private Card card;

    private Image cardImage;
    private float scale = 2f;

    private MoveActor topMove;
    private MoveActor bottomMove;

    private float rotation = 0;

    /**
     *
     * @param card the card this actor should represent.
     */
    public CardGroup(Card card) {
        this.card = card;

        //this card's position are determined by the parent, but height is determined by this
        //actor


        //create everything used in the draw method
        Texture cardTexture = new Texture(getImageFileName());
        this.cardImage = new Image(cardTexture);
        cardImage.setBounds(0,0,cardTexture.getWidth()*scale,cardTexture.getHeight()*scale);
        //add the move info
        topMove = new MoveActor(card.topMoveInfo());
        bottomMove = new MoveActor(card.bottomMoveInfo());
        addActor(cardImage);
        addActor(topMove);
        addActor(bottomMove);

        positionEverything();

        //click event
        cardImage.addListener(new InputListener() {
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                rotate();
                return true;
            }
        });

    }

    /**
     * position everything based on this group's coordinates and bounds
     */
    private void positionEverything() {

        topMove.setPosition(25,cardImage.getHeight() - topMove.getHeight());
        bottomMove.setPosition(25,cardImage.getHeight() - bottomMove.getHeight());
        //set origins to this card's middle point
        this.setOrigin(getWidth()/2,getHeight()/2);
        topMove.setOrigin(getWidth()/2 - topMove.getX(), getHeight()/2 - topMove.getY());
        //need 45 and -50
        bottomMove.setOrigin(getWidth()/2 - bottomMove.getX(), getHeight()/2 - bottomMove.getY());

        bottomMove.setRotation(rotation + 180);
        topMove.setRotation(rotation);
    }



    /**
     * rotate the card so the other side is on top, via an animation
     */
    public void rotate() {
        this.addAction(Actions.rotateBy(180,0.5f, Interpolation.pow4));
//        topMove.addAction(Actions.rotateBy(180,0.5f, Interpolation.pow4));
//        bottomMove.addAction(Actions.rotateBy(180,0.5f, Interpolation.pow4));
//        cardImage.addAction(Actions.rotateBy(180,0.5f, Interpolation.pow4));
    }

    @Override
    public float getHeight() {
        return cardImage.getHeight();
    }

    @Override
    public float getWidth() {
        return cardImage.getWidth();
    }

    /**
     * set the position of this card
     * @param x
     * @param y
     */
    @Override
    public void setPosition(float x, float y) {
        super.setPosition(x, y);
        positionEverything();

    }

    /* @Override
    public void draw(Batch batch, float alpha) {
        cardSprite.setBounds(getX(),getY(),scale * cardSprite.getTexture().getWidth(),
                scale * cardSprite.getTexture().getHeight());
        cardSprite.draw(batch);
    }*/

    /**
     *
     * @return the string that is the name of the image file (including cards/ before it)
     * to use for this card's
     * base poker card image.
     */
    private String getImageFileName() {
        if (card.isJoker()) {
            return "cards/53.png";
        }
        PokerValue value = card.getValue();
        int suitOffset = 0;
        if (value.getSuit().equals(PokerValue.Suit.CLUBS)) {
            suitOffset = 0;
        } else if (value.getSuit().equals(PokerValue.Suit.SPADES)) {
            suitOffset = 1;
        } else if (value.getSuit().equals(PokerValue.Suit.HEARTS)) {
            suitOffset = 2;
        } else {
            suitOffset = 3;
        }

        PokerRank rank = value.getRank();
        rank.getIntegerValue();
        int startingNumber =  53 - (rank.getIntegerValue() - 1)*4;

        return "cards/" + (startingNumber + suitOffset) + ".png";

    }
}

/**
 * actor for drawing move info
 */
class MoveActor extends Group {
    private final MoveInfo move;

    private Label moveInfoLabel;

    public MoveActor(MoveInfo move) {
        this.move = move;

        String moveInfoString = "";
        if (move instanceof OffensiveMoveInfo) {
            OffensiveMoveInfo offense = (OffensiveMoveInfo) move;
            if (offense.getMoveType().equals(MoveType.ATTACK)) {
                moveInfoString += "Attack\n";
            } else {
                moveInfoString += "Throw\n";
            }
            moveInfoString += "Speed: " + offense.getSpeed().toString() + "\n";
            moveInfoString += "Combo Points: " + offense.getComboPoints() + "\n";
            if (!(offense.getComboType().equals(ComboType.CANTCOMBO) ||
                    offense.getComboType().equals(ComboType.NORMAL))) {
                moveInfoString += offense.getComboType().toString() + "\n";
            }
            if (offense.hasKnockdown()) {
                moveInfoString += "Knockdown\n";
            }
            if (offense.isPumpable()) {
                String pumpString = "";
                for (int i = 0; i < offense.getPumpLimit(); i++) {
                    pumpString += "+" + offense.getPumpRank().toString();
                }
                moveInfoString += "Pump: " + pumpString + "\n";
            }
        } else {
            if (move.getMoveType().equals(MoveType.BLOCK)) {
                moveInfoString += "Block";
            } else {
                moveInfoString += "Dodge";
            }
        }

        moveInfoLabel = new Label(moveInfoString, new Label.LabelStyle(new BitmapFont(),Color.BLACK));
        addActor(moveInfoLabel);
    }

    @Override
    public float getHeight() {
        return moveInfoLabel.getHeight();
    }
}
