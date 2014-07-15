package com.chairbender.yomi.api.card.move;

/**
 * Describes the cost for pumping a move (and limit)
 */
public class PumpCost {
    private PokerRank rank;
    private int limit;

    /**
     *
     * @param limit the max number of cards that can be used to pump
     * @param rank the rank of card that can be used to pump. Joker indicates any card can be used.
     */
    public PumpCost(int limit,PokerRank rank) {
        this.rank = rank;
        this.limit = limit;
    }

    /**
     *
     * @param s a string that is either 'N' or a number from 2 to 10 or J Q K or A repeated some number of times. or null or empty string.
     * @return a pump cost where the card that must be spent is ANY if the letter is N, otherwise it is whatever the poker rank
     * is of the character. The limit is based on how many times that letter is repeated. returns null if s was null or empty.
     */
    public static PumpCost fromString(String s) {
        if (s == null || s.length() == 0) {
            return null;
        }

        PokerRank rank = null;
        if (s.substring(0,1).equalsIgnoreCase("N")) {
            rank = new PokerRank(PokerRank.Royal.JOKER);
        } else {
            rank = PokerRank.fromString(s.substring(0,1));
        }

        return new PumpCost(s.length(),rank);
    }

    @Override
    public String toString() {
        if (rank.equals(PokerRank.JOKER)) {
            return "+" + limit + " any";
        } else {
            return "+" + limit + " " + rank.toString();

        }
    }

    public int getLimit() {
        return limit;
    }

    public PokerRank getRank() {
        return rank;
    }
}
