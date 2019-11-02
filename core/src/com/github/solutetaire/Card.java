package com.github.solutetaire;

public class Card {
    private char suit;
    private int rank;
    private boolean faceUp;

    public Card(char suit, int rank, boolean faceUp) {
        this.suit = suit;
        this.rank = rank;
        this.faceUp = faceUp;
    }

    public void flip() {
        faceUp = !faceUp;
    }
}
