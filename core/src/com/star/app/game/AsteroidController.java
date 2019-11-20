package com.star.app.game;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.star.app.game.helpers.ObjectPool;

public class AsteroidController extends ObjectPool<Asteroid> {

    private GameController gc;

    @Override
    protected Asteroid newObject() {
        return new Asteroid(gc);
    }

    public AsteroidController(GameController gc) {
        this.gc = gc;

    }

    public void render(SpriteBatch batch) {
        for (int i = 0; i < activeList.size(); i++) {
            Asteroid asteroid = activeList.get(i);
            asteroid.render(batch);
        }
    }

    public void update(float dt) {
        if (activeList.size() == 0) {
            launch();
            return;
        }

        for (int i = 0; i < activeList.size(); i++) {
            activeList.get(i).update(dt);
        }
        checkPool();
    }

    public void launch() {
        getActiveElement().activate();
    }

    public boolean checkBulletCollision(Vector2 position) {
        for (int i = 0; i < activeList.size(); i++) {
            Asteroid asteroid = activeList.get(i);
            if (asteroid.isHit(position)) {
                asteroid.deactivate();
                return true;
            }
        }
        return false;
    }
}
