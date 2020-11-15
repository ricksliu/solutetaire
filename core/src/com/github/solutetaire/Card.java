package com.github.solutetaire;

import com.badlogic.gdx.Gdx;

public class Card {
    private char suit;
    private int rank;
    private boolean faceUp;
    private float[] coordinates;
    private float[] oldCoordinates;
    private float[] newCoordinates;

    public Card(char suit, int rank) {
        this.suit = suit;
        this.rank = rank;
        faceUp = false;
        coordinates = new float[2];
        coordinates[0] = Gdx.graphics.getWidth() / 2f;
        coordinates[1] = -Gdx.graphics.getHeight();
        oldCoordinates = new float[2];
        oldCoordinates[0] = 0;
        oldCoordinates[1] = 0;
        newCoordinates = new float[2];
        newCoordinates[0] = 0;
        newCoordinates[1] = 0;
    }

    public char getSuit() {
        return suit;
    }

    public int getRank() {
        return rank;
    }

    public float[] getCoordinates() {
        return coordinates;
    }

    public float[] getOldCoordinates() {
        return oldCoordinates;
    }

    public float[] getNewCoordinates() {
        return newCoordinates;
    }

    public void updateCoordinates() {
        coordinates[0] += 0.2f * (newCoordinates[0] - coordinates[0]);
        coordinates[1] += 0.2f * (newCoordinates[1] - coordinates[1]);
    }

    public void setOldCoordinates(float[] coordinates) {
        this.oldCoordinates = coordinates;
    }

    public void setNewCoordinates(float[] coordinates) {
        this.newCoordinates = coordinates;
    }

    public boolean isFaceUp() {
        return faceUp;
    }

    public void flip() {
        faceUp = !faceUp;
    }
}
