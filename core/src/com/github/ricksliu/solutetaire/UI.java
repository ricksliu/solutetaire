package com.github.ricksliu.solutetaire;

import com.badlogic.gdx.Gdx;
import java.lang.Math;

// Stores various values for the UI
public class UI {
    // Dimensions of the screen, used as shortcuts
    private float screenW;
    private float screenH;

    private int[] fontSizes;  // Point sizes of small, medium, and large text

    // Dimensions of buttons
    private float buttonW;
    private float buttonH;

    // Dimensions of the menu screen
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

        fontSizes = new int[] {(int) screenW / 60, (int) screenW / 24, (int) screenW / 12};

        buttonW = screenW / 4;
        buttonH = screenH / 4;

        title = new float[] {screenW * 1 / 24, screenH * 1 / 5};
        playButton = new float[] {screenW * 15 / 24, screenH * 15 / 16};

        cardW = screenW / 12;
        cardH = screenH / 4;

        stock = new float[] {screenW * 1 / 8 - cardW / 2, screenH * 4 / 5 - cardH / 2};
        waste = new float[] {screenW * 2 / 8 - cardW / 2, screenH * 4 / 5 - cardH / 2};
        foundations = new float[] {screenW * 4 / 8 - cardW / 2, screenH * 4 / 5 - cardH / 2};
        tableau = new float[] {screenW * 1 / 8 - cardW / 2, screenH * 1 / 2 - cardH / 2};
        tableauVerticalSpacing = screenH / 25;
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

    // i is used to offset horizontally
    public float[] getFoundations(float i) {
        return new float[] {foundations[0] + screenW * i / 8, foundations[1]};
    }

    public float[] getTableau() {
        return tableau;
    }

    public float getTableauVerticalSpacing() {
        return tableauVerticalSpacing;
    }

    // i is used to offset horizontally
    public float[] getTableau(float i) {
        return new float[] {tableau[0] + screenW * i / 8, tableau[1]};
    }

    // i is used to offset horizontally, j is used to offset stacks of cards
    public float[] getTableau(float i, float j) {
        return new float[] {tableau[0] + screenW * i / 8, tableau[1] - tableauVerticalSpacing * j};
    }
}
