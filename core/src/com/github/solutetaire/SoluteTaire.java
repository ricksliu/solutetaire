package com.github.solutetaire;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Game;
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
        generator.dispose();

        setGameScreen();
    }

	@Override
	public void render() {
	    super.render();
	}
	
	@Override
	public void dispose() {
        batch.dispose();
        fontSmall.dispose();
	}

    public void setMenuScreen() {
        setScreen(new MenuScreen(this));
    }

    public void setGameScreen() {
        setScreen(new GameScreen(this));
    }
}
