package com.github.ricksliu.solutetaire;

import java.util.ArrayList;

// Inherits from CardCollection and stores a designated suit
public class Foundation extends CardCollection {
    private ArrayList<Card> cards = new ArrayList<>();
    private char suit;

    public Foundation(char suit) {
        this.suit = suit;
    }

    public int getSuit() {
        return suit;
    }
}
