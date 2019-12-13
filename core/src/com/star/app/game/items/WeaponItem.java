package com.star.app.game.items;

import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector3;
import com.star.app.game.GameController;
import com.star.app.game.Hero;
import com.star.app.game.SpaceItem;
import com.star.app.game.Weapon;
import com.star.app.game.helpers.Poolable;
import com.star.app.screen.utils.Assets;

public class WeaponItem extends SpaceItem implements Poolable {

    private Weapon weapon;


    public WeaponItem(GameController gc) {
        super();
        this.gc = gc;
        this.scale = 1.0f;
        this.active = false;
        this.hitArea.radius = 48;
        this.texture = Assets.getInstance().getAtlas().findRegion("bullets");
    }

    protected void baseActivate() {
        this.weapon = generateWeapon();
    }

    private Weapon generateWeapon() {

        Vector3[] weaponDirections;
        int level = gc.getLevel();
        int weaponChanceKoef = MathUtils.random(1, 100) * level;
        int damage = 1;
        int hitRadius = 1;
        float bulletSpeed = 600.0f;
        float firePeriod = 0.3f;
        int maxBullets = MathUtils.random(level * 5 + 100, level * 5 + 300);
        String title;
        
        if (weaponChanceKoef < 150) {
            //на все 4 стороны
            weaponDirections = new Vector3[]{
                    new Vector3(24, 90, 90),
                    new Vector3(24, 0, 0),
                    new Vector3(24, -90, -90),
                    new Vector3(24, -180, -180)
            };
            title = "4 side gun";
        } else if (weaponChanceKoef < 350) {
            //Много орудий прямо
            weaponDirections = new Vector3[]{
                    new Vector3(24, 90, 10),
                    new Vector3(24, 90, 0),
                    new Vector3(24, 15, 5),
                    new Vector3(24, 0, 0),
                    new Vector3(24, -15, -5),
                    new Vector3(24, -90, -10),
                    new Vector3(24, -90, -0)
            };
            title = "Front hurricane gun";
        } else if (weaponChanceKoef < 600) {
            //веером
            int degreesShift = 15;
            weaponDirections = new Vector3[360 / degreesShift];
            for (int i = 0; i < 360; i += degreesShift) {
                weaponDirections[i / degreesShift] = new Vector3(24, i, i);
            }
            title = "Circle gun";
        } else if (weaponChanceKoef < 1000) {
            //Один сноряд с большим радиусом поражения
            weaponDirections = new Vector3[]{
                    new Vector3(24, 0, 0),
            };
            title = "Bazooka";
            damage = MathUtils.random(level, level + 10);
            bulletSpeed = 400.0f;
            hitRadius = 100;
        } else {
            //4 сноряда на все 4 стороны, с большим радиусом поражения
            weaponDirections = new Vector3[]{
                    new Vector3(24, 90, 90),
                    new Vector3(24, 0, 0),
                    new Vector3(24, -90, -90),
                    new Vector3(24, -180, -180)
            };
            title = "4 side bazooka";
            damage = MathUtils.random(level, level + 10);
            bulletSpeed = 400.0f;
            hitRadius = 100;
        }

        Weapon weapon = new Weapon(gc, null, title, firePeriod, damage, bulletSpeed, maxBullets, weaponDirections, hitRadius);
        return weapon;
    }

    @Override
    protected void collideHero(Hero hero) {
        this.weapon.setObject(hero);
        this.weapon.setExpired(true);
        hero.setCurrentWeapon(this.weapon);
        deactivate();
    }
}
