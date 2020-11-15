package com.github.solutetaire;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;

public class MenuScreen implements Screen{
    final SoluteTaire game;

    private OrthographicCamera camera;

    public MenuScreen(final SoluteTaire game) {
        this.game = game;

        // Sets camera
        camera = new OrthographicCamera();
        camera.setToOrtho(false, game.ui.getScreenW(), game.ui.getScreenH());
    }

    @Override
    public void render(float delta) {
        // If clicked or key pressed
        if(Gdx.input.isTouched() | Gdx.input.isKeyPressed(Input.Keys.ANY_KEY)) {
            game.setGameScreen();
        }

        // Clears screen
        Gdx.gl.glClearColor(0.4f, 0.2f, 0.6f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        // Updates camera
        camera.update();
        game.batch.setProjectionMatrix(camera.combined);

        game.batch.begin();
        game.fontLarge.draw(game.batch, "SoluteTaire", game.ui.getTitle()[0], game.ui.getTitle()[1]);
        game.fontMedium.draw(game.batch, "Press anything to start.", game.ui.getPlayButton()[0], game.ui.getPlayButton()[1]);
        game.batch.end();
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
    }
}
