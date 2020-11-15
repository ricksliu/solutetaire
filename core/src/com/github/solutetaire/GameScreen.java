package com.github.solutetaire;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector3;

public class GameScreen implements Screen{
    final SoluteTaire game;
    private int timeElapsed;  // Stores time since game started

    private OrthographicCamera camera;

    private Texture cardSpaceImage;
    private Texture cardBackImage;
    private Texture cardSpadeImage;
    private Texture cardHeartImage;
    private Texture cardClubImage;
    private Texture cardDiamondImage;
    private Texture cardSpaceSpadeImage;
    private Texture cardSpaceHeartImage;
    private Texture cardSpaceClubImage;
    private Texture cardSpaceDiamondImage;

    // Stores cards
    private CardCollection stock;
    private CardCollection waste;
    private Foundation[] foundations;
    private CardCollection[] tableau;
    private CardCollection hand;  // Cards being dragged around by the mouse

    private char[] handOrigin = new char[2];  // Stores where cards in hand came from; letter, then integer as a char
    private boolean pouring;
    private float backgroundHeight;

    public GameScreen(final SoluteTaire game) {
        this.game = game;
        timeElapsed = 0;
        game.initialClick = true;

        // Sets camera
        camera = new OrthographicCamera();
        camera.setToOrtho(false, game.ui.getScreenW(), game.ui.getScreenH());

        // Creates images
        cardSpaceImage = new Texture(Gdx.files.internal("cardSpace.png"));
        cardBackImage = new Texture(Gdx.files.internal("cardBack.png"));
        cardSpadeImage = new Texture(Gdx.files.internal("cardSpade.png"));
        cardHeartImage = new Texture(Gdx.files.internal("cardHeart.png"));
        cardClubImage = new Texture(Gdx.files.internal("cardClub.png"));
        cardDiamondImage = new Texture(Gdx.files.internal("cardDiamond.png"));
        cardSpaceSpadeImage = new Texture(Gdx.files.internal("cardSpaceSpade.png"));
        cardSpaceHeartImage = new Texture(Gdx.files.internal("cardSpaceHeart.png"));
        cardSpaceClubImage = new Texture(Gdx.files.internal("cardSpaceClub.png"));
        cardSpaceDiamondImage = new Texture(Gdx.files.internal("cardSpaceDiamond.png"));

        // Sets up all the stacks of cards
        stock = new CardCollection(true);  // Initially puts all cards in stock
        waste = new CardCollection(false);
        foundations = new Foundation[4];
        foundations[0] = new Foundation('s');
        foundations[1] = new Foundation('h');
        foundations[2] = new Foundation('c');
        foundations[3] = new Foundation('d');
        tableau = new CardCollection[7];
        for (int i = 0; i < 7; i++) {
            tableau[i] = new CardCollection();
        }
        hand = new CardCollection(false);

        stock.shuffle();

        // Adds cards to tableau, removing them from stock
        for (int i = 0; i < 7; i++) {
            for (int j = i; j < 7; j++) {
                tableau[j].addCard(stock.popLastCard());
            }
        }

        // Flips last cards in tableau to be face up
        for (int i = 0; i < 7; i++) {
            tableau[i].getLastCard().flip();
        }

        // Sets initial card positions
        for (int i = 0; i < stock.getSize(); i++) {
            stock.getCard(i).setNewCoordinates(game.ui.getStock());
        }
        for (int i = 0; i < waste.getSize(); i++) {
            waste.getCard(i).setNewCoordinates(game.ui.getWaste());
        }
        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < foundations[i].getSize(); j++) {
                foundations[i].getCard(j).setNewCoordinates(game.ui.getFoundations(i));
            }
        }
        for (int i = 0; i < 7; i++) {
            for (int j = 0; j < tableau[i].getSize(); j++) {
                tableau[i].getCard(j).setNewCoordinates(game.ui.getTableau(i, j));
            }
        }

        pouring = false;
        backgroundHeight = 0;
    }

    @Override
    public void render(float delta) {
        updateLogic();
        updateGraphics();
        drawGraphics();
        checkVictory();
    }

    @Override
    public void resize(int width, int height) {
    }

    @Override
    public void show() {
    }

    @Override
    public void hide() {
    }

    @Override
    public void pause() {
    }

    @Override
    public void resume() {
    }

    @Override
    public void dispose() {
        cardSpaceImage.dispose();
        cardBackImage.dispose();
        cardHeartImage.dispose();
        cardDiamondImage.dispose();
        cardSpadeImage.dispose();
        cardClubImage.dispose();
        cardSpaceSpadeImage.dispose();
        cardSpaceHeartImage.dispose();
        cardSpaceClubImage.dispose();
        cardSpaceDiamondImage.dispose();
    }

    // Updates logic of game, such as where cards are located data-structure wise
    private void updateLogic() {
        timeElapsed++;

        // If mouse is being clicked/held down
        if (Gdx.input.isTouched()) {
            game.mouse.set(Gdx.input.getX(), Gdx.input.getY(), 0);
            camera.unproject(game.mouse);

            // If initial click and hand is empty
            if (hand.getSize() == 0 & game.initialClick) {

                // If clicking on stock
                if (game.isInside(game.mouse.x, game.mouse.y, game.ui.getStock()[0], game.ui.getStock()[1], game.ui.getCardW(), game.ui.getCardH())) {
                    // If stock is empty, takes everything in waste, reverses it, and moves it to stock
                    if (stock.getSize() == 0) {
                        waste.reverse();
                        stock.setCards(waste.getCards());
                        waste.clear();
                        stock.flipAll();
                        for (int i = 0; i < stock.getSize(); i++) {
                            stock.getCard(i).setNewCoordinates(game.ui.getStock());
                        }
                        game.cardDownSound.play();
                    // If stock is not empty, moves top card to waste
                    } else {
                        waste.addCard(stock.popLastCard());
                        waste.getLastCard().flip();
                        waste.getLastCard().setNewCoordinates(game.ui.getWaste());
                        game.cardUpSound.play();
                    }
                }

                // If clicking on waste and waste is not empty
                if (game.isInside(game.mouse.x, game.mouse.y, game.ui.getWaste()[0], game.ui.getWaste()[1], game.ui.getCardW(), game.ui.getCardH()) & waste.getSize() > 0) {
                    hand.addCard(waste.popLastCard());
                    handOrigin[0] = 'w';
                    game.cardUpSound.play();
                }

                // If clicking on foundation and foundation is not empty
                for (int i = 0; i < 4; i++) {
                    if (game.isInside(game.mouse.x, game.mouse.y, game.ui.getFoundations(i)[0], game.ui.getFoundations(i)[1], game.ui.getCardW(), game.ui.getCardH()) & foundations[i].getSize() > 0) {
                        hand.addCard(foundations[i].popLastCard());
                        handOrigin[0] = 'f';
                        handOrigin[1] = (char) i;
                        game.cardUpSound.play();
                    }
                }

                // If clicking on tableau, tableau is not empty, and tableau card is face up
                for (int i = 0; i < 7; i++) {
                    // For loop is reversed so cards on top are prioritized
                    for (int j = tableau[i].getSize() - 1; j >= 0; j--) {
                        if (game.isInside(game.mouse.x, game.mouse.y, game.ui.getTableau(i, j)[0], game.ui.getTableau(i, j)[1], game.ui.getCardW(), game.ui.getCardH()) & tableau[i].getSize() > 0) {
                            if (tableau[i].getCard(j).isFaceUp()) {
                                hand.addCards(tableau[i].getCards(j));
                                tableau[i].clear(j);
                                handOrigin[0] = 't';
                                handOrigin[1] = (char) i;
                                game.cardUpSound.play();
                                break;
                            }
                        }
                    }
                }
            }

            game.initialClick = false;

        // If not clicked (mouse is not being held down)
        } else {
            // If hand is not empty, tries to empty it
            while (hand.getSize() > 0) {
                // Tries to place cards in foundation
                for (int i = 0; i < 4; i++) {
                    // If hovering over foundation and user is only holding one card
                    if (game.isInside(game.mouse.x, game.mouse.y, game.ui.getFoundations(i)[0], game.ui.getFoundations(i)[1], game.ui.getCardW(), game.ui.getCardH()) & hand.getSize() == 1) {
                        // If foundation is empty
                        if (foundations[i].getSize() == 0) {
                            // If card matches suit of foundation and is an ace
                            if (hand.getLastCard().getSuit() == foundations[i].getSuit() & hand.getLastCard().getRank() == 1) {
                                foundations[i].addCard(hand.popLastCard());
                                foundations[i].getLastCard().setNewCoordinates(game.ui.getFoundations(i));
                                // Starts pouring animation if card was water
                                if (i == 0) {
                                    pouring = true;
                                }
                                game.cardDownSound.play();
                            }
                        // If foundation is not empty
                        } else {
                            // If card matches suit of foundation and is one bigger
                            if (hand.getLastCard().getSuit() == foundations[i].getSuit() & hand.getLastCard().getRank() == foundations[i].getLastCard().getRank() + 1) {
                                foundations[i].addCard(hand.popLastCard());
                                foundations[i].getLastCard().setNewCoordinates(game.ui.getFoundations(i));
                                // Starts pouring animation if card was water
                                if (i == 0) {
                                    pouring = true;
                                }
                                game.cardDownSound.play();
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
                    if (game.isInside(game.mouse.x, game.mouse.y, game.ui.getTableau(i, tableau[i].getSize() - 1)[0], game.ui.getTableau(i, tableau[i].getSize() - 1)[1], game.ui.getCardW(), game.ui.getCardH())) {
                        // If tableau is empty
                        if (tableau[i].getSize() == 0) {
                            // If card is a king
                            if (hand.getCard(0).getRank() == 13) {
                                tableau[i].setCards(hand.getCards());
                                for (int j = 0; j < tableau[i].getSize(); j++) {
                                    tableau[i].getCard(j).setNewCoordinates(game.ui.getTableau(i, j));
                                }
                                hand.clear();
                                game.cardDownSound.play();
                            }
                        // If tableau is not empty
                        } else {
                            // If first card in hand is opposite suit colour of last card in tableau and is one smaller
                            if (isOppositeColour(hand.getCard(0), tableau[i].getLastCard()) & hand.getCard(0).getRank() == tableau[i].getLastCard().getRank() - 1) {
                                tableau[i].addCards(hand.getCards());
                                for (int j = 0; j < tableau[i].getSize(); j++) {
                                    tableau[i].getCard(j).setNewCoordinates(game.ui.getTableau(i, j));
                                }
                                hand.clear();
                                game.cardDownSound.play();
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
                        waste.getLastCard().setNewCoordinates(game.ui.getWaste());
                        game.cardDownSound.play();
                        break;
                    case 'f':
                        foundations[(int) handOrigin[1]].addCard(hand.popLastCard());
                        foundations[(int) handOrigin[1]].getLastCard().setNewCoordinates(game.ui.getFoundations((int) handOrigin[1]));
                        if ((int) handOrigin[1] == 0) {
                            pouring = true;
                        }
                        game.cardDownSound.play();
                        break;
                    case 't':
                        tableau[(int) handOrigin[1]].addCards(hand.getCards());
                        hand.clear();
                        for (int i = 0; i < tableau[(int) handOrigin[1]].getSize(); i++) {
                            tableau[(int) handOrigin[1]].getCard(i).setNewCoordinates(game.ui.getTableau((int) handOrigin[1], i));
                        }
                        game.cardDownSound.play();
                        break;
                }
            }

            game.initialClick = true;
        }
    }

    // Updates positions of graphics
    private void updateGraphics() {
        // Sets new positions for cards in hand
        for (int i = 0; i < hand.getSize(); i++) {
            hand.getCard(i).setNewCoordinates(new float[] {game.mouse.x - game.ui.getCardW() / 2, game.mouse.y - game.ui.getCardH() / 2 - game.ui.getTableauVerticalSpacing() * i});
        }

        // Updates current card positions
        for (int i = 0; i < stock.getSize(); i++) {
            stock.getCard(i).updateCoordinates();
        }
        for (int i = 0; i < waste.getSize(); i++) {
            waste.getCard(i).updateCoordinates();
        }
        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < foundations[i].getSize(); j++) {
                foundations[i].getCard(j).updateCoordinates();
            }
        }
        for (int i = 0; i < 7; i++) {
            for (int j = 0; j < tableau[i].getSize(); j++) {
                tableau[i].getCard(j).updateCoordinates();
            }
        }
        for (int i = 0; i < hand.getSize(); i++) {
            hand.getCard(i).updateCoordinates();
        }
    }

    // Draws all graphics of game screen
    private void drawGraphics() {
        // Clears screen
        Gdx.gl.glClearColor(0.4f, 0.2f, 0.6f, 1f);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        // Updates camera
        camera.update();
        game.batch.setProjectionMatrix(camera.combined);
        game.shape.setProjectionMatrix(camera.combined);

        // Draws background visuals
        game.shape.begin(ShapeRenderer.ShapeType.Filled);

        // Calculates new background height
        backgroundHeight += 0.1f * (game.ui.getScreenH() * foundations[0].getSize() / 13f - backgroundHeight);

        // Calculates a modifier to how pink the solution will be based on amount of water and indicator
        float pinkModifier;
        if (foundations[0].getSize() == 0) {
            pinkModifier = 0;
        } else {
            pinkModifier = ((float) foundations[1].getSize()) / ((float) foundations[0].getSize());
        }

        // If no base or acid, makes solution blue
        if (foundations[2].getSize() + foundations[3].getSize() == 0) {
            game.shape.setColor(new Color(0.5f, 0.25f, 1f, 1f));
            // If more acid than base, makes solution blue
        } else if (foundations[2].getSize() < foundations[3].getSize()) {
            game.shape.setColor(new Color(0.5f, 0.25f, 1f, 1f));
            // If equal base and acid, makes solution slightly pink
        } else if (foundations[2].getSize() == foundations[3].getSize()) {
            game.shape.setColor(new Color(0.5f + 0.25f * pinkModifier, 0.25f, 1f, 1f));
            // If more base than acid, makes solution pink
        } else if (foundations[2].getSize() > foundations[3].getSize()) {
            game.shape.setColor(new Color(0.5f + 0.5f * pinkModifier, 0.25f, 1f, 1f));
        }

        // Draws the solution
        game.shape.rect(0, 0, game.ui.getScreenW(), backgroundHeight);

        // Draws a pouring animation
        if (pouring) {
            // If background height animation is 95% done, stops pouring animation
            if (game.ui.getScreenH() * foundations[0].getSize() / 13f - backgroundHeight < 0.05 * game.ui.getScreenH() * foundations[0].getSize() / 13f) {
                pouring = false;
            } else {
                game.shape.setColor(new Color(0.5f, 0.25f, 1f, 1f));
                game.shape.rect(game.ui.getScreenW() * 7 / 15, 0, game.ui.getScreenW() / 15, game.ui.getScreenH());
            }
        }

        game.shape.end();

        // Draws foreground visuals
        game.batch.begin();

        // Draws stock
        game.draw(game.ui.getStock()[0], game.ui.getStock()[1], game.ui.getCardW(), game.ui.getCardH(), cardSpaceImage);
        if (stock.getSize() > 0) {
            for (int i = 0; i < stock.getSize(); i++) {
                drawCard(stock.getCard(i));
            }
        }

        // Draws waste
        game.draw(game.ui.getWaste()[0], game.ui.getWaste()[1], game.ui.getCardW(), game.ui.getCardH(), cardSpaceImage);
        if (waste.getSize() > 0) {
            for (int i = 0; i < waste.getSize(); i++) {
                drawCard(waste.getCard(i));
            }
        }

        // Draws foundations
        for (int i = 0; i < 4; i++) {
            switch (i) {
                case 0:
                    game.draw(game.ui.getFoundations(i)[0], game.ui.getFoundations(i)[1], game.ui.getCardW(), game.ui.getCardH(), cardSpaceSpadeImage);
                    break;
                case 1:
                    game.draw(game.ui.getFoundations(i)[0], game.ui.getFoundations(i)[1], game.ui.getCardW(), game.ui.getCardH(), cardSpaceHeartImage);
                    break;
                case 2:
                    game.draw(game.ui.getFoundations(i)[0], game.ui.getFoundations(i)[1], game.ui.getCardW(), game.ui.getCardH(), cardSpaceClubImage);
                    break;
                case 3:
                    game.draw(game.ui.getFoundations(i)[0], game.ui.getFoundations(i)[1], game.ui.getCardW(), game.ui.getCardH(), cardSpaceDiamondImage);
                    break;
            }
            if (foundations[i].getSize() > 0) {
                for (int j = 0; j < foundations[i].getSize(); j++) {
                    drawCard(foundations[i].getCard(j));
                }
            }
        }

        // Draws tableau
        for (int i = 0; i < 7; i++) {
            game.draw(game.ui.getTableau(i)[0], game.ui.getTableau(i)[1], game.ui.getCardW(), game.ui.getCardH(), cardSpaceImage);
            if (tableau[i].getSize() > 0) {
                // Makes sure the last card is face up if user is not holding anything
                if (!tableau[i].getLastCard().isFaceUp() & hand.getSize() == 0) {
                    tableau[i].getLastCard().flip();
                }
                for (int j = 0; j < tableau[i].getSize(); j++) {
                    drawCard(tableau[i].getCard(j));
                }
            }
        }

        // Draws cards in hand
        if (hand.getSize() > 0) {
            for (int i = 0; i < hand.getSize(); i++) {
                drawCard(hand.getCard(i));
            }
        }

        game.batch.end();
    }

    // Checks if game is over
    private void checkVictory() {
        boolean victory = true;
        for (int i = 0; i < 4; i++) {
            if (foundations[i].getSize() < 13) {
                victory = false;
            }
        }
        if (victory) {
            game.setEndScreen();
        }
    }

    // Draws card with given location, dimensions, suit, and card
    private void drawCard(Card card) {
        float x = card.getCoordinates()[0];
        float y = card.getCoordinates()[1];
        float w = game.ui.getCardW();
        float h = game.ui.getCardH();

        // If face up
        if (card.isFaceUp()) {
            // Draws card background with suit
            switch (card.getSuit()) {
                case 's':
                    game.batch.draw(cardSpadeImage, x, y, w, h);
                    break;
                case 'h':
                    game.batch.draw(cardHeartImage, x, y, w, h);
                    break;
                case 'c':
                    game.batch.draw(cardClubImage, x, y, w, h);
                    break;
                case 'd':
                    game.batch.draw(cardDiamondImage, x, y, w, h);
                    break;
            }
            // Draws rank on top
            switch (card.getRank()) {
                case 1:
                    game.fontSmall.draw(game.batch, "A", x + w / 20, y + h * 19 / 20);
                    break;
                case 11:
                    game.fontSmall.draw(game.batch, "J", x + w / 20, y + h * 19 / 20);
                    break;
                case 12:
                    game.fontSmall.draw(game.batch, "Q", x + w / 20, y + h * 19 / 20);
                    break;
                case 13:
                    game.fontSmall.draw(game.batch, "K", x + w / 20, y + h * 19 / 20);
                    break;
                default:
                    game.fontSmall.draw(game.batch, String.valueOf(card.getRank()), x + w / 20, y + h * 19 / 20);
                    break;
            }
        // If face down
        } else {
            game.batch.draw(cardBackImage, x, y, w, h);
        }
    }

    // Checks if two cards are opposite colours
    private boolean isOppositeColour(Card a, Card b) {
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
