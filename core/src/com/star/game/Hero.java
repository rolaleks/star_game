package com.star.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;

public class Hero {

    private final float frontSpeed = 240f;
    private final float rearSpeed = 120f;

    private Texture texture;
    private Vector2 position;
    private Vector2 lastDisplacement;
    private float angle;

    public Vector2 getLastDisplacement() {
        return lastDisplacement;
    }

    public Hero() {
        this.texture = new Texture("ship.png");
        this.position = new Vector2(640, 360);
        this.lastDisplacement = new Vector2(0, 0);
        this.angle = 0.0f;
    }

    public void render(SpriteBatch batch) {
        batch.draw(texture, position.x - 32, position.y - 32, 32, 32, 64, 64, 1, 1, angle, 0, 0, 64, 64, false, false);
    }

    public void update(float dt) {
        if (Gdx.input.isKeyPressed(Input.Keys.A)) {
            angle += 180.0f * dt;
        }
        if (Gdx.input.isKeyPressed(Input.Keys.D)) {
            angle -= 180.0f * dt;
        }
        if (Gdx.input.isKeyPressed(Input.Keys.W)) {
            position.x += (float) Math.cos(Math.toRadians(angle)) * frontSpeed * dt;
            position.y += (float) Math.sin(Math.toRadians(angle)) * frontSpeed * dt;
            lastDisplacement.set((float) Math.cos(Math.toRadians(angle)) * frontSpeed * dt, (float) Math.sin(Math.toRadians(angle)) * frontSpeed * dt);
        } else if (Gdx.input.isKeyPressed(Input.Keys.S)) {
            float xShift = (float) Math.cos(Math.toRadians(angle)) * rearSpeed * dt;
            float yShift = (float) Math.sin(Math.toRadians(angle)) * rearSpeed * dt;
            position.x -= xShift;
            position.y -= yShift;
            lastDisplacement.set(-xShift, -yShift);
        } else {
            lastDisplacement.set(0, 0);
        }

        if (position.x < 0.0f) {
            position.x = 0.0f;
        }
        if (position.x > ScreenManager.SCREEN_WIDTH) {
            position.x = ScreenManager.SCREEN_WIDTH;
        }
        if (position.y < 0.0f) {
            position.y = 0.0f;
        }
        if (position.y > ScreenManager.SCREEN_HEIGHT) {
            position.y = ScreenManager.SCREEN_HEIGHT;
        }
    }
}
