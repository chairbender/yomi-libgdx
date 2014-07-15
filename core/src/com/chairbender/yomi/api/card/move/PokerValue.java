package com.chairbender.yomi.api.card.move;

/**
 * Created by Kyle on 6/26/2014.
 */
public class PokerValue {

    public static final PokerValue JOKER = new PokerValue(new PokerRank(PokerRank.Royal.JOKER), null);
    private PokerRank rank;
    private Suit suit;

    public PokerRank getRank() {
        return rank;
    }

    public enum Suit {
        HEARTS("\u2665"),
        SPADES("\u2660"),
        DIAMONDS("\u2666"),
        CLUBS("\u2663");

        public String Symbol;

        private Suit(String symbol) {
            this.Symbol = symbol;
        }

    }

    /**
     *
     * @param rank rank to use
     * @param suit suit to use. null if joker.
     */
    public PokerValue(PokerRank rank, Suit suit) {
        this.rank = rank;
        this.suit = suit;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        PokerValue that = (PokerValue) o;

        //ignore suit if joker
        if (that.rank.equals(PokerRank.JOKER)) {
            return this.rank.equals(PokerRank.JOKER);
        }

        if (!rank.equals(that.rank)) return false;
        if (suit != that.suit) return false;

        return true;
    }

    @Override
    public int hashCode() {
        //ignore suit if joker
        if (rank.equals(PokerRank.JOKER)) {
            return rank.hashCode();
        } else {
            int result = rank.hashCode();
            return 31 * result + (suit != null ? suit.hashCode() : 0);
        }
    }

    @Override
    public String toString() {
        if (this.equals(JOKER)) {
            return "Joker";
        }
        return rank.toString() + suit.Symbol;
    }

}
