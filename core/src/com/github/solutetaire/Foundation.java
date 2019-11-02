package com.github.solutetaire;

import java.util.ArrayList;

public class Foundation extends CardCollection {
    private ArrayList<Card> cards = new ArrayList<>();
    private char rank;

    public Foundation(char rank) {
        this.rank = rank;
    }
}
