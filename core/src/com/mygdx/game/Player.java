package com.mygdx.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.audio.Sound;

import java.util.ArrayList;
import java.util.List;

public class Player extends Character {
    private float mapWidth;
    private float mapHeight;
    private int health;
    private Sound laserSfx;
    List<Laser> laser = new ArrayList<>();

    public Player(String texturePath, float mapWidth, float mapHeight, Sound laserSfx) {
        super(texturePath);
        this.mapWidth = mapWidth;
        this.mapHeight = mapHeight;
        this.laserSfx = laserSfx;
        this.health = 100;
        this.getSprite().setSize((this.getSprite().getWidth()) / 5.5f, this.getSprite().getHeight()/5.5f); //mengatur ukuran player
        this.setStartPosition(((mapWidth - this.getSprite().getWidth())+700) / 2f, 550); //agar player di tengah
    }

    //mengatur shoot laser
    public void shootlaser() {
        Laser newlaser = new Laser("laser.png", this.posX + (this.getSprite().getWidth() / 1.34f), this.posY + (this.getSprite().getHeight() / 1.1f));
        laser.add(newlaser);
        laserSfx.play(0.5f);
    }

    //mengatur pergerakan player
    @Override
    public int handleMovement() {
        if (Gdx.input.isKeyPressed(Input.Keys.LEFT)) { //player geser ke kiri
            this.posX -= 2000 * Gdx.graphics.getDeltaTime();
            if (this.posX < 0) {
                this.posX = 0;
            }
        }

        if (Gdx.input.isKeyPressed(Input.Keys.RIGHT)) { //player geser ke kanan
            this.posX += 2000 * Gdx.graphics.getDeltaTime();
            if (this.posX > (this.mapWidth+700) - this.getSprite().getWidth()) {
                this.posX = (this.mapWidth+700) - this.getSprite().getWidth();
            }
        }
        if (Gdx.input.isKeyJustPressed(Input.Keys.SPACE)) { //tekan space untuk menembak, menggunakan isKeyJustPressed agar sekali tekan hanya keluar satu laser
            shootlaser();
        }
        return 0;
    }

    public List<Laser> getLaser() {
        return laser;
    }

    public void setStartPosition(float x, float y) {
        this.posX = x;
        this.posY = y;
    }

    public int getHealth(){
        return health;
    }

    public void setHealth(int health){
        this.health = health;
    }
}
