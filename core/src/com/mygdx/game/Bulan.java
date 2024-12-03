package com.mygdx.game;

import com.badlogic.gdx.Gdx;

public class Bulan extends Character {
    private  float bulanTime;
    public Bulan(String texturePath, float posX, float posY, float apelutuhtime) {
        super(texturePath);
        setPosition(posX, posY);
        this.bulanTime = apelutuhtime;
        this.getSprite().setSize((this.getSprite().getWidth()) /15, this.getSprite().getHeight()/15); //mengatur ukuran apel
    }

    @Override
    public int handleMovement() {
        posY -= 400 * Gdx.graphics.getDeltaTime();
        return 0;
    }
    public float getBulanTime(){ return bulanTime;}
    public void setBulanTime(float  Time){ bulanTime = Time;}
}
