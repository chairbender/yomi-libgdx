package com.chairbender.yomi.api.card.move;


import com.chairbender.yomi.api.card.Card;

/**
 * Encapsulates offensive moves (throw and attack)
 */
public class OffensiveMoveInfo extends MoveInfo {
    private Speed speed;
    private int comboPoints;
    private ComboType comboType;
    private boolean hasKnockdown;
    private int baseDamage;
    private int blockDamage;
    private String moveName;
    private int acesCost;
    private MoveType moveType;
    private int pumpDamage;
    private PumpCost pumpCost;
    private int pumpLimit;
    private PokerRank pumpRank;

    /**
     * @param parent parent card
     * @param speed speed of the move
     * @param comboPoints combo points
     * @param comboType combo type
     * @param hasKnockdown whether the move has knockdown
     * @param baseDamage base damage
     * @param blockDamage block damage (0 if none)
     * @param moveName name of the move (null if none)
     * @param acesCost cost in aces to play (0 if none)
     * @param moveType type of the move
     * @param pumpDamage how much pump damage is done when pumped (0 if none)
     * @param pumpCost what card is used to pump (null if none)
     */
    public OffensiveMoveInfo(Card parent, Speed speed,
                             int comboPoints, ComboType comboType, boolean hasKnockdown,
                             int baseDamage, int blockDamage, String moveName, int acesCost, MoveType moveType, int pumpDamage, PumpCost pumpCost) {
        super(parent);
        this.speed = speed;
        this.comboPoints = comboPoints;
        this.comboType = comboType;
        this.hasKnockdown = hasKnockdown;
        this.baseDamage = baseDamage;
        this.blockDamage = blockDamage;
        this.moveName = moveName;
        this.acesCost = acesCost;
        this.moveType = moveType;
        this.pumpDamage = pumpDamage;
        this.pumpCost = pumpCost;
    }

    @Override
    public MoveType getMoveType() {
        return moveType;
    }


    @Override
    public String toString() {
        String result = "(" + getParentCard().getValue().toString() + ") ";
        result += moveName != null ? moveName + " " : "";
        result += moveType == MoveType.ATTACK ? "Attack " : "Throw ";
        result += baseDamage + ((pumpCost != null) ? "+" + pumpDamage + " (" + pumpCost.toString() + ") " : " ") +
                ((blockDamage > 0) ? " (" + blockDamage + ") dmg, " : " dmg, ");
        result += "speed " + speed.toString();
        result += ", " + comboPoints + " CP ";
        result += comboType.toString();
        result += acesCost > 0 ? " " + acesCost + " aces" : "";

        return result;

    }

    public Speed getSpeed() {
        return speed;
    }

    public int getAcesCost() {
        return acesCost;
    }

    public ComboType getComboType() {
        return comboType;
    }

    public boolean isPumpable() {
        return pumpCost != null;
    }

    public int getPumpLimit() {
        return pumpCost.getLimit();
    }

    public PokerRank getPumpRank() {
        return pumpCost.getRank();
    }

    public int getComboPoints() {
        return comboPoints;
    }

    public int getDamage() {
        return baseDamage;
    }

    public int getPumpDamage() {
        return pumpDamage;
    }

    public int getBlockDamage() {
        return blockDamage;
    }
}
