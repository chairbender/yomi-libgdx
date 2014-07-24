package com.chairbender.yomi.api.gamestate.model;

import com.chairbender.yomi.api.character.YomiCharacter;

/**
 * Tracks the entire playfield of a game of yomi - both players
 * decks, discards, hands, play areas, health, and character cards.
 */
public class PlayField {
    private PlayerField playerField;
    private PlayerField opponentField;

    /**
     *
     * @param playerField field for this player
     * @param opponentField field for the opponent
     */
    public PlayField(PlayerField playerField, PlayerField opponentField) {
        this.playerField = playerField;
        this.opponentField = opponentField;
    }

    /**
     *
     * @param playerCharacter character p1 will play as (the left player)
     * @param opponentCharacter character p2 will play as (the right player)
     * @return a new playField with both players having full shuffled decks, and
     * empty discards, hands, and play areas, and full health, and their chosen
     * yomi character.
     */
    public static PlayField getForNewGame(YomiCharacter playerCharacter, YomiCharacter opponentCharacter) {
        return new PlayField(PlayerField.getFieldForNewGame(playerCharacter),
                PlayerField.getFieldForNewGame(opponentCharacter));
    }

    public PlayerField getThisPlayerField() {
        return playerField;
    }
}
