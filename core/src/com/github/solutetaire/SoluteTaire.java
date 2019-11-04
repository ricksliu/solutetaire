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
	private CardCollection hand;  // Cards being dragged around by the mouse

	private char[] handOrigin = new char[2];

	public SoluteTaire() {
		timeSinceClick = 0;

		// Sets up all the stacks of cards
		stock = new CardCollection(true);
		waste = new CardCollection(false);
		foundations[0] = new Foundation('s');
		foundations[1] = new Foundation('h');
		foundations[2] = new Foundation('c');
		foundations[3] = new Foundation('d');
		for (int i = 0; i < 7; i++) {
			tableau[i] = new CardCollection();
		}
		hand = new CardCollection(false);

		// Adds cards to tableau
		stock.shuffle();
		for (int i = 0; i < 7; i++) {
			for (int j = i; j < 7; j++) {
				tableau[j].addCard(stock.popLastCard());
			}
		}

		// Flips last cards in tableau
		for (int i = 0; i < 7; i++) {
			tableau[i].getLastCard().flip();
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
		// Clears screen
		Gdx.gl.glClearColor(0.1f, 1, 0.1f, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

		// Updates camera
		camera.update();
		batch.setProjectionMatrix(camera.combined);

		batch.begin();

		// Shorter names for dimensions of various things
		float w = Gdx.graphics.getWidth();
		float h = Gdx.graphics.getHeight();
		float cardW = w / 12;
		float cardH = h / 4;

		if (timeSinceClick < 10) {
			timeSinceClick++;
		}

		// If clicked, gets mouse position and checks for other actions
		if(Gdx.input.isTouched()) {
			mouse.set(Gdx.input.getX(), Gdx.input.getY(), 0);
			camera.unproject(mouse);

			// If clicking on stock
			if (isInside(mouse.x, mouse.y, w * 1 / 8 - cardW / 2, h * 4 / 5 - cardH / 2, cardW, cardH) & timeSinceClick >= 10) {
				// If stock is empty, take everything in waste, reverse it, and move it to stock
				if (stock.getSize() == 0) {
					waste.reverse();
					stock.setCards(waste.getCards());
					waste.clear();
					stock.flipAll();
				// If stock is not empty, move the top card to waste
				} else {
					waste.addCard(stock.popLastCard());
					waste.getLastCard().flip();
				}
			}

			// If clicking on waste and waste is not empty
			if (isInside(mouse.x, mouse.y, w * 2 / 8 - cardW / 2, h * 4 / 5 - cardH / 2, cardW, cardH) & waste.getSize() > 0) {
				// If user is not already holding something
				if (hand.getSize() == 0) {
					hand.addCard(waste.popLastCard());
					handOrigin[0] = 'w';
				}
			}

			// If clicking on foundation and foundation is not empty
			for (int i = 0; i < 4; i++) {
				if (isInside(mouse.x, mouse.y, w * (4 + i) / 8 - cardW / 2, h * 4 / 5 - cardH / 2, cardW, cardH) & foundations[i].getSize() > 0) {
					// If user is not already holding something
					if (hand.getSize() == 0) {
						hand.addCard(foundations[i].popLastCard());
						handOrigin[0] = 'f';
						handOrigin[1] = (char) i;
					}
				}
			}

			// If clicking on tableau and tableau is not empty
			for (int i = 0; i < 7; i++) {
				for (int j = 0; j < tableau[i].getSize(); j++) {
					if (isInside(mouse.x, mouse.y, w * (1 + i) / 8 - cardW / 2, h / 2 - cardH / 2 - h / 20 * j, cardW, cardH) & tableau[i].getSize() > 0) {
						// If user is not already holding something and tableau card is face up
						if (hand.getSize() == 0 & tableau[i].getCard(j).isFaceUp()) {
							hand.addCards(tableau[i].getCards(j));
							tableau[i].clear(j);
							handOrigin[0] = 't';
							handOrigin[1] = (char) i;
						}
					}
				}
			}

			if (timeSinceClick >= 10) {
				timeSinceClick = 0;
			}

		// If not clicked
		} else {
			// If hand is not empty, tries to empty it
			while (hand.getSize() > 0) {
				// Tries to place cards in foundation
				for (int i = 0; i < 4; i++) {
					// If hovering over foundation and is only holding one card
					if (isInside(mouse.x, mouse.y, w * (4 + i) / 8 - cardW / 2, h * 4 / 5 - cardH / 2, cardW, cardH) & hand.getSize() == 1) {
						// If foundation is empty
						if (foundations[i].getSize() == 0) {
							// If card matches suit of foundation and is an ace
							if (hand.getLastCard().getSuit() == foundations[i].getSuit() & hand.getLastCard().getRank() == 1) {
								foundations[i].addCard(hand.popLastCard());
							}
						// If foundation is not empty
						} else {
							// If card matches suit of foundation and is one bigger
							if (hand.getLastCard().getSuit() == foundations[i].getSuit() & hand.getLastCard().getRank() == foundations[i].getLastCard().getRank() + 1) {
								foundations[i].addCard(hand.popLastCard());
							}
						}
					}
					if (hand.getSize() == 0) {
						break;
					}
				}
				if (hand.getSize() == 0) {
					break;
				}

				// Tries to place cards in tableau
				for (int i = 0; i < 7; i++) {
					// If hovering over last card in tableau
					if (isInside(mouse.x, mouse.y, w * (1 + i) / 8 - cardW / 2, h / 2 - cardH / 2 - h / 20 * (tableau[i].getSize() - 1), cardW, cardH)) {
						// If tableau is empty
						if (tableau[i].getSize() == 0) {
							// If card is a king
							if (hand.getLastCard().getRank() == 13) {
								tableau[i].setCards(hand.getCards());
								hand.clear();
							}
						// If tableau is not empty
						} else {
							// If first card in hand is opposite suit colour of last card in tableau and is one smaller
							if (isOppositeColour(hand.getCard(0), tableau[i].getLastCard()) & hand.getCard(0).getRank() == tableau[i].getLastCard().getRank() - 1) {
								tableau[i].addCards(hand.getCards());
								hand.clear();
							}
						}
					}
					if (hand.getSize() == 0) {
						break;
					}
				}
				if (hand.getSize() == 0) {
					break;
				}

				// If all else fails, places cards back to where they originated from
				switch (handOrigin[0]) {
					case 'w':
						waste.addCard(hand.popLastCard());
						break;
					case 'f':
						foundations[(int) handOrigin[1]].addCard(hand.popLastCard());
						break;
					case 't':
						tableau[(int) handOrigin[1]].addCards(hand.getCards());
						hand.clear();
						break;
				}
			}
		}

		// Draws stock
		if (stock.getSize() == 0) {
			batch.draw(cardSpaceImage, w * 1 / 8 - cardW / 2, h * 4 / 5 - cardH / 2, cardW, cardH);
		} else {
			drawCard(w * 1 / 8 - cardW / 2, h * 4 / 5 - cardH / 2, cardW, cardH, stock.getLastCard());
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
				drawCard(w * (4 + i) / 8 - cardW / 2, h * 4 / 5 - cardH / 2, cardW, cardH, foundations[i].getLastCard());
			}
		}

		// Draws tableau
		for (int i = 0; i < 7; i++) {
			if (tableau[i].getSize() == 0) {
				batch.draw(cardSpaceImage, w * (1 + i) / 8 - cardW / 2, h / 2 - cardH / 2, cardW, cardH);
			} else {
				// Makes sure that last card is face up if not holding anything
				if (!tableau[i].getLastCard().isFaceUp() & hand.getSize() == 0) {
					tableau[i].getLastCard().flip();
				}

				for (int j = 0; j < tableau[i].getSize(); j++) {
					drawCard(w * (1 + i) / 8 - cardW / 2, h / 2 - cardH / 2 - h / 20 * j, cardW, cardH, tableau[i].getCard(j));
				}
			}
		}

		// Draws cards in hand
		if (hand.getSize() > 0) {
			for (int i = 0; i < hand.getSize(); i++) {
				drawCard(mouse.x - cardW / 2, mouse.y - cardH / 2 - h / 20 * i, cardW, cardH, hand.getCard(i));
			}
		}

		batch.end();

		// Checks if game is over
		boolean victory = true;
		for (int i = 0; i < 7; i++) {
			if (tableau[i].getSize() > 0) {
				victory = false;
			}
		}
		if (victory) {
			Gdx.app.exit();
		}
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
		// If face up
		if (card.isFaceUp()) {
			// Draws card background with suit
			switch (card.getSuit()) {
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
			switch (card.getRank()) {
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
		// If face down
		} else {
			batch.draw(cardBackImage, x, y, w, h);
		}
	}

	// Checks if two cards are opposite colours
	public boolean isOppositeColour(Card a, Card b) {
		if (a.getSuit() == 's' | a.getSuit() == 'c') {
			if (b.getSuit() == 'h' | b.getSuit() == 'd') {
				return true;
			} else {
				return false;
			}
		} else {
			if (b.getSuit() == 's' | b.getSuit() == 'c') {
				return true;
			} else {
				return false;
			}
		}
	}
}
