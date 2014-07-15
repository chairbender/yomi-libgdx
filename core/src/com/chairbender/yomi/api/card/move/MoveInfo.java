package com.chairbender.yomi.api.card.move;


import com.chairbender.yomi.api.card.Card;

/**
 * Encapsulates the information that describes one of the moves on one of the cards.
 */
public abstract class MoveInfo {

    private Card parent;

    protected MoveInfo(Card parent) {
        this.parent = parent;
    }

    /**
     *
     * @return the card this move appears on
     */
    public Card getParentCard() {
        return parent;
    }

    public abstract MoveType getMoveType();
}
