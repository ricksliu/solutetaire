package com.github.solutetaire;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.audio.Sound;
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
    public boolean initialClick;  // Set to false in the middle of a click (when a click is being held)

    public SpriteBatch batch;
    public ShapeRenderer shape;
    public BitmapFont fontSmall;
    public BitmapFont fontMedium;
    public BitmapFont fontLarge;
    public Sound newGameSound;
    public Sound cardUpSound;
    public Sound cardDownSound;
    public Sound victorySound;

    public SoluteTaire() {
    }

	@Override
	public void create() {
	    ui = new UI();

        mouse = new Vector3();

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

        // Creates sounds
        newGameSound = Gdx.audio.newSound(Gdx.files.internal("newGameSound.ogg"));
        cardUpSound = Gdx.audio.newSound(Gdx.files.internal("cardUpSound.ogg"));
        cardDownSound = Gdx.audio.newSound(Gdx.files.internal("cardDownSound.ogg"));
        victorySound = Gdx.audio.newSound(Gdx.files.internal("newGameSound.ogg"));

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
        newGameSound.dispose();
        cardUpSound.dispose();
        cardDownSound.dispose();
        victorySound.dispose();
	}

    public void setMenuScreen() {
        setScreen(new MenuScreen(this));
        initialClick = true;
    }

    public void setGameScreen() {
        setScreen(new GameScreen(this));
        newGameSound.play();
        initialClick = true;
    }

    public void setEndScreen() {
        setScreen(new EndScreen(this));
        victorySound.play();
        initialClick = true;
    }

    // Checks if (x0, y0) is inside rectangle defined by point (x, y) and width and height w and h
    public boolean isInside(float x0, float y0, float x, float y, float w, float h) {
        if (x <= x0 & x0 <= x + w & y <= y0 & y0 <= y + h) {
            return true;
        } else {
            return false;
        }
    }

    // Draws a texture with a given location, width and height
    public void draw(float x, float y, float w, float h, Texture image) {
        batch.draw(image, x, y, w, h);
    }
}
