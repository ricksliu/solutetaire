package com.github.solutetaire;

import com.badlogic.gdx.Gdx;
import java.lang.Math;

// Stores values for the ui
public class UI {
    // Dimensions of screen, used as shortcuts
    private float screenW;
    private float screenH;

    private int[] fontSizes;  // Point sizes of small, medium, and large text

    // Dimensions of buttons
    private float buttonW;
    private float buttonH;

    // Dimensions of menu screen
    private float[] title;
    private float[] playButton;

    // Dimensions of cards
    private float cardW;
    private float cardH;

    // Dimensions of card collections
    private float[] stock;
    private float[] waste;
    private float[] foundations;  // Dimensions of left-most foundation
    private float[] tableau;  // Dimensions of left-most tableau
    private float tableauVerticalSpacing;

    public UI() {
        screenW = Gdx.graphics.getWidth();
        screenH = Gdx.graphics.getHeight();

        fontSizes = new int[] {(int) screenW / 60, (int) screenW / 20, (int) screenW / 10};

        buttonW = screenW / 4;
        buttonH = screenH / 4;

        title = new float[] {screenW * 1 / 24, screenH * 1 / 5};
        playButton = new float[] {screenW * 13 / 24, screenH * 15 / 16};

        cardW = screenW / 12;
        cardH = screenH / 4;

        stock = new float[] {screenW * 1 / 8 - cardW / 2, screenH * 4 / 5 - cardH / 2, cardW, cardH};
        waste = new float[] {screenW * 2 / 8 - cardW / 2, screenH * 4 / 5 - cardH / 2, cardW, cardH};
        foundations = new float[] {screenW * 4 / 8 - cardW / 2, screenH * 4 / 5 - cardH / 2, cardW, cardH};
        tableau = new float[] {screenW * 1 / 8 - cardW / 2, screenH * 1 / 2 - cardH / 2, cardW, cardH};
        tableauVerticalSpacing = screenH / 25;
    }

    public void offsetDimensions(int t) {
        // stock[0] = screenW * 1 / 8 - cardW / 2;
        stock[1] = screenH * 4 / 5 - cardH / 2 - Math.max(0, screenH - screenH / 20 * t) + (float) (Math.sin(t / 50f) * screenH / 150);
        // waste[0] = screenW * 2 / 8 - cardW / 2;
        waste[1] = screenH * 4 / 5 - cardH / 2 - Math.max(0, screenH - screenH / 20 * t) + (float) (Math.sin(t / 50f) * screenH / 150);
        // foundations[0] = screenW * 4 / 8 - cardW / 2;
        foundations[1] = screenH * 4 / 5 - cardH / 2 - Math.max(0, screenH * 2 - screenH / 20 * t) + (float) (Math.sin(t / 50f + 1.047) * screenH / 150);
        // tableau[0] = screenW * 1 / 8 - cardW / 2;
        tableau[1] = screenH * 1 / 2 - cardH / 2 - Math.max(0, screenH * 3 - screenH / 20 * t) + (float) (Math.sin(t / 50f + 2.094) * screenH / 150);
        // tableauVerticalSpacing = screenH / 20;
    }

    public float getScreenW() {
        return screenW;
    }

    public float getScreenH() {
        return screenH;
    }

    public int getFontSizes(int i) {
        return fontSizes[i];
    }

    public float getButtonW() {
        return buttonW;
    }

    public float getButtonH() {
        return buttonH;
    }

    public float[] getTitle() {
        return title;
    }

    public float[] getPlayButton() {
        return playButton;
    }

    public float getCardW() {
        return cardW;
    }

    public float getCardH() {
        return cardH;
    }

    public float[] getStock() {
        return stock;
    }

    public float[] getWaste() {
        return waste;
    }

    public float[] getFoundations() {
        return foundations;
    }

    public float[] getFoundations(float i) {
        return new float[] {foundations[0] + screenW * i / 8, foundations[1], foundations[2], foundations[3]};
    }

    public float[] getTableau() {
        return tableau;
    }

    public float[] getTableau(float i) {
        return new float[] {tableau[0] + screenW * i / 8, tableau[1], tableau[2], tableau[3]};
    }

    public float[] getTableau(float i, float j) {
        return new float[] {tableau[0] + screenW * i / 8, tableau[1] - tableauVerticalSpacing * j, tableau[2], tableau[3]};
    }

    // Returns dimensions that would draw a card centered around the given point
    public float[] getCardDimensions(float x, float y) {
        return new float[] {x - cardW / 2, y - cardH / 2, cardW, cardH};
    }

    public float[] getCardDimensions(float x, float y, float i) {
        return new float[] {x - cardW / 2, y - cardH / 2 - tableauVerticalSpacing * i, cardW, cardH};
    }
}
