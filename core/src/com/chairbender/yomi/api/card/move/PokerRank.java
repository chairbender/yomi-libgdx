package com.chairbender.yomi.api.card.move;

import java.util.ArrayList;
import java.util.List;

/**
 * Represents the rank of a card (1,2,3...J,K,Q,A, including the possiblity of joker
 */
public class PokerRank {
    public static final PokerRank ACE = new PokerRank(Royal.ACE);
    public static final PokerRank QUEEN = new PokerRank(Royal.QUEEN);
    private int value;

    public static final PokerRank JOKER = new PokerRank(Royal.JOKER);
    public static final PokerRank KING = new PokerRank(Royal.KING);

    /**
     *
     * @param s string that should be a number from 1 to 10, or J K Q or A
     * @return a poker rank represented by that string
     */
    public static PokerRank fromString(String s) {

        if (s.equals("J")) {
            return new PokerRank(Royal.JACK);
        } else if (s.equals("Q")) {
            return new PokerRank(Royal.QUEEN);
        } else if (s.equals("K")) {
            return new PokerRank(Royal.KING);
        } else if (s.equals("A")) {
            return new PokerRank(Royal.ACE);
        } else {
            return new PokerRank(Integer.parseInt(s));
        }
    }

    /**
     *
     * @param rank rank to check
     * @return true if rank and this card are not face cards or aces, and true if the numerical
     * value of this card is one higher than rank.
     */
    public boolean succeeds(PokerRank rank) {
        return (value <= 10) && (rank.value <= 10) && (rank.value == value - 1);
    }

    /**
     *
     * @return a list of all possible poker ranks
     */
    public static List<PokerRank> getPossibleValues() {
        List<PokerRank> result = new ArrayList<PokerRank>();
        for (int i = 2; i <= 15; i++) {
            result.add(new PokerRank(i));
        }
        return result;
    }


    /**
     *
     * @return the integer value representing the rank. for Ace, it's 15, for KING, 13, for Queen
     * 12, for JACK, 11
     */
    public int getIntegerValue() {
        return value;
    }


    public enum Royal {
        JACK(11),
        QUEEN(12),
        KING(13),
        ACE(14),
        JOKER(15);

        /**
         * Value
         */
        public final int Value;

        private Royal(int value)
        {
            Value = value;
        }
    }

    /**
     *
     * @param value a number from 1 to 10 representing the rank.
     */
    public PokerRank(int value) {
        this.value = value;
    }

    /**
     *
     * @param royal a royal (jack, queen, king, ace, or joker)
     */
    public PokerRank(Royal royal) {
        this.value = royal.Value;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        PokerRank pokerRank = (PokerRank) o;

        if (value != pokerRank.value) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return value;
    }


    public String toString() {
        if (value <= 10) {
            return value + "";
        } else {
            if (value == Royal.JACK.Value) {
                return "J";
            } else if (value == Royal.QUEEN.Value) {
                return "Q";
            }  else if (value == Royal.KING.Value) {
                return "K";
            } else if (value == Royal.ACE.Value) {
                return "A";
            } else {
                return "Joker";
            }
        }
    }

}
