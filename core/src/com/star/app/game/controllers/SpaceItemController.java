package com.star.app.game.controllers;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.star.app.game.Collidable;
import com.star.app.game.GameController;
import com.star.app.game.SpaceItem;
import com.star.app.game.helpers.ObjectPool;
import com.star.app.game.items.BulletItem;
import com.star.app.game.items.HpItem;
import com.star.app.game.items.MoneyItem;
import com.star.app.game.items.WeaponItem;
import com.star.app.game.pools.BulletPool;
import com.star.app.game.pools.HpPool;
import com.star.app.game.pools.MoneyPool;
import com.star.app.game.pools.WeaponPool;

import java.util.ArrayList;
import java.util.List;

public class SpaceItemController {
    private GameController gc;

    private HpPool hpPool;
    private MoneyPool moneyPool;
    private BulletPool bulletPool;
    private WeaponPool weaponPool;

    private ArrayList<ObjectPool<? extends SpaceItem>> objectPools;

    public SpaceItemController(GameController gc) {
        this.gc = gc;
        this.objectPools = new ArrayList<>();
        this.hpPool = new HpPool(gc);
        this.moneyPool = new MoneyPool(gc);
        this.bulletPool = new BulletPool(gc);
        this.weaponPool = new WeaponPool(gc);

        objectPools.add(hpPool);
        objectPools.add(moneyPool);
        objectPools.add(bulletPool);
        objectPools.add(weaponPool);
    }

    public void render(SpriteBatch batch) {
        for (int i = 0; i < objectPools.size(); i++) {
            for (int j = 0; j < objectPools.get(i).getActiveList().size(); j++) {
                objectPools.get(i).getActiveList().get(j).render(batch);
            }
        }
    }

    public void update(float dt) {
        for (int i = 0; i < objectPools.size(); i++) {
            for (int j = 0; j < objectPools.get(i).getActiveList().size(); j++) {
                objectPools.get(i).getActiveList().get(j).update(dt);
            }
            objectPools.get(i).checkPool();
        }
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

    public void launchWeapon(float x, float y, float vx, float vy, float scale) {
        weaponPool.getActiveElement().activate(x, y, vx, vy, scale);
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

    public WeaponPool getWeaponPool() {
        return weaponPool;
    }

    public ArrayList<ObjectPool<? extends SpaceItem>> getObjectPools() {
        return objectPools;
    }
}
