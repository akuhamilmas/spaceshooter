package com.mygdx.game;

public abstract class Monster extends Character {
    protected int health;

    public Monster(String texturePath, int health, float posX, float posY) {
        super(texturePath);
        this.health = health;
        setPosition(posX, posY);
        this.getSprite().setSize(this.getSprite().getWidth() / 11, this.getSprite().getHeight() / 11); //mengatur ukuran monster
    }

    public int getHealth() {
        return health;
    }

    public void setHealth(int health) {
        this.health = health;
    }

    @Override
    public int handleMovement() {
        return 0;
    }
}