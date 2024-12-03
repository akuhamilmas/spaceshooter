package com.mygdx.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
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

public class MainMenu implements Screen {
    // Screen size
    public final static int BG_WIDTH = 1080;
    public final static int BG_HEIGHT = 1920;

    protected Stage stage;
    private Viewport viewport;
    private OrthographicCamera camera;

    GameLose gameLoseScreen;


    // asset
    Texture laser;
    Texture background;
    Texture backgroundGround;
    Texture gameExplanation;
    Texture gameTitle;
    Texture apple;
    Texture playButtonActive;
    Texture playButtonInactive;
    Texture pesawat;

    Texture matahari;
    Texture carrot;
    Texture leaves;
    MyGdxGame game;
    Music mainMenuMusic;

    GameScreen gameScreen;

    public MainMenu(MyGdxGame game) {
        this.game = game;
        this.camera = new OrthographicCamera();
        this.viewport = new FitViewport(BG_WIDTH, BG_HEIGHT, camera);
        this.viewport.apply();
        this.gameScreen = new GameScreen(game);
    }

    @Override
    public void show() {
        stage = new Stage(viewport, game.batch);
        Gdx.input.setInputProcessor(stage);


        laser = new Texture("laser.png");
        background = new Texture("bg.png");
        //backgroundGround = new Texture("bg_ground.png");
        gameExplanation = new Texture("game_explanation.png");
        gameTitle = new Texture("game_title.png");
        apple = new Texture("medium.png");
        playButtonActive = new Texture("play_button_active.png");
        playButtonInactive = new Texture("play_button_inactive.png");
        pesawat =new Texture("pesawat.png");
        matahari = new Texture("matahari.png");
        carrot = new Texture("strong.png");
        leaves = new Texture("weak.png");

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

                if (game.getGameState().equals("START")) {
                    game.setGameState("PLAYING");  // Change game state to "PLAYING"
                    game.setScreen(new GameScreen(game));  // Change screen to GameScreen

                    if (game.currentScreen instanceof GameLose) {
                        ((GameLose) game.currentScreen).dispose();
                    }
                    if (game.currentScreen instanceof GameWin) {
                        ((GameWin) game.currentScreen).dispose();
                    }
                }
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

        viewport.update(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        game.batch.setProjectionMatrix(camera.combined);
        game.batch.begin();

        // Drawing background
        game.batch.draw(background, 0, 0,viewport.getWorldWidth(), viewport.getWorldHeight());

//        // Drawing ground for background
//        game.batch.draw(backgroundGround, 0, 0, viewport.getWorldWidth(), viewport.getWorldHeight());

        // Drawing game title
        float gameTitleScale = 1.25f;
        float gameTitleWidth = gameTitle.getWidth() * gameTitleScale;
        float gameTitleHeight = gameTitle.getHeight() * gameTitleScale;
        float gameTitleX = (viewport.getWorldWidth() - gameTitle.getWidth()) /2f;
        float gameTitleY = (viewport.getWorldHeight() - gameTitle.getHeight()) - 250;
        float gameTitleXOffset = (gameTitleWidth - gameTitle.getWidth()) / 2f;
        game.batch.draw(gameTitle, gameTitleX - gameTitleXOffset, gameTitleY, gameTitleWidth, gameTitleHeight);

        // Drawing game explanation
        float gameExplanationScale = 1.13f;
        float gameExplanationWidth = gameExplanation.getWidth() * gameExplanationScale;
        float gameExplanationHeight = gameExplanation.getHeight() * gameExplanationScale;
        float gameExplanationX = (viewport.getWorldWidth() - gameExplanation.getWidth()) /2f;
        float gameExplanationY = (viewport.getWorldHeight() - gameExplanation.getHeight()) - 500;
        float gameExplanationXOffset = (gameExplanationWidth - gameExplanation.getWidth()) /2f;
        game.batch.draw(gameExplanation, gameExplanationX - gameExplanationXOffset, gameExplanationY, gameExplanationWidth, gameExplanationHeight);

        // ini untuk membuat pesawat /Drawing pesawat
        float pesawatScale = 0.3f;
        float pesawatWidth = pesawat.getWidth() * pesawatScale;
        float pesawatHeight = pesawat.getHeight() * pesawatScale;
        float pesawatX = (viewport.getWorldWidth() - pesawat.getWidth()) / 2f;
        float pesawatY = (viewport.getWorldHeight() - pesawat.getHeight())/ 2f;
        float pesawatXOffset = (pesawatWidth - pesawat.getWidth()) / 2f;
        game.batch.draw(pesawat, pesawatX - pesawatXOffset, pesawatY, pesawatWidth, pesawatHeight);

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
        if(background != null) background.dispose();
        if(gameTitle != null) gameTitle.dispose();
        if(playButtonInactive != null) playButtonInactive.dispose();
        if(playButtonActive != null) playButtonActive.dispose();
        if(laser!= null) laser.dispose();
        if(backgroundGround != null) backgroundGround.dispose();
        if(gameExplanation != null) gameExplanation.dispose();
        if(apple != null) apple.dispose();
        if(pesawat != null) pesawat.dispose();
        if(matahari != null) matahari.dispose();
        if(carrot != null) carrot.dispose();
        if(leaves != null) leaves.dispose();
        if(stage != null) stage.dispose();
    }
}
