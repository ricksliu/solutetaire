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
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Vector3;
import java.util.Collections;

public class SoluteTaire extends ApplicationAdapter {
	private OrthographicCamera camera;
	private SpriteBatch batch;
	private BitmapFont fontSmall;
	private Texture cardSpaceImage;
	private Texture cardBackImage;
	private Texture heartImage;
	private Texture diamondImage;
	private Texture spadeImage;
	private Texture clubImage;

	private Vector3 mouse = new Vector3();
	private int timeSinceClick;

	// Stores cards
	private CardCollection stock;
	private CardCollection waste;
	private Foundation[] foundations = new Foundation[4];
	private CardCollection[] tableau = new CardCollection[7];

	public SoluteTaire() {
		timeSinceClick = 0;

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
		parameter.color = Color.BLACK;
		parameter.size = Gdx.graphics.getHeight() / 20;
		fontSmall = generator.generateFont(parameter);
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
		// Shorter names for dimensions of various things
		float w = Gdx.graphics.getWidth();
		float h = Gdx.graphics.getHeight();
		float cardW = w / 12;
		float cardH = h / 4;

		timeSinceClick++;

		// Gets mouse position if clicked
		if(Gdx.input.isTouched() & timeSinceClick >= 10) {
			mouse.set(Gdx.input.getX(), Gdx.input.getY(), 0);
			camera.unproject(mouse);
			timeSinceClick = 0;

			// If clicking on stock
			if (isInside(mouse.x, mouse.y, w * 1 / 8 - cardW / 2, h * 4 / 5 - cardH / 2, cardW, cardH)) {
				// If stock is empty, take everything in waste, reverse it, and move it to stock
				if (stock.getSize() == 0) {
					waste.reverse();
					stock.setCards(waste.getCards());
					waste.clear();
				// If stock is not empty, move the top card to waste
				} else {
					waste.addCard(stock.popLastCard());
				}
			}
		}



		// Clears screen
		Gdx.gl.glClearColor(0.1f, 1, 0.1f, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

		// Updates camera
		camera.update();
		batch.setProjectionMatrix(camera.combined);

		batch.begin();

		// Draws stock
		if (stock.getSize() == 0) {
			batch.draw(cardSpaceImage, w * 1 / 8 - cardW / 2, h * 4 / 5 - cardH / 2, cardW, cardH);
		} else {
			batch.draw(cardBackImage, w * 1 / 8 - cardW / 2, h * 4 / 5 - cardH / 2, cardW, cardH);
		}

		// Draws waste
		if (waste.getSize() == 0) {
			batch.draw(cardSpaceImage, w * 2 / 8 - cardW / 2, h * 4 / 5 - cardH / 2, cardW, cardH);
		} else {
			drawCard(w * 2 / 8 - cardW / 2, h * 4 / 5 - cardH / 2, cardW, cardH, waste.getLastCard());
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
						drawCard(w * (1 + i) / 8 - cardW / 2, h / 2 - cardH / 2 - h / 20 * j, cardW, cardH, tableau[i].getLastCard());
					} else {
						batch.draw(cardBackImage, w * (1 + i) / 8 - cardW / 2, h / 2 - cardH / 2 - h / 20 * j, cardW, cardH);
					}
				}
			}
		}

		batch.end();
	}
	
	@Override
	public void dispose() {
		batch.dispose();
		fontSmall.dispose();
		cardSpaceImage.dispose();
		cardBackImage.dispose();
		heartImage.dispose();
		diamondImage.dispose();
		spadeImage.dispose();
		clubImage.dispose();
	}

	// Checks if given coordinates are inside given location and dimensions
	public boolean isInside(float x0, float y0, float x, float y, float w, float h) {
		if (x <= x0 & x0 <= x + w & y <= y0 & y0 <= y + h) {
			return true;
		} else {
			return false;
		}
	}

	// Draws cards with given location, dimensions, suit, and card
	public void drawCard(float x, float y, float w, float h, Card card) {
		// Draws card background with suit
		switch(card.getSuit()) {
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
		// Draws rank on top
		switch(card.getRank()) {
			case 1:
				fontSmall.draw(batch, "A", x + w / 20, y + h * 19 / 20);
				break;
			case 11:
				fontSmall.draw(batch, "J", x + w / 20, y + h * 19 / 20);
				break;
			case 12:
				fontSmall.draw(batch, "Q", x + w / 20, y + h * 19 / 20);
				break;
			case 13:
				fontSmall.draw(batch, "K", x + w / 20, y + h * 19 / 20);
				break;
			default:
				fontSmall.draw(batch, String.valueOf(card.getRank()), x + w / 20, y + h * 19 / 20);
				break;
		}
	}
}
