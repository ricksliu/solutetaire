package com.github.solutetaire;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Vector3;

public class SoluteTaire extends Game {
    public UI ui;

    public Vector3 mouse;
    public int timeSinceClick;  // Stores time since the last click
    public int clickDelay;  // Delay between clicks registering

    public SpriteBatch batch;
    public ShapeRenderer shape;
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
        shape = new ShapeRenderer();

        // Creates fonts
        FreeTypeFontGenerator generator = new FreeTypeFontGenerator(Gdx.files.internal("oswald.ttf"));
        FreeTypeFontGenerator.FreeTypeFontParameter parameter = new FreeTypeFontGenerator.FreeTypeFontParameter();
        // Small font
        parameter.color = Color.BLACK;
        parameter.size = ui.getFontSizes(0);
        fontSmall = generator.generateFont(parameter);
        // Medium font
        parameter.color = Color.WHITE;
        parameter.size = ui.getFontSizes(1);
        fontMedium = generator.generateFont(parameter);
        // Large font
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
        shape.dispose();
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

    public void runClickTimer() {
        if (timeSinceClick < 10) {
            timeSinceClick++;
        }
    }

    public boolean canClick() {
        if (timeSinceClick >= clickDelay) {
            return true;
        } else {
            return false;
        }
    }

    // Checks if given coordinates are inside given location and dimensions
    public boolean isInside(float x0, float y0, float x, float y, float w, float h) {
        if (x <= x0 & x0 <= x + w & y <= y0 & y0 <= y + h) {
            return true;
        } else {
            return false;
        }
    }

    // Draws a texture with a given location and dimensions
    public void draw(float x, float y, float w, float h, Texture image) {
        batch.draw(image, x, y, w, h);
    }
}
