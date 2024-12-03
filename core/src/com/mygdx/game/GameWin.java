package com.mygdx.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

public class GameWin extends Actor implements Screen {
    public final static int BG_WIDTH = 1080;
    public final static int BG_HEIGHT = 1920;

    protected Stage stage;
    private Viewport viewport;
    private OrthographicCamera camera;
    private BitmapFont font;
    Image homeButton;

    Texture background;
    Texture playButtonActive;
    Texture playButtonInactive;
    Texture victory;
    Texture gameOver;
    Texture score;
    Texture homeActive;
    Texture homeInactive;
    Texture win;
    Texture win1;
    Texture lose;
    Texture lose1;
    MyGdxGame game;
    MainMenu menu;
    Sound winSfx;

    public GameWin(MyGdxGame game) {
        this.game = game;
        this.camera = new OrthographicCamera();
        this.viewport = new FitViewport(BG_WIDTH, BG_HEIGHT, camera);
        this.viewport.apply();
        this.font = font;
    }

    @Override
    public void show() {
        stage = new Stage(viewport, game.batch);
        Gdx.input.setInputProcessor(stage);

        playButtonActive = new Texture("play_button_active.png");
        playButtonInactive = new Texture("play_button_inactive.png");
        background = new Texture("bg.png");
        victory = new Texture("victory.png");
        gameOver = new Texture("game_over.png");
        score = new Texture("score.png");
        homeActive = new Texture("home_active.png");
        homeInactive = new Texture("home_inactive.png");
        win = new Texture("you_win(1).png");
        lose = new Texture("you_lose(1).png");

        //Deklarasi sound menang
        winSfx = Gdx.audio.newSound(Gdx.files.internal("victory_stage_clear.mp3"));
        winSfx.play(0.6f);


        createPlayButton();
    }

    private void createPlayButton(){
        float playButtonScale = 1.2f;
        float playButtonWidth = playButtonInactive.getWidth() * playButtonScale;
        float playButtonHeight = playButtonInactive.getHeight() * playButtonScale;
        float playButtonX = (viewport.getWorldWidth() - playButtonInactive.getWidth()) / 2f;
        float playButtonY = (viewport.getWorldHeight() - playButtonInactive.getHeight())/ 2f - 50;
        float playButtonXOffset = (playButtonWidth - playButtonInactive.getWidth()) /2f;

        final Image playButton = new Image(playButtonInactive);
        playButton.setPosition(playButtonX - playButtonXOffset, playButtonY);
        playButton.setSize(playButtonWidth, playButtonHeight);
        stage.addActor(playButton);

        playButton.addListener(new ClickListener() {

            // ini untuk ngeredirect dari layar MainMenu ke layar GameScreen,
            // ini bisa diubah kok nanti tinggal ubah aja dari new GameScreen() ke class nama game yang kamu punya
            @Override
            public void clicked(InputEvent event, float x, float y) {
                game.resetGame();
                if (game.getGameState().equals("START")) {
                    game.setGameState("PLAYING");  // Change game state to "PLAYING"
                    game.setScreen(new GameScreen(game));  // Change screen to GameScreen
                }
                System.out.println(game.getGameState());
            }

            @Override
            public void enter(InputEvent event, float x, float y, int pointer, Actor fromActor) {
                playButton.setDrawable(new TextureRegionDrawable(new TextureRegion(playButtonActive)));

            }

            @Override
            public void exit(InputEvent event, float x, float y, int pointer, Actor toActor) {
                playButton.setDrawable(new TextureRegionDrawable(new TextureRegion(playButtonInactive)));
            }
        });

        stage.addActor(playButton);
    }


    @Override
    public void render(float delta) {
        ScreenUtils.clear(1, 0, 0, 1);
        Gdx.gl.glClearColor(.1f, .1f, .15f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        // update for camera and viewport
        viewport.update(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        game.batch.setProjectionMatrix(camera.combined);
        game.batch.begin();

        // Drawing background
        game.batch.draw(background, 0, 0, viewport.getWorldWidth(), viewport.getWorldHeight());

        // Drawing gameover
        float gameOverScale = 1.1f;
        float gameOverWidth = gameOver.getWidth() * gameOverScale;
        float gameOverHeight = gameOver.getHeight() * gameOverScale;
        float gameOverX = (viewport.getWorldWidth() - gameOver.getWidth()) / 2f;
        float gameOverY = (viewport.getWorldHeight() - gameOver.getHeight()) - 250;
        float gameOverXOffset = (gameOverWidth - gameOver.getWidth()) / 2f;
        game.batch.draw(gameOver, gameOverX - gameOverXOffset, gameOverY, gameOverWidth, gameOverHeight);

        // Drawing win
        float winScale = 1.05f;
        float winWidth = win.getWidth() * winScale;
        float winHeight = win.getHeight() * winScale;
        float winX = (viewport.getWorldWidth() - win.getWidth()) / 2f;
        float winY = (viewport.getWorldHeight() - win.getHeight()) - 200f;
        float winXOffset = (winWidth - win.getWidth()) / 2f;
        game.batch.draw(win, winX - winXOffset, winY, winWidth, winHeight);

//        float touchX = Gdx.input.getX();
//        float touchY = Gdx.graphics.getHeight() - Gdx.input.getY();
//        if (isHomeButtonClicked(touchX, touchY)) {
//            game.setScreen(new MainMenu(game));
//        }

        game.batch.end();

        stage.act(delta);
        stage.draw();
    }

    @Override
    public void resize(int width, int height) {
        viewport.update(width, height);
        camera.position.set(camera.viewportWidth / 2f, camera.viewportHeight / 2f, 0);
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
        background.dispose();
        homeInactive.dispose();
        homeActive.dispose();
        playButtonActive.dispose();
        playButtonInactive.dispose();
        victory.dispose();
        gameOver.dispose();
        score.dispose();
        win.dispose();
        lose.dispose();
    }
}


