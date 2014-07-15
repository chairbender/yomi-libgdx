package com.chairbender.yomi.api.gamestate.model;

import com.chairbender.yomi.api.character.YomiCharacter;

/**
 * Tracks the entire playfield of a game of yomi - both players
 * decks, discards, hands, play areas, health, and character cards.
 */
public class PlayField {
    private PlayerField p1Field;
    private PlayerField p2Field;

    public PlayField(PlayerField p1Field, PlayerField p2Field) {
        this.p1Field = p1Field;
        this.p2Field = p2Field;
    }

    /**
     *
     * @param p1Character character p1 will play as (the left player)
     * @param p2Character character p2 will play as (the right player)
     * @return a new playField with both players having full shuffled decks, and
     * empty discards, hands, and play areas, and full health, and their chosen
     * yomi character.
     */
    public static PlayField getForNewGame(YomiCharacter p1Character, YomiCharacter p2Character) {
        return new PlayField(PlayerField.getFieldForNewGame(p1Character),
                PlayerField.getFieldForNewGame(p2Character));
    }
}
