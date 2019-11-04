package com.github.solutetaire;

import java.util.ArrayList;

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
