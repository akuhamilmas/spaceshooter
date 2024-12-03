package com.mygdx.game;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.Texture;

public abstract class Character {
    protected Sprite sprite;
    protected float posX;
    protected float posY;

    public Character(String texturePath) {
        this.sprite = new Sprite(new Texture(texturePath));
    }

    public void setPosition(float x, float y) {
        this.posX = x;
        this.posY = y;
    }

    public float getPosX() {
        return posX;
    }

    public float getPosY() {
        return posY;
    }

    public Sprite getSprite() {
        return sprite;
    }

    public abstract int handleMovement();
}
