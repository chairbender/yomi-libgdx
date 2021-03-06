package com.chairbender.yomi.api.character;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.chairbender.yomi.api.ability.CharacterAbility;
import com.chairbender.yomi.api.card.Card;
import com.chairbender.yomi.api.card.move.PokerRank;
import com.chairbender.yomi.api.card.move.PokerValue;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * Encapsulates all the info that describes a character in Yomi. Contains
 * constants to refer to each of the characters.
 */
public class YomiCharacter {

    private String name;
    private int maxHealth;
    private int maxCombo;
    private Collection<? extends CharacterAbility> innates;

    public static final YomiCharacter GRAVE = new YomiCharacter("Grave",90,4, null);

    /**
     *
     * @param name name to use to look up the card values in the cards.csv file
     */
    private YomiCharacter(String name,int maxHealth,int maxCombo,Collection<? extends CharacterAbility> innates) {

        this.name = name;
        this.maxHealth = maxHealth;
        this.maxCombo = maxCombo;
        this.innates = innates;
    }

    public int getMaxHealth() {
        return maxHealth;
    }

    /**
     *
     * @return all 54 cards in this character's deck. Not guaranteed to be random or in any particular order.
     */
    public List<Card> allCards() {
        //Get the cards csv file from resources
        CSVReader reader = null;
        List<String[]> entries = null;
        try {
            FileHandle handle = Gdx.files.internal("definitions/cards.csv");
            reader = new CSVReader(new BufferedReader(new InputStreamReader((handle.read()))));
            entries = reader.readAll();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        List<Card> result = new ArrayList<Card>();
        //Process each entry for this character
        for (String[] line : entries) {
            if (line[0].equalsIgnoreCase(name)) {
                //Get the rank
                PokerRank rank = PokerRank.fromString(line[1]);

                //create one for each suit
                for (PokerValue.Suit suit : PokerValue.Suit.values()) {
                    PokerValue pokerValue = new PokerValue(rank,suit);
                    //TODO: SpecialAbility ability = SpecialAbilityUtil.fromClassName(line[24]);
                    Card toAdd = Card.getFromCSVLine(line,pokerValue,null);
                    result.add(toAdd);
                }
            }
        }

        //Add the two jokers
        result.add(Card.createJoker(this));
        result.add(Card.createJoker(this));

        return result;


    }


    public int getMaxCombo() {
        return maxCombo;
    }

    /**
     *
     * @param s string that is the name of one of the characters
     * @return the character whose name matches s
     */
    public static YomiCharacter fromStringName(String s) {
        if (s.equalsIgnoreCase("grave")) {
            return GRAVE;
        }
        //TODO: Add other characters
        return GRAVE;
    }
}
