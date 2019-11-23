package com.github.solutetaire;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Vector3;

public class SoluteTaire extends Game {
    public UI ui;

    public Vector3 mouse;
    public int timeSinceClick;
    public int clickDelay;  // Time between clicks registering

    public SpriteBatch batch;
    public BitmapFont fontSmall;
    public BitmapFont fontMedium;
    public BitmapFont fontLarge;

    public SoluteTaire() {
    }

	@Override
	public void create() {
	    ui = new UI();

        mouse = new Vector3();
        timeSinceClick = 0;
        clickDelay = 10;

        batch = new SpriteBatch();

        // Creates fonts
        FreeTypeFontGenerator generator = new FreeTypeFontGenerator(Gdx.files.internal("oswald.ttf"));
        FreeTypeFontGenerator.FreeTypeFontParameter parameter = new FreeTypeFontGenerator.FreeTypeFontParameter();
        parameter.color = Color.BLACK;
        parameter.size = ui.getFontSizes(0);
        fontSmall = generator.generateFont(parameter);
        parameter.color = Color.WHITE;
        parameter.size = ui.getFontSizes(1);
        fontMedium = generator.generateFont(parameter);
        parameter.color = Color.WHITE;
        parameter.size = ui.getFontSizes(2);
        fontLarge = generator.generateFont(parameter);
        generator.dispose();

        setMenuScreen();
    }

	@Override
	public void render() {
	    super.render();
	}
	
	@Override
	public void dispose() {
        batch.dispose();
        fontSmall.dispose();
        fontMedium.dispose();
        fontLarge.dispose();
	}

    public void setMenuScreen() {
        timeSinceClick = 0;
        setScreen(new MenuScreen(this));
    }

    public void setGameScreen() {
        timeSinceClick = 0;
        setScreen(new GameScreen(this));
    }

    public void setEndScreen() {
        timeSinceClick = 0;
        setScreen(new EndScreen(this));
    }

    // Checks if given coordinates are inside given location and dimensions
    public boolean isInside(float x0, float y0, float x, float y, float w, float h) {
        if (x <= x0 & x0 <= x + w & y <= y0 & y0 <= y + h) {
            return true;
        } else {
            return false;
        }
    }

    // Same as the original method but overloaded so the dimensions come from a single array
    public boolean isInside(float x0, float y0, float[] dimensions) {
        return isInside(x0, y0, dimensions[0], dimensions[1], dimensions[2], dimensions[3]);
    }

    // Draws texture with given location and dimensions
    public void draw(float x, float y, float w, float h, Texture image) {
        batch.draw(image, x, y, w, h);
    }

    // Same as the previous method but overloaded so the dimensions come from a single array
    public void draw(float[] dimensions, Texture image) {
        draw(dimensions[0], dimensions[1], dimensions[2], dimensions[3], image);
    }
}
