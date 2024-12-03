package com.mygdx.game;

import com.badlogic.gdx.Screen;

public class GameScreen implements Screen {
    MyGdxGame game; // ini merujuk ke kelas MyGdxGame Anda, yang akan saya ganti menjadi GameScreen.
    MainMenu menu;
    boolean paused;

    public GameScreen(MyGdxGame game) {
        this.game = game;
    }

    public GameScreen(MainMenu menu) {
        this.menu = menu;
    }

    @Override
    public void show() {

    }

    @Override
    public void render(float delta) {


    }

    @Override
    public void resize(int width, int height) {

    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void hide() {

    }

    @Override
    public void dispose() {

    }
}
