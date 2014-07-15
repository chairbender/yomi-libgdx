package com.chairbender.yomi.api.card.move;

/**
 * Created by Kyle on 6/26/2014.
 */
public class MoveType {

    public static final MoveType ATTACK = new MoveType("Attack");
    public static final  MoveType THROW = new MoveType("Throw");
    public static final  MoveType DODGE = new MoveType("Dodge");
    public static final  MoveType BLOCK = new MoveType("Block");

    private final String friendlyName;

    private MoveType(String friendlyName) {
        this.friendlyName = friendlyName;
    }

    /**
     *
     * @param s a string that is "Attack" "Throw" "Dodge" or "Block" (ignoring case). undefined otherwise.
     * @return the movetype corresponding to the string
     */
    public static MoveType fromString(String s) {
        if (s.equalsIgnoreCase("Attack")) {
            return ATTACK;
        } else if (s.equalsIgnoreCase("Throw")) {
            return THROW;
        }  else if (s.equalsIgnoreCase("BLOCK")) {
            return BLOCK;
        }  else {
            return DODGE;
        }

    }

    @Override
    public boolean equals(Object other) {
        return other instanceof MoveType && ((MoveType) other).friendlyName.equals(this.friendlyName);
    }

    @Override
    public int hashCode() {
        return friendlyName.hashCode();
    }
}
