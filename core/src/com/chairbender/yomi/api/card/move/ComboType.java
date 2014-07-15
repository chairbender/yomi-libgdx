package com.chairbender.yomi.api.card.move;

/**
 * Represents a cards combo options.
 */
public class ComboType {
    private ComboTypeEnum type;

    public static final ComboType STARTER = new ComboType(ComboTypeEnum.STARTER);
    public static final ComboType LINKER = new ComboType(ComboTypeEnum.LINKER);
    public static final ComboType ENDER = new ComboType(ComboTypeEnum.ENDER);
    public static final ComboType CANTCOMBO = new ComboType(ComboTypeEnum.CANTCOMBO);
    public static final ComboType NORMAL = new ComboType(ComboTypeEnum.NORMAL);

    /**
     *
     * @param s a string that is normal, ender, linker, starter, or null or empty
     * @return if null or empty CANTCOMBO, otherwise returns the combo type based on the string.
     */
    public static ComboType fromString(String s) {
        if (s == null || s.isEmpty()) {
            return CANTCOMBO;
        } else {
            if (s.equalsIgnoreCase("STARTER")) {
                return STARTER;
            } else if (s.equalsIgnoreCase("LINKER")) {
                return LINKER;
            } else if (s.equalsIgnoreCase("ENDER")) {
                return ENDER;
            }  else {
                return NORMAL;
            }
        }
    }

    private enum ComboTypeEnum {
        STARTER,
        LINKER,
        ENDER,
        CANTCOMBO,
        NORMAL
    }

    private ComboType(ComboTypeEnum type) {
        this.type =type;
    }

    @Override
    public String toString() {
        return type.name();
    }

}
