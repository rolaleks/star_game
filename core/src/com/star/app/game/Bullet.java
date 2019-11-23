package com.star.app.game;

import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.Vector2;
import com.star.app.game.helpers.Poolable;
import com.star.app.screen.ScreenManager;

public class Bullet extends GameObject implements Poolable {

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

    public Bullet() {
        super();
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
            this.deactivate();
        }
    }
}