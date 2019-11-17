package com.star.game;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;

public class Asteroid {
    private Vector2 position;
    private Vector2 velocity;
    private Texture texture;
    private StarGame game;

    public Asteroid(StarGame game) {
        this.position = new Vector2(MathUtils.random(-128, ScreenManager.SCREEN_WIDTH + 128), MathUtils.random(-128, ScreenManager.SCREEN_HEIGHT + 128));
        this.velocity = new Vector2(MathUtils.random(-100, 100), MathUtils.random(-100, 100));
        this.texture = new Texture("asteroid.png");
        this.game = game;
    }

    public void render(SpriteBatch batch) {

        batch.draw(texture, position.x - 128, position.y - 128, 128, 128, 256, 256, 1, 1, 0, 0, 0, 256, 256, false, false);
    }

    public void update(float dt) {
        position.x += (velocity.x - game.getBackground().getBackgroundDisplacement().x) * dt;
        position.y += (velocity.y - game.getBackground().getBackgroundDisplacement().y) * dt;
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
}
