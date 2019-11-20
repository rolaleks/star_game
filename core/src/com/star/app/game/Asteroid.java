package com.star.app.game;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.star.app.game.helpers.Poolable;
import com.star.app.screen.ScreenManager;
import com.star.app.StarGame;

public class Asteroid implements Poolable {
    private Vector2 position;
    private Vector2 velocity;
    private Texture texture;
    private boolean active;
    private GameController gc;

    public Asteroid(GameController gc) {
        generatePositionAndVelocity();
        this.texture = new Texture("asteroid.png");
        this.gc = gc;
    }

    private void generatePositionAndVelocity() {
        this.position = new Vector2(MathUtils.random(-128, ScreenManager.SCREEN_WIDTH + 128), MathUtils.random(-128, ScreenManager.SCREEN_HEIGHT + 128));
        this.velocity = new Vector2(MathUtils.random(-100, 100), MathUtils.random(-100, 100));
    }

    public void render(SpriteBatch batch) {

        batch.draw(texture, position.x - 128, position.y - 128, 128, 128, 256, 256, 1, 1, 0, 0, 0, 256, 256, false, false);
    }

    public void update(float dt) {
        position.x += (velocity.x - gc.getBackground().getBackgroundDisplacement().x) * dt;
        position.y += (velocity.y - gc.getBackground().getBackgroundDisplacement().y) * dt;
        if (position.x < -128) {
            position.x = ScreenManager.SCREEN_WIDTH + 128;
        }
        if (position.x > ScreenManager.SCREEN_WIDTH + 128) {
            position.x = -128;
        }
        if (position.y < -128) {
            position.y = ScreenManager.SCREEN_HEIGHT + 128;
        }
        if (position.y > ScreenManager.SCREEN_HEIGHT + 128) {
            position.y = -128;
        }
    }

    @Override
    public boolean isActive() {
        return active;
    }

    public void deactivate() {
        active = false;
    }

    public void activate() {
        generatePositionAndVelocity();
        active = true;
    }
}
