package com.star.app.game.pools;

import com.star.app.game.GameController;
import com.star.app.game.helpers.ObjectPool;
import com.star.app.game.items.HpItem;

public class HpPool extends ObjectPool<HpItem> {

    private GameController gc;


    public HpPool(GameController gc) {
        this.gc = gc;
    }

    @Override
    protected HpItem newObject() {
        return new HpItem(gc);
    }
}
