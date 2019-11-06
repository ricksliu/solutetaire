package com.github.solutetaire;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Vector3;

public class MenuScreen implements Screen{
    final SoluteTaire game;
    private Vector3 mouse;

    private OrthographicCamera camera;

    public MenuScreen(final SoluteTaire game) {
        this.game = game;

        // Creates and sets camera
        camera = new OrthographicCamera();
        camera.setToOrtho(false, 1280, 720);
    }

    @Override
    public void render(float delta) {
        // If clicked, gets mouse position and checks for other actions
        if(Gdx.input.isTouched()) {
            mouse.set(Gdx.input.getX(), Gdx.input.getY(), 0);
            camera.unproject(mouse);
        }

        // Clears screen
        Gdx.gl.glClearColor(0.4f, 0.2f, 0.6f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        // Updates camera
        camera.update();
        game.batch.setProjectionMatrix(camera.combined);

        game.batch.begin();

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
