package com.star.app.game.items;

import com.star.app.game.Collidable;
import com.star.app.game.GameController;
import com.star.app.game.Hero;
import com.star.app.game.SpaceItem;
import com.star.app.game.helpers.Poolable;
import com.star.app.screen.utils.Assets;

public class HpItem extends SpaceItem implements Poolable {

    private int amount;

    public HpItem(GameController gc) {
        super();
        this.gc = gc;
        this.scale = 1.0f;
        this.active = false;
        this.hitArea.radius = 48;
        this.amount = 50;
        this.texture = Assets.getInstance().getAtlas().findRegion("hp");

    }

    @Override
    protected void collideHero(Hero hero) {
        hero.addHp(amount);
        deactivate();
    }

}
