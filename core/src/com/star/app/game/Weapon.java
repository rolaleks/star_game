package com.star.app.game;

import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.star.app.screen.utils.Assets;

public class Weapon {

    private GameController gc;
    private GameObject object;
    private String title;
    private float firePeriod;
    private int damage;
    private float bulletSpeed;
    private int maxBullets;
    private int curBullets;
    private Sound shootSound;

    // Когда мы описываем слот Vector3[] slots:
    //   x - это то на сколько пикселей он смещен относительно центра
    //   y - угол смещения относильно центра корабля
    //   z - угол смещения вылета пуль относительно направления корабля
    private Vector3[] slots;


    public float getFirePeriod() {
        return firePeriod;
    }

    public int getMaxBullets() {
        return maxBullets;
    }

    public int getCurBullets() {
        return curBullets;
    }

    public Weapon(GameController gc, GameObject object, String title, float firePeriod, int damage, float bulletSpeed, int maxBullets, Vector3[] slots) {
        this.gc = gc;
        this.object = object;
        this.title = title;
        this.firePeriod = firePeriod;
        this.damage = damage;
        this.bulletSpeed = bulletSpeed;
        this.maxBullets = maxBullets;
        this.curBullets = this.maxBullets;
        this.slots = slots;
        this.shootSound = Assets.getInstance().getAssetManager().get("audio/Shoot.mp3");
    }

    public void fire() {
        if (curBullets > 0) {
            curBullets--;
            shootSound.play();

            for (int i = 0; i < slots.length; i++) {
                float x, y, vx, vy;
                x = object.getPosition().x + slots[i].x * MathUtils.cosDeg(object.getAngle() + slots[i].y);
                y = object.getPosition().y + slots[i].x * MathUtils.sinDeg(object.getAngle() + slots[i].y);
                vx = object.getVelocity().x + bulletSpeed * MathUtils.cosDeg(object.getAngle() + slots[i].z);
                vy = object.getVelocity().y + bulletSpeed * MathUtils.sinDeg(object.getAngle() + slots[i].z);
                gc.getBulletController().setup(x, y, vx, vy, object.getAngle() + slots[i].z, object);
            }
        }
    }

    public void addBullets(int amount) {
        curBullets += amount;
        if (curBullets > maxBullets) {
            curBullets = maxBullets;
        }
    }
}
