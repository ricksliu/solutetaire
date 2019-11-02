package com.github.solutetaire;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator.*;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.graphics.Color;

public class SoluteTaire extends ApplicationAdapter {
	private OrthographicCamera camera;
	private SpriteBatch batch;
	private BitmapFont font12;
	private Texture testImg;
	private Rectangle test;
	
	@Override
	public void create () {
		// Creates and sets camera
		camera = new OrthographicCamera();
		camera.setToOrtho(false, 1280, 720);

		batch = new SpriteBatch();

		// Creates fonts
		FreeTypeFontGenerator generator = new FreeTypeFontGenerator(Gdx.files.internal("oswald.ttf"));
		FreeTypeFontParameter parameter = new FreeTypeFontParameter();
		parameter.color = Color.WHITE;
		parameter.size = 12;
		font12 = generator.generateFont(parameter);
		generator.dispose();

		testImg = new Texture(Gdx.files.internal("badlogic.jpg"));
		test = new Rectangle();
		test.x = 200;
		test.y = 100;
		test.width = 200;
		test.height = 100;

		// Sets up all stacks of cards
		CardCollection stock = new CardCollection(true);
		CardCollection waste = new CardCollection(false);
		Foundation[] foundations = {new Foundation('h'), new Foundation('d'), new Foundation('s'), new Foundation('c')};
		CardCollection[] tableau = new CardCollection[7];
		for (int i = 0; i < 7; i++) {
			tableau[i] = new CardCollection();
		}

		// Adds cards to tableau
		stock.shuffle();
		for (int i = 0; i < 7; i++) {
			for (int j = i; j < 7; j++) {
				tableau[j].addCard(stock.popLastCard());
			}
		}

		//Turns last cards in tableau face up
		for (CardCollection t : tableau) {
			t.getLastCard().flip();
		}
	}

	@Override
	public void render () {




		// Clears screen
		Gdx.gl.glClearColor(0.1f, 1, 0.1f, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

		// Updates camera
		camera.update();
		batch.setProjectionMatrix(camera.combined);

		batch.begin();

		font12.draw(batch, "Hello World", 200, 200);
		batch.draw(testImg, 0, 0);

		batch.end();
	}
	
	@Override
	public void dispose () {
		batch.dispose();
		font12.dispose();
		testImg.dispose();
	}
}
