package com.mygdx.game;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

import java.io.*;
import java.util.ArrayList;
import java.util.Random;

import static com.badlogic.gdx.math.MathUtils.random;

public class MyGdxGame extends ApplicationAdapter {

    private String gameState;

    public String getGameState() {
        return gameState;
    }

    public void setGameState(String gameState) {
        this.gameState = gameState;
    }

    final int MAP_WIDTH = 1080; //Lebar map
    final int MAP_HEIGHT = 1920; //tinggi map
    boolean gameOverLose;
    private boolean gameOverWin;
    private final int monsterPos = MAP_HEIGHT - 300; //start position makanan
    private static final int totalMonster = 8; //banyak monster per baris
    private static final int totalRow = 6; //jumlah row monster
    private static final float monsterSize = 64;
    private static final int monsterDistance = 60; //jarak antar makanan
    private ArrayList<Monster> monsters = new ArrayList<>();
    private ArrayList<Matahari> matahariArrayList = new ArrayList<>();
    private ArrayList<Bulan> Bulan = new ArrayList<>();
    public GameLose gameLoseScreen;
    public GameWin gameWinScreen;
    public MainMenu mainMenuScreen;
    private GameScreen gameScreen;


    public MyGdxGame() {
        // inisialisasi objek-objek lainnya
        gameScreen = new GameScreen(this);
    }

    public GameScreen getGameScreen() {
        return gameScreen;
    }


    Player player;
    SpriteBatch batch;
    OrthographicCamera cam;
    Viewport viewport;
    Texture map;
    Texture energy;
    int playerHealth;
    int energyWidth = 89;

    int energyHeight = 89;

    private float matahariTime;
    private float bulantime;

    BitmapFont timeFont;
    private static final float timer = 45f;
    private float timerCount = 45f;
    BitmapFont scoreFont;
    private int score = 0;
    private boolean saveTime;
    private int lastTime;
    int highScore;
    public Screen currentScreen;


    private Music mainMusic;
    private Music mainMenuMusic;
    private Sound laserSfx;
    private Sound laserHitSfx;
    private Sound pesawatDeathSfx;
    private Sound matahariDropSfx;
    private boolean laserSfxPlayed = false;
    private float laserSfxCooldown = 0;

    //create untuk membuat display awal tru asset sound,dll
    @Override
    public void create() {
        // Deklarasi viewport & camera
        float w = Gdx.graphics.getWidth();
        float h = Gdx.graphics.getHeight();
        cam = new OrthographicCamera();
        viewport = new FitViewport((float) (MAP_WIDTH), MAP_HEIGHT, cam);
        cam.update();

        // Gambar map
        map = new Texture("bg.png");
        map.setWrap(Texture.TextureWrap.Repeat, Texture.TextureWrap.Repeat);


		// Deklarasi musik Game
		mainMusic = Gdx.audio.newMusic(Gdx.files.internal("in_game_sound.mp3"));
		mainMusic.setVolume(0.2f);
		mainMusic.setLooping(true);
		mainMusic.play();

		// Deklarasi Musik MainMenu
		mainMenuMusic = Gdx.audio.newMusic(Gdx.files.internal("main_menu_sound.mp3"));
		mainMenuMusic.setVolume(0.5f);
		mainMenuMusic.setLooping(true);
		mainMenuMusic.play();



		// Deklarasi sound effect
		laserSfx = Gdx.audio.newSound(Gdx.files.internal("laser_sound.mp3"));
		laserHitSfx = Gdx.audio.newSound(Gdx.files.internal("laser_impact.mp3"));
		pesawatDeathSfx = Gdx.audio.newSound(Gdx.files.internal("pesawat_hancur_sound.mp3"));
		matahariDropSfx = Gdx.audio.newSound(Gdx.files.internal("matahari_drop.mp3"));


		// Deklarasi pesawat
		batch = new SpriteBatch();
		player = new Player("pesawat.png", MAP_WIDTH, MAP_HEIGHT, laserSfx);

        // Deklarasi monster
        int monsterRowWidth = (int) (totalMonster * (monsterSize + monsterDistance) - monsterDistance); // Total lebar semua monster dalam satu baris
        int startPos = (int) ((MAP_WIDTH - monsterRowWidth) / 1.5 / 2); // Menghitung posisi x awal agar makanan berada di tengah

        //menyusun monster agar muncul dari kiri ke kanan lalu atas ke bawah
        for (int i = 0; i < totalRow; i++) { //loop baris
            for (int j = 0; j < totalMonster; j++) { //loop kolom
                float posX = startPos + j * (monsterSize + monsterDistance);
                float posY = monsterPos - i * (monsterSize + monsterDistance);

                //jika index baris 0, maka memunculkan strong monster
                if (i == 0) {
                    monsters.add(new StrongEnemy(posX, posY));
                }

                //jika index baris 1 atau 2 muncul monster medium
                else if (i == 1 || i == 2) {
                    monsters.add(new MediumEnemy(posX, posY));
                }

                //jika index baris 3 atau 4 atau 5, muncul monster strong
                else {
                    monsters.add(new WeakEnemy(posX, posY));
                }
            }
        }

        //Deklarasi matahari
        matahariTime = 1.5f;
        Random random1 = new Random();
        for (int i = 0; i < 4; i++) { //maksimal memunculkan 4 matahri
            float posX = monsterSize / 2f + random1.nextFloat() * (MAP_WIDTH - monsterSize * 2); // perlu dikurangi dengan 2 kali ukuran matahari untuk mencegah muncul di luar layar
            float posY = MAP_HEIGHT - monsterSize / 2f - random1.nextFloat() * (MAP_HEIGHT - monsterSize * 2);  // agar matahari muncul dari atas tapi tidak melebihi MAP_HEIGHT
            matahariArrayList.add(new Matahari("matahari.png", posX, posY, matahariTime));
        }

        //Deklarasi bulan
         bulantime = 1.5f;
        Random random2 = new Random();
        for (int i = 0; i < 4; i++) { //maksimal memunculkan 4 bulan
            float posX = monsterSize / 2f + random2.nextFloat() * (MAP_WIDTH - monsterSize * 2); // perlu dikurangi dengan 2 kali ukuran bulan untuk mencegah muncul di luar layar
            float posY = MAP_HEIGHT - monsterSize / 2f - random2.nextFloat() * (MAP_HEIGHT - monsterSize * 2);  // agar bulan muncul dari atas tapi tidak melebihi MAP_HEIGHT
            Bulan.add(new Bulan("bulan.png", posX, posY, bulantime));
        }


        // Score font
        FreeTypeFontGenerator generator = new FreeTypeFontGenerator(Gdx.files.internal("Pixeled.ttf"));
        FreeTypeFontGenerator.FreeTypeFontParameter parameter = new FreeTypeFontGenerator.FreeTypeFontParameter();
        parameter.size = 40;
        scoreFont = generator.generateFont(parameter);

        // Timer
        timeFont = generator.generateFont(parameter);
        generator.dispose();
        saveTime = true;

        // Membaca file high score
        try {
            BufferedReader br = new BufferedReader(new FileReader("saveScore.txt"));
            highScore = Integer.parseInt(br.readLine());
            br.close();
        } catch (Exception e) {

        }

        // Deklarasi energy. Satu energy mewakili 20 health
        energy = new Texture("energy.png");
        playerHealth = player.getHealth() / 20;

        setGameState("START"); // Set the initial game state to "START"
        setScreen(new MainMenu(this));

        mainMenuScreen = new MainMenu(this); // Sets the current screen to MainMenu
        gameLoseScreen = new GameLose(this);
        gameWinScreen = new GameWin(this);

        setScreen(mainMenuScreen); // Activates the screen

    }
    //set scrren buat mindah tampilan jika == apa jika kalah sama dengan apa
    public void setScreen(Screen screen) {
        // Dispose the current screen and set the new screen
        if (currentScreen != null) {
            currentScreen.dispose();
        }
        currentScreen = screen;
        currentScreen.show();
        currentScreen.resize(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        if (gameState.equals("LOSE")) {
        }
        if (gameState.equals("WIN")) {
        }

    }
    //ini di game kaya play music, atau ini bisa digunakan untuk memulai awal game
    @Override
    public void render() {
        // Check the current screen and update music accordingly
        if (currentScreen instanceof GameScreen) {
            // If the current screen is the GameScreen, play the mainMusic
            if (!mainMusic.isPlaying()) {
                mainMusic.play();
            }
        } else {
            // If the current screen is not the GameScreen, stop the mainMusic
            if (mainMusic.isPlaying()) {
                mainMusic.stop();
            }
        }

        // Check the current screen and update music accordingly
        if (currentScreen instanceof MainMenu) {
            // If the current screen is the mainMenuScreen, play the mainMenuMusic
            if (!mainMenuMusic.isPlaying()) {
                mainMenuMusic.play();
            }
        } else {
            // If the current screen is not the mainMenuScreen, stop the mainMenuMusic
            if (mainMenuMusic.isPlaying()) {
                mainMenuMusic.stop();
            }
        }


        // cek kondisi game over dan update gameState
        if (gameState.equals("PLAYING")) {

            // kode render untuk game

            // Untuk memberi bg warna
            ScreenUtils.clear(1, 1, 1, 1);

            cam.update();
            batch.setProjectionMatrix(cam.combined);

            //player
            batch.begin();
            batch.draw(map, 0, 0, MAP_WIDTH, MAP_HEIGHT);
            player.handleMovement();
            player.getSprite().setPosition((player.getPosX() / 2f)+25, player.getPosY() / 2f);
            player.getSprite().draw(batch);

            //laser
            float deltaTime = Gdx.graphics.getDeltaTime();
            laserSfxCooldown -= deltaTime;
            if (!laserSfxPlayed && player.getLaser().size() > 0 && laserSfxCooldown <= 0) {
                laserSfx.play(0.5f);
                laserSfxPlayed = true;
                laserSfxCooldown = 0.0000000000000000000001f; // Set the cooldown duration
            } else if (player.getLaser().size() == 0) {
                laserSfxPlayed = false;
            }

            for (Laser laser : player.getLaser()) {
                laser.handleMovement(); //mengatur pergerakan laser
                Sprite laserSprite = laser.getSprite();
                laserSprite.setPosition(laser.getPosX(), laser.getPosY()-200); //mengatur posisi laser
                laserSprite.draw(batch);
            }

            // Draw monster
            for (int i = 0; i < monsters.size(); i++) {
                Monster monster = monsters.get(i);
                monster.getSprite().setPosition(monster.getPosX(), monster.getPosY());
                monster.getSprite().draw(batch);

                // Cek apakah laser dan monster berada pada posisi yang sama
                for (int j = 0; j < player.getLaser().size(); j++) {
                    Laser laser = player.getLaser().get(j);

                    //Jika monster dan laser berada pada posisi yang sama, maka health monster dihilangkan
                    if (laser.getSprite().getBoundingRectangle().overlaps(monster.getSprite().getBoundingRectangle())) {
                        monster.setHealth(monster.getHealth() - laser.getDamage());
                        player.getLaser().remove(j);
                        j--;

                        //remove moonster jika healthnya 0
                        if (monster.getHealth() <= 0) {
                            if (monster instanceof WeakEnemy) {
                                score += 5;
                            } else if (monster instanceof MediumEnemy) {
                                score += 10;
                            } else {
                                score += 15;
                            }
                            monsters.remove(i);
							laserHitSfx.play(0.5f); //tiap kali monster diremove bakal bunyi
							i--;
                            break;
                        }
                    }
                }
            }

            matahariTime += Gdx.graphics.getDeltaTime();

            //draw tiap matahari
            for (Matahari matahari : matahariArrayList) {
                matahari.handleMovement();
                Sprite matahariSprite = matahari.getSprite();
                matahariSprite.setPosition(matahari.getPosX(), matahari.getPosY());
                matahariSprite.draw(batch);

                // Menghilangkan matahari jika mencapai batas bawah layar
                if (matahari.getPosY() < 200) {
                    matahari.setPosition(random.nextFloat() * MAP_WIDTH, MAP_HEIGHT + random.nextFloat() * MAP_HEIGHT);
                }

                // Cek apakah matahari dan player berada pada posisi yang sama
                if (matahariSprite.getBoundingRectangle().overlaps(player.getSprite().getBoundingRectangle())) {
					matahariDropSfx.play(0.2f); // sfx tiap kali player kena apple
					player.setHealth(player.getHealth() - 20);  // Kurangi health player
                    playerHealth = player.getHealth() / 20;  // Update jumlah energy
                    matahari.setPosition(random.nextFloat() * MAP_WIDTH, MAP_HEIGHT + random.nextFloat() * MAP_HEIGHT);  // Reset posisi meteor
                }
            }
            //pemisah
            bulantime += Gdx.graphics.getDeltaTime();

            //draw tiap bulan
            for (Bulan Bulan : this.Bulan) {
                Bulan.handleMovement();
                Sprite bulanSprite = Bulan.getSprite();
                bulanSprite.setPosition(Bulan.getPosX(), Bulan.getPosY());
                bulanSprite.draw(batch);

                // Menghilangkan bulan jika mencapai batas bawah layar
                if (Bulan.getPosY() < 200) {
                    Bulan.setPosition(random.nextFloat() * MAP_WIDTH, MAP_HEIGHT + random.nextFloat() * MAP_HEIGHT);
                }

                // Cek apakah bulan dan player berada pada posisi yang sama
                if (bulanSprite.getBoundingRectangle().overlaps(player.getSprite().getBoundingRectangle())) {
                    matahariDropSfx.play(0.2f); // sfx tiap kali player kena apple
                    timerCount+= 1; //Gdx.graphics.getDeltaTime();
                    Bulan.setPosition(random.nextFloat() * MAP_WIDTH, MAP_HEIGHT + random.nextFloat() * MAP_HEIGHT);  // Reset posisi meteor
                }
            }
            // Countdown
            GlyphLayout timeLayout = new GlyphLayout(timeFont, "Time: ");
            timeFont.draw(batch, timeLayout, MAP_WIDTH - 850 - timeLayout.width, 30 + timeLayout.height);
            if (timerCount > 0) {
                GlyphLayout timerLayout = new GlyphLayout(timeFont, String.valueOf((int) timerCount));
                timeFont.draw(batch, timerLayout, MAP_WIDTH - 680 - timeLayout.width, 30 + timeLayout.height);
                timerCount -= Gdx.graphics.getDeltaTime();
            } else {
                GlyphLayout timerLayout = new GlyphLayout(timeFont, String.valueOf(0));
                timeFont.draw(batch, timerLayout, MAP_WIDTH - 680 - timeLayout.width, 30 + timeLayout.height);
            }

            // Score
            GlyphLayout scoreLabelLayout = new GlyphLayout(scoreFont, "Score: " + score);
            scoreFont.draw(batch, scoreLabelLayout, MAP_WIDTH - 140 - scoreLabelLayout.width, 30 + scoreLabelLayout.height);

            GlyphLayout highScoreLabelLayout1 = new GlyphLayout(scoreFont, "High score: ");
            scoreFont.draw(batch, highScoreLabelLayout1, MAP_WIDTH - 630 - highScoreLabelLayout1.width, 1800 + highScoreLabelLayout1.height);
            GlyphLayout highScoreLabelLayout2 = new GlyphLayout(scoreFont, String.valueOf(highScore));
            scoreFont.draw(batch, highScoreLabelLayout2, MAP_WIDTH - 500 - highScoreLabelLayout2.width, 1800 + highScoreLabelLayout2.height);

            if (monsters.size() == 0 && saveTime) {
                saveTime = false;
                lastTime = (int) timerCount;
                score += (timer - lastTime) * 2;
                System.out.println(score);
                if (score >= highScore) {
                    highScore = score;

                    try {
                        BufferedWriter bw = new BufferedWriter(new FileWriter("saveScore.txt"));
                        bw.write("" + highScore);
                        bw.close();
                    } catch (Exception e) {

                    }
                }
            }

            // Draw energy
            for (int i = 0; i < playerHealth; i++) {
                batch.draw(energy, 40 + i * (energyWidth + 3), 40 + timeLayout.height, energyWidth, energyHeight);
            }

            batch.end();
            if (playerHealth <= 0 || timerCount <= 0 && monsters.size() > 0) {
                gameState = "LOSE";
                setScreen(gameLoseScreen);
            } else if (playerHealth > 0 && monsters.size() == 0 && timerCount > 0) {
                gameState = "WIN";
                setScreen(gameWinScreen);
            }

        }
        currentScreen.render(Gdx.graphics.getDeltaTime());
    }

    public void resetGame() {
        // Stop all music and sound effects
        mainMusic.stop();
        mainMenuMusic.stop();
        laserSfx.stop();
        laserHitSfx.stop();
        pesawatDeathSfx.stop();
        matahariDropSfx.stop();

        // Reset player
        player = new Player("pesawat.png", MAP_WIDTH, MAP_HEIGHT, laserSfx);
        playerHealth = player.getHealth() / 20;

        // monster
        monsters.clear();
        int monsterRowWidth = (int) (totalMonster * (monsterSize + monsterDistance) - monsterDistance); // Total lebar semua monster dalam satu baris
        int startPos = (int) ((MAP_WIDTH - monsterRowWidth) / 1.5 / 2); // Menghitung posisi x awal agar makanan berada di tengah
        for (int i = 0; i < totalRow; i++) { //loop baris
            for (int j = 0; j < totalMonster; j++) { //loop kolom
                float posX = startPos + j * (monsterSize + monsterDistance);
                float posY = monsterPos - i * (monsterSize + monsterDistance);

                //jika index baris 0, maka memunculkan strong monster
                if (i == 0) {
                    monsters.add(new StrongEnemy(posX, posY));
                }

                //jika index baris 1 atau 2 muncul monster medium
                else if (i == 1 || i == 2) {
                    monsters.add(new MediumEnemy(posX, posY));
                }

                //jika index baris 3 atau 4 atau 5, muncul monster strong
                else {
                    monsters.add(new WeakEnemy(posX, posY));
                }
            }
        }

        // matahari
        matahariArrayList.clear();
        matahariTime = 1.5f;
        Random random = new Random();
        for (int i = 0; i < 4; i++) {
            float posX = monsterSize / 2f + random.nextFloat() * (MAP_WIDTH - monsterSize);
            float posY = MAP_HEIGHT + random.nextFloat() * MAP_HEIGHT;
            matahariArrayList.add(new Matahari("matahari.png", posX, posY, matahariTime));
        }

        // Reset score and timer
        score = 0;
        saveTime = true;
        timerCount = timer;  // Assuming `timer` is the initial timer value

        // Reset game state
        gameState = "START";

        // Reset current screen to MainMenu
        setScreen(mainMenuScreen);
    }



    @Override
    public void resize(int width, int height) {
        viewport.update(width, height);
        cam.position.set(MAP_WIDTH / 2f, MAP_HEIGHT / 2f, 0);
    }

    public SpriteBatch getBatch() {
        return batch;
    }

    public void setGameScreen(GameScreen gameScreen) {
        this.gameScreen = gameScreen;
    }
}
