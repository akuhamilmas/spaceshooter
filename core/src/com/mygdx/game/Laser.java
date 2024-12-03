package com.mygdx.game;

import com.badlogic.gdx.Gdx;

public class Laser extends Character {
    int damage;
    public Laser(String texturePath, float posX, float posY) {
        super(texturePath);
        this.getSprite().setSize((this.getSprite().getWidth()) / 12f, this.getSprite().getHeight()/12f); // Perkecil ukuran laser
        this.setPosition(posX/2, posY);
        this.damage = 50;

    }

    @Override
    public int handleMovement() {
        posY += 1000 * Gdx.graphics.getDeltaTime(); //mengatur kecepatan laser
        return 0;
    }

    public int getDamage() {
        return damage;
    }
}
