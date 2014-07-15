package com.chairbender.yomi.api.card.move;


import com.chairbender.yomi.api.card.Card;

/**
 * Represents dodge and block
 */
public class DefensiveMoveInfo extends MoveInfo {
    private MoveType type;

    public DefensiveMoveInfo(Card parent, MoveType type) {
        super(parent);
        this.type = type;
    }

    @Override
    public MoveType getMoveType() {
        return type;
    }

    @Override
    public String toString() {
        if (type == MoveType.BLOCK) {
            return "Block" + " (" + getParentCard().getValue() + ")";
        } else {
            return "Dodge" + " (" + getParentCard().getValue() + ")";
        }
    }
}
