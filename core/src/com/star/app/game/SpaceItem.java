package com.star.app.game;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.star.app.game.helpers.Poolable;
import com.star.app.screen.ScreenManager;

public abstract class SpaceItem extends GameObject implements Poolable {

    protected TextureRegion texture;
    protected GameController gc;
    protected float scale;
    protected boolean active;

    public void activate(float x, float y, float vx, float vy, float scale) {
        this.position.set(x, y);
        this.velocity.set(vx, vy);
        this.scale = scale;
        this.hitArea.setPosition(position);
        this.active = true;
        baseActivate();
    }

    public void update(float dt) {
        position.mulAdd(velocity, dt);
        this.hitArea.setPosition(position);
        super.update();
        if (position.x < -100.0f || position.x > ScreenManager.SCREEN_WIDTH + 100.0f || position.y < 0.0f || position.y > ScreenManager.SCREEN_HEIGHT) {
            deactivate();
        }
    }

    public void render(SpriteBatch batch) {
        batch.draw(texture, position.x - 24, position.y - 24, 24, 24, 48, 48, scale, scale, 0);
    }

    protected void baseActivate() {

    }

    public void deactivate() {
        active = false;
    }

    @Override
    public boolean isActive() {
        return active;
    }

    abstract protected void collideHero(Hero hero);

    @Override
    public void collide(Collidable collidable) {
        if (collidable instanceof Hero) {
            collideHero((Hero) collidable);
        }
    }
}
