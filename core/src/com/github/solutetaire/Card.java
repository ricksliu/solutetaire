package com.github.solutetaire;

public class Card {
    private char suit;
    private int rank;
    private boolean faceUp;

    public Card(char suit, int rank) {
        this.suit = suit;
        this.rank = rank;
        this.faceUp = false;
    }

    public char getSuit() {
        return suit;
    }

    public int getRank() {
        return rank;
    }

    public boolean isFaceUp() {
        return faceUp;
    }

    public void flip() {
        faceUp = !faceUp;
    }
}
