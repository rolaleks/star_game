package com.star.app.game.controllers;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.star.app.game.Asteroid;
import com.star.app.game.GameController;
import com.star.app.game.helpers.ObjectPool;
import com.star.app.screen.utils.Assets;

public class AsteroidController extends ObjectPool<Asteroid> {

    private GameController gc;
    private int initAsteroidCount;
    private int initMaxHp;

    @Override
    protected Asteroid newObject() {
        return new Asteroid(gc);
    }

    public AsteroidController(GameController gc, int initAsteroidCount) {
        this.gc = gc;
        this.initAsteroidCount = initAsteroidCount;
        this.initMaxHp = 1;
    }

    public void render(SpriteBatch batch) {
        for (int i = 0; i < activeList.size(); i++) {
            Asteroid asteroid = activeList.get(i);
            asteroid.render(batch);
        }
    }

    public void update(float dt) {
        if (activeList.size() == 0) {
            int level = gc.nextLevel();
            //Каждые 5 уровней добавляем по 1 астероиду
            int extraAsteroids = (level - level % 5) / 5;
            for (int i = 0; i < initAsteroidCount + extraAsteroids; i++) {
                //с каждым уровне увеличиваем здоровье встероида на 1
                launch(initMaxHp * (level - 1));
            }
            return;
        }

        for (int i = 0; i < activeList.size(); i++) {
            activeList.get(i).update(dt);
        }
        checkPool();
    }


    public void launch(float x, float y, float vx, float vy, float scale, int hpMax) {
        getActiveElement().activate(x, y, vx, vy, scale, hpMax);
    }

    public void launch() {
        getActiveElement().activate();
    }

    public void launch(int maxHp) {
        getActiveElement().activate(maxHp);
    }
}
