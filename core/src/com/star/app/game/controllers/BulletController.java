package com.star.app.game.controllers;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.star.app.game.Bullet;
import com.star.app.game.GameController;
import com.star.app.game.GameObject;
import com.star.app.game.helpers.ObjectPool;
import com.star.app.screen.ScreenManager;
import com.star.app.screen.utils.Assets;

public class BulletController extends ObjectPool<Bullet> {
    private GameController gc;
    private TextureRegion bulletTexture;

    @Override
    protected Bullet newObject() {
        return new Bullet(gc);
    }

    public BulletController(GameController gc) {
        this.gc = gc;
        this.bulletTexture = Assets.getInstance().getAtlas().findRegion("bullet32");
    }

    public void render(SpriteBatch batch) {
        for (int i = 0; i < activeList.size(); i++) {
            Bullet b = activeList.get(i);
            batch.draw(bulletTexture, b.getPosition().x - 32, b.getPosition().y - 16, 32, 16, 64, 32, 1, 1, b.getAngle());
            if(b.getPosition().x > GameController.SPACE_WIDTH - ScreenManager.HALF_SCREEN_WIDTH) {
                batch.draw(bulletTexture, b.getPosition().x - 32 - GameController.SPACE_WIDTH, b.getPosition().y - 16, 32, 16, 64, 32, 1, 1, b.getAngle());
            }
            if(b.getPosition().x < ScreenManager.HALF_SCREEN_WIDTH) {
                batch.draw(bulletTexture, b.getPosition().x - 32 + GameController.SPACE_WIDTH, b.getPosition().y - 16, 32, 16, 64, 32, 1, 1, b.getAngle());
            }
            if(b.getPosition().y > GameController.SPACE_HEIGHT - ScreenManager.HALF_SCREEN_HEIGHT) {
                batch.draw(bulletTexture, b.getPosition().x - 32, b.getPosition().y - 16 - GameController.SPACE_HEIGHT, 32, 16, 64, 32, 1, 1, b.getAngle());
            }
            if(b.getPosition().y < ScreenManager.HALF_SCREEN_HEIGHT) {
                batch.draw(bulletTexture, b.getPosition().x - 32, b.getPosition().y - 16 + GameController.SPACE_HEIGHT, 32, 16, 64, 32, 1, 1, b.getAngle());
            }
        }
    }

    public void setup(float x, float y, float vx, float vy, float angle, GameObject owner) {
        getActiveElement().activate(x, y, vx, vy, angle, owner);
    }

    public void update(float dt) {
        for (int i = 0; i < activeList.size(); i++) {
            activeList.get(i).update(dt);
        }
        checkPool();
    }
}
