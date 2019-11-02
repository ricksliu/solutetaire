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
	private Texture cardSpaceImage;
	private Texture cardBackImage;
	private Texture heartImage;
	private Texture diamondImage;
	private Texture spadeImage;
	private Texture clubImage;
	private Rectangle test;

	// Stores cards
	private CardCollection stock;
	private CardCollection waste;
	private Foundation[] foundations = new Foundation[4];
	private CardCollection[] tableau = new CardCollection[7];

	public SoluteTaire() {
		// Sets up all stacks of cards
		stock = new CardCollection(true);
		waste = new CardCollection(false);
		foundations[0] = new Foundation('s');
		foundations[1] = new Foundation('h');
		foundations[2] = new Foundation('c');
		foundations[3] = new Foundation('d');

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
	}

	@Override
	public void create() {
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

		// Creates images
		cardSpaceImage = new Texture(Gdx.files.internal("cardSpace.png"));
		cardBackImage = new Texture(Gdx.files.internal("cardBack.png"));
		heartImage = new Texture(Gdx.files.internal("heart.png"));
		diamondImage = new Texture(Gdx.files.internal("diamond.png"));
		spadeImage = new Texture(Gdx.files.internal("spade.png"));
		clubImage = new Texture(Gdx.files.internal("club.png"));
	}

	@Override
	public void render() {
		// Clears screen
		Gdx.gl.glClearColor(0.1f, 1, 0.1f, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

		// Updates camera
		camera.update();
		batch.setProjectionMatrix(camera.combined);

		// Shorter names for dimensions of various things
		float w = Gdx.graphics.getWidth();
		float h = Gdx.graphics.getHeight();
		float cardW = w / 12;
		float cardH = h / 4;

		batch.begin();

		// Draws stock
		batch.draw(cardBackImage, w * 1 / 8 - cardW / 2, h * 4 / 5 - cardH / 2, cardW, cardH);

		// Draws waste
		if (waste.getSize() == 0) {
			batch.draw(cardSpaceImage, w * 2 / 8 - cardW / 2, h * 4 / 5 - cardH / 2, cardW, cardH);
		} else {

		}

		// Draws foundations
		for (int i = 0; i < 4; i++) {
			if (foundations[i].getSize() == 0) {
				batch.draw(cardSpaceImage, w * (4 + i) / 8 - cardW / 2, h * 4 / 5 - cardH / 2, cardW, cardH);
			} else {
				batch.draw(cardBackImage, w * (4 + i) / 8 - cardW / 2, h * 4 / 5 - cardH / 2, cardW, cardH);
			}
		}

		// Draws tableau
		for (int i = 0; i < 7; i++) {
			if (tableau[i].getSize() == 0) {
				batch.draw(cardSpaceImage, w * (1 + i) / 8 - cardW / 2, h / 2 - cardH / 2, cardW, cardH);
			} else {
				for (int j = 0; j < tableau[i].getSize(); j++) {
					if (j == tableau[i].getSize() - 1) {
						drawCard(w * (1 + i) / 8 - cardW / 2, h / 2 - cardH / 2 - h / 24 * j, cardW, cardH, tableau[i].getLastCard().getSuit(), tableau[i].getLastCard().getRank());
					} else {
						batch.draw(cardBackImage, w * (1 + i) / 8 - cardW / 2, h / 2 - cardH / 2 - h / 24 * j, cardW, cardH);
					}
				}
			}
		}

		// font12.draw(batch, "Hello World", 200, 200);

		batch.end();
	}
	
	@Override
	public void dispose() {
		batch.dispose();
		font12.dispose();
		cardSpaceImage.dispose();
		cardBackImage.dispose();
		heartImage.dispose();
		diamondImage.dispose();
		spadeImage.dispose();
		clubImage.dispose();
	}

	public void drawCard(float x, float y, float w, float h, char suit, int rank) {
		switch (suit)
		{
			case 's':
				batch.draw(spadeImage, x, y, w, h);
				break;
			case 'h':
				batch.draw(heartImage, x, y, w, h);
				break;
			case 'c':
				batch.draw(clubImage, x, y, w, h);
				break;
			case 'd':
				batch.draw(diamondImage, x, y, w, h);
				break;
		}
	}
}
