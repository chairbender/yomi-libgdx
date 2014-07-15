package com.chairbender.yomi.api.card.move;

/**
 * Represents the speed of a move
 */
public class Speed {
    private double value;

    /**
     * Creates a speed with the given value
     * @param value the value to use
     */
    public Speed(double value) {
        this.value = value;
    }

    /**
     *
     * @param s a string of the form #.# or #, where # is a positive integer
     * @return the speed represented by the string
     */
    public static Speed fromString(String s) {

       return new Speed(Double.parseDouble(s));
    }

    @Override
    public String toString() {
        return value + "";
    }

    /**
     *
     * @param speed
     * @return true if this speed is higher than the other speed
     */
    public boolean isFasterThan(Speed speed) {
        return this.value > speed.value;
    }
}
