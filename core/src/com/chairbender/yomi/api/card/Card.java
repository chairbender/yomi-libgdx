package com.chairbender.yomi.api.card;

import com.chairbender.yomi.api.ability.CardAbility;
import com.chairbender.yomi.api.card.move.*;
import com.chairbender.yomi.api.csv.CSVUtility;

import java.util.ArrayList;

/**
 * Represents a card in Yomi. Each card has two moves on the top and bottom, each with their own info.
 * top and bottom moves are null if this card is a joker.
 */
public class Card {
    private final ArrayList<MoveInfo> moves;
    private MoveInfo top;
    private MoveInfo bottom;
    private PokerValue value;
    private CardAbility specialAbility;

    /**
     *
     * @param value poker value of the card
     * @param specialAbility the special ability. null if none.
     */
    private Card(PokerValue value, CardAbility specialAbility) {
        this.value = value;
        this.specialAbility = specialAbility;
        this.moves = new ArrayList<MoveInfo>();

    }

    public void setTop(MoveInfo top) {

        this.top = top;
        moves.add(top);
    }

    public void setBottom(MoveInfo bottom) {
        this.bottom = bottom;
        moves.add(bottom);
    }

    public PokerValue getValue() {
        return value;
    }

    @Override
    public String toString() {
        if (value.equals(PokerValue.JOKER)) {
            return "Joker";
        } else {
            String result = "";
            result += value.toString();
            result += " | Top " + top.toString();
            result += " | Bottom " + bottom.toString();
            result += specialAbility != null ? " | " + specialAbility.getClass().getName() : "";

            return result;
        }
    }

    public CardAbility getSpecialAbility() {
        return specialAbility;
    }

    public boolean hasSpecialAbility() {
        return specialAbility != null;
    }

    public MoveInfo topMoveInfo() {
        return top;
    }

    public MoveInfo bottomMoveInfo() {
        return bottom;
    }

    public static Card getFromCSVLine(String[] line, PokerValue pokerValue, CardAbility ability) {
        Card result = new Card(pokerValue,ability);

        MoveInfo top = getMoveInfoFromCSVAtIndex(result, 2, line);
        MoveInfo bottom = getMoveInfoFromCSVAtIndex(result, 13, line);

        result.setTop(top);
        result.setBottom(bottom);

        return result;
    }

    /**
     *
     * @param index index where the move info starts in the line (where MoveType is)
     * @param line line from cards.csv
     * @return the move info
     */
    private static MoveInfo getMoveInfoFromCSVAtIndex(Card parent, int index, String[] line) {
        MoveType moveType = MoveType.fromString(line[index]);
        if (moveType.equals(MoveType.ATTACK) || moveType.equals(MoveType.THROW)) {
            int baseDamage = Integer.parseInt(line[index+1]);
            int blockDamage = CSVUtility.parseIntWithDefault(line[index + 2], 0);
            Speed speed = Speed.fromString(line[index + 3]);
            int comboPoints = Integer.parseInt(line[index + 4]);
            ComboType comboType = ComboType.fromString(line[index + 5]);
            boolean hasKnockdown = CSVUtility.parseIntWithDefault(line[index + 6],0) == 1;
            String moveName = line[index + 7].isEmpty() ? null : line[index + 7];
            int acesCost = CSVUtility.parseIntWithDefault(line[index + 8],0);
            PumpCost pumpCost = PumpCost.fromString(line[index + 9]);
            int pumpDamage = CSVUtility.parseIntWithDefault(line[index + 10],0);

            return new OffensiveMoveInfo(parent, speed,comboPoints,comboType,hasKnockdown,baseDamage,blockDamage,moveName,acesCost,moveType,pumpDamage,pumpCost);
        } else {
            return new DefensiveMoveInfo(parent, moveType);
        }
    }

    public ArrayList<MoveInfo> getMoves() {
        return moves;
    }

    public boolean isJoker() {
        return this.value.equals(PokerValue.JOKER);
    }

    /**
     * Create a card from scratch. if movetype for the move is
     * defend or dodge, the other values are ignored for that move
     * @param pokerValue
     * @param ability
     * @param topMoveMoveType
     * @param topMoveSpeed
     * @param topMoveComboPoints
     * @param topMoveComboType
     * @param topMoveHasKnockdown
     * @param topMoveBaseDamage
     * @param topMoveBlockDamage
     * @param topMoveName
     * @param topMoveAcesCost
     * @param topMovePumpDamage
     * @param topMovePumpCost
     * @param bottomMoveMoveType
     * @param bottomMoveSpeed
     * @param bottomMoveComboPoints
     * @param bottomMoveComboType
     * @param bottomMoveHasKnockdown
     * @param bottomMoveBaseDamage
     * @param bottomMoveBlockDamage
     * @param bottomMoveName
     * @param bottomMoveAcesCost
     * @param bottomMovePumpDamage
     * @param bottomMovePumpCost
     * @return
     */
    public static Card create(PokerValue pokerValue, CardAbility ability,
                              MoveType topMoveMoveType,
                              Speed topMoveSpeed, int topMoveComboPoints,
                              ComboType topMoveComboType, boolean topMoveHasKnockdown, int topMoveBaseDamage,
                              int topMoveBlockDamage, String topMoveName, int topMoveAcesCost, int topMovePumpDamage,
                              PumpCost topMovePumpCost,
                              MoveType bottomMoveMoveType,
                              Speed bottomMoveSpeed, int bottomMoveComboPoints,
                              ComboType bottomMoveComboType, boolean bottomMoveHasKnockdown, int bottomMoveBaseDamage,
                              int bottomMoveBlockDamage, String bottomMoveName, int bottomMoveAcesCost, int bottomMovePumpDamage,
                              PumpCost bottomMovePumpCost) {
        MoveInfo topMove = null;
        MoveInfo bottomMove = null;
        Card result = new Card(pokerValue,ability);
        if (topMoveMoveType.equals(MoveType.BLOCK) || topMoveMoveType.equals(MoveType.DODGE)) {
            topMove = new DefensiveMoveInfo(result,topMoveMoveType);
        } else {
            //offense top
            topMove = new OffensiveMoveInfo(result,topMoveSpeed,topMoveComboPoints,topMoveComboType,topMoveHasKnockdown,topMoveBaseDamage,
                    topMoveBlockDamage,topMoveName,topMoveAcesCost,topMoveMoveType,topMovePumpDamage,topMovePumpCost);
        }

        if (bottomMoveMoveType.equals(MoveType.BLOCK) || bottomMoveMoveType.equals(MoveType.DODGE)) {
            bottomMove = new DefensiveMoveInfo(result,bottomMoveMoveType);
        } else {
            //offense bottom
            bottomMove = new OffensiveMoveInfo(result,bottomMoveSpeed,bottomMoveComboPoints,bottomMoveComboType,bottomMoveHasKnockdown,bottomMoveBaseDamage,
                    bottomMoveBlockDamage,bottomMoveName,bottomMoveAcesCost,bottomMoveMoveType,bottomMovePumpDamage,bottomMovePumpCost);
        }

        result.setTop(topMove);
        result.setBottom(bottomMove);

        return result;
    }

    public static Card createJoker() {
        return new Card(PokerValue.JOKER, null);
    }
}
