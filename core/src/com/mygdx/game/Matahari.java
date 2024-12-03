package com.mygdx.game;

import com.badlogic.gdx.Gdx;

public class Matahari extends Character {
    private float matahariTime;
    public Matahari(String texturePath, float posX, float posY, float matahariTime) {
        super(texturePath);
        setPosition(posX, posY);
        this.matahariTime = matahariTime;
        this.getSprite().setSize((this.getSprite().getWidth()) /15, this.getSprite().getHeight()/15); //mengatur ukuran apel
    }

    @Override
    public int handleMovement() {
        posY -= 400 * Gdx.graphics.getDeltaTime();  // Mengatur kecepatan apel
        return 0;
    }

    public float getMatahariTime() {
        return matahariTime;
    }

    public void setMatahariTime(float time){
        matahariTime = time;
    }
}
