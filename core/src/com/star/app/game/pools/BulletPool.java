package com.star.app.game.pools;

import com.star.app.game.GameController;
import com.star.app.game.helpers.ObjectPool;
import com.star.app.game.items.BulletItem;
import com.star.app.game.items.MoneyItem;

public class BulletPool extends ObjectPool<BulletItem> {

    private GameController gc;


    public void BulletPool(GameController gc) {
        this.gc = gc;
    }

    @Override
    protected BulletItem newObject() {
        return new BulletItem(gc);
    }
}
