package com.star.app.game;

import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.star.app.game.helpers.Poolable;
import com.star.app.screen.ScreenManager;

public class Bullet extends GameObject implements Poolable {
    private GameController gc;
    private float angle;
    private boolean active;

    public float getAngle() {
        return angle;
    }

    @Override
    public boolean isActive() {
        return active;
    }

    public void deactivate() {
        active = false;
    }

    public Bullet(GameController gc) {
        super();
        this.gc = gc;
        this.position = new Vector2(0, 0);
        this.velocity = new Vector2(0, 0);
        this.active = false;
    }

    public void activate(float x, float y, float vx, float vy, float angle) {
        this.position.set(x, y);
        this.velocity.set(vx, vy);
        this.active = true;
        this.angle = angle;
    }

    public void update(float dt) {
        position.mulAdd(velocity, dt);
        super.update();
        if (position.x < 0.0f || position.x > ScreenManager.SCREEN_WIDTH || position.y < 0.0f || position.y > ScreenManager.SCREEN_HEIGHT) {
            deactivate();
        }
    }

    @Override
    public void collide(Collidable collidable) {
        if (collidable instanceof Asteroid) {
            collideAsteroid((Asteroid) collidable);
        }
    }

    private void collideAsteroid(Asteroid asteroid) {
        this.deactivate();
        gc.getParticleController().setup(
                this.getPosition().x + MathUtils.random(-4, 4), this.getPosition().y + MathUtils.random(-4, 4),
                this.getVelocity().x * -0.3f + MathUtils.random(-30, 30), this.getVelocity().y * -0.3f + MathUtils.random(-30, 30),
                0.2f,
                2.2f, 1.7f,
                1.0f, 1.0f, 1.0f, 1.0f,
                0.0f, 0.0f, 1.0f, 0.0f
        );
    }
}