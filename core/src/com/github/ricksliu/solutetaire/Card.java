package com.github.ricksliu.solutetaire;

import com.badlogic.gdx.Gdx;
import java.util.Random;

public class Card {
    private char suit;
    private int rank;
    private boolean faceUp;
    private float[] coordinates;
    private float[] newCoordinates;

    public Card(char suit, int rank) {
        this.suit = suit;
        this.rank = rank;
        faceUp = false;

        Random rand = new Random();
        coordinates = new float[2];
        coordinates[0] = -Gdx.graphics.getWidth() + rand.nextInt(3 * Gdx.graphics.getWidth());
        coordinates[1] = -Gdx.graphics.getHeight() + rand.nextInt(3 * Gdx.graphics.getHeight());
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

    public boolean isFaceUp() {
        return faceUp;
    }

    public void flip() {
        faceUp = !faceUp;
    }

    public float[] getCoordinates() {
        return coordinates;
    }

    public float[] getNewCoordinates() {
        return newCoordinates;
    }

    public void updateCoordinates() {
        coordinates[0] += 0.15f * (newCoordinates[0] - coordinates[0]);
        coordinates[1] += 0.15f * (newCoordinates[1] - coordinates[1]);
    }

    public void setNewCoordinates(float[] coordinates) {
        this.newCoordinates = coordinates;
    }
}
