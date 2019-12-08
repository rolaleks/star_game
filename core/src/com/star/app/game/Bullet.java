package com.star.app.game;

import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.star.app.game.helpers.Poolable;
import com.star.app.screen.ScreenManager;

public class Bullet extends GameObject implements Poolable {
    private GameController gc;
    private boolean active;
    private float timer;
    private GameObject owner;


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
        timer = 0.0f;
    }

    public void activate(float x, float y, float vx, float vy, float angle, GameObject owner) {
        this.position.set(x, y);
        this.velocity.set(vx, vy);
        this.active = true;
        this.angle = angle;
        this.owner = owner;
        //Делаем что бы пуля всегда пролетала растояние равной самой короткой стороне карты
        this.timer = Math.min(GameController.SPACE_HEIGHT, GameController.SPACE_WIDTH) / this.velocity.len();
    }

    public boolean isOwner(Object object) {
        return object == this.owner;
    }

    public void update(float dt) {
        timer -= dt;
        position.mulAdd(velocity, dt);
        super.update();
        if (timer <= 0.0f) {
            deactivate();
        }
        if (position.x < 0.0f) {
            position.x = GameController.SPACE_WIDTH;
        }
        if (position.x > GameController.SPACE_WIDTH) {
            position.x = 0;
        }
        if (position.y < 0.0f) {
            position.y = GameController.SPACE_HEIGHT;
        }
        if (position.y > GameController.SPACE_HEIGHT) {
            position.y = 0;
        }
    }

    @Override
    public void collide(Collidable collidable) {
        if (collidable instanceof Asteroid) {
            collideAsteroid((Asteroid) collidable);
        } else if (collidable instanceof Damageable) {
            collideHero((Damageable) collidable);
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


    private void collideHero(Damageable object) {
        if (!this.isOwner(object)) {
            this.deactivate();
            object.takeDamage(1);
        }
    }
}