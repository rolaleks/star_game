package com.star.app.game.pools;

import com.star.app.game.GameController;
import com.star.app.game.helpers.ObjectPool;
import com.star.app.game.items.MoneyItem;

public class MoneyPool extends ObjectPool<MoneyItem> {

    private GameController gc;


    public void MoneyPool(GameController gc) {
        this.gc = gc;
    }

    @Override
    protected MoneyItem newObject() {
        return new MoneyItem(gc);
    }
}
