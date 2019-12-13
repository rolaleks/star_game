package com.star.app.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.StringBuilder;
import com.star.app.game.helpers.Poolable;
import com.star.app.screen.GameOverScreen;
import com.star.app.screen.ScreenManager;
import com.star.app.screen.utils.Assets;
import com.star.app.screen.utils.OptionsUtils;

public class Bot extends GameObject implements Damageable, Poolable {
    private GameController gc;
    private final float speed = 400f;

    private TextureRegion texture;
    private float fireTimer;
    private int hpMax;
    private int hp;
    private Weapon currentWeapon;
    private StringBuilder strBuilder;
    private Vector2 tmpVector;
    private boolean active;

    public int getHp() {
        return hp;
    }

    public Weapon getCurrentWeapon() {
        return currentWeapon;
    }

    public Bot(GameController gc) {
        super();
        this.gc = gc;
        this.texture = Assets.getInstance().getAtlas().findRegion("aliens");
        this.position = new Vector2(640, 360);
        //this.position = new Vector2(MathUtils.random(0, GameController.SPACE_WIDTH), MathUtils.random(0, GameController.SPACE_HEIGHT));
        this.angle = 0.0f;
        this.hitArea.radius = 34;
        this.hpMax = 5;
        this.hp = hpMax;
        this.strBuilder = new StringBuilder();
        this.tmpVector = new Vector2(0, 0);
        this.currentWeapon = new Weapon(
                gc, this, "Laser", 0.9f, 1, 400.0f, 99999,
                new Vector3[]{
                        new Vector3(28, 0, 0),
                },
                1
        );
    }

    public void render(SpriteBatch batch) {
        batch.draw(texture, position.x - 36, position.y - 36, 36, 36, 72, 72, 1, 1, angle);
    }

    public void update(float dt) {

        fireTimer += dt;

        tryToFire();

        tmpVector.set(gc.getHero().position);
        tmpVector.sub(this.position);
        //поворачиваем бота в сторону героя

        float angleDiff = (tmpVector.angle() - angle);
        if (angleDiff > 180) {
            angleDiff = 360 - angleDiff;
        }
        if (angleDiff < -180) {
            angleDiff = angleDiff + 360;
        }
        angle += angleDiff * dt;


        velocity.x += (float) Math.cos(Math.toRadians(angle)) * speed * dt;
        velocity.y += (float) Math.sin(Math.toRadians(angle)) * speed * dt;

        position.mulAdd(velocity, dt);
        float stopKoef = 1.0f - 2.0f * dt;
        //замедляем бота при приближении к герою
        stopKoef -= 1 / gc.getHero().position.dst(this.position);
        if (stopKoef < 0.0f) {
            stopKoef = 0.0f;
        }
        velocity.scl(stopKoef);

        float bx, by;
        bx = position.x - 28.0f * (float) Math.cos(Math.toRadians(angle));
        by = position.y - 28.0f * (float) Math.sin(Math.toRadians(angle));

        for (int i = 0; i < 5; i++) {
            gc.getParticleController().setup(
                    bx + MathUtils.random(-4, 4), by + MathUtils.random(-4, 4),
                    velocity.x * -0.3f + MathUtils.random(-20, 20), velocity.y * -0.3f + MathUtils.random(-20, 20),
                    0.5f,
                    1.2f, 0.2f,
                    1.0f, 0.5f, 0.0f, 1.0f,
                    1.0f, 1.0f, 1.0f, 0.0f
            );
        }
        checkSpaceBorders();

        super.update();
    }

    @Override
    public boolean takeDamage(int amount) {
        hp -= amount;
        if (hp <= 0) {
            return true;
        }
        return false;
    }


    public void tryToFire() {
        if (fireTimer > currentWeapon.getFirePeriod()) {
            fireTimer = 0.0f;
            currentWeapon.fire();
        }
    }

    @Override
    public void collide(Collidable collidable) {
        if (collidable instanceof Asteroid) {
            collideAsteroid((Asteroid) collidable);
        }
    }

    private void collideAsteroid(Asteroid asteroid) {

        this.takeDamage((int) (asteroid.getScale() * 10));
    }

    public void deactivate() {
        active = false;
    }

    @Override
    public boolean isActive() {
        return active;
    }


    public void activate(int hpMax) {
        this.hpMax = hpMax;
        this.hp = hpMax;
        this.active = true;
        this.angle = MathUtils.random(0.0f, 360.0f);
        this.hitArea.setPosition(position);
    }
}
