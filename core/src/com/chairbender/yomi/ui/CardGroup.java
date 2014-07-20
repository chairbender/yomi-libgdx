package com.chairbender.yomi.ui;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.HorizontalGroup;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.utils.Align;
import com.chairbender.yomi.api.card.Card;
import com.chairbender.yomi.api.card.move.*;
import com.chairbender.yomi.api.character.YomiCharacter;

/**
 * Represents a card. The position of this should be set by the parent, but it should only be resized by
 * setting scale.
 * Position is relative to the bottom left
 */
public class CardGroup extends Group {

    private Card card;

    private Image cardImage;

    private MoveInfoGroup topMove;
    private MoveInfoGroup bottomMove;

    //group so we can have separate rotation and scale groups
    private Group rotateGroup;

    private static final float LEFT_MARGIN = 14;

    /**
     *
     * @param card the card this actor should represent.
     */
    public CardGroup(Card card) {
        this.card = card;

        //this card's position are determined by the parent, but height is determined by this
        //actor

        rotateGroup = new Group();
        //create everything used in the draw method
        Texture cardTexture = new Texture(getImageFileName());
        this.cardImage = new Image(cardTexture);
        //add the move info
        topMove = new MoveInfoGroup(card.topMoveInfo(),this);
        bottomMove = new MoveInfoGroup(card.bottomMoveInfo(),this);
        rotateGroup.addActor(cardImage);

        //Create an outer group for scaling. The rotation will be done on the
        //groups inside the scaling group
        Group scalingTopGroup = new Group();
        Group scalingBottomGroup = new Group();
        scalingTopGroup.setBounds(0,0, getScaledImageWidth(), getScaledImageHeight());
        scalingBottomGroup.setBounds(0,0, getScaledImageWidth(), getScaledImageHeight());
        topMove.setPosition(LEFT_MARGIN,cardImage.getHeight() - topMove.getHeight());
        bottomMove.setPosition(LEFT_MARGIN,cardImage.getHeight() - bottomMove.getHeight());

        //set origins to this card's middle point
        this.setOrigin(getScaledImageWidth()/2,getHeight()/2);
        topMove.setOrigin(getScaledImageWidth()/2 - topMove.getX(), getScaledImageHeight()/2 - topMove.getY());
        //need 45 and -50
        bottomMove.setOrigin(getScaledImageWidth()/2 - bottomMove.getX(), getScaledImageHeight()/2 - bottomMove.getY());

        bottomMove.setRotation(180);

        scalingTopGroup.addActor(topMove);
        scalingBottomGroup.addActor(bottomMove);

        scalingTopGroup.setOrigin(LEFT_MARGIN,scalingTopGroup.getHeight());
        scalingTopGroup.setScale(0.44f);
        scalingBottomGroup.setOrigin(getScaledImageWidth() - LEFT_MARGIN,0);
        scalingBottomGroup.setScale(0.44f);
        rotateGroup.addActor(scalingTopGroup);
        rotateGroup.addActor(scalingBottomGroup);



        addActor(rotateGroup);

    }



    /**
     * rotate the card so the other side is on top, via an animation
     */
    public void rotate() {
        rotateGroup.addAction(Actions.rotateBy(180,0.5f, Interpolation.pow4));
    }

    public float getScaledImageHeight() {
        return cardImage.getHeight()*getScaleX();
    }

    public float getScaledImageWidth() {
        return cardImage.getWidth()*getScaleY();
    }


    @Override
    public void setScale(float scale) {
        super.setScale(scale);
        //reset the inner group origin
        rotateGroup.setOrigin(cardImage.getWidth()/2,cardImage.getHeight()/2);
        this.setBounds(getX(),getY(),cardImage.getWidth()*scale,cardImage.getHeight()*scale);
    }

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
 * group for drawing move info
 */
class MoveInfoGroup extends Group {
    private static final float MOVE_SPACING = 0;
    private final MoveInfo move;

    private static final float moveNameScale = 0.8f;
    private static final float iconScale = 0.7f;
    private static final float blockDamageIconScale = 0.3f;
    private final Texture knockDownTexture = new Texture("icons/knockdown.png");
    private Label moveTypeLabel;

    private HorizontalGroup infoGroup;

    private Image moveTypeIcon;
    private Label damageLabel;

    private Label blockDamageLabel;
    private Image blockDamageImage;
    private static final Texture blockTexture = new Texture("icons/block.png");




    public MoveInfoGroup(MoveInfo move,CardGroup parent) {
        this.move = move;

        String moveTypeString = "";
        String moveTypeIconFile = "";
        String damageString = "";
        String blockDamageString = null;
        Color typeColor;
        //TODO: Reuse the move icon textures
        if (move instanceof OffensiveMoveInfo) {
            OffensiveMoveInfo offense = (OffensiveMoveInfo) move;
            if (offense.getMoveType().equals(MoveType.ATTACK)) {
                moveTypeString += "Attack\n";
                moveTypeIconFile = "icons/attack.png";
                typeColor = Color.RED;
            } else {
                moveTypeString += "Throw\n";
                typeColor = Color.BLACK;
                moveTypeIconFile = "icons/throw.png";
            }
            if (offense.getBlockDamage() > 0) {
                blockDamageString = offense.getBlockDamage() + "";
            }
            damageString = offense.getDamage() + "";
            if (offense.getPumpDamage() > 0) {
                damageString += "+" + offense.getPumpDamage();
            }

            infoGroup = new HorizontalGroup();
            infoGroup.align(Align.bottom);


            SpeedBoxGroup speedBoxGroup = new SpeedBoxGroup(offense.getSpeed());
            infoGroup.addActor(speedBoxGroup);

            ComboBoxGroup comboBoxGroup = new ComboBoxGroup(offense.getComboPoints(),move.getParentCard().getCharacter());
            infoGroup.addActor(comboBoxGroup);

            if (!(offense.getComboType().equals(ComboType.CANTCOMBO) ||
                    offense.getComboType().equals(ComboType.NORMAL))) {
                ComboTypeGroup comboTypeGroup = new ComboTypeGroup(offense.getComboType());
                infoGroup.addActor(comboTypeGroup);
            }
            if (offense.hasKnockdown()) {
                Group kdGroup = new Group();
                Image knockDown = new Image(knockDownTexture);
                kdGroup.addActor(knockDown);
                kdGroup.setWidth(knockDownTexture.getWidth());
                kdGroup.setHeight(knockDownTexture.getHeight());
                infoGroup.addActor(kdGroup);
            }
            if (offense.isPumpable()) {
                PumpCostGroup pumpCostGroup = new PumpCostGroup(offense);
                infoGroup.addActor(pumpCostGroup);
            }


            infoGroup.setOrigin(0,infoGroup.getPrefHeight());
            infoGroup.setScale(0.55f);
            addActor(infoGroup);
        } else {
            if (move.getMoveType().equals(MoveType.BLOCK)) {
                moveTypeString += "Block";
                typeColor = Color.BLUE;
                moveTypeIconFile = "icons/block.png";
            } else {
                moveTypeString += "Dodge";
                typeColor = Color.PURPLE;
                moveTypeIconFile = "icons/dodge.png";
            }
        }

        //The move type text (Attack, Block,...)
        moveTypeLabel   = new Label(moveTypeString, new Label.LabelStyle(new BitmapFont(),typeColor));
        if (infoGroup != null) {
            moveTypeLabel.setPosition(0,infoGroup.getPrefHeight() + infoGroup.getY() + MOVE_SPACING);
        }
        moveTypeLabel.setFontScale(moveNameScale);
        addActor(moveTypeLabel);

        Texture moveIcon = new Texture(moveTypeIconFile);
        moveTypeIcon = new Image(moveIcon);
        damageLabel = new Label(damageString, new Label.LabelStyle(new BitmapFont(),Color.WHITE));
        positionDamageIcon(moveTypeIcon, damageLabel,moveTypeLabel.getX() + 88,moveTypeLabel.getY() - 20, iconScale);

        addActor(moveTypeIcon);
        addActor(damageLabel);

        //handle block damage
        if (blockDamageString != null) {
            blockDamageImage = new Image(blockTexture);
            blockDamageLabel = new Label(blockDamageString, new Label.LabelStyle(new BitmapFont(),Color.WHITE));
            blockDamageLabel.setFontScale(.8f);
            positionDamageIcon(blockDamageImage, blockDamageLabel,
                    moveTypeIcon.getX() + moveTypeIcon.getWidth() - blockDamageImage.getWidth() / 2,
                    moveTypeIcon.getY() - (blockDamageImage.getHeight() * blockDamageIconScale) / 2, blockDamageIconScale);

            addActor(blockDamageImage);
            addActor(blockDamageLabel);

        }
    }

    private void positionDamageIcon(Image icon, Label damage,float x, float y, float iconScale) {
        //position relative to move label
        icon.setPosition(x,y);
        icon.setWidth(icon.getWidth()*iconScale);
        icon.setHeight(icon.getHeight() * iconScale);
        damage.setPosition(icon.getX(),icon.getY());
        damage.setCenterPosition(icon.getCenterX(),icon.getCenterY());
    }

    @Override
    public float getHeight() {
        return getScaleY() * (moveTypeLabel.getHeight() + (infoGroup != null ? infoGroup.getPrefHeight() + MOVE_SPACING: 0));
    }

}

class SpeedBoxGroup extends Group {
    private static final Texture speedTexture = new Texture("icons/speed.png");
    public SpeedBoxGroup(Speed speed) {
        Image speedBox = new Image(speedTexture);
        Label speedLabel = new Label(speed+ "", new Label.LabelStyle(new BitmapFont(),Color.BLACK));
        speedLabel.setPosition(speedBox.getX() + 23,speedBox.getY() - 1);
        speedLabel.setFontScale(0.8f);
        addActor(speedBox);
        addActor(speedLabel);
    }

    @Override
    public float getWidth() {
        return speedTexture.getWidth() * getScaleX();
    }
    @Override
    public float getHeight() {
        return speedTexture.getHeight() * getScaleY();
    }
}

class ComboBoxGroup extends Group {
    private static final Texture comboTexture = new Texture("icons/combo_points.png");
    private static final Texture filledTexture = new Texture("icons/combo_filled.png");
    private static final Texture emptyTexture = new Texture("icons/combo_empty.png");
    private static final int HORIZ_SPACING = 2;

    public ComboBoxGroup(int comboPoints, YomiCharacter character) {
        Image box = new Image(comboTexture);
        addActor(box);

        //Fill it
        for (int i = 0; i < character.getMaxCombo(); i++) {
            //first line
            Image nextStone = comboPoints < i ? new Image(emptyTexture) : new Image(filledTexture);
            if (i <= 2) {
                //first line
                nextStone.setPosition(i * (HORIZ_SPACING + filledTexture.getWidth()) + 18,
                        8);
            } else {
                //second line
                nextStone.setPosition((i - 3) * (HORIZ_SPACING + filledTexture.getWidth())  + 18,
                        2);
            }
            addActor(nextStone);
        }
    }

    @Override
    public float getWidth() {
        return comboTexture.getWidth() * getScaleX();
    }
}

class ComboTypeGroup extends Group {
    private static final Texture boxTexture = new Texture("icons/combo_type.png");

    public ComboTypeGroup(ComboType comboType) {
        Image box = new Image(boxTexture);
        Label text = new Label(comboType.toString(), new Label.LabelStyle(new BitmapFont(),Color.BLACK));
        text.setOrigin(text.getTextBounds().width/2,text.getTextBounds().height/2);
        //text.setCenterPosition(boxTexture.getWidth()/2,boxTexture.getHeight()/2);
        text.setFontScale((boxTexture.getWidth() - 8) / text.getTextBounds().width);
        text.setPosition(5,0);

        addActor(box);
        addActor(text);
    }

    @Override
    public float getWidth() {
        return boxTexture.getWidth() * getScaleX();
    }
}

class PumpCostGroup extends Group {
    private static final Texture pumpTexture = new Texture("icons/pump.png");

    public PumpCostGroup(OffensiveMoveInfo offense) {
        Image box = new Image(pumpTexture);
        String pumpString = "";
        for (int i = 0; i < offense.getPumpLimit(); i++) {
            pumpString += "+" + offense.getPumpRank().toString();
        }
        Label text = new Label(pumpString, new Label.LabelStyle(new BitmapFont(),Color.BLACK));
        text.setOrigin(text.getTextBounds().width/2,text.getTextBounds().height/2);
        //text.setCenterPosition(boxTexture.getWidth()/2,boxTexture.getHeight()/2);
        text.setFontScale((pumpTexture.getHeight() - 8) / text.getTextBounds().height);
        text.setPosition(15,-1);

        addActor(box);
        addActor(text);
    }

    @Override
    public float getWidth() {
        return pumpTexture.getWidth() * getScaleX();
    }
}
