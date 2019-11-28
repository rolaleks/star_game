package com.star.app.game.controllers;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.star.app.game.Collidable;
import com.star.app.game.GameController;
import com.star.app.game.SpaceItem;
import com.star.app.game.helpers.ObjectPool;
import com.star.app.game.items.BulletItem;
import com.star.app.game.items.HpItem;
import com.star.app.game.items.MoneyItem;
import com.star.app.game.pools.BulletPool;
import com.star.app.game.pools.HpPool;
import com.star.app.game.pools.MoneyPool;

import java.util.ArrayList;
import java.util.List;

public class SpaceItemController {
    private GameController gc;

    private HpPool hpPool;
    private MoneyPool moneyPool;
    private BulletPool bulletPool;

    public SpaceItemController(GameController gc) {
        this.gc = gc;
        this.hpPool = new HpPool();
        this.moneyPool = new MoneyPool();
        this.bulletPool = new BulletPool();
    }

    public void render(SpriteBatch batch) {
        for (int i = 0; i < hpPool.getActiveList().size(); i++) {
            HpItem hpItem = hpPool.getActiveList().get(i);
            hpItem.render(batch);
        }
        for (int i = 0; i < moneyPool.getActiveList().size(); i++) {
            MoneyItem moneyItem = moneyPool.getActiveList().get(i);
            moneyItem.render(batch);
        }
        for (int i = 0; i < bulletPool.getActiveList().size(); i++) {
            BulletItem bulletItem = bulletPool.getActiveList().get(i);
            bulletItem.render(batch);
        }
    }

    public void update(float dt) {
        for (int i = 0; i < hpPool.getActiveList().size(); i++) {
            hpPool.getActiveList().get(i).update(dt);
        }
        for (int i = 0; i < moneyPool.getActiveList().size(); i++) {
            moneyPool.getActiveList().get(i).update(dt);
        }
        for (int i = 0; i < bulletPool.getActiveList().size(); i++) {
            bulletPool.getActiveList().get(i).update(dt);
        }
        hpPool.checkPool();
        moneyPool.checkPool();
        bulletPool.checkPool();
    }


    public void launchHp(float x, float y, float vx, float vy, float scale) {
        hpPool.getActiveElement().activate(x, y, vx, vy, scale);
    }

    public void launchMoney(float x, float y, float vx, float vy, float scale) {
        moneyPool.getActiveElement().activate(x, y, vx, vy, scale);
    }

    public void launchBullet(float x, float y, float vx, float vy, float scale) {
        bulletPool.getActiveElement().activate(x, y, vx, vy, scale);
    }

    public HpPool getHpPool() {
        return hpPool;
    }

    public MoneyPool getMoneyPool() {
        return moneyPool;
    }

    public BulletPool getBulletPool() {
        return bulletPool;
    }
}
