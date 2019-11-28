package com.star.app.game.items;

import com.star.app.game.Collidable;
import com.star.app.game.GameController;
import com.star.app.game.Hero;
import com.star.app.game.SpaceItem;
import com.star.app.game.helpers.Poolable;
import com.star.app.screen.utils.Assets;

public class BulletItem extends SpaceItem implements Poolable {

    private int amount;


    public BulletItem(GameController gc) {
        super();
        this.gc = gc;
        this.scale = 1.0f;
        this.active = false;
        this.hitArea.radius = 48;
        this.amount = 100;
        this.texture = Assets.getInstance().getAtlas().findRegion("bullets");

    }

    @Override
    protected void collideHero(Hero hero) {
        hero.getCurrentWeapon().addBullets(amount);
        deactivate();
    }
}