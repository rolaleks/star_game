package com.star.app.game;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.star.app.game.helpers.Poolable;
import com.star.app.screen.ScreenManager;
import com.star.app.screen.utils.Assets;

public class Asteroid extends GameObject implements Poolable, Damageable {
    private GameController gc;
    private TextureRegion texture;
    private int hpMax;
    private int hp;
    private float scale;
    private float angle;
    private float rotationSpeed;
    private boolean active;

    private final float BASE_SIZE = 256.0f;
    private final float BASE_RADIUS = BASE_SIZE / 2.0f;

    public int getHpMax() {
        return hpMax;
    }

    public float getScale() {
        return scale;
    }

    @Override
    public boolean isActive() {
        return active;
    }

    public void deactivate() {
        active = false;
    }

    public Asteroid(GameController gc) {
        super();
        this.gc = gc;
        this.scale = 1.0f;
        this.active = false;
        this.texture = Assets.getInstance().getAtlas().findRegion("asteroid");
    }

    public boolean takeDamage(int amount) {
        hp -= amount;
        if (hp <= 0) {
            deactivate();
            if (scale > 0.25f) {
                gc.getAsteroidController().launch(position.x, position.y, MathUtils.random(-150.0f, 150.0f), MathUtils.random(-150.0f, 150.0f), scale - 0.2f);
                gc.getAsteroidController().launch(position.x, position.y, MathUtils.random(-150.0f, 150.0f), MathUtils.random(-150.0f, 150.0f), scale - 0.2f);
                gc.getAsteroidController().launch(position.x, position.y, MathUtils.random(-150.0f, 150.0f), MathUtils.random(-150.0f, 150.0f), scale - 0.2f);
            }
            return true;
        }
        return false;
    }


    public void render(SpriteBatch batch) {
        batch.draw(texture, position.x - 128, position.y - 128, 128, 128, 256, 256, scale, scale, angle);
    }

    public void update(float dt) {
        position.x += (velocity.x - gc.getBackground().getBackgroundDisplacement().x) * dt;
        position.y += (velocity.y - gc.getBackground().getBackgroundDisplacement().y) * dt;
        angle += rotationSpeed * dt;
        if (position.x < -BASE_RADIUS * scale) {
            position.x = ScreenManager.SCREEN_WIDTH + BASE_RADIUS * scale;
        }
        if (position.x > ScreenManager.SCREEN_WIDTH + BASE_RADIUS * scale) {
            position.x = -BASE_RADIUS * scale;
        }
        if (position.y < -BASE_RADIUS * scale) {
            position.y = ScreenManager.SCREEN_HEIGHT + BASE_RADIUS * scale;
        }
        if (position.y > ScreenManager.SCREEN_HEIGHT + BASE_RADIUS * scale) {
            position.y = -BASE_RADIUS * scale;
        }
        hitArea.setPosition(position);

        super.update();
    }

    public void activate() {
        this.scale = 1.0f;
        generatePositionAndVelocity();
        baseActivate();
    }

    public void activate(float x, float y, float vx, float vy, float scale) {
        this.position.set(x, y);
        this.velocity.set(vx, vy);
        this.scale = scale;
        baseActivate();
    }


    private void baseActivate() {
        this.active = true;
        this.hpMax = (int) (10 * scale);
        this.hp = this.hpMax;
        this.angle = MathUtils.random(0.0f, 360.0f);
        this.hitArea.setPosition(position);
        this.rotationSpeed = MathUtils.random(-60.0f, 60.0f);
        this.hitArea.setRadius(BASE_RADIUS * scale * 0.9f);
    }

    private void generatePositionAndVelocity() {
        this.position = new Vector2(MathUtils.random(-128, ScreenManager.SCREEN_WIDTH + 128), MathUtils.random(-128, ScreenManager.SCREEN_HEIGHT + 128));
        this.velocity = new Vector2(MathUtils.random(-100, 100), MathUtils.random(-100, 100));
    }


    @Override
    public void collide(Collidable collidable) {

        if (collidable instanceof Bullet) {
            Bullet bullet = (Bullet) collidable;
            bullet.deactivate();
            if (this.takeDamage(1)) {
                gc.getHero().addScore(this.getHpMax() * 100);
            }
        } else if (collidable instanceof Hero) {
            Vector2 initAsteroidVelocity = new Vector2(this.velocity.x, this.velocity.y);
            Hero hero = (Hero) collidable;
            float overlapDst = this.hitArea.radius + hero.hitArea.radius - hero.position.dst(this.position);
            Vector2 overlapImpulse = new Vector2(this.position.x - hero.position.x, this.position.y - hero.position.y);
            overlapImpulse = overlapImpulse.nor().scl(overlapDst + 2);
            this.position.add(overlapImpulse);
            this.velocity.add(hero.velocity);
            if (this.velocity.len() > 150) {
                this.velocity = this.velocity.nor().scl(150);
            }
            Vector2 heroOverlapImpulse = new Vector2(hero.position.x - this.position.x, hero.position.y - this.position.y);
            heroOverlapImpulse = heroOverlapImpulse.nor().scl(overlapDst + 2);
            hero.position.add(heroOverlapImpulse);
            hero.velocity.mulAdd(initAsteroidVelocity, this.getScale() * 5.2f);
            if (this.takeDamage(5)) {
                gc.getHero().addScore(this.getHpMax() * 100);
            }
        }

    }
}
