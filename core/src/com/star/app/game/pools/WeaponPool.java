package com.star.app.game.pools;

import com.star.app.game.GameController;
import com.star.app.game.helpers.ObjectPool;
import com.star.app.game.items.BulletItem;
import com.star.app.game.items.WeaponItem;

public class WeaponPool extends ObjectPool<WeaponItem> {

    private GameController gc;


    public WeaponPool(GameController gc) {
        this.gc = gc;
    }

    @Override
    protected WeaponItem newObject() {
        return new WeaponItem(gc);
    }
}
